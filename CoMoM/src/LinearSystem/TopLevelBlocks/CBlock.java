package LinearSystem.TopLevelBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MacroBlocks.CMacroBlock;
import LinearSystem.MacroBlocks.MacroBlock;

public class CBlock extends TopLevelBlock {

	public CBlock(QNModel qnm, CoMoMBasis basis) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, new Position(0,0));		
	}

	public CBlock(QNModel qnm, CoMoMBasis basis, Position position) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);		
	}

	public CBlock(CBlock full_block, int current_class) throws BTFMatrixErrorException {
		super(full_block,  current_class);
		
		takeTailMacroBlocks(full_block, current_class);
	}
	
	@Override
	protected void addMacroBlock(Position block_position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		macro_blocks[h] = new CMacroBlock(qnm, basis, block_position, h);
	}

	@Override
	protected MacroBlock SubMacroBlock(TopLevelBlock full_block, int index) {
		return new CMacroBlock(full_block.macro_blocks[index], current_class);

	}

	@Override
	public void printRow(int row, int starting_column, int ending_column) {
		// TODO Auto-generated method stub

	}

}
