/**
 * Represents a Queen's location
 * @author turnera1
 *
 */
public class QueenLocation {
	
	private int row, col;

	public QueenLocation(int row, int col) {
		
		setRow(row);
		setCol(col);
	
	}
	
	public int getRow(){
		
		return row;
	}
	
	public int getCol(){
		
		return col;
	}
	
	
	public void setRow(int row){
		
		this.row = row;
	}
	
	public void setCol(int col){
		
		this.col = col;
	}
	
	public int shiftRight(){
		
		return ++col;
		
	}

}
