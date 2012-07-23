package LinearSystem.TopLevelBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MacroBlocks.B1MacroBlock;
import LinearSystem.MacroBlocks.MacroBlock;

public class B1Block extends TopLevelBlock {

	public B1Block(QNModel qnm, CoMoMBasis basis)
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, new Position(0,0));		
	}
	
	public B1Block(QNModel qnm, CoMoMBasis basis, Position position)
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);		
	}

	public B1Block(B1Block full_block, int current_class) {
		super(full_block, current_class);
		takeLeadingMacroBlocks(full_block, current_class);
	}

	@Override
	protected void addMacroBlock(Position block_position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		macro_blocks[h] = new B1MacroBlock(qnm, basis, block_position, h);
	}

	@Override
	public void printRow(int row, int starting_column, int ending_column) {
		for(int i = 0; i < macro_blocks.length; i++) {
			macro_blocks[i].printRow(row, starting_column, ending_column);
		}
	}

	@Override
	protected MacroBlock SubMacroBlock(TopLevelBlock full_block, int index) {
		return new B1MacroBlock(full_block.macro_blocks[index], current_class);		
	}

}
