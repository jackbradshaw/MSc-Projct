package LinearSystem.MicroBlocks;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import LinearSystem.Position;

public abstract class MatrixMicroBlock extends MicroBlock {

	//current row at which to insert next equation
	protected int row;	
			
	//underlying array
	protected BigRational[][] array;
		
	protected MatrixMicroBlock(QNModel qnm, CoMoMBasis basis,
			Position position, int h) {
		super(qnm, basis, position, h);
	}


	public MatrixMicroBlock(MatrixMicroBlock micro_block, int current_class) {
		super(micro_block, current_class);
		this.array = micro_block.array;
	}

	/**
	 * Initialiser to be called by subclasses 
	 */
	final protected void initialise() {
		row = 0;
		array = new BigRational[size.row][size.col];
		
		//fill with zeros
		for(int x = 0; x < size.row; x++) {
			for(int y = 0; y < size.col; y++) {
				array[x][y] = BigRational.ZERO;
			}
		}
		
	}
	
	@Override
	public void multiply(BigRational[] result, BigRational[] input) throws BTFMatrixErrorException {		
		
		if(position.col + size.col > input.length) throw new BTFMatrixErrorException("Matrix exceeds end of vector when multiplying");
		
		for (int i = 0; i < size.row; i++) {           
            for (int j = 0; j < size.col; j++) {
                if (!array[i][j].isZero()) {
                    if (input[j + position.col].isPositive()) {
                        result[i + position.row] = result[i + position.row].add(array[i][j].multiply(input[j + position.col]));
                    } else if (input[j + position.col].isUndefined()) {
                        result[i + position.row] = new BigRational(-1);
                        result[i + position.row].makeUndefined();
                        break;
                    }
                }
            }
		}
	}
	
	protected abstract void computeDimensions();	
	
	@Override
	public void printRow2(int row) {
		int row_to_print = row - position.row;		
		if(row_to_print >= 0 && row_to_print < size.row) {
			//print white space offset
			for(int i = cols_printed; i < position.col; i++) {
				System.out.format("%2s ", " ");	
				cols_printed++;
			}
			for(int col = 0; col < size.col; col++) {				
				System.out.format("%2s ", array[row_to_print][col].toString());
				cols_printed++;
			}
		}
	}

}
