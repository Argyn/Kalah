package MKAgent;

public class KalahPlayer {

  private Side mySide;

  private Kalah kalah;

  private KalahAlphaBetaMiniMax optimizer;

  private BoardEvaluator evaluator;

  private static final int MAX_DEPTH = 14;
  private static final int MIN_DEPTH = 12;

  private int depth;

  public KalahPlayer(Side side, Kalah kalah) {
    this.mySide = side;
    this.kalah = kalah;
    this.depth = MIN_DEPTH;
    this.evaluator = new MancalaDiffEvaluator();
    initilizeOptimizer();
  }

  private void initilizeOptimizer() {
    this.optimizer = new KalahAlphaBetaMiniMax(mySide);
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
    KalahAlphaBetaMiniMax oppOptimizer = new KalahAlphaBetaMiniMax(mySide.opposite());
    // see what's the first move I would make if I was going first
    Logger.INSTANCE.info("Checking if we should swap. Impersonating side " + mySide.opposite().name());
    Logger.INSTANCE.info(kalah.getBoard().toString());
    OptimizeResult result = oppOptimizer.optimizeNextMove(kalah, kalah.getBoard(), mySide.opposite(), depth);

    if(move == result.hole) {
      Logger.INSTANCE.info("Player: Opposing player plays our strategy, do SWAP");
      return true;
    } else {
      Logger.INSTANCE.info(String.format("Player: No need to SWAP. I would have played %d", result.hole));
    }

    return false;
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

  public int makeMove() throws Exception {
    OptimizeResult result = optimizer.optimizeNextMove(kalah, kalah.getBoard(), mySide, depth);

    makeMove(result.hole);

    // if we are losing, after this move, search deeper
    if(evaluator.evaluateBoard(kalah.getBoard(), mySide) < 0) {
      if(depth+2 <= MAX_DEPTH) {
        depth+=2;
        Logger.INSTANCE.info(String.format("Increasing search depth to %d", depth));
      }
    }

    return result.hole;
  }

}
