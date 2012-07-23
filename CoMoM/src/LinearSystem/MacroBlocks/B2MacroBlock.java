package LinearSystem.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MicroBlocks.B2MicroBlock;
import LinearSystem.MicroBlocks.MicroBlock;

public class B2MacroBlock extends MacroBlock {

	public B2MacroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position, h);		
	}

	public B2MacroBlock(MacroBlock full_block, int current_class) {
		super(full_block, current_class);
		takeHeadMicroBlocks(full_block, current_class); 
	}

	@Override
	protected void addMicroBlock(Position block_position, int index, int h) {
		micro_blocks[index] = new B2MicroBlock(qnm, basis, block_position, h);

	}

	@Override
	public void printRow(int row, int starting_column, int ending_column) {
		for(int i = 0; i < micro_blocks.length; i++) {
			micro_blocks[i].printRow(row, starting_column, ending_column);
		}
	}

	@Override
	protected MicroBlock SubMicroBlock(MacroBlock full_block, int index) {
		return new B2MicroBlock(full_block.micro_blocks[index], current_class);			
	}

}
