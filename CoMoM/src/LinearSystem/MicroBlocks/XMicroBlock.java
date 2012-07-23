package LinearSystem.MicroBlocks;

import java.util.HashSet;
import java.util.LinkedList;

import javax.naming.OperationNotSupportedException;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import DataStructures.Tuple;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystemSolver.SimpleSolver;
import LinearSystemSolver.Solver;

public class XMicroBlock extends MatrixMicroBlock {

	private Solver solver;
	
	private LUP_Decomposition LUP;
	
	public XMicroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) throws InconsistentLinearSystemException {
		super(qnm, basis, position, h);
		computeDimensions();
		initialise();				
	}

	public XMicroBlock(XMicroBlock micro_block, int current_class) {
		super(micro_block, current_class);
		this.LUP = micro_block.LUP;
	}
	
	public void LUPDecompose() throws InconsistentLinearSystemException {
		LUP = new LUP_Decomposition(basis, position, array);
	}

	@Override
	protected void computeDimensions() {
		rows = MiscFunctions.binomialCoefficient(qnm.M, h) * qnm.M;
		cols = rows;
	}

	/**
	 * 
	 * @param row
	 * @param col The first column of the containing macro block
	 */
	@Override
	public void printRow(int row, int starting_column, int ending_column) {
		int row_to_print = row - position.row;		
		if(row_to_print >= 0 && row_to_print < rows) {
			//print whitespace offset
			for(int i = starting_column; i < position.col; i++) {
				System.out.format("%2s ", " ");
			}
		
			for(int col = 0; col < cols; col++) {				
				System.out.format("%2s ", array[row_to_print][col].toString());
			}
			
			for(int i = position.col + cols; i < ending_column; i++) {
				System.out.format("%2s ", " ");
			}
		}
	}

	@Override
	public int addCE(int vector_index, PopulationChangeVector n, int queue)
			throws BTFMatrixErrorException, InternalErrorException {
		
		int col = basis.indexOf(n, queue);	
		
		col -= position.col;		
			
		if(col < 0 || col >= array.length) throw new BTFMatrixErrorException("Column not in micro block, when considering n:" + n +" queue: " + queue);
			
		array[row][col] = BigRational.ONE;			
			
		for(int s = 1; s <= qnm.R - 1; s++) {
			
			n.plusOne(s);				
			
			col = basis.indexOf(n, queue);				
			n.restore();
				
			col -= position.col;			
			
			//Column is in this block
			if(col >= 0 && col < array.length) {							
				array[row][col] =  qnm.getDemandAsBigRational(queue - 1, s - 1).negate();
			}		
			
		}
			
		//Increment row, ready for next equation
		int inserted_at_row = row;
		row++;
		return inserted_at_row + position.row;
	}

	@Override
	public int addPC(int vector_index, PopulationChangeVector n, int _class)
			throws BTFMatrixErrorException, InternalErrorException {		
		
		int col = basis.indexOf(n, 1);	
		
		col -= position.col;		
		
		if(col < 0 || col >= array.length) throw new BTFMatrixErrorException("Column not in micro block, when considering n:" + n +" class: " + _class);
		if( col + qnm.M - 1 >= array.length) throw new BTFMatrixErrorException("PC will not fit, when considering:" + n +" class: " + _class);
		
		for(int k = 1; k <= qnm.M; k++) {			
			
			array[row][col] = qnm.getDemandAsBigRational( k - 1, _class -1).negate();  
			col++;  //NOTE: Specific to this order
		}
		
		//Increment row, ready for next equation
		int inserted_at_row = row;
		row++;
		return inserted_at_row + position.row;
	}

	//@Override
	public void solve2(BigRational[] rhs) throws BTFMatrixErrorException, OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException {
		System.out.print("Solving XMicroBlock...\n\n");
		
		BigRational[] sysB = new BigRational[rows];
		BigRational[] result = new BigRational[rows];
		
		//copy portion of rhs to sysB
		for(int i = 0; i < rows; i++) {
			sysB[i] = rhs[position.row + i];
		}
		
		System.out.println("SysB");
		MiscFunctions.printMatrix(sysB);
		
		//Solve...
		solver = new SimpleSolver();
		solver.initialise(array,  new LinkedList<Tuple<Integer, Integer>>()	, new HashSet<Integer>());
		
		result = solver.solve(sysB);
		
		System.out.println("result");
		MiscFunctions.printMatrix(result);
		//copy result to basis
		for(int i = 0; i < rows; i++) {
			basis.setValue(result[i], position.row + i);
		}
		
	}
	
	@Override
	public void solve(BigRational[] rhs) {
		LUP.solve(rhs);
	}


}
