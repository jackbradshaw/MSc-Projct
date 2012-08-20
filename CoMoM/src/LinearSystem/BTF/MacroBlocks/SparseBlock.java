package LinearSystem.BTF.MacroBlocks;

import java.util.ArrayList;
import java.util.Iterator;

import Basis.Basis;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import LinearSystem.BTF.Position;

public class SparseBlock {

	private BigRational[] values;
	private int[] columns;
	
	private Basis basis;
	
	private int rows;
	private int cols;
	
	private Position position;
		
	public SparseBlock(CoMoMBasis basis, Position position, int rows, int cols) {
		values = new BigRational[rows];
		columns = new int[rows];
		
		this.position = position;
		
		this.basis = basis;
		
		this.rows = rows;
		this.cols = cols;
	}

	public void write(int row, int col, BigRational value) {
		values[row] = value.copy();
		columns[row] = col;
	}

	public BigRational get(int row, int col) {
		if(columns[row] == col) {
			return values[row];
		} else {
			return BigRational.ZERO;
		}
	}
	
	public void multiply(BigRational[] result) {
		 for(int i = 0; i < rows; i++) {
			 result[position.row + i] = basis.getOldValue(position.col + columns[i]).multiply(values[i]);
		 }
	} 

	public void solve(BigRational[] rhs) {
		for(int i = 0; i < rows; i++) {
			 rhs[position.row + i] = rhs[position.row + i].add((basis.getNewValue(position.col + columns[i]).multiply(values[i])).negate());
		} 		
	}

	
	class Triple {
		
		protected int row;
		protected int col;
		protected BigRational value;
		
		protected Triple(int row, int col, BigRational value) {
			this.row = row;
			this.col = col;
			this.value = value;
		}		
	}
}
