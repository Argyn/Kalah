package MKAgent;

public class OptimizedMove implements Comparable<OptimizedMove> {

  private int hole;
  private double score;

  public OptimizedMove(int hole, double score) {
    this.hole = hole;
    this.score = score;
  }

  public OptimizedMove(double score) {
    this.score = score;
  }

  public int hole() {
    return hole;
  }

  public double score() {
    return score;
  }

  @Override
  public boolean equals(Object other) {
    if(!(other instanceof OptimizedMove)) {
      return false;
    }

    OptimizedMove otherMove = (OptimizedMove)other;

    return otherMove.score == score && otherMove.hole == hole;
  }

  @Override
  public int compareTo(OptimizedMove other) {
    if(score == other.score) {
      return 0;
    }

    if(score < other.score) {
      return 1;
    }

    return -1;
  }

  @Override
  public String toString() {
    return String.format("Hole: %d, Score: %.2f", hole, score);
  }


}
