package MKAgent;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class KalahAlphaBetaMiniMax {

  private BoardEvaluator evaluator;

  private Side mySide;

  public KalahAlphaBetaMiniMax(Side side, BoardEvaluator evaluator) {
    this.evaluator = evaluator;
    this.mySide = side;
  }

  private OptimizeResult alphaBeta(Kalah kalah,
                                  Board board,
                                  Side side,
                                  int depth,
                                  double alpha,
                                  double beta,
                                  boolean maximizingPlayer) throws Exception {
    if(depth == 0 || kalah.gameOver(board)) {
      return new OptimizeResult(evaluator.evaluateBoard(board, mySide));
    }

    double bestValue = 0;
    int bestHole = 0;
    if(maximizingPlayer) {
      bestValue = Integer.MIN_VALUE;
      for(int playHole=1; playHole<=board.getNoOfHoles(); playHole++) {
        if(board.getSeeds(side, playHole) == 0)
          continue;

        Board currentBoard = board.clone();
        Side newSide = kalah.makeMove(currentBoard, new Move(side, playHole));

        OptimizeResult result;
        if(currentBoard.previousMoveWasFirst()) {
          // pie rule, if we got extra move, do not use it, pass the move
          result = alphaBeta(kalah, currentBoard, newSide, depth-1, alpha, beta, false);
        }
        else {
          // either pass the move, or use extra move, depending on the side
          result = alphaBeta(kalah, currentBoard, newSide, depth-1, alpha, beta, ((side == newSide) ? true : false));
        }

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

  private List<OptimizedMove> doGetOptimizedMoves(Kalah kalah, Board board,
                                                  Side side, int depth) throws Exception{
    List<OptimizedMove> optimizedMoves = new ArrayList<>();
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;

    // assuming always maximizing
    for(int playHole=1; playHole<=board.getNoOfHoles(); playHole++) {
      if(board.getSeeds(side, playHole) == 0) {
        continue;
      }

      Board currentBoard = board.clone();
      Side newSide = kalah.makeMove(currentBoard, new Move(side, playHole));

      OptimizeResult result;
      if(currentBoard.previousMoveWasFirst()) {
        // pie rule, if we got extra move, do not use it, pass the move
        result = alphaBeta(kalah, currentBoard, newSide, depth-1, alpha, beta, false);
      }
      else {
        // either pass the move, or use extra move, depending on the side
        result = alphaBeta(kalah, currentBoard, newSide, depth-1, alpha, beta, ((side == newSide) ? true : false));
      }

      // record the result
      optimizedMoves.add(new OptimizedMove(playHole, result.score));
    }

    Collections.sort(optimizedMoves);

    return optimizedMoves;
  }

  public List<OptimizedMove> getOptimizedMoves(Kalah kalah, Board board,
                                               Side side, int depth) throws Exception {
    return doGetOptimizedMoves(kalah, board, side, depth);
  }

  public OptimizeResult optimizeNextMove(Kalah kalah,
                                         Board board,
                                         Side side,
                                         int depth) throws Exception {
    return alphaBeta(kalah, board, side, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
  }

}
