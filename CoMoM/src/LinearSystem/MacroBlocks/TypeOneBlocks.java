package LinearSystem.MacroBlocks;

import DataStructures.QNModel;
import LinearSystem.MicroBlocks.MicroBlock;
import Utilities.MiscFunctions;

public class TypeOneBlocks extends MicroBlockSelectionPolicy {

	protected TypeOneBlocks(QNModel qnm, MacroBlock full_block) {
		super(qnm, full_block);		
	}

	@Override
	protected MicroBlock[] selectMicroBlocks(int current_class) {
		
		int number_of_micro_blocks = MiscFunctions.binomialCoefficient(current_class - 1, full_block.h);
		
		System.out.println("currnet_class: " + current_class);
		//Take required macro blocks
		MicroBlock[] micro_blocks = new MicroBlock[number_of_micro_blocks];
		for(int i = 0; i < micro_blocks.length; i++) {
			micro_blocks[i] = full_block.SubMicroBlock(full_block, i);
		}
		return micro_blocks;
	}

}
