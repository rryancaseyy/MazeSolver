package components;

import java.util.ArrayList;

public class PathFinder {

	private final int PATH 		= 0;
//	private final int BARRIER 	= 1;
	private final int START 	= 2;
	private final int FINISH 	= 3;
	private final int TRAIL 	= 4;
	private final int WRONGPATH = 5;

	// Define movement variables
	private final int RIGHT = 1;
	private final int DOWN 	= 2;
	private final int LEFT 	= 3;
	private final int UP 	= 4;

	private int[][] solvedMaze;
	public ArrayList<Integer> mazeDirections;

	public PathFinder(MazeButton[][] maze, int startY, int startX) {

		solvedMaze = new int[maze.length][maze[0].length];
		findPath(maze, startY, startX);
		mazeDirections = getFinalPath(maze, startY, startX);

	}

	private ArrayList<Integer> findPath(MazeButton[][] maze, int y, int x) {
		
		final DirectionalBlock R = new DirectionalBlock(RIGHT, y, x + 1, (x + 1 < maze[0].length));
		final DirectionalBlock D = new DirectionalBlock(DOWN, y + 1, x, (y + 1 < maze.length));
		final DirectionalBlock L = new DirectionalBlock(LEFT, y, x - 1, (x - 1 >= 0));
		final DirectionalBlock U = new DirectionalBlock(UP, y - 1, x, (y - 1 >= 0));
		final DirectionalBlock[] d = {R, D, L, U};
		
		ArrayList<Integer> finalDirections = new ArrayList<Integer>();

		// Check if one block away from exit
		for (int i = 0; i < d.length; i++) {
			if (d[i].isInBounds && maze[d[i].y][d[i].x].componentType == FINISH) {
				finalDirections = new ArrayList<Integer>();
				finalDirections.add(d[i].direction);
				solvedMaze[y][x] = TRAIL;
				solvedMaze[d[i].y][d[i].x] = START;
				return finalDirections;
			}
		}

		for (int i = 0; i < d.length; i++) {
			if (d[i].isInBounds) {
				if (maze[d[i].y][d[i].x].componentType == PATH) {
					finalDirections = new ArrayList<Integer>();
					finalDirections.add(d[i].direction);
					MazeButton[][] mazeCopy = maze;
					mazeCopy[d[i].y][d[i].x].componentType = TRAIL;
					solvedMaze[y][x] = mazeCopy[y][x].componentType;
					ArrayList<Integer> path = findPath(mazeCopy, d[i].y, d[i].x);
					if (path == null) {
						finalDirections = null;
					}
					else {
						finalDirections.addAll(path);
						return finalDirections;
					}
				}
				// (x + i) % 4 WHERE 1 <= x <= 3
				else if (maze[d[i].y][d[i].x].componentType == TRAIL
						&& maze[d[i].y][d[i].x].componentType != WRONGPATH
						&& (!d[(1 + i) % 4].isInBounds || maze[d[(1 + i) % 4].y][d[(1 + i) % 4].x].componentType != PATH)
						&& (!d[(2 + i) % 4].isInBounds || maze[d[(2 + i) % 4].y][d[(2 + i) % 4].x].componentType != PATH)
						&& (!d[(3 + i) % 4].isInBounds || maze[d[(3 + i) % 4].y][d[(3 + i) % 4].x].componentType != PATH)) {
					finalDirections = new ArrayList<Integer>();
					finalDirections.add(WRONGPATH);
					MazeButton[][] mazeCopy = maze;
					mazeCopy[y][x].componentType = WRONGPATH;
					solvedMaze[y][x] = mazeCopy[y][x].componentType;
					ArrayList<Integer> path = findPath(mazeCopy, d[i].y, d[i].x);
					if (path == null) {
						finalDirections = null;
					}
					else {
						finalDirections.addAll(path);
						finalDirections.remove(finalDirections.size() - 1);
						return finalDirections;
					}
				}
			}
		}
		
		return finalDirections;
		
	}
	
	// Initialize new arraylist to hold coordinates of correct path
	ArrayList<Integer> path = new ArrayList<Integer>();
	
	private ArrayList<Integer> getFinalPath(MazeButton[][] maze, int y, int x) {

		final DirectionalBlock R = new DirectionalBlock(RIGHT, y, x + 1, (x + 1 < solvedMaze[0].length));
		final DirectionalBlock D = new DirectionalBlock(DOWN, y + 1, x, (y + 1 < solvedMaze.length));
		final DirectionalBlock L = new DirectionalBlock(LEFT, y, x - 1, (x - 1 >= 0));
		final DirectionalBlock U = new DirectionalBlock(UP, y - 1, x, (y - 1 >= 0));
		final DirectionalBlock[] d = {R, D, L, U};

		for (int i = 0; i < d.length; i++) {
			if (d[i].isInBounds 
					&& (maze[d[i].y][d[i].x].componentType == TRAIL
					|| maze[d[i].y][d[i].x].componentType == FINISH)) {
				maze[y][x].componentType = WRONGPATH;
				path.add(d[i].direction);
				getFinalPath(maze, d[i].y, d[i].x);
			}
		}
		
		return path;
		
	}

	private class DirectionalBlock {

		int direction;
		int y;
		int x;
		boolean isInBounds;
	
		private DirectionalBlock(int direction, int y, int x, boolean isInBounds) {

			this.direction = direction;
			this.y = y;
			this.x = x;
			this.isInBounds = isInBounds;

		}

	}

}
