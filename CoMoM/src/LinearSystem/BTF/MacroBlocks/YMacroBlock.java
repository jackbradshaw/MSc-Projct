package LinearSystem.BTF.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;
import LinearSystem.BTF.MicroBlocks.MicroBlock;
import LinearSystem.BTF.MicroBlocks.YMicroBlock;

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
	protected MicroBlock newMicroBlock(Position block_position, int h) throws InternalErrorException {
		return new YMicroBlock(qnm, basis, block_position, h);
	}
}
