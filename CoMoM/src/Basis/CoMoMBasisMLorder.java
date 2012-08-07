package Basis;

import Utilities.MiscFunctions;
import Basis.Comparators.SortVectorMatLab;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public class CoMoMBasisMLorder extends BTFCoMoMBasis{

	public CoMoMBasisMLorder(QNModel qnm) {
		super(qnm);
		setComparator(new SortVectorMatLab());
	}

	@Override
	public int indexOf(PopulationChangeVector n, int m) throws InternalErrorException {
		
		int population_position = order.indexOf(n);
		int queue_added = m;	
		
		if(population_position == -1) throw new InternalErrorException("Invalid PopulationChangeVector");
		
		if(queue_added == 0) {
			return MiscFunctions.binomialCoefficient(M + R - 1 , M)*M + population_position;
		} 		
		return population_position * M + queue_added - 1;	
	}
}
