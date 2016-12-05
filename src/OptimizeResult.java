package MKAgent;

public class OptimizeResult {
  public int hole;
  public int score;

  public OptimizeResult(int hole, int result) {
    this.hole = hole;
    this.score = result;
  }

  public OptimizeResult(int score) {
    this.score = score;
    this.hole = 0;
  }
}
