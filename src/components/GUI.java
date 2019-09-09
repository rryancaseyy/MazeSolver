package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GUI extends JFrame {

	/**
	 * Default Serial ID
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		JFrame frame = new GUI();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Maze Solver");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
	}

	final private int WINDOW_HEIGHT 	= 750;
	final private int WINDOW_WIDTH 		= 750;
	final private int GRID_HEIGHT 		= 30;
	final private int GRID_WIDTH 		= 30;

	final private Color WINDOW_PAINT 	= new Color(255, 255, 255);
	final private Color BARRIER_PAINT 	= new Color(102, 102, 102);
	final private Color PATH_PAINT 		= new Color(255, 255, 255);
	final private Color START_PAINT 	= new Color(255, 204, 153);
	final private Color FINISH_PAINT 	= new Color(153, 204, 255);
	final private Color SOLVE_PAINT		= new Color(204, 255, 204);
	final private Color RESET_PAINT		= new Color(255, 204, 204);
	final private Color TRAIL_PAINT		= new Color(255, 230, 204);

	private GUI() {
		
		this.setSize(WINDOW_HEIGHT, WINDOW_WIDTH);
		this.add(toolbar(), BorderLayout.NORTH);
		this.add(mazeBoard());
		
	}
	
	final private JButton DRAW_BARRIER 	= new JButton("Barrier");
	final private JButton DRAW_PATH 	= new JButton("Path");
	final private JButton DRAW_START 	= new JButton("Start Point");
	final private JButton DRAW_FINISH 	= new JButton("End Point");
	final private JButton CLEAR_BOARD 	= new JButton("Clear Board");
	final private JButton SOLVE			= new JButton("Solve");
	final private JButton RESET			= new JButton("Reset");
	
	private JPanel toolbar() {

		JPanel toolbar 	= new JPanel();
		JPanel left 	= new JPanel();
		JPanel right 	= new JPanel();
		
		JButton[] buttons = new JButton[] {
			DRAW_BARRIER, DRAW_PATH, DRAW_START, DRAW_FINISH, CLEAR_BOARD, 	// Left buttons
			SOLVE, RESET 													// Right buttons
		};

		left.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		right.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		
		toolbar.setBackground(WINDOW_PAINT);
		left.setBackground(WINDOW_PAINT);
		right.setBackground(WINDOW_PAINT);

		for (int i = 0; i < 7; i++) {
			buttons[i].addActionListener(new ClickListener());
			buttons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			if (i < 5) {
				buttons[i].setPreferredSize(new Dimension(110, 25));
				left.add(buttons[i]);
			}
			else {
				buttons[i].setPreferredSize(new Dimension(58, 25));
				right.add(buttons[i]);
			}
		}
		
		DRAW_BARRIER.setBackground(BARRIER_PAINT);
		DRAW_PATH.setBackground(PATH_PAINT);
		DRAW_START.setBackground(START_PAINT);
		DRAW_FINISH.setBackground(FINISH_PAINT);
		CLEAR_BOARD.setBackground(BARRIER_PAINT);
		SOLVE.setBackground(SOLVE_PAINT);
		RESET.setBackground(RESET_PAINT);
		
		DRAW_BARRIER.setForeground(WINDOW_PAINT);
		CLEAR_BOARD.setForeground(WINDOW_PAINT);
		
		toolbar.add(left);
		toolbar.add(right);
		
		return toolbar;
		
	}
	
	private MazeButton[][] maze = new MazeButton[GRID_WIDTH][GRID_HEIGHT];
	
	private JPanel mazeBoard() {

		JPanel mazeBoard = new JPanel();
		mazeBoard.setBackground(WINDOW_PAINT);
		mazeBoard.setLayout(new GridLayout(GRID_HEIGHT, GRID_WIDTH));
		
		for (int i = 0; i < GRID_WIDTH; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				MazeButton mazePanel = mazePanel(i, j);
				mazeBoard.add(mazePanel);
				maze[i][j] = mazePanel;
				maze[i][j].addActionListener(new ClickListener());
			}
		}
		return mazeBoard;
		
	}
	
	private MazeButton mazePanel(int x, int y) {

		MazeButton mazePanel = new MazeButton(x, y);
		mazePanel.setBackground(BARRIER_PAINT);
		mazePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		return mazePanel;
		
	}
	
	final private int PATH 		= 0;
	final private int BARRIER 	= 1;
	final private int START 	= 2;
	final private int FINISH 	= 3;
	final private int TRAIL 	= 4;
	final private int WRONGPATH = 5;

	private Color currentPaint = PATH_PAINT;
	private Color startCoordinatePaint = BARRIER_PAINT;
	private Color finishCoordinatePaint = BARRIER_PAINT;
	private MazeButton startCoordinate;
	private MazeButton finishCoordinate;
	
	ArrayList<Integer> moves;
	
	Timer timer;
	
	class ClickListener implements ActionListener {

		// Begin actionPerformed method
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == DRAW_BARRIER) {
				currentPaint = BARRIER_PAINT;
			}
			else if (e.getSource() == DRAW_PATH) {
				currentPaint = PATH_PAINT;
			}
			else if (e.getSource() == DRAW_START) {
				currentPaint = START_PAINT;
			}
			else if (e.getSource() == DRAW_FINISH) {
				currentPaint = FINISH_PAINT;
			}
			else if (e.getSource() == CLEAR_BOARD) {
				
				for (int row = 0; row < GRID_WIDTH; row++) {
					for (int col = 0; col < GRID_HEIGHT; col++) {
						
						maze[row][col].setBackground(BARRIER_PAINT);
						maze[row][col].componentType = 1;
						startCoordinate = null;
						finishCoordinate = null;
						
					}
				}
				
				startCoordinatePaint = BARRIER_PAINT;
				finishCoordinatePaint = BARRIER_PAINT;
				
			}
			else if (e.getSource() == SOLVE) {
				PathFinder pf = new PathFinder(maze, startCoordinate.y, startCoordinate.x);
				if (startCoordinate != null && finishCoordinate != null) {
					moves = pf.mazeDirections;
					timer = new Timer(200, new TimerListener());
					timer.start();
				}
			}
			else if (e.getSource() == RESET) {
				timer.stop();
				paintedPoints = new ArrayList<Point>();
				for (int i = 0; i < maze[0].length; i++) {
					for (int j = 0; j < maze.length; j++) {
						if (maze[i][j].componentType == TRAIL ||
								maze[i][j].componentType == WRONGPATH) {
							maze[i][j].componentType = PATH;
							maze[i][j].setBackground(PATH_PAINT);
						}
					}
				}
				if (startCoordinate != null && finishCoordinate != null) {
					maze[startCoordinate.y][startCoordinate.x].componentType = START;
					maze[startCoordinate.y][startCoordinate.x].setBackground(START_PAINT);
					maze[finishCoordinate.y][finishCoordinate.x].componentType = FINISH;
					maze[finishCoordinate.y][finishCoordinate.x].setBackground(FINISH_PAINT);
				}
			}
			else {

				if (currentPaint == START_PAINT) {
					handleSinglePainters(START_PAINT, ((MazeButton) e.getSource()));
				}
				else if (currentPaint == FINISH_PAINT) {
					handleSinglePainters(FINISH_PAINT, ((MazeButton) e.getSource()));
				}
				else {
					((MazeButton) e.getSource()).setBackground(currentPaint);
					changeButtonValue(currentPaint, ((MazeButton) e.getSource()));
				}
				
			}

		} // End actionPerformed method
		
		private void handleSinglePainters(Color paint, MazeButton b) {
			
			if (startCoordinatePaint == FINISH_PAINT) {
				startCoordinatePaint = finishCoordinatePaint;
			}
			if (finishCoordinatePaint == START_PAINT) {
				finishCoordinatePaint = startCoordinatePaint;
			}
			
			if (paint == START_PAINT) {
				if (startCoordinate != null) {
					startCoordinate.setBackground(startCoordinatePaint);
					changeButtonValue(startCoordinatePaint, startCoordinate);
				}
				startCoordinatePaint = b.getBackground();
				b.setBackground(paint);
				changeButtonValue(paint, b);
				startCoordinate = b;
			}
			if (paint == FINISH_PAINT) {
				if (finishCoordinate != null) {
					finishCoordinate.setBackground(finishCoordinatePaint);
					changeButtonValue(finishCoordinatePaint, finishCoordinate);
				}
				finishCoordinatePaint = b.getBackground();
				b.setBackground(paint);
				changeButtonValue(paint, b);
				finishCoordinate = b;
			}
			
		} // End handleSinglePainters method
		
		private void changeButtonValue(Color selectedPaint, MazeButton selectedButton) {
			if ((selectedPaint.toString()).equals(PATH_PAINT.toString())) {
				selectedButton.componentType = PATH;
			}
			else if ((selectedPaint.toString()).equals(BARRIER_PAINT.toString())) {
				selectedButton.componentType = BARRIER;
			}
			else if ((selectedPaint.toString()).equals(START_PAINT.toString())) {
				selectedButton.componentType = START;
			}
			else if ((selectedPaint.toString()).equals(FINISH_PAINT.toString())) {
				selectedButton.componentType = FINISH;
			}
			else {
				selectedButton.componentType = BARRIER;
			}
		}

	} // End ClickListener class

	protected ArrayList<Point> paintedPoints;
	
	class TimerListener implements ActionListener {

		// Instantiate move counter
		int moveNum = 0;
		int y = startCoordinate.y;
		int x = startCoordinate.x;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (moveNum < moves.size()) {

				// Display visited blocks
				maze[y][x].setBackground(TRAIL_PAINT);
				
				/**
				 * Numerical Direction Values
				 * 1: Right
				 * 2: Down
				 * 3: Left
				 * 4: Up
				 */
				
				switch (moves.get(moveNum)) {
				case 1: x += 1;
				break;
				case 2: y += 1;
				break;
				case 3: x -= 1;
				break;
				case 4: y -= 1;
				break;
				}
				moveNum++;
				// Display player location
				maze[y][x].setBackground(START_PAINT);
				
				repaint();
				
			} else { timer.stop(); }

		}
		
	}

}
