package MKAgent;

public class KalahMiniMax {

  public KalahMiniMax() {

  }

  public OptimizeResult maximizeNextMove(Kalah kalah, Board board, Side side, int steps) throws Exception {
    int maxSoFar = Integer.MIN_VALUE;
    int hole = 0;
    for(int playHole=1; playHole<=board.getNoOfHoles(); playHole++) {
      if(board.getSeeds(side, playHole) == 0)
        continue;
      Board currentBoard = board.clone();
      Side newSide = kalah.makeMove(currentBoard, new Move(side, playHole));
      OptimizeResult result;
      if(side == newSide) {
        result = maximizeNextMove(kalah, currentBoard, side, steps);
      } else {
        result = minimizeNextMove(kalah, currentBoard, newSide, steps-1);
      }
      if(result.score > maxSoFar) {
        maxSoFar = result.score;
        hole = playHole;
      }
    }

    return new OptimizeResult(hole, maxSoFar);
  }

  public OptimizeResult minimizeNextMove(Kalah kalah, Board board, Side side, int steps) throws Exception  {
    int minSoFar = 0;
    int hole = 0;
    for(int playHole=1; playHole<=board.getNoOfHoles(); playHole++) {
      if(board.getSeeds(side, playHole) == 0)
        continue;
      Board currentBoard = board.clone();
      Side newSide = kalah.makeMove(currentBoard, new Move(side, playHole));
      OptimizeResult result;
      if(steps - 1 == 0) {
        result = new OptimizeResult(currentBoard.getTotalNumberOfSeeds(side.opposite()) - currentBoard.getTotalNumberOfSeeds(side));
      } else if(side == newSide){
        result = minimizeNextMove(kalah, currentBoard, side, steps);
      } else {
        result = maximizeNextMove(kalah, currentBoard, side.opposite(), steps);
      }
      if(result.score < minSoFar) {
        minSoFar = result.score;
        hole = playHole;
      }
    }

    return new OptimizeResult(hole, minSoFar);
  }

  public OptimizeResult optimizeNextMove(Kalah kalah,
                                         Board board,
                                         Side side,
                                         int steps) throws Exception {
    return maximizeNextMove(kalah, board, side, steps);
  }

}
