package Matrix;

import Utilities.MiscFunctions;
import DataStructures.BigRational;
import DataStructures.QNModel;

public class StandardMatrix extends Matrix {

	private BigRational[][] array;
	
	public StandardMatrix( int size) {
		super(size);
		array = new BigRational[size][size];
		
	}

	 /**
	  * Makes every entry in the array 0
	  */
	 public void fillWithZeros() {
		 for (int i = 0; i < size; i++) {
	    	 for (int j = 0; j < size; j++) {
	    		 array[i][j] = BigRational.ZERO; 	    		
	    	 }	    		   
	     }
	 }
	 
	/**
	 * Writes value v at position (row,col) in matrix
	 * @param v value to be written
	 * @param row row to be written at
	 * @param col column two be written at
	*/
	@Override
	public void write(int row, int col, BigRational v) {
		array[row][col] = v.copy();
		//TODO do you need to copy?
	}
	
	/**
	 * Prints the matrix to screen
	 */
	@Override
	public void print() {
		MiscFunctions.dotprintMatrix(array);
	}

	/**
	 * Returns value a (row, col)
	 * @param row row
	 * @param col column
	 * @return array[row][column]
	 */
	@Override
	public BigRational get(int row, int col) {
		return array[row][col];
	}

	public BigRational[][] getArray() {
		return array;
	}
	
	/**
	 * Multiplies v by the matrix
	 * @param v
	 * @return
	 */
	@Override
	public BigRational[] multiply(BigRational[] v) {
		//Taken for MiscFunctions
		//TODO: Matrix multiplication must be parallelised
		BigRational[][] A = array;
        int rowsA = A.length;
        int columnsA = A[0].length;
        int rowsV = v.length;

        if (columnsA == rowsV) {
            BigRational[] c = new BigRational[rowsA];
            for (int i = 0; i < rowsA; i++) {
                c[i] = BigRational.ZERO;
                for (int j = 0; j < columnsA; j++) {
                    if (!A[i][j].isZero()) {
                        if (v[j].isPositive()) {
                            c[i] = c[i].add(A[i][j].multiply(v[j]));
                        } else if (v[j].isUndefined()) {
                            c[i] = new BigRational(-1);
                            c[i].makeUndefined();
                            break;
                        }
                    }
                }
            }
            return c;
        } else {
            throw new ArithmeticException("Cannot multiply matrices with wrong sizes! (" + rowsA + "x" + columnsA + ")x(" + rowsV + "x1)");
        }
    }
	
	/**
	 * Adapted from code found at http://professorjava.weebly.com/matrix-determinant.html
	 * @param matrix
	 * @return
	 */
	private BigRational determinant(BigRational[][] matrix){ //method sig. takes a matrix (two dimensional array), returns determinant.
	    BigRational sum = BigRational.ZERO; 
	    BigRational s;
	    if(matrix.length==1){  //bottom case of recursion. size 1 matrix determinant is itself.
	        return(matrix[0][0]);
	    }
	    for(int i=0;i<matrix.length;i++){ //finds determinant using row-by-row expansion
	    BigRational[][]smaller= new BigRational[matrix.length-1][matrix.length-1]; //creates smaller matrix- values not in same row, column
	    	for(int a=1;a<matrix.length;a++){
	    		for(int b=0;b<matrix.length;b++){
	    			if(b<i){
	    				smaller[a-1][b]=matrix[a][b];
	    			}
	    			else if(b>i){
	    				smaller[a-1][b-1]=matrix[a][b];
	    			}
	    		}
	    	}
	    	if(i%2==0){ //sign changes based on i
	    		s = BigRational.ONE;
	    	}
	    	else{
	    		s= BigRational.ONE.negate();
	    	}
	    	sum = sum.add( s.multiply(matrix[0][i].multiply((determinant(smaller))))); // recursive step: determinant of larger determined by smaller.
	    	}
	    return(sum); //returns determinant value. once stack is finished, returns final determinant.
	}
	
	public  BigRational getDeterminatnt() {
		return determinant(array);
	}

}
