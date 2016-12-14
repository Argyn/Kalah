package MKAgent;

public class OptimizeResult {
  public int hole;
  public double score;

  public OptimizeResult(int hole, double score) {
    this.hole = hole;
    this.score = score;
  }

  public OptimizeResult(double score) {
    this.score = score;
    this.hole = 0;
  }
}
