package LinearSystem.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MicroBlocks.MicroBlock;
import LinearSystem.MicroBlocks.YMicroBlock;

public class YMacroBlock extends MacroBlock {

	public YMacroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position, h);
	}

	public YMacroBlock(MacroBlock full_block, int current_class) {
		super(full_block, current_class);
		takeHeadMicroBlocks(full_block, current_class); 
	}

	@Override
	protected void addMicroBlock(Position block_position, int index, int h) {
		micro_blocks[index] = new YMicroBlock(qnm, basis, block_position, h);
	}

	@Override
	public void printRow(int row, int starting_column, int ending_column) {
		int row_to_print = row - position.row;
		if(row_to_print >= 0 && row_to_print < size.row) {			
			for(int i = 0; i < micro_blocks.length; i++) {
				micro_blocks[i].printRow(row, position.col, ending_column);
			}	
		}		
	}

	@Override
	protected MicroBlock SubMicroBlock(MacroBlock full_block, int index) {
		return new YMicroBlock((YMicroBlock) full_block.micro_blocks[index], current_class);			
	}

}
