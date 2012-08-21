package LinearSystem.BTF.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;
import LinearSystem.BTF.MicroBlocks.MicroBlock;
import LinearSystem.BTF.MicroBlocks.XMicroBlock;

public class XMacroBlock extends MacroBlock {

	public XMacroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position, h);
		selection_policy = new TypeOneBlocks(qnm, this); 
	}

	public XMacroBlock(MacroBlock full_block, int current_class) {
		super(full_block, current_class);
	}

	@Override
	protected MacroBlock subBlockCopy(int current_class) {
		return new XMacroBlock(this, current_class);
	}
	
	public void LUPDecompose(boolean in_place) throws InconsistentLinearSystemException {
		for(int i = 0; i < micro_blocks.length; i++) {
			((XMicroBlock) micro_blocks[i]).LUPDecompose(in_place);
		}
	}
	
	@Override
	protected MicroBlock newMicroBlock(Position block_position, int h) throws InconsistentLinearSystemException, InternalErrorException {
		return new XMicroBlock(qnm, basis, block_position, h);
	}
}
