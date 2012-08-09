package LinearSystem.BTF.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;
import LinearSystem.BTF.MicroBlocks.B2MicroBlock;
import LinearSystem.BTF.MicroBlocks.MicroBlock;

public class B2MacroBlock extends MacroBlock {

	public B2MacroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position, h);	
		selection_policy = new TypeOneBlocks(qnm, this); 
	}

	public B2MacroBlock(MacroBlock full_block, int current_class) {
		super(full_block, current_class);
	}
	
	@Override
	protected MacroBlock subBlockCopy(int current_class) {
		return new B2MacroBlock(this, current_class);
	}

	@Override
	protected MicroBlock newMicroBlock(Position block_position, int h) {
		return new B2MicroBlock(qnm, basis, block_position, h);
	}
}
