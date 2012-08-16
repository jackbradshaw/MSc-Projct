package LinearSystem.BTF.MicroBlocks;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;

public class B2MicroBlock extends MicroBlock {
	
	public B2MicroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) {
		super(qnm, basis, position, h);
		computeDimensions();		
	}

	public B2MicroBlock(MicroBlock micro_block, int current_class) {
		super(micro_block, current_class);
	}

	@Override
	protected void initialiseDataStructures() {
		//No underlying data structure		
	}
	
	@Override
	protected MicroBlock subBlockCopy(int current_class) {
		return new B2MicroBlock(this, current_class);
	}
	
	@Override
	protected void computeDimensions() {
		size.row = MiscFunctions.binomialCoefficient(qnm.M, h);
		size.col = 0;
	}

	@Override
	public int addCE(int position, PopulationChangeVector n, int queue)
			throws BTFMatrixErrorException, InternalErrorException {
		//Do Nothing, should never be used
		return position;
	}

	@Override
	public int addPC(int position, PopulationChangeVector n, int _class)
			throws BTFMatrixErrorException, InternalErrorException {
		//Do Nothing, should never be used
		return position;
	}
		
	@Override
	public void printRow2(int row) {
		
		int number_of_queue_constants = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M;
		
		int row_to_print = row - position.row;		
		if(row_to_print >= 0 && row_to_print < size.row) {
			
			//cols_printed = 0;
			//print white offset
			int first_column = (position.row + row_to_print - number_of_queue_constants) * qnm.M;
			for(int i = 0; i < first_column; i++) {
				System.out.format("%2s ", " ");
				cols_printed++;
			}
			//print Demand for each queue
			for(int k = 1; k <= qnm.M; k++) {
				System.out.format("%2s ", qnm.getDemandAsBigInteger(k - 1, current_class - 1)).toString();
				cols_printed++;
			}			
			//print white space to next block
			for(int i = cols_printed; i < number_of_queue_constants; i++) {
				System.out.format("%2s ", " ");
			}			
			//print whitespace to delays
			for(int i = 0; i <= (row - number_of_queue_constants); i++) {
				System.out.format("%2s ", " ");
			}
			//print Delay
			System.out.format("%2s ", qnm.getDelayAsBigRational(current_class - 1).toString());
		}

	}
	
	private BigRational multiplyRow(int index) {
		
		int number_of_queue_constants = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M;
		
		BigRational result = BigRational.ZERO;
		
		for(int k = 1; k <= qnm.M; k++) {
			result = result.
					add((basis.getOldValue(((position.row + index - number_of_queue_constants) * qnm.M) + k - 1).
							multiply(qnm.getDemandAsBigInteger(k - 1, current_class - 1))));
		}
		result = result.
				add((basis.getOldValue(position.row + index)).
					multiply(qnm.getDelayAsBigRational(current_class - 1)));
		
		return result;
	}
	
	@Override
	public void multiply(BigRational[] result)
			throws BTFMatrixErrorException {
		
		for(int i = 0; i < size.row; i++) {
			result[position.row + i] = multiplyRow(i);
		}
	}
	
	@Override
	public void solve(BigRational[] rhs) {
		
		BigRational value;
		
		for(int i = 0; i < size.row; i++) {
			
			value = multiplyRow(i);
			
			//Divide by N_r in A
			value = value.divide(new BigRational(current_class_population));
			basis.setValue(value, position.row + i);
		
		}		
	}
}
