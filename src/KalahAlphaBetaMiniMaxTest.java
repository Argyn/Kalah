package MKAgent;

import java.util.Scanner;
import java.util.List;

public class KalahAlphaBetaMiniMaxTest {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Number of holes = ");
    int numberOfHoles = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Number of seeds = ");
    int numberOfSeeds = scanner.nextInt();

    Board board = new Board(numberOfHoles, numberOfSeeds);
    Kalah kalah = new Kalah(board);

    BoardEvaluator evaluator = BoardEvaluatorFactory.getEvaluator();
    KalahAlphaBetaMiniMax alphaBeta = new KalahAlphaBetaMiniMax(Side.SOUTH, evaluator);

    try {
      Side playerSide = Side.SOUTH;
      Side side = Side.SOUTH;
      while(!kalah.gameOver()) {
        if(side != playerSide) {
          System.out.print("Hole = ");
          int hole = scanner.nextInt();
          side = kalah.makeMove(new Move(side, hole));
          System.out.println(board);
          continue;
        }

        List<OptimizedMove> optimalMoves = alphaBeta.getOptimizedMoves(kalah, board, side, 14);

        System.out.println(String.format("Fetched %d optimal moves", optimalMoves.size()));
        for(OptimizedMove move : optimalMoves) {
          System.out.println(move);
        }

        OptimizedMove nextMove = optimalMoves.get(1);

        System.out.println(String.format(side.name()+" : Playing hole %d, with score %.2f", nextMove.hole(), nextMove.score()));
        side = kalah.makeMove(new Move(side, nextMove.hole()));
        System.out.println(board);
      }
    } catch(Exception e) {
      System.out.println(kalah.gameOver());
      e.printStackTrace();
    }

  }
}
