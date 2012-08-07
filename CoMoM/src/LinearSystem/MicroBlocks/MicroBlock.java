package LinearSystem.MicroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;
import LinearSystem.ComponentBlock;
import LinearSystem.Position;
import LinearSystem.MacroBlocks.MacroBlock;

public abstract class MicroBlock extends ComponentBlock {

	/**
	 * Number of non-zero elements in the pattern associated to the micro block
	 */
	int h;		
		
	/**
	 * Constructor - First phase of construction
	 * @param qnm The Model under study
	 * @param basis The associated basis
	 * @param position The starting position of the block
	 * @param h The number of non-zero elements in the pattern associated to the micro block
	 */
	protected MicroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) {
		super(qnm, basis, position);
		this.h = h;
	}	
	
	/**
	 * Second Phase of construction
	 * 
	 * Computes dimension of the block @see MicroBlock#computeDimensions() computeDimesions()
	 * and initialises data structures @see MicroBlock#initialiseDataStructures() initialiseDataStructures() 
	 * @throws InternalErrorException
	 */
	public void initialise() throws InternalErrorException {
		computeDimensions();
		initialiseDataStructures();
	}

	/**
	 * Initialise any underlying data structures
	 * @throws InternalErrorException
	 */
	protected abstract void initialiseDataStructures() throws InternalErrorException;

	/**
	 * Copy Constructor
	 * 
	 * @param full_block Block to be copied
	 * @param current_class The class for which the copy is being created
	 */
	public MicroBlock(MicroBlock micro_block, int current_class) {
		super(micro_block, current_class);
		this.h = micro_block.h;
		this.size = micro_block.size;		
	}

	/**
	 * Builder method that encapsulates the creation of copy MicroBlocks
	 * 
	 * @param current_class The class for which the copy is being created
	 * @return A shallow copy of calling block, for the current class
	 */
	public MicroBlock subBlock(int current_class) {
		
		//Create Shallow copy of full block
		MicroBlock sub_block = subBlockCopy(current_class);
		
		return sub_block;
	}	
	
	/**
	 * Factory Method for sub-block creation
	 * 
	 * This method is to be overridden by subclasses to instantiate a copy
	 * of the current block (of the correct type)
	 * 
	 * For instance, a copy of an XMicroBlocks needs to be a XMicroBlock
	 * 
	 * @param current_class The class for which the copy is created
	 * @return A shallow copy of calling block, for the current_class. Without micro blocks.
	 */
	protected abstract MicroBlock subBlockCopy(int current_class);

	/**
	 * Computes the dimensions of block and stores them in the member field <code>size</code>
	 */
	protected abstract void computeDimensions();

}
