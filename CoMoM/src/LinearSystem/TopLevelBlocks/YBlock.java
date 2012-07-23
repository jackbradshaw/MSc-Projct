package LinearSystem.TopLevelBlocks;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MacroBlocks.MacroBlock;
import LinearSystem.MacroBlocks.YMacroBlock;
import LinearSystem.MacroBlocks.YSecondaryMacroBlock;

public class YBlock extends ATopLevelBlock {
	
	public YBlock(QNModel qnm, CoMoMBasis basis) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, new Position(0, MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M));		
	}

	public YBlock(QNModel qnm, CoMoMBasis basis, Position position) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);		
	}

	public YBlock(YBlock full_block, int current_class) throws BTFMatrixErrorException {
		super(full_block,  current_class);
	}

	@Override
	protected void addMacroBlock(Position block_position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		macro_blocks[h] = new YMacroBlock(qnm, basis, block_position, h);
	}
	
	@Override
	protected void addSecondaryMacroBlock(int h, MacroBlock block_1, MacroBlock block_2) throws BTFMatrixErrorException {
		Position block_position = new Position(block_2.getStartingRow(), block_1.getStartingCol());
		sec_macro_blocks[h] = new YSecondaryMacroBlock(qnm, basis, block_position, block_1, block_2);
	}

	/**
	 * 
	 * @param row
	 * @param column the start column of the block //TODO column un-used
	 */
	public void printRow(int row, int starting_column, int ending_column) {
		
		for(int i = 0; i < sec_macro_blocks.length; i++) {			
			sec_macro_blocks[i].printRow(row, starting_column, ending_column);
		}
		for(int i = 0; i < macro_blocks.length; i++) {
			macro_blocks[i].printRow(row, starting_column, ending_column);
		}		
		
	}

	@Override
	protected void addSubSecMacroBlock(ATopLevelBlock full_block, int index) {
		sec_macro_blocks[index] = 
				new YSecondaryMacroBlock((YSecondaryMacroBlock) full_block.sec_macro_blocks[index], 
				current_class, macro_blocks[index], 
				macro_blocks[index + 1]);
		
	}

	@Override
	protected MacroBlock SubMacroBlock(TopLevelBlock full_block, int index) {
		return new YMacroBlock(full_block.macro_blocks[index], current_class);		
	}
	
	@Override
	public void printRow2(int row) {
		for(int i = 0; i < sec_macro_blocks.length; i++) {
			sec_macro_blocks[i].printRow2(row);
		}
		super.printRow2(row);		
	}
}
