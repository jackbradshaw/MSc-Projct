package LinearSystem.BTF.MicroBlocks;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InternalErrorException;
import Exceptions.UndefinedMultiplyException;
import LinearSystem.BTF.Position;

public abstract class MatrixMicroBlock extends MicroBlock {

	/**
	 * The row at which to insert next equation
	 */	
	protected int row;	
			
	/**
	 * The underlying array
	 */
	protected BigRational[][] array;
		
	protected MatrixMicroBlock(QNModel qnm, CoMoMBasis basis,
			Position position, int h) {
		super(qnm, basis, position, h);
	}

	public MatrixMicroBlock(MatrixMicroBlock micro_block, int current_class) {
		super(micro_block, current_class);
		this.array = micro_block.array;
	}

	@Override
	protected void initialiseDataStructures() {
		row = 0;
		array = new BigRational[size.row][size.col];
		
		//fill with zeros
		for(int x = 0; x < size.row; x++) {
			for(int y = 0; y < size.col; y++) {
				array[x][y] = BigRational.ZERO;
			}
		}
		
	}
	protected BigRational multiplyRow(int index) throws UndefinedMultiplyException {
		
		BigRational result = BigRational.ZERO;
		
		for (int j = 0; j < size.col; j++) {
			if (!array[index][j].isZero()) {
				if (basis.getNewValue(j + position.col).isPositive()) {
					result = result.add((array[index][j].multiply(basis.getNewValue(j + position.col))));						
				} else if (basis.getNewValue(j + position.col).isUndefined()) { 
					throw new UndefinedMultiplyException();               	                 
                }
            }
		}
		return result;
	}
	
	@Override
	public void multiply(BigRational[] result) throws BTFMatrixErrorException {		
		
		if(position.col + size.col > basis.getSize()) throw new BTFMatrixErrorException("Matrix exceeds end of vector when multiplying");
		
		for (int i = 0; i < size.row; i++) {           
            try {
            	result[position.row + i] = result[position.row + i].add(multiplyRow(i));
			} catch (UndefinedMultiplyException e) {
				result[position.row + i] = new BigRational(-1);
                result[position.row + i].makeUndefined();
			}
		}
	}
	
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
