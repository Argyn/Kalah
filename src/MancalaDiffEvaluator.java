package MKAgent;

public class MancalaDiffEvaluator implements BoardEvaluator {

  public MancalaDiffEvaluator() {

  }

  public double evaluateBoard(Board board, Side side) {
    double kalahsDiff = board.getSeedsInStore(side) - board.getSeedsInStore(side.opposite());
    double seedsDiff = board.getNumbersOfSeedsInPits(side) - board.getNumbersOfSeedsInPits(side.opposite());

    return kalahsDiff*2 + seedsDiff;
  }

}
