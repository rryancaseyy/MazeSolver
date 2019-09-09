package components;

import java.awt.Color;

public class Paint extends Color {

	/**
	 * Default Serial ID
	 */
	private static final long serialVersionUID = 1L;
	
	String name;
	
	public Paint(int r, int g, int b, String name) {
		super(r, g, b);
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
