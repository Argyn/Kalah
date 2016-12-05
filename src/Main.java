package MKAgent;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * The main application class. It also provides methods for communication
 * with the game engine.
 */
public class Main
{
    /**
     * Input from the game engine.
     */
    private static Reader input = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Sends a message to the game engine.
     * @param msg The message.
     */
    public static void sendMsg (String msg)
    {
    	System.out.print(msg);
    	System.out.flush();
    }

    /**
     * Receives a message from the game engine. Messages are terminated by
     * a '\n' character.
     * @return The message.
     * @throws IOException if there has been an I/O error.
     */
    public static String recvMsg() throws IOException
    {
    	StringBuilder message = new StringBuilder();
    	int newCharacter;

    	do
    	{
    		newCharacter = input.read();
    		if (newCharacter == -1)
    			throw new EOFException("Input ended unexpectedly.");
    		message.append((char)newCharacter);
    	} while((char)newCharacter != '\n');

		return message.toString();
    }

  public static Side readSideFromStartMsg() throws IOException, InvalidMessageException {
    String startMsg = recvMsg();
    if(Protocol.getMessageType(startMsg) != MsgType.START) {
      StringBuilder builder = new StringBuilder();
      builder.append("Expected START message. Instead received:");
      builder.append(startMsg);

      throw new InvalidMessageException(builder.toString());
    }

    if(Protocol.interpretStartMsg(startMsg)) {
      return Side.SOUTH;
    }

    return Side.NORTH;
  }

  public static Protocol.MoveTurn readStateMsg(Board board) throws IOException, InvalidMessageException {
    String stateMsg = recvMsg();

    Logger.INSTANCE.info("Received " + stateMsg);

    if(Protocol.getMessageType(stateMsg) != MsgType.STATE) {
      StringBuilder builder = new StringBuilder();
      builder.append("Expected STATE message. Instead received:");
      builder.append(stateMsg);

      throw new InvalidMessageException(builder.toString());
    }

    return Protocol.interpretStateMsg(stateMsg, board);
  }

  public static void reportGameResults(Kalah kalah, Side mySide) {
    Side winner = mySide;
    Board board = kalah.getBoard();
    int diff = board.getSeedsInStore(mySide) - board.getSeedsInStore(mySide.opposite());
    if(diff > 0) {
      Logger.INSTANCE.info(String.format("Winner: %s. GG WP", winner.name()));
    } else if(diff < 0){
      Logger.INSTANCE.info(String.format("Winner: %s =(", winner.name()));
    } else if(diff == 0) {
      Logger.INSTANCE.info("Result: Draw");
    }

    Logger.INSTANCE.info(kalah.getBoard().toString());
  }

	/**
	 * The main method, invoked when the program is started.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) throws CloneNotSupportedException
	{
    // create Kalah
    Kalah kalah = new Kalah(new Board(7,7));
    Board cloneBoard = kalah.getBoard().clone();

    Side firstToMove = Side.SOUTH;
    Side mySide = Side.SOUTH;
    Side turn = firstToMove;
    boolean prevTurn = false;
    boolean canSwap = false;

    try {
      mySide = readSideFromStartMsg();

      canSwap = mySide != firstToMove;

      KalahPlayer player = new KalahPlayer(mySide, kalah);

      while(!kalah.gameOver()) {
        prevTurn = false;

        if(mySide == turn) {
          // make move
          sendMsg(Protocol.createMoveMsg(player.makeMove()));
          prevTurn = true;
        }

        // read state msg
        Protocol.MoveTurn state = readStateMsg(cloneBoard);
        if(state.end) {
          break;
        } else if(state.again) {
          turn = mySide;
        } else {
          turn = mySide.opposite();
        }

        // if I was not the one who moved previous, then read state message
        // otherwise, ignore, as we already now the state
        if(!prevTurn) {
          // check if opposing player wants to swap
          // no check if swap is legal, hoping engine handles that
          if(state.move == -1) {
            player.swap();
            mySide = player.side();
            turn = mySide;
          } else {
            // check if we can swap and if should - then do it
            Logger.INSTANCE.info("Can swap = "+canSwap);
            if(canSwap) {
              Logger.INSTANCE.info("Checking if we should swap");
              if(player.shouldSwap(state.move)) {
                player.swap();
                // and make move
                player.makeMove(state.move);
                mySide = player.side();
                turn = mySide.opposite();

                canSwap = false;

                sendMsg(Protocol.createSwapMsg());
                continue;
              }
              // no swap after first move
              canSwap = false;
            }
            // seems like we can't swap, so just accept opponents'move
            player.opponentMove(state.move);
          }
        }
      }

      reportGameResults(kalah, mySide);

    } catch(Exception e) {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String exceptionAsString = sw.toString();
      Logger.INSTANCE.severe(exceptionAsString);
    }
	}
}
