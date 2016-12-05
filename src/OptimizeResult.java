package MKAgent;

public class OptimizeResult {
  public int hole;
  public int score;

  public OptimizeResult(int hole, int score) {
    this.hole = hole;
    this.score = score;
  }

  public OptimizeResult(int score) {
    this.score = score;
    this.hole = 0;
  }
}
