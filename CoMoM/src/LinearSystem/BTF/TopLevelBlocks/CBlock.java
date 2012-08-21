package LinearSystem.BTF.TopLevelBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;
import LinearSystem.BTF.MacroBlocks.CMacroBlock;
import LinearSystem.BTF.MacroBlocks.MacroBlock;

public class CBlock extends TopLevelBlock {

	public CBlock(QNModel qnm, CoMoMBasis basis) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, new Position(0,0));	
		selection_policy = new TypeTwoABlocks(qnm, this, current_class);
	}

	public CBlock(QNModel qnm, CoMoMBasis basis, Position position) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);		
		selection_policy = new TypeTwoABlocks(qnm, this, current_class);
	}

	public CBlock(CBlock full_block, int current_class) throws BTFMatrixErrorException {
		super(full_block,  current_class);				
	}
	
	@Override
	protected TopLevelBlock subBlockCopy(int current_class) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		return new CBlock(this, current_class);
	}
	
	@Override
	protected MacroBlock newMacroBlock(Position block_position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		return new CMacroBlock(qnm, basis, block_position, h);
	}
}
