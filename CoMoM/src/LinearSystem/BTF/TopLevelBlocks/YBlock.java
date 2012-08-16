package LinearSystem.BTF.TopLevelBlocks;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;
import LinearSystem.BTF.MacroBlocks.MacroBlock;
import LinearSystem.BTF.MacroBlocks.YMacroBlock;
import LinearSystem.BTF.MacroBlocks.YSecondaryMacroBlock;

public class YBlock extends ATopLevelBlock {
	
	public YBlock(QNModel qnm, CoMoMBasis basis) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, new Position(0, MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M));	
		selection_policy = new TypeOneBlocks(qnm, this, current_class);	
	}

	public YBlock(QNModel qnm, CoMoMBasis basis, Position position) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);		
		selection_policy = new TypeOneBlocks(qnm, this, current_class);	
	}

	public YBlock(YBlock full_block, int current_class) throws BTFMatrixErrorException {
		super(full_block,  current_class);
	}

	@Override
	protected TopLevelBlock subBlockCopy(int current_class) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		return new YBlock(this, current_class);
	}
	@Override
	protected MacroBlock newMacroBlock(Position block_position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		return new YMacroBlock(qnm, basis, block_position, h);
	}
	
	@Override
	protected void newSecondaryMacroBlock(int h, MacroBlock block_1, MacroBlock block_2) throws BTFMatrixErrorException {
		Position block_position = new Position(block_2.getStartingRow(), block_1.getStartingCol());
		sec_macro_blocks[h] = new YSecondaryMacroBlock(qnm, basis, block_position, block_1, block_2);
	}
	
	@Override
	public void printRow2(int row) {
		for(int i = 0; i < sec_macro_blocks.length; i++) {
			sec_macro_blocks[i].printRow2(row);
		}
		super.printRow2(row);		
	}
}
