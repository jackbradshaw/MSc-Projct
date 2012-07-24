package LinearSystem.MicroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import LinearSystem.ComponentBlock;
import LinearSystem.Position;
import LinearSystem.MacroBlocks.MacroBlock;

public abstract class MicroBlock extends ComponentBlock {

	//number of non zeros
	int h;		
		
	protected MicroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) {
		super(qnm, basis, position);
		this.h = h;
	}	

	public MicroBlock(MicroBlock micro_block, int current_class) {
		super(micro_block, current_class);
		this.h = micro_block.h;
		this.size = micro_block.size;		
	}

	public MicroBlock subBlock(int current_class) {
		
		//Create Shallow copy of full block
		MicroBlock sub_block = subBlockCopy(current_class);
		
		return sub_block;
	}	
	
	protected abstract MicroBlock subBlockCopy(int current_class);

	protected abstract void computeDimensions();

}
