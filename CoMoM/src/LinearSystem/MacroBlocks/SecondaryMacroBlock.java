package LinearSystem.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import LinearSystem.ComponentBlock;
import LinearSystem.Position;

public abstract class SecondaryMacroBlock extends ComponentBlock {

	protected BlockMatrix matrix;
	
	protected SecondaryMacroBlock(QNModel qnm, CoMoMBasis basis, Position position, MacroBlock block_1, MacroBlock block_2) throws BTFMatrixErrorException {
		super(qnm, basis, position);
		initialise(block_1, block_2);
	}
	
	/**
	 * Constructor for lower classes
	 * @param full_block
	 * @param current_class
	 * @param block_1
	 * @param block_2
	 */
	protected  SecondaryMacroBlock(SecondaryMacroBlock full_block, int current_class, MacroBlock block_1, MacroBlock block_2) {
		super(full_block, current_class);
		Position divisions = computeDivisions(block_1, block_2);
		matrix = new BlockMatrix(full_block.matrix, divisions);
		size = matrix.getSize();
	}
	
	private void initialise(MacroBlock block_1, MacroBlock block_2) throws BTFMatrixErrorException {
		size = computeDimensions(block_1, block_2);
		Position divisions = computeDivisions(block_1, block_2);
		matrix = new BlockMatrix(basis, position, size, divisions);
	}
		
	protected abstract Position computeDivisions(MacroBlock block_1, MacroBlock block_2);

	protected abstract Position computeDimensions(MacroBlock block_1, MacroBlock block_2);

	public void multiply(BigRational[] result, BigRational[] input) throws BTFMatrixErrorException {
		matrix.multiply(result, input);
	}
	
	/**
	 * 
	 * @param row
	 * @param column The first column of Y
	 */
	@Override
	public void printRow2(int row) {
		int row_to_print = row - position.row;
		if(row_to_print >= 0 && row_to_print < size.row) {
			//print white space offset
			for(int i = cols_printed; i < position.col; i++) {
				System.out.format("%2s ", " ");	
				cols_printed++;
			}
			matrix.printRow(row);
			cols_printed += matrix.cols;
		}
	}
	
	@Override
	public void solve(BigRational[] rhs) throws BTFMatrixErrorException {
		matrix.solve(rhs);		
	}

}
