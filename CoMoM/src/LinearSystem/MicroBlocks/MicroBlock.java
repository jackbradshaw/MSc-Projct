package LinearSystem.MicroBlocks;

import Basis.CoMoMBasis;
import DataStructures.QNModel;
import LinearSystem.ComponentBlock;
import LinearSystem.Position;

public abstract class MicroBlock extends ComponentBlock {

	//number of non zeros
	int h;	
	
	//matrix dimensions
	int rows;
	int cols;
		
	protected MicroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) {
		super(qnm, basis, position);
		this.h = h;
	}	

	public MicroBlock(MicroBlock micro_block, int current_class) {
		super(micro_block, current_class);
		this.h = micro_block.h;
		this.rows = micro_block.rows;
		this.cols = micro_block.cols;
	}

	protected abstract void computeDimensions();
	
	public Position size() {
		return new Position(rows, cols);
	}
}
