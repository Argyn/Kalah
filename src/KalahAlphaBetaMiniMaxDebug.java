package MKAgent;

public class KalahAlphaBetaMiniMaxDebug {

  private BoardEvaluator evaluator;

  private Side mySide;

  public KalahAlphaBetaMiniMaxDebug(Side side) {
    this.mySide = side;
    evaluator = new MancalaDiffEvaluator();
  }

  private OptimizeResult alphaBeta(Kalah kalah,
                                  Board board,
                                  Side side,
                                  int depth,
                                  int alpha,
                                  int beta,
                                  boolean maximizingPlayer) throws Exception {
    if(depth == 0 || kalah.gameOver(board)) {
      return new OptimizeResult(evaluator.evaluateBoard(board, mySide));
    }

    int bestValue = 0;
    int bestHole = 0;
    if(maximizingPlayer) {
      System.out.println("MAXIMIZING");
      bestValue = Integer.MIN_VALUE;
      for(int playHole=1; playHole<=board.getNoOfHoles(); playHole++) {
        if(board.getSeeds(side, playHole) == 0)
          continue;

        Board currentBoard = board.clone();
        Side newSide = kalah.makeMove(currentBoard, new Move(side, playHole));

        System.out.println(String.format("MAXIMIZING: Playing hole: %d", playHole));
        System.out.println(currentBoard);

        if(side == newSide) {
          System.out.println("Got extra move, MAXIMIZING again");
        }

        OptimizeResult result = alphaBeta(kalah, currentBoard, newSide, depth-1, alpha, beta, ((side == newSide) ? true : false));

        if(result.score > bestValue) {
          bestValue = result.score;
          bestHole = playHole;
        }
        alpha = Math.max(alpha, bestValue);
        if(beta <= alpha) {
          break;
        }
      }
    } else {
      bestValue = Integer.MAX_VALUE;

      System.out.println("MINIMIZING");
      for(int playHole=1; playHole<=board.getNoOfHoles(); playHole++) {
        if(board.getSeeds(side, playHole) == 0)
          continue;

        Board currentBoard = board.clone();
        Side newSide = kalah.makeMove(currentBoard, new Move(side, playHole));

        System.out.println(String.format("MINIMIZING: Playing hole: %d", playHole));
        System.out.println(currentBoard);

        OptimizeResult result = alphaBeta(kalah, currentBoard, newSide, depth-1, alpha, beta, ((side == newSide) ? false : true));
        if(result.score < bestValue) {
          bestValue = result.score;
          bestHole = playHole;
        }
        beta = Math.min(bestValue, beta);

        if(beta <= alpha) {
          break;
        }
      }
    }

    return new OptimizeResult(bestHole, bestValue);
  }

  public OptimizeResult optimizeNextMove(Kalah kalah,
                                         Board board,
                                         Side side,
                                         int depth) throws Exception {
    return alphaBeta(kalah, board, side, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
  }

}
