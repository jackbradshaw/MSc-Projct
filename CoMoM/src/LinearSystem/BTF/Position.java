package LinearSystem.BTF;

public class Position {
	
	public int row;
	public int col;
	
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Position copy() {
		return new Position(row, col);
	}
	
	public void add(Position offset) {
		this.row += offset.row;
		this.col += offset.col;
	}
}
