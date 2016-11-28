import java.util.Scanner;

public class KalahMinMaxTest {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Number of holes = ");
    int numberOfHoles = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Number of seeds = ");
    int numberOfSeeds = scanner.nextInt();


    Board board = new Board(numberOfHoles, numberOfSeeds);
    Kalah kalah = new Kalah(board);


    KalahMiniMax miniMaxer = new KalahMiniMax();
    try {
      Side playerSide = Side.SOUTH;
      Side side = Side.SOUTH;
      while(!kalah.gameOver()) {
        if(side != playerSide) {
          System.out.print("Hole = ");
          int hole = scanner.nextInt();
          side = kalah.makeMove(new Move(side, hole));
          System.out.println(board);
          continue;
        }

        OptimizeResult result = miniMaxer.optimizeNextMove(kalah, board, side, 14);
        System.out.println(String.format(side.name()+" : Playing hole %d, with score %d", result.hole, result.score));
        side = kalah.makeMove(new Move(side, result.hole));
        System.out.println(board);
      }
    } catch(Exception e) {
      System.out.println(kalah.gameOver());
      e.printStackTrace();
    }
  }



}
