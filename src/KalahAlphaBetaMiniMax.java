package MKAgent;

public class KalahAlphaBetaMiniMax {


  public KalahAlphaBetaMiniMax() {

  }

  private int evaluateBoard2(Board board, Side side, boolean maximizing) {
    int kalahsDiff = board.getSeedsInStore(side.opposite()) - board.getSeedsInStore(side);
    int seedsDiff = board.getNumbersOfSeedsInPits(side.opposite()) - board.getNumbersOfSeedsInPits(side);

    return kalahsDiff*2 + seedsDiff;
  }

  private int evaluateBoard(Board board, Side side, boolean maximizing) {
    int kalahsDiff = board.getSeedsInStore(Side.SOUTH) - board.getSeedsInStore(Side.NORTH);
    int seedsDiff = board.getNumbersOfSeedsInPits(Side.SOUTH) - board.getNumbersOfSeedsInPits(Side.NORTH);

    return kalahsDiff*2 + seedsDiff;
  }

  private OptimizeResult alphaBeta(Kalah kalah,
                                  Board board,
                                  Side side,
                                  int depth,
                                  int alpha,
                                  int beta,
                                  boolean maximizingPlayer) throws Exception {
    if(depth == 0 || kalah.gameOver(board)) {
      int score = evaluateBoard(board, side, maximizingPlayer);

      return new OptimizeResult(score);
    }

    int bestValue = 0;
    int bestHole = 0;
    if(maximizingPlayer) {
      bestValue = Integer.MIN_VALUE;
      for(int playHole=1; playHole<=board.getNoOfHoles(); playHole++) {
        if(board.getSeeds(side, playHole) == 0)
          continue;

        Board currentBoard = board.clone();
        Side newSide = kalah.makeMove(currentBoard, new Move(side, playHole));
        OptimizeResult result = alphaBeta(kalah, currentBoard, newSide, depth-1, alpha, beta, side == newSide);
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
      for(int playHole=1; playHole<=board.getNoOfHoles(); playHole++) {
        if(board.getSeeds(side, playHole) == 0)
          continue;

        Board currentBoard = board.clone();
        Side newSide = kalah.makeMove(currentBoard, new Move(side, playHole));
        OptimizeResult result = alphaBeta(kalah, currentBoard, newSide, depth-1, alpha, beta, side == newSide);
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
