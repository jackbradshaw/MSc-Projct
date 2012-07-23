package LinearSystem.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;

public class XSecondaryMacroBlock extends SecondaryMacroBlock {

	public XSecondaryMacroBlock(QNModel qnm, CoMoMBasis basis,
			Position position, MacroBlock block_1, MacroBlock block_2) throws BTFMatrixErrorException {
		super(qnm, basis, position, block_1, block_2);
	}
	
	public  XSecondaryMacroBlock(XSecondaryMacroBlock full_block, int current_class, MacroBlock block_1, MacroBlock block_2) {
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
			matrix.printRow(row);
			//print whitespace offset
			for(int i = position.col + size.col; i < ending_column; i++) {
				System.out.format("%2s ", " ");					
			}
		}
	}

	
	/**
	 * Inserts CE at the given row (of the overall matrix)
	 * @param row
	 * @param row
	 * @param queue 
	 * @return 
	 * @throws InternalErrorException 
	 */
	@Override
	public int addCE(int row, PopulationChangeVector n, int queue) throws InternalErrorException {
		
		int row_of_block = row - position.row;		
			
		int col;
		
		for(int s = 1; s <= qnm.R - 1; s++) {
			
			n.plusOne(s);				
			
			col = basis.indexOf(n, queue);				
			n.restore();
				
			col -= position.col;			
			
			//Column is in this block
			if(col >= 0 && col < size.col) {							
				matrix.write( row_of_block , col, qnm.getDemandAsBigRational(queue - 1, s - 1).negate());
			}					
		}
		return row;		
	}

	@Override
	public int addPC(int row, PopulationChangeVector n, int _class)
			throws BTFMatrixErrorException, InternalErrorException {
		//Do Nothing
		return row;
	}

	@Override
	protected Position computeDivisions(MacroBlock block_1, MacroBlock block_2) {
		return new Position(block_1.numberOfMicroBlocks(), block_2.numberOfMicroBlocks());
	}

	@Override
	protected Position computeDimensions(MacroBlock block_1, MacroBlock block_2) {
		return new Position(block_1.getRows(), block_2.getCols());
	}

}
