package LinearSystem.BTF.TopLevelBlocks;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;
import LinearSystem.BTF.MacroBlocks.B2MacroBlock;
import LinearSystem.BTF.MacroBlocks.MacroBlock;

public class B2Block extends TopLevelBlock {

	public B2Block(QNModel qnm, CoMoMBasis basis)
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, new Position(MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M, 0));	
		selection_policy = new TypeOneBlocks(qnm, this, current_class);
	}
	
	public B2Block(QNModel qnm, CoMoMBasis basis, Position position) //TODO two
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);
		selection_policy = new TypeOneBlocks(qnm, this, current_class);		
	}
	
	public B2Block(B2Block full_block, int current_class) {
		super(full_block, current_class);		
	}

	@Override
	protected TopLevelBlock subBlockCopy(int current_class) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		return new B2Block(this, current_class);
	}
	
	@Override
	protected MacroBlock newMacroBlock(Position block_position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		return new B2MacroBlock(qnm, basis, block_position, h);
	}
	
	public void update(int current_class_population) {
		for(int i = 0; i < macro_blocks.length; i++) {
			((B2MacroBlock) macro_blocks[i]).update(current_class_population);
		}
	}
}
