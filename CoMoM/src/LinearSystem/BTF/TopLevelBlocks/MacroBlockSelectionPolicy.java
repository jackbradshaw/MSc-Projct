package LinearSystem.BTF.TopLevelBlocks;

import DataStructures.QNModel;
import LinearSystem.BTF.MacroBlocks.MacroBlock;

/**
 * A class to encapsulate the various policies for selecting Macro Blocks when considering lower classes
 * @author Jack Bradshaw *
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
	 * and returns a list of the sub-blocks of those macro blocks
	 * @param currnet_class The current class being considered.
	 * @return Array of selected sub-MacroBlocks
	 */
	protected abstract MacroBlock[] selectMacroBlocks(int current_class);
	
}
