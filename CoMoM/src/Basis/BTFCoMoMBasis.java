package Basis;

import java.util.Collections;

import Utilities.MiscFunctions;

import Comparators.SortVectorByhThenRmnz;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public class BTFCoMoMBasis extends CoMoMBasis {

	public BTFCoMoMBasis(QNModel m) {
		super(m);
	}	
	
	/**
	 * Finds the index of Normalising Constant G(N-n, m) as determined by n and m
	 * @param n Change in population vector
	 * @param m index of queue added (0 for no queue added)
	 * @return Index of G(N-n, m) in basis
	 * @throws InternalErrorException
	 */
	//@Override
	public int indexOf2(PopulationChangeVector n, int m) throws InternalErrorException {
		int population_position = order.indexOf(n);
		if(population_position == -1) throw new InternalErrorException("Invalid PopulationChangeVector");
		int queue_added = m;
		//order multiplicities 1,2,3,...M,0			
		int multiplicity_order;
		if(queue_added == 0) multiplicity_order = M;
		else multiplicity_order = queue_added - 1;		
		return population_position + multiplicity_order* MiscFunctions.binomialCoefficient(M + R - 1, M);
	}
	
	@Override
	public int indexOf(PopulationChangeVector n, int m) throws InternalErrorException {	
		
		int population_position = order.indexOf(n);		
		int queue_added = m;	
		
		if(population_position == -1) throw new InternalErrorException("Invalid PopulationChangeVector:" + n);
		
		if(queue_added == 0) {
			return MiscFunctions.binomialCoefficient(M + R - 1 , M)*M + population_position;
		} 		
		return population_position * M + queue_added - 1;	
	}
	
	@Override
	protected void sort() {
		Collections.sort(order, new SortVectorByhThenRmnz());
	}

}
