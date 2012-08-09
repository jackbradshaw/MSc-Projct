package LinearSystem.BTF.MicroBlocks;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.Position;

public class CMicroBlock extends MicroBlock {

	private int[][] array;
	
	//class for which this pattern has right-most non-zero
	private int _class;
	
	public CMicroBlock(QNModel qnm, CoMoMBasis basis, Position position, int h) throws InternalErrorException {
		super(qnm, basis, position, h);	
		computeDimensions();
	}
	
	public CMicroBlock(CMicroBlock full_block, int current_class) {
		super(full_block, current_class);
		this.array = full_block.array;
		this._class = full_block._class;
	}
	
	@Override
	protected MicroBlock subBlockCopy(int current_class) {
		return new CMicroBlock(this, current_class);
	}

	protected void initialiseDataStructures() throws InternalErrorException {
		
		array = new int[size.row][2]; 
		
		//current_class = 1;
		//The next row to write to
		int row = 0; 
		
		int vector_index = position.row / (qnm.M + 1);
		
		PopulationChangeVector n = basis.getPopulationChangeVector(vector_index) ; 
		
		_class = n.RightMostNonZero() + 1;
		
		int number_of_n_s = MiscFunctions.binomialCoefficient(qnm.M, h) ;
		
		
		//Loop over all n's contained in this microblock
		for(int i = 0; i < number_of_n_s ; i++) {
			n = basis.getPopulationChangeVector(vector_index++); 
			
			//Loop over all queues (including no queue)
			for(int k = 0; k <= qnm.M; k++) {
				array[row][0] = basis.indexOf(n, k);
				n.minusOne(_class);
				array[row][1] = basis.indexOf(n, k);
				n.restore();
				row++;
			}			
		}
	}

	@Override
	protected void computeDimensions() {
		//Need to consider queues 1,..,M and no queue for each n (i.e. M + 1)
		size.row = MiscFunctions.binomialCoefficient(qnm.M, h) * (qnm.M + 1);
		size.col = 0;
	}

	@Override
	public void printRow2(int row) {
		// TODO Auto-generated method stub

	}

	@Override
	public int addCE(int position, PopulationChangeVector n, int queue)
			throws BTFMatrixErrorException, InternalErrorException {
		return position;
	}

	@Override
	public int addPC(int position, PopulationChangeVector n, int _class)
			throws BTFMatrixErrorException, InternalErrorException {
		return position;
	}
	
	@Override
	public void multiply(BigRational[] result, BigRational[] input)
			throws BTFMatrixErrorException {
		//negative population, constant is zero
		if(_class > current_class) {
			for(int i = 0; i < size.row; i++) {
				result[array[i][0]] = BigRational.ZERO;
			}
		//carry forward
		} else {
			for(int i = 0; i < size.row; i++) {				
				result[array[i][0]] = input[array[i][1]].copy();
			}
		}
	}
	
	@Override
	public void solve(BigRational[] rhs) {
		//negative population, constant is zero
		if(_class > current_class) {
			for(int i = 0; i < size.row; i++) {
				basis.setValue(BigRational.ZERO, array[i][0]);
			}
		//carry forward
		} else {
			for(int i = 0; i < size.row; i++) {	
				basis.setValue( basis.getOldValue(array[i][1]), array[i][0]);			
			}
		}
	}

}
