package LinearSystem.TopLevelBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MacroBlocks.MacroBlock;
import LinearSystem.MacroBlocks.XMacroBlock;
import LinearSystem.MacroBlocks.XSecondaryMacroBlock;

public class XBlock extends ATopLevelBlock {

	public XBlock(QNModel qnm, CoMoMBasis basis) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {		
		super(qnm, basis, new Position(0,0));		
	}
	
	public XBlock(QNModel qnm, CoMoMBasis basis, Position position) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);		
	}

	public XBlock(XBlock full_block, int current_class) throws BTFMatrixErrorException {
		super(full_block,  current_class);
	}

	@Override
	protected void addMacroBlock(Position block_position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		macro_blocks[h] = new XMacroBlock(qnm, basis, block_position, h);
	}
	
	@Override
	protected void addSecondaryMacroBlock(int h,
			MacroBlock block_1, MacroBlock block_2) throws BTFMatrixErrorException {
		Position block_position = new Position(block_1.getStartingRow(), block_2.getStartingCol());
		sec_macro_blocks[h] = new XSecondaryMacroBlock(qnm, basis, block_position, block_1, block_2);

	}
	
	public void LUPDecompose() throws InconsistentLinearSystemException {
		for(int i = 0; i < macro_blocks.length; i++) {
			((XMacroBlock) macro_blocks[i]).LUPDecompose();
		}
	}
	
	@Override
	public void printRow2(int row) {
		super.printRow2(row);		
		for(int i = 0; i < sec_macro_blocks.length; i++) {
			sec_macro_blocks[i].printRow2(row);
		}
	}
	
	/**
	 * 
	 * @param row
	 * @param column the end column of the block
	 */
	@Override
	public void printRow(int row, int starting_column, int ending_column) {
		for(int i = 0; i < macro_blocks.length; i++) {
			//System.out.println("Macro Block h = " + i);
			macro_blocks[i].printRow(row, position.col, ending_column);
		}		
		for(int i = 0; i < sec_macro_blocks.length; i++) {			
			sec_macro_blocks[i].printRow(row, starting_column, ending_column);
		}
	}

	@Override
	protected MacroBlock SubMacroBlock(TopLevelBlock full_block, int index) {
		return new XMacroBlock(full_block.macro_blocks[index], current_class);
		
	} 
	
	@Override
	protected void addSubSecMacroBlock(ATopLevelBlock full_block, int index) {
		sec_macro_blocks[index] = new XSecondaryMacroBlock((XSecondaryMacroBlock) full_block.sec_macro_blocks[index], current_class, macro_blocks[index], macro_blocks[index + 1]);		
	}

}