package LinearSystem.BTF.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;
import LinearSystem.BTF.MicroBlocks.CMicroBlock;
import LinearSystem.BTF.MicroBlocks.MicroBlock;

public class CMacroBlock extends MacroBlock {
	
	public CMacroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position, h);		
		selection_policy = new TypeTwoABlocks(qnm, this); 
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
