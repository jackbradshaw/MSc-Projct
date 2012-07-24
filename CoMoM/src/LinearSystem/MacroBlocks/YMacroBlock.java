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
		selection_policy = new TypeOneBlocks(qnm, this); 
	}

	public YMacroBlock(MacroBlock full_block, int current_class) {
		super(full_block, current_class);
	}

	@Override
	protected MacroBlock subBlockCopy(int current_class) {
		return new YMacroBlock(this, current_class);
	}
	
	@Override
	protected void addMicroBlock(Position block_position, int index, int h) {
		micro_blocks[index] = new YMicroBlock(qnm, basis, block_position, h);
	}

	@Override
	protected MicroBlock SubMicroBlock(MacroBlock full_block, int index) {
		return new YMicroBlock((YMicroBlock) full_block.micro_blocks[index], current_class);			
	}

}
