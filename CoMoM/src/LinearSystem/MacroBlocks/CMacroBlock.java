package LinearSystem.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MicroBlocks.CMicroBlock;
import LinearSystem.MicroBlocks.MicroBlock;

public class CMacroBlock extends MacroBlock {
	
	public CMacroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position, h);		
		selection_policy = new TypeTwoBlocks(qnm, this); 
	}

	public CMacroBlock(MacroBlock full_block, int current_class) {
		super(full_block, current_class);		
	}
	
	@Override
	protected MicroBlock SubMicroBlock(MacroBlock full_block, int index) {
		return new CMicroBlock((CMicroBlock) full_block.micro_blocks[index], current_class);			
	}

	@Override
	protected void addMicroBlock(Position block_position, int index, int h) throws InternalErrorException {
		micro_blocks[index] = new CMicroBlock(qnm, basis, block_position, h);
	}	

}
