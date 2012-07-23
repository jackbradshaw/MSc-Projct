package LinearSystem.TopLevelBlocks;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MacroBlocks.B2MacroBlock;
import LinearSystem.MacroBlocks.MacroBlock;

public class B2Block extends TopLevelBlock {

	public B2Block(QNModel qnm, CoMoMBasis basis)
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, new Position(MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M, 0));		
	}
	
	public B2Block(QNModel qnm, CoMoMBasis basis, Position position)
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);
		
	}
	
	public B2Block(B2Block full_block, int current_class) {
		super(full_block, current_class);
		takeLeadingMacroBlocks(full_block, current_class);
	}

	@Override
	protected void addMacroBlock(Position block_position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		macro_blocks[h] = new B2MacroBlock(qnm, basis, block_position, h);

	}

	@Override
	public void printRow(int row, int starting_column, int ending_column) {
		for(int i = 0; i < macro_blocks.length; i++) {
			macro_blocks[i].printRow(row, starting_column, ending_column);
		}
	}

	@Override
	protected MacroBlock SubMacroBlock(TopLevelBlock full_block, int index) {
		return new B2MacroBlock(full_block.macro_blocks[index], current_class);		
	}

}