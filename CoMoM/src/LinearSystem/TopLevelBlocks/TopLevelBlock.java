package LinearSystem.TopLevelBlocks;

import javax.naming.OperationNotSupportedException;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.ComponentBlock;
import LinearSystem.Position;
import LinearSystem.MacroBlocks.MacroBlock;

public abstract class TopLevelBlock extends ComponentBlock {

	protected MacroBlock[] macro_blocks;
	
	protected MacroBlockSelectionPolicy selection_policy;
	
	protected TopLevelBlock(QNModel qnm, CoMoMBasis basis, Position position) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);
		initialise();
	}
		
	/**
	 * Constructor for lower classes	 
	 */
	protected TopLevelBlock(TopLevelBlock full_block, int current_class) {
		super(full_block, current_class);
		
		this.current_class = current_class;		
		
		macro_blocks = full_block.selection_policy.selectMacroBlocks(current_class);
	}
	
	private void initialise() throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		
		int number_of_macro_blocks = (qnm.R - 1 < qnm.M ? qnm.R - 1: qnm.M) + 1;
		
		macro_blocks = new MacroBlock[number_of_macro_blocks];
		
		//Instantiate macro blocks
		Position block_position = position.copy();
		
		for(int h = 0; h < number_of_macro_blocks; h++) {	
			addMacroBlock(block_position.copy(), h);
			block_position.add(macro_blocks[h].size());
		}
		
		//Store the size of the block
		size = block_position;
	}	
	
	protected abstract void addMacroBlock(Position position, int h) throws InternalErrorException, InconsistentLinearSystemException;
	
	protected abstract MacroBlock SubMacroBlock(TopLevelBlock full_block, int index);
	
	protected int findMacroBlock(int position) throws BTFMatrixErrorException {

		if(position < 0) throw new  BTFMatrixErrorException("Trying to find macro block containing index: " + position);
		
		int block = 0;
		
		for(int i = 1; i < macro_blocks.length; i++) {
			if(position >= (macro_blocks[i].getStartingRow())) {
				block++;
			}
		}
		
		if(block >=  macro_blocks.length) throw new  BTFMatrixErrorException("Ran out of macro blocks! position = " + position);
		
		return block;
	}
	
	@Override
	public int addCE(int position, PopulationChangeVector n, int queue) throws BTFMatrixErrorException, InternalErrorException {
		int block = findMacroBlock(position);
		
		return  macro_blocks[block].addCE(position, n, queue);		
	}
	
	@Override
	public int addPC(int position, PopulationChangeVector n, int _class) throws BTFMatrixErrorException, InternalErrorException {
		int block = findMacroBlock(position);		
		
		return  macro_blocks[block].addPC(position, n, _class);			
	}
	
	@Override
	public void multiply(BigRational[] result, BigRational[] input) throws BTFMatrixErrorException {
		
		for(int i = 0; i < macro_blocks.length; i++) {
			macro_blocks[i].multiply(result, input);
		}
	}
	
	@Override
	public void solve(BigRational[] rhs) throws BTFMatrixErrorException, OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException {
		for(int i = 0; i < macro_blocks.length; i++) {
			macro_blocks[i].solve(rhs);
		}
	}
	
	@Override
	public void printRow2(int row) {
		for(int i = 0; i < macro_blocks.length; i++) {
			macro_blocks[i].printRow2(row);
		}
	}
	
}
