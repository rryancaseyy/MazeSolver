package components;

import javax.swing.JButton;

public class MazeButton extends JButton {

	/**
	 * Default serial ID
	 */
	private static final long serialVersionUID = 1L;
	
	int componentType;
	int y;
	int x;

	public MazeButton(int y, int x) {
		
		componentType = 1;
		this.y = y;
		this.x = x;
		
	}
	
	

}
