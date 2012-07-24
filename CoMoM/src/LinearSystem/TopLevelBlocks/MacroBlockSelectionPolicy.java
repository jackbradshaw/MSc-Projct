package LinearSystem.TopLevelBlocks;

import DataStructures.QNModel;
import LinearSystem.MacroBlocks.MacroBlock;

/**
 * A class to encapsulate the various policies for selecting Macro Blocks when considering lower classes
 * @author Jack Bradshaw
 *
 */
public abstract class MacroBlockSelectionPolicy {
	
	/**
	 * The full TopLevelBlock containing ALL macro blocks
	 */
	protected TopLevelBlock full_block;	
	
	/**
	 * The Model under consideration.
	 */
	QNModel qnm;

	/**
	 * Constructor
	 * @param full_block The full TopLevelBlock containing ALL macro blocks
	 * @param currnet_class The current class being considered.
	 */
	protected MacroBlockSelectionPolicy(QNModel qnm, TopLevelBlock full_block) {
		this.qnm = qnm;
		this.full_block = full_block;		
	}	

	/**
	 * Selects the required macro blocks as per the policy
	 * @param currnet_class The current class being considered.
	 */
	protected abstract MacroBlock[] selectMacroBlocks(int current_class);
}
