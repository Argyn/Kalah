package MKAgent;

import java.util.List;

public class KalahPlayer {

  private Side mySide;

  private Kalah kalah;

  private KalahAlphaBetaMiniMax optimizer;

  private BoardEvaluator evaluator;

  private static final int DEPTH = 14;

  private int depth;

  public KalahPlayer(Side side, Kalah kalah, BoardEvaluator evaluator) {
    this.mySide = side;
    this.kalah = kalah;
    this.depth = DEPTH;
    this.evaluator = evaluator;
    initilizeOptimizer();
  }

  private void initilizeOptimizer() {
    this.optimizer = new KalahAlphaBetaMiniMax(mySide, evaluator);
  }

  public Side side() {
    return mySide;
  }

  public void swap() {
    mySide = mySide.opposite();
    Logger.INSTANCE.info(String.format("Opposite: Player want to SWAP. Taking side: %s", mySide.name()));
    initilizeOptimizer();
  }

  public boolean shouldSwap(int move) throws Exception {
    // try being
    KalahAlphaBetaMiniMax oppOptimizer = new KalahAlphaBetaMiniMax(mySide.opposite(), evaluator);

    // see what's the first move I would make if I was going first
    Logger.INSTANCE.info("Checking if we should swap. Impersonating side " + mySide.opposite().name());
    Logger.INSTANCE.info(kalah.getBoard().toString());

    List<OptimizedMove> optimizedMoves = oppOptimizer.getOptimizedMoves(kalah,
                                                                        kalah.getBoard(),
                                                                        mySide.opposite(),
                                                                        depth);

    return optimizedMoves.get(0).hole() == move || optimizedMoves.get(1).hole() == move;
  }

  public void opponentMove(int move) {
    Logger.INSTANCE.info(String.format("Opponent: Makes Move %d", move));
    Logger.INSTANCE.info(kalah.getBoard().toString());

    kalah.makeMove(new Move(mySide.opposite(), move));
  }

  public void makeMove(int playHole) throws Exception {
    kalah.makeMove(new Move(mySide, playHole));

    Logger.INSTANCE.info(String.format("Player: Makes Move %d", playHole));
    Logger.INSTANCE.info(kalah.getBoard().toString());
  }

  private OptimizedMove decideNextMove(boolean firstMove) throws Exception{
    List<OptimizedMove> optimizedMoves = optimizer.getOptimizedMoves(kalah,
                                                                     kalah.getBoard(),
                                                                     mySide,
                                                                     depth);

    if(firstMove) {
      // do not take the best move
      if(optimizedMoves.size() > 2) {
        Logger.INSTANCE.info("Choosing 2nd best move");
        return optimizedMoves.get(1);
      }
    }

    OptimizedMove nextMove = optimizedMoves.get(0);
    Logger.INSTANCE.info(String.format("Making move: %d", nextMove.hole()));

    return optimizedMoves.get(0);
  }

  public int decideAndMakeNextMove(boolean firstMove) throws Exception {
    OptimizedMove nextMove = decideNextMove(firstMove);

    Logger.INSTANCE.info(String.format("Making move: %d", nextMove.hole()));

    makeMove(nextMove.hole());

    return nextMove.hole();
  }

}
