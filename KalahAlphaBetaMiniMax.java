public class KalahAlphaBetaMiniMax {


  public KalahAlphaBetaMiniMax() {

  }

  public OptimizeResult alphaBeta(Kalah kalah,
                                  Board board,
                                  Side side,
                                  int depth,
                                  int alpha,
                                  int beta,
                                  boolean maximizingPlayer) throws Exception {
    if(depth == 0 || kalah.gameOver(board)) {
      int result = board.getTotalNumberOfSeeds(side.opposite()) - board.getTotalNumberOfSeeds(side);
      if(maximizingPlayer) {
        result = Math.abs(result);
      }

      return new OptimizeResult(result);
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
        //System.out.println("Opp playing move = "+playHole);
        Side newSide = kalah.makeMove(currentBoard, new Move(side, playHole));
        OptimizeResult result = alphaBeta(kalah, currentBoard, newSide, depth-1, alpha, beta, side == newSide);
        if(result.score < bestValue) {
          bestValue = result.score;
          bestHole = result.hole;
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
