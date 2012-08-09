package LinearSystem.BTF.TopLevelBlocks;

import DataStructures.QNModel;
import LinearSystem.BTF.MacroBlocks.MacroBlock;

/**
 * TODO comment regarding which blocks are taken
 * @author Jack Bradshaw
 */
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
			macro_blocks[i] = full_block.macro_blocks[i].subBlock(current_class);
		}
		return macro_blocks;
	}

}
