package Basis;

import Utilities.MiscFunctions;
import Basis.Comparators.SortVectorByhThenRmnz;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public class BTFCoMoMBasis extends CoMoMBasis {

	public BTFCoMoMBasis(QNModel m) {
		super(m);
		setComparator(new SortVectorByhThenRmnz());
	}	
	
	@Override
	public int indexOf(PopulationChangeVector n, int m) throws InternalErrorException {	
		 
		//Find the position of the n vector in the ordering
		int population_position = order.indexOf(n);		
		int queue_added = m;	
		
		if(population_position == -1) throw new InternalErrorException("Invalid PopulationChangeVector:" + n);
		
		//No queues added, constant is in Lambda_Y
		if(queue_added == 0) {
			return MiscFunctions.binomialCoefficient(M + R - 1 , M)*M + population_position;
		} 
		
		//Queue added, constant is in Lambda_X
		return population_position * M + queue_added - 1;	
	}
}
