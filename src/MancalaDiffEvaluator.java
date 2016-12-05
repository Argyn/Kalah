package MKAgent;

public class MancalaDiffEvaluator implements BoardEvaluator {

  public MancalaDiffEvaluator() {

  }

  public int evaluateBoard(Board board, Side side) {
    int kalahsDiff = board.getSeedsInStore(side) - board.getSeedsInStore(side.opposite());
    int seedsDiff = board.getNumbersOfSeedsInPits(side) - board.getNumbersOfSeedsInPits(side.opposite());

    return kalahsDiff*2 + seedsDiff;
  }

}
