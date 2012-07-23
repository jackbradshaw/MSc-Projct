package Matrix;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import DataStructures.BigRational;
import DataStructures.QNModel;
import DataStructures.Tuple;

public abstract class Matrix {	

		
	/**
	 * List of positions to update
	 */
	 protected List<Tuple<Integer, Integer>> update_list;
	 
	 /**
	  * Size of Square Matrix
	  */
	 protected int size;	 
	 
		 
	 /**
	  * Constructor
	  * @param qnm The Queuing Network Model under study
	  * @param size The size of the basis (will make a matrix of size:  size x size)
	  */
	 public Matrix(int size) {	
		 this.size = size;
		 update_list = new LinkedList<Tuple<Integer, Integer>>();		 
	 }
	 
	 /**
	  * Writes value v at position (row,col) in matrix
	  * @param v value to be written
	  * @param row row to be written at
	  * @param col column two be written at
	  */
	 public abstract void write( int row, int col, BigRational v);
	 
	 /**
	  * Returns value a (row, col)
	  * @param row row
	  * @param col column
	  * @return array[row][column]
	  */
	 public abstract BigRational get( int row, int col);
	 
	 public void reset() {
		 fillWithZeros();
		 update_list = new LinkedList<Tuple<Integer, Integer>>();			 
	 }
	 
	 /**
	  * Makes every entry in the array 0
	  */
	 public abstract void fillWithZeros();
	 
	 /**
	  * Adds (row, col) to the list of of positions to be updated
	  * @param row row 
	  * @param col column
	  */
	 public void toBeUpdated(int row, int col) {
		 update_list.add(new Tuple<Integer, Integer>(row,col));
	 }
	 
	 /**
	  * Updates the values at the positions in the update_list
	  */
	 public void update() {
		 for(Tuple<Integer,Integer> position : update_list) {
			 write(position.getX(), position.getY(), get(position.getX(),position.getY()).copy().add(BigRational.ONE));
		 }
	 }
	 
	 public List<Tuple<Integer,Integer>> get_update_list() {
		 return update_list;
	 }
	 
	 /**
	  * Multiplies vector by the matrix
	  * @param vector
	  * @return
	  */
	 public abstract BigRational[] multiply(BigRational[] vector);
	 
	 /**
	  * Prints the matrix to screen
	  */
	 public abstract void print();
}
