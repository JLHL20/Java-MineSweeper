import java.util.Random;

/*

Website used for completing this GRID file:

https://www.geeksforgeeks.org/cpp-implementation-minesweeper-game/ used as a helper to get the graph.

Websites used to complete the GUI file:

1- https://stackoverflow.com/questions/15957076/java-swing-hold-both-mouse-buttons
(how to add an action to right mouse clicks)

2- https://stackoverflow.com/questions/13360430/jframe-dispose-vs-system-exit
(Used to learn the differnce between System.exit() and JFrame.dispose() )

3- https://www.youtube.com/watch?v=KcEvHq8Pqs0
(Youtube video about graphics and manipulation of images in java)

4- https://www.geeksforgeeks.org/java-util-timer-class-java/
(Websited used to learn about the timers for my game)

5- https://stackoverflow.com/questions/6810581/how-to-center-the-text-in-a-jlabel
5.1 - https://www.youtube.com/watch?v=8PrnvoEvh-k
(Website used to learn how to center a text in a JLabel) Along with a youtube video)

Count all the mines in the 8 adjacent cells

    N.W   N   N.E
      \   |   /
       \  |  /
  W-----Cell-----E
        / | \
       /  |  \
    S.W   S   S.E

Cell-->Current Cell (row, column)
N -->  North        (row-1, column)
S -->  South        (row+1, column)
E -->  East         (row, column+1)
W -->  West         (row, column-1)
N.E--> North-East   (row-1, column+1)
N.W--> North-West   (row-1, column-1)
S.E--> South-East   (row+1, column+1)
S.W--> South-West   (row+1, column-1)

*/

public class Grid {

	private boolean[][] bombGrid;
	private int[][] countGrid;
	private int numRows;
	private int numColumns;
	private int numBombs;

	// This is the 10x10, 25 bombs
	public Grid() {
		this(10, 10, 25);
	}

	// The Grid that can be modified
	public Grid(int rows, int columns) {
		this(rows, columns, 25);
	}

	// Fully modified grid with whatever values you want
	public Grid(int rows, int columns, int numBombs) {
		this.numRows = rows;
		this.numColumns = columns;
		this.numBombs = numBombs;
		bombGrid = new boolean[rows][columns];
		countGrid = new int[rows][columns];
		createBombGrid();
		createCountGrid();
	}

	private void createBombGrid() {
		Random randomNum = new Random(); // Random number generator
		int bombPlacer = 0; // Counter of bombs to handle the #bombs placed
		while (bombPlacer < numBombs) { // Looper
			int r = randomNum.nextInt(numRows); // r = rows generates a random num between 0 < numRows
			int c = randomNum.nextInt(numColumns); // c = columns generates a random num between 0 < numColums
			if (!bombGrid[r][c]) { // Checks if there is not a bomb already inside the cell
				bombGrid[r][c] = true; // Place the bomb
				bombPlacer++; // increments de counter of bombs A.K.A (bombPlacer) by 1
			}
		}
	}

	private void createCountGrid() {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) { // these 2 loops are base loops to go inside a 2D Array
				int count = 0; // Counter of grids

				// I am going to use distX / distY referring to --> "Distance of X / Distance of
				// Y"
				for (int distx = -1; distx <= 1; distx++) { // These 2 for loops are use to iterate in the 8 directions
					for (int disty = -1; disty <= 1; disty++) { // 9 if we include the center, it goes like the graph
																// above
						int numbersi = i + distx;
						int numbersj = j + disty;
						if (numbersi >= 0 && numbersi < numRows && numbersj >= 0 && numbersj < numColumns
								&& bombGrid[numbersi][numbersj]) {
							count++;
						}
					}
				}

				countGrid[i][j] = count; // The counter ++ depending of how many bombs are around
			}
		}
	}

	// Getters
	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public int getNumBombs() {
		return numBombs;
	}

	public boolean[][] getBombGrid() {
		boolean[][] BombGridCopy = deepCopy2DArrayOfBombGrid(bombGrid);
		return BombGridCopy;

	}

	public int[][] getCountGrid() {
		int[][] CountGridCopy = deepCopy2DArrayOfCountGrid(bombGrid);
		return CountGridCopy;
	}

	public boolean isBombAtLocation(int row, int col) {
		return bombGrid[row][col];
	}

	public int getCountAtLocation(int row, int col) {
		return countGrid[row][col];
	}

	private boolean[][] deepCopy2DArrayOfBombGrid(boolean[][] bombGrid) {
		int rows = bombGrid.length;
		int colums = bombGrid[0].length;

		boolean[][] BombGridCopy = new boolean[rows][colums];

		for (int i = 0; i < rows; i++) {

			BombGridCopy[i] = new boolean[bombGrid[i].length]; // use to create space on the Rows

			for (int j = 0; j < colums; j++) {

				BombGridCopy[i][j] = bombGrid[i][j]; // assign the Copy with the values of the original
			}
		}
		return BombGridCopy;
	}

	// This method was just copy and paste tbh from the one above ^^
	private int[][] deepCopy2DArrayOfCountGrid(boolean[][] bombGrid2) {
		int rows = countGrid.length;
		int colums = countGrid[0].length;

		int[][] CountGridCopy = new int[rows][colums];

		for (int i = 0; i < rows; i++) {

			CountGridCopy[i] = new int[countGrid[i].length];

			for (int j = 0; j < colums; j++) {

				CountGridCopy[i][j] = countGrid[i][j];
			}
		}

		return CountGridCopy;
	}

}