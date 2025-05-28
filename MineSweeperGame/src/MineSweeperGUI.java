import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class MineSweeperGUI {
	private JFrame frame;
	private Grid gameGrid;
	private JButton[][] buttons;
	private boolean[][] revealedCells;
	private JFrame DeveloperFrame;
	private Difficulty selectedDifficulty;

//IMAGES
	private ImageIcon pickaxe;
	private ImageIcon flagIcon;
	private ImageIcon bombIcon;
	private ImageIcon confetiIcon;
	@SuppressWarnings("unused")
	private ImageIcon explosionImg;
	@SuppressWarnings("unused")
	private ImageIcon bombCryIcon;
	@SuppressWarnings("unused")
	private ImageIcon revealIcon; // Eclipse says I can remove these 3 and just leave it with (ImagesHandler = "")
									// but it would make it look more unorganized I think)
	private ImageIcon bombExplotionIcon;

// Define an enum for difficulty levels
	public static enum Difficulty {
		EASY(8, 8, 10, "Easy (8x8, 10 Bombs)"), MEDIUM(10, 10, 25, "Medium (10x10, 25 Bombs)"), // Your original default
		HARD(16, 16, 40, "Hard (16x16, 40 Bombs)"), SUPER_HARD(24, 24, 200, "Super Hard (24×24, 200 Bombs)"),
		IMPOSSIBLE(30, 30, 300, "Impossible (30×30, 300 Bombs)"); // note the semicolon here

		private final int rows;
		private final int cols;
		private final int bombs;
		private final String description;

		Difficulty(int rows, int cols, int bombs, String description) {
			this.rows = rows;
			this.cols = cols;
			this.bombs = bombs;
			this.description = description;
		}

		public int getRows() {
			return rows;
		}

		public int getCols() {
			return cols;
		}

		public int getBombs() {
			return bombs;
		}

		@Override
		public String toString() {
			return description; // This will be displayed in the JOptionPane
		}
	}

	public MineSweeperGUI() {
		this.selectedDifficulty = promptDifficulty();

		if (this.selectedDifficulty == null) {
			// User cancelled or closed the difficulty selection dialog
			System.out.println("No difficulty selected. Existing.");
			System.exit(0);
			return; // Stop initialization
		}
		// Initialize gameGrid with selected difficulty parameters
		// This assumes your Grid class has a constructor like: new Grid(rows, cols,
		// numBombs)
		gameGrid = new Grid(selectedDifficulty.getRows(), selectedDifficulty.getCols(), selectedDifficulty.getBombs());
		buttons = new JButton[gameGrid.getNumRows()][gameGrid.getNumColumns()];
		revealedCells = new boolean[gameGrid.getNumRows()][gameGrid.getNumColumns()];
		createAndShowGUI();
	}

	// Method to prompt the user for difficulty
	private Difficulty promptDifficulty() {
		Difficulty[] options = { Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD, Difficulty.SUPER_HARD,
				Difficulty.IMPOSSIBLE };
		// Parent component is null as the frame isn't created yet
		Difficulty chosenDifficulty = (Difficulty) JOptionPane.showInputDialog(null, "Select Difficulty:",
				"Minesweeper Difficulty", JOptionPane.PLAIN_MESSAGE, pickaxe, // No custom icon
				options, // Array of choices
				options[1] // Default choice (Medium)
		);
		return chosenDifficulty; // Will be null if the user cancels
	}

	private void createAndShowGUI() {
		frame = new JFrame("MineSweeper - " + selectedDifficulty.description);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // This is use to create the X button where you exit a tap

		// Dynamically adjust frame size based on grid dimensions
		int cellSize = 40; // Approximate size of a cell button
		int topUIOffset = 80; // For title bar, potential menu bar
		int sideUIOffset = 30; // For frame borders
		int frameWidth = Math.max(350, gameGrid.getNumColumns() * cellSize + sideUIOffset);
		int frameHeight = Math.max(350, gameGrid.getNumRows() * cellSize + topUIOffset);
		frame.setSize(frameWidth, frameHeight);
		frame.setLayout(new GridLayout(gameGrid.getNumRows(), gameGrid.getNumColumns()));
		frame.setLocationRelativeTo(null); // Center the frame

		// Images Handlers
		flagIcon = ImagesHandler("images/flag.png", 40, 40);
		bombIcon = ImagesHandler("images/bomb.png", 40, 40);
		confetiIcon = ImagesHandler("images/confeti.png", 50, 50);
		explosionImg = new ImageIcon("images/explosion.png");
		bombCryIcon = ImagesHandler("images/bombCry.png", 80, 80);
		revealIcon = ImagesHandler("images/reveal.png", 30, 30);
		bombExplotionIcon = ImagesHandler("images/bombExplotion.png", 40, 40);
		pickaxe = new ImageIcon("images/Pickaxe.png");

		// Create a separate small frame for the developer tools
		DeveloperFrame = new JFrame("Developer Tools");
		DeveloperFrame.setSize(200, 150); // Adjusted size
		// Position developer tools relative to the main frame (e.g., to the right)
		// This needs to be called after the main frame is visible or has its location
		// set.
		// For now, using a fixed offset from screen origin or deferring setLocation.
		DeveloperFrame.setLocation(frame.getX() + frame.getWidth() + 10, frame.getY());

		DeveloperFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		DeveloperFrame.setResizable(false);

		// Reveal Button
		ImageIcon revealIcon = ImagesHandler("images/reveal.png", 40, 40);
		JButton revealButton = new JButton(revealIcon);
		revealButton.setToolTipText("Reveal All Bombs (Testing Only)");
		revealButton.addActionListener(e -> revealAll());

		// Close Button
		JButton closeButton = new JButton("Close");
		closeButton.setToolTipText("Close Developer Tools");
		closeButton.addActionListener(e -> DeveloperFrame.dispose());

		// Arrange buttons vertically
		JPanel devPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		devPanel.add(revealButton);
		devPanel.add(closeButton);

		DeveloperFrame.add(devPanel);
		DeveloperFrame.setVisible(true);

		// Creation of button for the cells which will contain bombs and numbers
		for (int row = 0; row < gameGrid.getNumRows(); row++) {
			for (int col = 0; col < gameGrid.getNumColumns(); col++) {
				JButton button = new JButton();
				buttons[row][col] = button;

				// Design of the buttons
				button.setBackground(Color.LIGHT_GRAY);
				button.setFocusPainted(false); // Removes the weird outline when clicked
				button.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Thin gray border
				button.setFont(new Font("Arial", Font.BOLD, 20)); // Nicer font

				// Button with MouseListener
				int r = row, c = col;
				button.addMouseListener(new MouseAdapter() { // this line was created with MouseListener because I
																// wanted to add flags on the cells

					@Override
					public void mouseClicked(MouseEvent e) { // This method of LefMouse and Right was found on
																// stackOverflow, Link inside GRID file
						if (SwingUtilities.isLeftMouseButton(e)) {
							LeftClick(r, c);
						} else if (SwingUtilities.isRightMouseButton(e)) {
							RightClick(r, c);
						}
					}
				});

				frame.add(button); // adding the buttons to JFrame
			}
		}

		frame.setVisible(true); // Setting visible for the User
	}

	// Tracker of the cell that was clicked
	private int explodedRow = -1;
	private int explodedCol = -1;

	private void LeftClick(int row, int col) {
		if (revealedCells[row][col]) {
			return;
		}

		revealedCells[row][col] = true;

		if (gameGrid.isBombAtLocation(row, col)) {

			explodedRow = row;
			explodedCol = col;

			shakeButton(buttons[row][col]);
			showExplosionAt(row, col); // Explotion Image

			buttons[row][col].setBackground(Color.RED); // Turn the cell background into red
			revealAll(); // Reveal all bombs and numbers

			// ⏳ Then delay the popup
			Timer popupTimer = new Timer(400, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Load explosion background
					ImageIcon explosionBackground = new ImageIcon("images/explosion.png");
					Image explosionImg = explosionBackground.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
					BackgroundPanel panel = new BackgroundPanel(explosionImg);

					// Crying bomb icon
					JLabel cryingBombLabel = new JLabel(new ImageIcon("images/bombCry.png"));
					cryingBombLabel.setHorizontalAlignment(SwingConstants.CENTER);

					// Game over text, the info to center a text in java was found on a website and
					// a video, LINK on GRID file.
					JLabel textLabel = new JLabel("<html><div style='text-align: center;'>"
							+ "<br><b style='font-size:20px;'>BOOM!</b><br><br>" + "Oh No, you clicked a Bomb!<br><br>"
							+ " !!GAME OVER!! Good Luck Next Time! </div></html>", SwingConstants.CENTER);

					textLabel.setFont(new Font("Arial", Font.BOLD, 20));
					textLabel.setOpaque(true);
					textLabel.setBackground(new Color(0, 0, 0, 170)); // semi-transparent black
					textLabel.setForeground(Color.WHITE);

					panel.add(cryingBombLabel, BorderLayout.NORTH);
					panel.add(textLabel, BorderLayout.CENTER);

					JOptionPane.showMessageDialog(frame, panel, "GAME OVER", JOptionPane.PLAIN_MESSAGE);

					askToRestart();
				}
			});
			popupTimer.setRepeats(false);
			popupTimer.start();

		} else {
			revealCell(row, col);

			if (checkWin()) {
				revealAll();
				JPanel winPanel = new JPanel(new BorderLayout());

				// Add confetti image
				JLabel confetiLabel = new JLabel(confetiIcon);
				confetiLabel.setHorizontalAlignment(SwingConstants.CENTER);

				// Add congratulation text
				JLabel winText = new JLabel(
						"<html><div style='text-align: center;'>"
								+ "<br><b style='font-size:20px;'>Congratulations!</b><br><br>"
								+ "You cleared all the mines, what a Champion!<br><br>" + "🏆 YOU WIN! 🏆</div></html>",
						SwingConstants.CENTER);

				winText.setFont(new Font("Arial", Font.BOLD, 18));
				winText.setForeground(new Color(0, 128, 0)); // Green for success!

				winPanel.add(confetiLabel, BorderLayout.NORTH);
				winPanel.add(winText, BorderLayout.CENTER);

				JOptionPane.showMessageDialog(frame, winPanel, "YOU WIN!", JOptionPane.PLAIN_MESSAGE);

				askToRestart();
			}
		}
	}

	private void RightClick(int row, int col) {
		if (revealedCells[row][col])
		 {
			return; // Cannot flag revealed cells
		}

		Icon currentIcon = buttons[row][col].getIcon();
		if (currentIcon == flagIcon) {
			buttons[row][col].setIcon(null); // remove the flag image
		} else {
			buttons[row][col].setIcon(flagIcon); // sets the flag image
		}
	}

	private void revealCell(int row, int col) {
		buttons[row][col].setBackground(Color.WHITE); // Background for selected cells

		int count = gameGrid.getCountAtLocation(row, col);

		buttons[row][col].setIcon(null); // Clear all flag icons

		if (count > 0) {
			buttons[row][col].setText(String.valueOf(count));

			// Setter of color based on the number
			switch (count) {
			case 1:
				buttons[row][col].setForeground(Color.BLUE);
				break;
			case 2:
				buttons[row][col].setForeground(new Color(0, 128, 0));
				break; // this is a green more dark than the original
			case 3:
				buttons[row][col].setForeground(Color.RED);
				break;
			default:
				buttons[row][col].setForeground(new Color(0, 0, 128));
				break; // this is a dark blue or similar
			}
		} else {
			buttons[row][col].setText(""); // Empty for 0s
		}
		if (count == 0) {
			floodReveal(row, col);
		}
	}

	private void floodReveal(int row, int col) {
		for (int distancex = -1; distancex <= 1; distancex++) {
			for (int distancey = -1; distancey <= 1; distancey++) {
				int r = row + distancex;
				int c = col + distancey;
				if (r >= 0 && r < gameGrid.getNumRows() && c >= 0 && c < gameGrid.getNumColumns()) {
					if (!revealedCells[r][c] && !gameGrid.isBombAtLocation(r, c)) {
						revealedCells[r][c] = true;
						revealCell(r, c);
					}
				}
			}
		}
	}

	private void revealAll() {
		for (int i = 0; i < gameGrid.getNumRows(); i++) {
			for (int j = 0; j < gameGrid.getNumColumns(); j++) {
				if (gameGrid.isBombAtLocation(i, j)) {
					if (i == explodedRow && j == explodedCol) {
						buttons[i][j].setIcon(bombExplotionIcon); // Added the special icon when clicked
					} else {
						buttons[i][j].setIcon(bombIcon); // Normal bombs Icon
					}
				} else {
					int count = gameGrid.getCountAtLocation(i, j);

					buttons[i][j].setIcon(null); // This line is used to clear the flags placed after revealing

					if (count > 0) {
						buttons[i][j].setText(String.valueOf(count));

						// Setter of colorers based on the number
						switch (count) {
						case 1:
							buttons[i][j].setForeground(Color.BLUE);
							break;
						case 2:
							buttons[i][j].setForeground(new Color(0, 128, 0));
							break;
						case 3:
							buttons[i][j].setForeground(Color.RED);
							break;
						default:
							buttons[i][j].setForeground(new Color(0, 0, 128));
							break;
						}
					} else {
						buttons[i][j].setText("");
					}

				}
			}
		}
	}

	private boolean checkWin() {
		for (int i = 0; i < gameGrid.getNumRows(); i++) {
			for (int j = 0; j < gameGrid.getNumColumns(); j++) {
				if (!gameGrid.isBombAtLocation(i, j) && !revealedCells[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	private void askToRestart() {
		int response = JOptionPane.showConfirmDialog(frame, "Would you like to play again?", "💥Play💥",
				JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			frame.dispose();
			if (DeveloperFrame != null) {
				DeveloperFrame.dispose(); // close Developer Tools too
			}
			new MineSweeperGUI(); // restart the game
		} else {
			frame.dispose();
			if (DeveloperFrame != null) {
				DeveloperFrame.dispose(); // close Developer Tools
			}
		}
	}

	// Images Handlers
	private ImageIcon ImagesHandler(String path, int width, int height) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image resized = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(resized);
	}

	// Class to make the background of the explosion Image
	@SuppressWarnings("serial")
	class BackgroundPanel extends JPanel {
		private Image backgroundImage;

		public BackgroundPanel(Image backgroundImage) {
			this.backgroundImage = backgroundImage;
			setLayout(new BorderLayout());
		}

		// This .drawImage method, I founded on a youtube video about Graphics link
		// inside Grid File
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
	}

	// Explotion image replace
	private void showExplosionAt(int row, int col) {
		ImageIcon explosionIcon = ImagesHandler("images/bombExplotion.png", 40, 40); // or adjust size
		buttons[row][col].setIcon(explosionIcon);
		buttons[row][col].setBackground(Color.RED); // Optional: make background red too
	}

	// Shaking animation button
	private void shakeButton(JButton button) {
		final Point originalLocation = button.getLocation(); // this line saves the original position in screen
		Timer timer = new Timer(20, null); // 20 is equal to 20 milisecons
		final int[] count = { 0 };

		timer.addActionListener(e -> {
			int x = (int) (Math.random() * 5) - 2; // random small
			int y = (int) (Math.random() * 5) - 2;
			button.setLocation(originalLocation.x + x, originalLocation.y + y);
			count[0]++;
			if (count[0] > 10) { // Shakes for 10 times
				timer.stop();
				button.setLocation(originalLocation); // it goes back to the original location after all the shakes
			}
		});
		timer.start();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MineSweeperGUI::new);
	}
}