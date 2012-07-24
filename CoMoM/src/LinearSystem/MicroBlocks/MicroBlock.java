package LinearSystem.MicroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import LinearSystem.ComponentBlock;
import LinearSystem.Position;

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

	protected abstract void computeDimensions();	
}
