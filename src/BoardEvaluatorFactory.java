package MKAgent;

public class BoardEvaluatorFactory {

  private BoardEvaluatorFactory() {

  }

  public static BoardEvaluator getEvaluator() {
    return new MancalaDiffEvaluator();
  }

}
