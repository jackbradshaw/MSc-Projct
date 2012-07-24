package LinearSystem.MicroBlocks;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InternalErrorException;
import LinearSystem.Position;

public class B2MicroBlock extends MicroBlock {
	
	public B2MicroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) {
		super(qnm, basis, position, h);
		computeDimensions();		
	}

	public B2MicroBlock(MicroBlock micro_block, int current_class) {
		super(micro_block, current_class);
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

	@Override
	public void multiply(BigRational[] result, BigRational[] input)
			throws BTFMatrixErrorException {
	
		int number_of_queue_constants = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M;
		
		for(int i = 0; i < size.row; i++) {
			
			//System.out.println("result: " + result[position.row + i]);
			for(int k = 1; k <= qnm.M; k++) {
				result[position.row + i] = result[position.row + i].
						add((input[((position.row + i - number_of_queue_constants) * qnm.M) + k - 1].
								multiply(qnm.getDemandAsBigInteger(k - 1, current_class - 1))));
			}
			result[position.row + i] = result[position.row + i].
					add((input[position.row + i]).
						multiply(qnm.getDelayAsBigRational(current_class - 1)));
		}
	}

	@Override
	public void solve(BigRational[] rhs) {
		int number_of_queue_constants = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M;
		
		BigRational value;
		
		for(int i = 0; i < size.row; i++) {
			
			basis.setValue(BigRational.ZERO, position.row + i);
	
			for(int k = 1; k <= qnm.M; k++) {
				value = basis.getNewValue(position.row + i).
						add((basis.getOldValue(((position.row + i - number_of_queue_constants) * qnm.M) + k - 1).
								multiply(qnm.getDemandAsBigInteger(k - 1, current_class - 1))));
				
				basis.setValue(value, position.row + i);
			}
			value = basis.getNewValue(position.row + i).
					add((basis.getOldValue(position.row + i)).
						multiply(qnm.getDelayAsBigRational(current_class - 1)));
			
			basis.setValue(value, position.row + i);
			
			//Divide by N_r in A
			value = basis.getNewValue(position.row + i).divide(new BigRational(current_class_population));
			basis.setValue(value, position.row + i);
		
		}
		
	}

}
