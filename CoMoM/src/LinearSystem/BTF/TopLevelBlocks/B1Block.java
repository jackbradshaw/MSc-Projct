package LinearSystem.BTF.TopLevelBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;
import LinearSystem.BTF.MacroBlocks.B1MacroBlock;
import LinearSystem.BTF.MacroBlocks.MacroBlock;

public class B1Block extends TopLevelBlock {

	public B1Block(QNModel qnm, CoMoMBasis basis) //TODO why two?
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, new Position(0,0));	
		selection_policy = new TypeOneBlocks(qnm, this, current_class);
	}
	
	public B1Block(QNModel qnm, CoMoMBasis basis, Position position)
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);	
		selection_policy = new TypeOneBlocks(qnm, this, current_class);
	}

	public B1Block(B1Block full_block, int current_class) {
		super(full_block, current_class);
	}
	
	@Override
	protected TopLevelBlock subBlockCopy(int current_class) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		return new B1Block(this, current_class);
	}

	@Override
	protected MacroBlock newMacroBlock(Position block_position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		return new B1MacroBlock(qnm, basis, block_position, h);
	}
}
