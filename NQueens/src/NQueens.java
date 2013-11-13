import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.prefs.BackingStoreException;

/*
 *@author Anthony Turner
 *<b>Description:</b>NQueens assignment #2 CSIII
 *<b>Date:</b> September 11, 2013
 */

public class NQueens{
	
	public static final String QUEEN_PIECE = "Q";
	public static final String EMPTY_BOARD_LOCATION = " ";

	private Stack<QueenLocation>queen_locations;//Holds locations of each queen on the board.
	private String[][] board; //board is a visual representation of each queen on the NxN board
	private int filled;

	
	/**
	 * Construct a new NQueens problem solver with a given number of queens
	 * 
	 * @param n the number of queens for the board
	 */
	public NQueens(int n){

		queen_locations = new Stack<QueenLocation>();
		initBoard(n);
		evaluateQueens(n);
	}
	

	/*
	 * Initialize an empty board
	 */
	private void initBoard(int n) {
		
		board = new String[n][n];
		for(int row = 0; row < n; row++){

			for(int col = 0; col < n; col++){
				
				board[row][col] = EMPTY_BOARD_LOCATION;
				
			}
		}
	}
	

	private void evaluateQueens(int num_queens){
		
		int current_row = 0, current_col = 0, total_solutions = 0;

		addQueen(current_row, current_col);
		filled = 0;
		
		boolean success = false;
		
		while(!success && !queen_locations.isEmpty()){
			
			QueenLocation current_queen = itemAt(current_row);

			if( !hasConflict(current_queen)){//Queens don't conflict
			
				filled++;
				if( filled == num_queens){

					total_solutions++;
					printQueens();//Print each solution
					
					//The following code can be improved, and is similiar to the backtrack process in this method's last else statement
					//If I had more time, i'd refactor to get rid of the duplicate code present in both
					while( !queen_locations.isEmpty() ){
						
						//Remove queen from board display
						queen_locations.pop();
						filled--;
						board[current_row][current_col] = EMPTY_BOARD_LOCATION;	
					
						//special case that signals we have found all solutions
						if( queen_locations.isEmpty() ){
							
							success = true;
							break;
						}else{
						
							//Get current queen and try shifting it right
							//If we succeed, then we found our next position for the next solution
							current_queen = queen_locations.peek();
							current_row = current_queen.getRow();
							current_col = current_queen.getCol();
							if( canShiftRight(current_queen)){
								
								current_col = moveQueenRight(current_queen);
								filled--;
								break;
							}
						}
						
						
					}
					
				}else{
					
					//Move to next row
					current_row++;
					current_col = 0;
					addQueen(current_row, current_col);

				}

			}
			//Otherwise, there is a conflict
			else if( canShiftRight(current_queen)){
				//Move queen rightwards
				current_col = moveQueenRight(current_queen);				
			}
			//Else, there is a conflict and we can't shift right, so backtrack
			else{
				
				while(  !canShiftRight(current_queen) ){

					queen_locations.pop();//pop stack backtracking
					filled--;
					board[current_row][current_col] = EMPTY_BOARD_LOCATION;//Remove queen from board display
					
					if( queen_locations.isEmpty() ){
						success = true;
						break;
						
					}else{
					
						current_row = queen_locations.peek().getRow();
						current_queen = itemAt(current_row);

					}
				}
				
				if( !success)				
					current_col = moveQueenRight(current_queen);
		
			}
			
		}
		
		if( success ){
			print("" + total_solutions + " solutions found");
		}
		else
			print("No solutions found");
		
	}
	
	public void print(String msg){
		
		System.out.println(msg);
	}
	
	/*
	 * This method is not used due to lack of time of reimplementing it
	 * This method keeps popping queens (moving backwards) until we find a queen that can move right
	 * this method returns the row where the queen backtracked to (destination).
	 */
	private int backTrack(){
		
		//Backtrack
		boolean is_back_tracking = true;
		int current_row = 0; 
		
		while( !queen_locations.isEmpty() && is_back_tracking  ){
			
			//Move back down a row to previous queen
			QueenLocation queen = queen_locations.pop();//Remove the top queen
			current_row = queen.getRow();
			int current_col = queen.getCol();	
			board[current_row][current_col] = EMPTY_BOARD_LOCATION;//Remove queen from board display

			filled--;
			current_row--;//Move to previous queen's row

			QueenLocation current_queen = itemAt(current_row);

			//Try moving the queen right
			if( canShiftRight(current_queen) ){
				
				moveQueenRight(current_queen);
				is_back_tracking = false;
			}
		}
		
		return current_row;
	}

	/*
	 * Adds a new Queen piece at the given row and column
	 */
	private QueenLocation addQueen(int row, int col) {

		QueenLocation queen = new QueenLocation(row, col);
		queen_locations.push(queen);
		board[row][col] = QUEEN_PIECE;
		return queen;

	}

	/**
	 * Gets the item (queen's location) that is n items from the top of the stack<br>
	 * <b>Precondition:</b> 0 <= n and n < size() <br>
	 * <b>Postcondition:</b> The return value is the item that is n from the top<br>
	 * (with the top at n=0, the next at n=1, and so on).<br> 
	 * The stack is not changed.<br>
	 *  
	 * @param n the item at the nth position to get starting from the top of the stack moving down.
	 * @return the item at the nth position from the top of the stack
	 * @throws NoSuchElementException if the nth value is negative or greater than the number of queen locations 
	 */
	public QueenLocation itemAt(int n){

		if( n < 0 || n >= queen_locations.size() ){

			throw new NoSuchElementException("No item at position " + n +  " exists");

		}

		return queen_locations.get(n);
	}


	/*
	 * Moves the given queen right and returns the new column 
	 * which it was moved to
	 */

	private int moveQueenRight(QueenLocation queen) {
		//Move current queen right adjusting the record on the top of the stack to indicate the new position
		int col = queen.getCol();
		int row = queen.getRow();
		
		board[row][col] = EMPTY_BOARD_LOCATION;//Erase previous queen's position
		
		col = queen.shiftRight();
		
		board[row][col] = QUEEN_PIECE;
		return col;
	}

	/*
	 * *********************************************************************************************************************
	 * 
	 * 										Display/output methods
	 * 
	 * *********************************************************************************************************************
	 */
	
	private void printQueens() {
		
		//A terrible method for printing the queens.
		//Four for loops is too many, 
		//but it prints out a nice display
		//TODO improve!
	
		for(int x = 1; x < board.length*4; x++){

			System.out.print("=");
		}
		
		System.out.println("");

		for(int n_row = 0; n_row < board.length; n_row++){

			for(int n_col = 0; n_col < board.length; n_col++){
				
				
				System.out.print(board[n_row][n_col]);
				System.out.print(" | ");	
			}	
			
			System.out.println("");

			for(int x = 1; x < board.length*4; x++){

				System.out.print("=");
			}
			System.out.println("");
		}
		
		System.out.println("\n");
	}
	
	
	/*
	 * *********************************************************************************************************************
	 * 
	 * 										Boolean/Predicate methods
	 * 
	 * *********************************************************************************************************************
	 */

	/*
	 * Determines if the Queen at the current row and column can be shifted right
	 */
	private boolean canShiftRight(QueenLocation current_queen) {


		if( (current_queen.getCol() +1) < board.length){
		
			return true;
		}
		else
			return false;
	}
	
	private boolean hasConflict(QueenLocation current_queen) {
		
		int row = current_queen.getRow();
		int col = current_queen.getCol();
	
		if( hasRowConflict(row) || hasColumnConflict(row, col) || hasDiagonalConflict(row,col)){
			
			return true;
			
		}else{
			
			return false;
		}
	}

	private boolean hasRowConflict(int current_row) {
		
		//Check same row +1 and all possible columns
		
		for(int other_row = 0; other_row <  queen_locations.size() -1; other_row++){
					
			if( other_row == current_row){//The two queens are in the same row which is not valid
				
				return true;
			}
		}
		
		return false;//No horizontal conflicts have occurred.
	}


	private boolean hasColumnConflict(int row, int col) {
		
		for(int n = 0; n <  queen_locations.size() -1; n++){
			
			QueenLocation next_queen = itemAt(n);
			int other_column = next_queen.getCol();

			if( other_column == col){//The two queens are in the same column which is not valid
				return true;
			}
		}
		
		return false;//No horizontal conflicts have occurred.
	}

	private boolean hasDiagonalConflict(int current_row, int current_col) {
		
		for(int other_row = 0; other_row <  queen_locations.size() -1; other_row++){
			
			QueenLocation next_queen = itemAt(other_row);
			int other_col = next_queen.getCol();
			
			int delta_row = Math.abs(other_row - current_row);
			int delta_col =  Math.abs(other_col - current_col);
			
			if( delta_row == delta_col ){//The queens are diagonal from each other
			
				return true;
			}
		}	
		
		return false;//No conflicts
	}

	public static void main(String[]args){

		final int NUM_QUEENS = 7;
		new NQueens(NUM_QUEENS);

	}

}
