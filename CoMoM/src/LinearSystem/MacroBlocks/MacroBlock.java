package LinearSystem.MacroBlocks;

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
import LinearSystem.MicroBlocks.MicroBlock;
import LinearSystem.TopLevelBlocks.TypeOneBlocks;
import Utilities.MiscFunctions;

public abstract class MacroBlock extends ComponentBlock{

	protected MicroBlock[] micro_blocks;
	
	protected MicroBlockSelectionPolicy selection_policy;
	
	//number of non-zeros
	protected int h;	
	
	protected MacroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) throws InternalErrorException, InconsistentLinearSystemException {
		super(qnm, basis, position);
		this.h = h;
		initialise();
	}
	
	/**
	 * Constructor for lower classes	 
	 */
	protected MacroBlock(MacroBlock full_block, int current_class) {
		super(full_block, current_class);
		
		this.current_class  = current_class;
		this.h = full_block.h;
		this.size = full_block.size; //TODO really?		
	}
		
	public MacroBlock subBlock(int current_class) {
		
		//Create Shallow copy of full block
		MacroBlock sub_block = subBlockCopy(current_class);
		
		//Take required micro blocks as determined by selection policy
		sub_block.micro_blocks = selection_policy.selectMicroBlocks(current_class);
		
		return sub_block;
	}
	
	protected abstract MacroBlock subBlockCopy(int current_class);
	
	protected abstract MicroBlock SubMicroBlock(MacroBlock full_block, int index);

	public void initialise() throws InternalErrorException, InconsistentLinearSystemException {
	
		int number_of_micro_blocks = MiscFunctions.binomialCoefficient(qnm.R - 1, h);
	
		micro_blocks = new MicroBlock[number_of_micro_blocks];
	
		size = new Position(0,0);
		
		Position block_position = position.copy();
	
		//Instantiate micro blocks
		for(int i = 0; i < number_of_micro_blocks; i++) {
			addMicroBlock(block_position.copy(), i, h);			
			block_position.add(micro_blocks[i].size());
			size.add(micro_blocks[i].size());
		
		}
	}
	
	protected abstract void addMicroBlock(Position block_position, int index, int h) throws InternalErrorException, InconsistentLinearSystemException;

	private int findMicroBlock(int position) throws BTFMatrixErrorException {
		if(position < 0) throw new  BTFMatrixErrorException("Trying to find macro block containing index: " + position);
		
		int block = 0;
		
		for(int i = 1; i < micro_blocks.length; i++) {
			if(position >= (micro_blocks[i].getStartingRow())) {
				block++;
			}
		}
		
		if(block >=  micro_blocks.length) throw new  BTFMatrixErrorException("Ran out of micro blocks! position = " + position);
		return block;
	}
	
	@Override
	public int addCE(int position, PopulationChangeVector n, int queue) throws BTFMatrixErrorException, InternalErrorException {
		int block = findMicroBlock(position);
		
		int row_inserted_at =  micro_blocks[block].addCE(position, n, queue);	
		
		return row_inserted_at;
	}
	
	@Override
	public int addPC(int position, PopulationChangeVector n, int _class) throws BTFMatrixErrorException, InternalErrorException {
		int block = findMicroBlock(position);
		
		int row_inserted_at = micro_blocks[block].addPC(position, n, _class);
		
		return row_inserted_at;
	}
	
	@Override
	public void multiply(BigRational[] result, BigRational[] input) throws BTFMatrixErrorException {
		
		for(int i = 0; i < micro_blocks.length; i++) {
			micro_blocks[i].multiply(result, input);
		}		
	}
	
	@Override
	public void solve(BigRational[] rhs) throws BTFMatrixErrorException, OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException {		
		for(int i = 0; i <  micro_blocks.length ; i++) {
			micro_blocks[i].solve(rhs);
		}
	}

	@Override
	public void printRow2(int row) {
		for(int i = 0; i < micro_blocks.length; i++) {
			micro_blocks[i].printRow2(row);
		}
	}

	public int numberOfMicroBlocks() {
		return micro_blocks.length;
	}
}
