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
	}

	public XMacroBlock(MacroBlock full_block, int current_class) {
		super(full_block, current_class);
		takeHeadMicroBlocks(full_block, current_class); 
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
	
	/**
	 * 
	 * @param row
	 * @param column the starting column of the containing top level block
	 */
	@Override
	public void printRow(int row, int starting_column, int ending_column) {		
		int row_to_print = row - position.row;
		if(row_to_print >= 0 && row_to_print < size.row) {
			for(int i = starting_column; i < position.col; i++) {
				System.out.format("%2s ", " ");	
			}
			for(int i = 0; i < micro_blocks.length; i++) {
				micro_blocks[i].printRow(row, position.col, position.col + size.col);
			}	
		}
	}

	@Override
	protected MicroBlock SubMicroBlock(MacroBlock full_block, int index) {
		return new XMicroBlock((XMicroBlock) full_block.micro_blocks[index], current_class);		
	}
}
