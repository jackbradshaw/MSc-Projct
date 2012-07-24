package LinearSystem.TopLevelBlocks;

import DataStructures.QNModel;
import LinearSystem.MacroBlocks.MacroBlock;

public class TypeOneBlocks extends MacroBlockSelectionPolicy{

	protected TypeOneBlocks(QNModel qnm, TopLevelBlock full_block, int currnet_class) {
		super(qnm, full_block);		
	}

	@Override
	protected MacroBlock[] selectMacroBlocks(int current_class) {
		int number_of_macro_blocks = (current_class - 1 < qnm.M ? current_class - 1: qnm.M) + 1;
		
		//Take required macro blocks
		MacroBlock[] macro_blocks = new MacroBlock[number_of_macro_blocks];
		for(int i = 0; i < macro_blocks.length; i++) {
			macro_blocks[i] = full_block.SubMacroBlock(full_block, i);
		}
		return macro_blocks;
	}

}
