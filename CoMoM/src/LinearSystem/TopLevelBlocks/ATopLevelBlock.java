package LinearSystem.TopLevelBlocks;

import javax.naming.OperationNotSupportedException;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;
import LinearSystem.MacroBlocks.MacroBlock;
import LinearSystem.MacroBlocks.SecondaryMacroBlock;

public abstract class ATopLevelBlock extends TopLevelBlock {

	protected SecondaryMacroBlock[] sec_macro_blocks;
	
	protected ATopLevelBlock(QNModel qnm, CoMoMBasis basis, Position position)
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);
		selection_policy = new TypeOneBlocks(qnm, this, current_class);
		initialiseSecondaryMacroBlocks();
	}		
	
	/**
	 * Constructor for lower classes	 
	 * @throws BTFMatrixErrorException 
	 */
	protected ATopLevelBlock(ATopLevelBlock full_block, int current_class) throws BTFMatrixErrorException {
		super(full_block, current_class);
		
		//Take required secondary macro blocks
		sec_macro_blocks = new SecondaryMacroBlock[macro_blocks.length - 1];
		for(int i = 0; i < sec_macro_blocks.length; i++) {
			addSubSecMacroBlock(full_block, i);
		}
	}

	private void initialiseSecondaryMacroBlocks() throws BTFMatrixErrorException {
		sec_macro_blocks = new SecondaryMacroBlock[macro_blocks.length - 1];
		//Instantiate secondary macro blocks		
		for(int h = 0; h < macro_blocks.length - 1; h++) {				
			addSecondaryMacroBlock(h, macro_blocks[h], macro_blocks[h+1]);			
		}
	}
	
	@Override
	public int addCE(int position, PopulationChangeVector n, int queue) throws BTFMatrixErrorException, InternalErrorException {	
		
		int block = findMacroBlock(position);		
		
		int row_inserted_at =  macro_blocks[block].addCE(position, n, queue);
		
		if(block < sec_macro_blocks.length) {
			sec_macro_blocks[block].addCE(row_inserted_at, n, queue);
		}	
		return row_inserted_at;
	}
	
	@Override
	public int addPC(int position, PopulationChangeVector n, int _class) throws BTFMatrixErrorException, InternalErrorException {
		
		int block = findMacroBlock(position);		
		
		int row_inserted_at =  macro_blocks[block].addPC(position, n, _class);	
		
		if(block > 0) {
			sec_macro_blocks[block - 1].addPC(row_inserted_at, n, _class);
		}
		
		return row_inserted_at;
	}
	
	@Override
	public void multiply(BigRational[] result, BigRational[] input) throws BTFMatrixErrorException {
		
		super.multiply(result, input);
		
		//Also multiply sec_macro_blocks
		for(int i = 0; i < sec_macro_blocks.length; i++) {
			sec_macro_blocks[i].multiply(result, input);
		}
	}
	
	@Override
	public void solve(BigRational[] rhs) throws BTFMatrixErrorException, OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException {
		
		//Need to solve in bottom to top order, with secondary macro blocks interleaved
		
		//First solve last, alone macro block
		macro_blocks[macro_blocks.length - 1].solve(rhs);
		
		//Now, solve secondary macro block, marco block pairs in reverse order:
		for(int i =  sec_macro_blocks.length - 1; i >= 0; i--) {
			sec_macro_blocks[i].solve(rhs);
			macro_blocks[i].solve(rhs);
		}
	}
	
	
	protected abstract void addSecondaryMacroBlock(int h, MacroBlock block_1, MacroBlock block_2) throws BTFMatrixErrorException;
	
	protected abstract void addSubSecMacroBlock(ATopLevelBlock full_block, int index);
}
