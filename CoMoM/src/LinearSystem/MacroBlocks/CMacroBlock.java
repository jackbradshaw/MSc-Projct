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
	protected MacroBlock subBlockCopy(int current_class) {
		return new CMacroBlock(this, current_class);
	}

	@Override
	protected MicroBlock newMicroBlock(Position block_position, int h) throws InternalErrorException {
		return new CMicroBlock(qnm, basis, block_position, h);
	}	

}
