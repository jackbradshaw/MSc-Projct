package LinearSystem;

import javax.naming.OperationNotSupportedException;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;

public abstract class ComponentBlock {
	
	protected static int cols_printed;
	
	protected Position position;
	
	protected CoMoMBasis basis;
	
	protected QNModel qnm;
	
	protected int current_class;
	
	protected static int current_class_population;
	
	protected ComponentBlock(QNModel qnm, CoMoMBasis basis, Position position) {
		
		this.qnm = qnm;
		this.basis = basis;
		this.position = position;	
		
		//Default value for current class since the matrix is only constructed once, for the final class.
		current_class = qnm.R;
	}
	
	/**
	 * Copy Constructor - Shallow Copy
	 * @param block
	 */
	protected ComponentBlock(ComponentBlock block, int current_class) {
		this.position = block.position;
		this.basis = block.basis;
		this.qnm = block.qnm;
		this.current_class = current_class;
	}

	public int getStartingCol() {		
		return position.col;
	}
	
	public int getStartingRow() {		
		return position.row;
	}
	
	public static void newLine() {
		cols_printed = 0;
		System.out.print("\n");
	}
	
	public static void setCurrentClassPopulation(int population) {
		current_class_population = population;
	}
	public abstract void printRow2(int row);
	
	public abstract int addCE(int position, PopulationChangeVector n, int queue) throws BTFMatrixErrorException, InternalErrorException;
	
	public abstract int addPC(int position, PopulationChangeVector n, int _class) throws BTFMatrixErrorException, InternalErrorException;
	
	public abstract void printRow(int row, int starting_column, int ending_column);
	
	public abstract void multiply(BigRational[] result, BigRational[] input) throws BTFMatrixErrorException;
	
	public abstract void solve(BigRational[] rhs) throws BTFMatrixErrorException, OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException;
}
