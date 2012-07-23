package LinearSystem.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;

public class YSecondaryMacroBlock extends SecondaryMacroBlock {

	public YSecondaryMacroBlock(QNModel qnm, CoMoMBasis basis,
			Position position, MacroBlock block_1, MacroBlock block_2) throws BTFMatrixErrorException {
		super(qnm, basis, position, block_1, block_2);		
	}

	public  YSecondaryMacroBlock(YSecondaryMacroBlock full_block, int current_class, MacroBlock block_1, MacroBlock block_2) {
		super(full_block, current_class, block_1, block_2);
	}
	
	/**
	 * 
	 * @param row
	 * @param column The first column of Y
	 */
	@Override
	public void printRow(int row, int starting_column, int ending_column) {
		int row_to_print = row - position.row;
		if(row_to_print >= 0 && row_to_print < size.row) {
			//print whitespace offset
			for(int i = starting_column; i < position.col; i++) {
				System.out.format("%2s ", " ");					
			}
			matrix.printRow(row);
		}
	}

	@Override
	public int addCE(int position, PopulationChangeVector n, int queue)
			throws BTFMatrixErrorException, InternalErrorException {
		//Do Nothing
		return position;
	}

	@Override
	public int addPC(int row, PopulationChangeVector n, int _class)
			throws BTFMatrixErrorException, InternalErrorException {
				
		int row_of_block = row - position.row;		
		if(row_of_block < 0 || row_of_block >= size.row) throw new BTFMatrixErrorException("Row: " + row + " not in micro block, when considering n:" + n +" class: " + _class);
		
		n.minusOne(_class);
		int col =  basis.indexOf(n, 0);
		int col_of_block = col - position.col;	
		
		if(col_of_block >= 0 && col_of_block < size.col) { //column is in this block				
			matrix.write(row_of_block, col_of_block, qnm.N.getAsBigRational(_class - 1).subtract(n.getAsBigRational(_class - 1)));		
		}
		n.restore();
		
		return row;
	}

	@Override
	protected Position computeDivisions(MacroBlock block_1, MacroBlock block_2) {
		return new Position(block_2.numberOfMicroBlocks(), block_1.numberOfMicroBlocks());
	}

	@Override
	protected Position computeDimensions(MacroBlock block_1, MacroBlock block_2) {
		return new Position(block_2.getRows(), block_1.getCols());
	}

}
