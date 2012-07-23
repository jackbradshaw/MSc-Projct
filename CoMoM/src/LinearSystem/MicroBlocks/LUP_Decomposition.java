package LinearSystem.MicroBlocks;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import Exceptions.InconsistentLinearSystemException;
import LinearSystem.Position;

public class LUP_Decomposition {
	/**
	 * Basis for the model
	 */
	private CoMoMBasis basis;
	
	/**
	 * Starting position of the matrix
	 */
	private Position position;
	
	/**
	 * Decomposed Matrix
	 */
	private BigRational[][] A_prime;
	
	/**
	 * Permutation P
	 */
	private int[] P;
	
	/**
	 * Dimension of square matrix
	 */
	private int size;
	
	/**
	 * 
	 * @param A
	 * @throws InconsistentLinearSystemException 
	 */
	public LUP_Decomposition(CoMoMBasis basis, Position position, BigRational[][] A) throws InconsistentLinearSystemException {
		
		//store basis
		this.basis = basis;
		
		//store the psotion of the matirx
		this.position = position;
		
		//store size of matrix A
		size = A.length;
		
		//A_prime is a copy of A, where decomposition will be performed in place
		A_prime = new BigRational[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j ++) {
				A_prime[i][j] = A[i][j].copy(); //TODO copy()?
			}
		}
		
		//The permutation P is initially the identity
		P = new int[size];
		for(int i = 0; i < size; i++) {
			P[i] = i;
		}
			
		//Compute the in place decomposition
		decompose();
		
		//MiscFunctions.printMatrix(A_prime);
		//MiscFunctions.printMatrix(P);
	}
	
	private void decompose() throws InconsistentLinearSystemException {
		System.out.println("A_prime: ");
		MiscFunctions.printMatrix(A_prime);
		BigRational p;
		BigRational abs_A;
		int k_prime = -1;
		
		for(int k = 0; k < size; k++) {
			p = BigRational.ZERO;
			for(int i = k; i < size; i++) {
				abs_A = A_prime[i][k].abs();
				if(abs_A.greaterThan(p)) {
					p = abs_A;
					k_prime = i;
				}
			}
			if( p.isZero()) {
				throw new InconsistentLinearSystemException("LUP Decomposition failed: Singular Matrix");
			}
			P_exchange(k, k_prime);
			for(int i = 0; i < size; i++) {
				A_prime_exchange(k, k_prime, i);
			}
			for(int i = k + 1; i < size; i++) {
				A_prime[i][k] = A_prime[i][k].divide(A_prime[k][k]);
				for(int j = k + 1; j < size; j++) {
					A_prime[i][j] = A_prime[i][j].subtract(A_prime[i][k].multiply(A_prime[k][j]));
				}
			}
		}
	}
	
	private void P_exchange(int i, int j) {
		int temp = P[i];
		P[i] = P[j];
		P[j] = temp;
	}
	
	private void A_prime_exchange(int row_1, int row_2, int col) {
		BigRational temp = A_prime[row_1][col];
		A_prime[row_1][col] = A_prime[row_2][col];
		A_prime[row_2][col] = temp;		
	}	
	
	public BigRational[] solve2(BigRational[] b) {
		BigRational[] x = new BigRational[size];
		BigRational[] y = new BigRational[size];
		
		//Forward substitution
		for(int i = 0; i < size; i++) {
			BigRational sum = BigRational.ZERO;
			for(int j = 0; j <= i - 1; j++) {
				sum = sum.add(A_prime[i][j].multiply(y[j]));
			}
			y[i] = b[P[i]].subtract(sum);
		}
		
		//Backward substitution
		for(int i = size - 1; i >= 0; i--) {
			BigRational sum = BigRational.ZERO;
			for(int j = i + 1; j < size; j++) {
				sum = sum.add(A_prime[i][j].multiply(x[j]));
			}
			x[i] = y[i].subtract(sum);
			x[i] = x[i].divide(A_prime[i][i]);
		}
		return x;
	}
	
	public void solve(BigRational[] rhs) {
		//BigRational[] x = new BigRational[size];
		BigRational[] y = new BigRational[size];
		
		//Forward substitution
		for(int i = 0; i < size; i++) {
			BigRational sum = BigRational.ZERO;
			for(int j = 0; j <= i - 1; j++) {
				sum = sum.add(A_prime[i][j].multiply(y[j]));
			}
			y[i] = rhs[position.row + P[i]].subtract(sum);
		}
		
		//Backward substitution
		for(int i = size - 1; i >= 0; i--) {
			BigRational sum = BigRational.ZERO;
			for(int j = i + 1; j < size; j++) {
				sum = sum.add(A_prime[i][j].multiply(basis.getNewValue(position.row + j)));
			}
			basis.setValue(y[i].subtract(sum), position.row + i);
			basis.setValue(basis.getNewValue(position.row + i).divide(A_prime[i][i]), position.row + i);
		}		
	}
}
