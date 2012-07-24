package LinearSystem.TopLevelBlocks;

import DataStructures.QNModel;
import LinearSystem.MacroBlocks.MacroBlock;

/**
 * TODO comment regarding which blocks are taken
 * @author Jack Bradshaw
 */
public class TypeTwoBlocks extends MacroBlockSelectionPolicy{

	protected TypeTwoBlocks(QNModel qnm, TopLevelBlock full_block, int currnet_class) {
		super(qnm, full_block);		
	}

	@Override
	protected MacroBlock[] selectMacroBlocks(int current_class) {
		//Don't need to take h = 0 macro block
		//Take required macro blocks
		MacroBlock[] macro_blocks = new MacroBlock[full_block.macro_blocks.length - 1];
		for(int i = 0; i < macro_blocks.length; i++) {
			macro_blocks[i] = full_block.macro_blocks[i + 1].subBlock(current_class);
		}
		return macro_blocks;
	}
}