public class KalahAlphaBetaMiniMax {


  public KalahAlphaBetaMiniMax() {

  }

  public int evaluateScore(Board previousBoard, Board board, Side side) {
  // int numberOfSeedsOpPlayer = 0;
  // int numberOfSeedsPlayer = 0;
  //
  // for(int hole=1; hole<=6; hole++) {
  //   numberOfSeedsOpPlayer += board.getSeeds(side.opposite(), hole);
  //   numberOfSeedsPlayer += board.getSeeds(side, hole);
  // }
  //
  // int h1 = numberOfSeedsOpPlayer - numberOfSeedsPlayer;
  //
  // int h2 = board.getSeedsInStore(side.opposite()) - board.getSeedsInStore(side);

  // System.out.println("Previous Board");
  // System.out.println(previousBoard);
  //
  // System.out.println("Current Board");
  // System.out.println(board);
  // System.out.println("\n\n");

  int h1=0;
  for (int hole=1; hole<=6; hole++)
  {
    if(board.getSeeds(side.opposite(), hole) < previousBoard.getSeeds(side.opposite(), hole)) {
      h1 += 1;
    }
    else {
      h1 -= 1;
    }

    if(board.getSeeds(side, hole) > previousBoard.getSeeds(side, hole)) {
      h1 += 1;
    }
    else {
      h1 -= 1;
    }

    if(board.getSeedsInStore(side.opposite()) < previousBoard.getSeedsInStore(side.opposite())) {
      h1 += 4;
    }
    else {
      h1 -=2;
    }

    if (board.getSeedsInStore(side) > previousBoard.getSeedsInStore(side)) {
      h1 += 4;
    }
    else {
      h1 -= 2;
    }
  }

  return h1;
}

  public OptimizeResult alphaBeta(Kalah kalah,
                                  Board previousBoard,
                                  Board board,
                                  Side side,
                                  int depth,
                                  int alpha,
                                  int beta,
                                  boolean maximizingPlayer) throws Exception {
    if(depth == 0 || kalah.gameOver(board)) {
      int result = evaluateScore(previousBoard, board, side);
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
        OptimizeResult result = alphaBeta(kalah, previousBoard, currentBoard, newSide, depth-1, alpha, beta, side == newSide);
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
        OptimizeResult result = alphaBeta(kalah, previousBoard, currentBoard, newSide, depth-1, alpha, beta, side == newSide);
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
                                         Board previousBoard,
                                         Board board,
                                         Side side,
                                         int depth) throws Exception {
    return alphaBeta(kalah, previousBoard, board, side, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
  }

}
