package Basis;

import Utilities.MiscFunctions;
import Basis.CoMoMBasis;
import Basis.Comparators.SortVectorByRightmostNZ;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public class CoMoMReorderingBasis extends CoMoMBasis {
	
	
	private int[] order_offset;
	private int[] previous_order_offset;
	
	public CoMoMReorderingBasis(QNModel m) {
		super(m);
		setComparator(new SortVectorByRightmostNZ());
		order_offset = new int[size];
		previous_order_offset = new int[size];
	}	

	@Override
	public void initialiseForClass(int current_class) throws InternalErrorException {
		if(current_class > 1) {
			System.arraycopy(order_offset, 0, previous_order_offset, 0, size);
		}
		updateOffset(current_class);
		
		MiscFunctions.printMatrix(order_offset);
		MiscFunctions.printMatrix(previous_order_offset);	
		
		//copy basis to previous basis
		System.arraycopy(basis, 0, previous_basis, 0, size);		
		
		for(int i = 0; i < basis.length; i++) {	
			
			basis[i + order_offset[i]] = previous_basis[i+previous_order_offset[i]];
		}		
	}
	
	public void updateOffset(int current_class) {
		
		int total_moved = MiscFunctions.binomialCoefficient(M + current_class - 1 , M); //correct		
		int size = MiscFunctions.binomialCoefficient(M + R - 1 , M)*( M + 1 );
		
		int moved_count = 0;		
		int index = 0;
		
		for(int n_pos = 0; n_pos < order.size(); n_pos++) {
			
			
			if(order.get(n_pos).sumTail(current_class-1) <= 0) {
				moved_count++;
				//send 0 queue to back	
				order_offset[index] = - index + size - total_moved + moved_count - 1;			
			}else {
				//don't send o queue to back
				order_offset[index] = -moved_count;
			}
			index++;
			//deal with other added queues
			for(int k = 1 ; k <= qnm.M; k++) {
				order_offset[index] = - moved_count;
				index++;
			}
		}
	}
	
	/**
	 * Finds the index of Normalising Constant G(N-n, m) as determined by n and m
	 * @param n Change in population vector
	 * @param m index of queue added (0 for no queue added)
	 * @return Index of G(N-n, m) in basis
	 * @throws InternalErrorException
	 */
	public int indexOf(PopulationChangeVector n, int m) throws InternalErrorException {
		int index = super.indexOf(n, m);
		index += order_offset[index];
		return index;
	}
	
	public int[] getOrderOffset() {
		return order_offset;
	}
}
