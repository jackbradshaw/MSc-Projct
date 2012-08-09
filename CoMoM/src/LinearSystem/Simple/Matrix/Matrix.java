package LinearSystem.Simple.Matrix;

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
	  * Returns the matrix as a BigRational[][] array
	  * @return underlying matrix
	  */
	 public abstract BigRational[][] getArray();
	 
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
	 

	 /**
	  * Multiplies vector by the matrix
	  * @param vector
	  * @return
	  */
	 public abstract BigRational[] multiply(BigRational[] vector);
	 
	 /**
	  * Adds (row, col) to the list of of positions to be updated
	  * @param row row 
	  * @param col column
	  */	 
	 public void toBeUpdated(int row, int col) {
		 update_list.add(new Tuple<Integer, Integer>(row,col));
	 }
	 
	 /**
	  * @return the list of positions to be updated
	  */
	 public List<Tuple<Integer,Integer>> getUpdateList() {
		 return update_list;
	 }
	 
	 /**
	  * Reset the data structures for the generation of the linear system for a new class.
	  */
	 public void reset() {
		 fillWithZeros();
		 update_list = new LinkedList<Tuple<Integer, Integer>>();			 
	 }
	 	 
	 /**
	  * Makes every entry in the array 0
	  */
	 protected abstract void fillWithZeros();
	 
	 /**
	  * Prints the matrix to screen
	  */
	 public abstract void print();
}
