package LinearSystem.MacroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MicroBlocks.MicroBlock;
import LinearSystem.MicroBlocks.XMicroBlock;

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
	
	public void LUPDecompose() throws InconsistentLinearSystemException {
		for(int i = 0; i < micro_blocks.length; i++) {
			((XMicroBlock) micro_blocks[i]).LUPDecompose();
		}
	}
	
	@Override
	protected void addMicroBlock(Position block_position, int index, int h) throws InconsistentLinearSystemException {
		micro_blocks[index] = new XMicroBlock(qnm, basis, block_position, h);
	}

	@Override
	protected MicroBlock SubMicroBlock(MacroBlock full_block, int index) {
		return new XMicroBlock((XMicroBlock) full_block.micro_blocks[index], current_class);		
	}
}
