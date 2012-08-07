package LinearSystem;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public class LinearSystem {

	protected  CoMoMBasis basis;
	
	protected QNModel qnm;
	
	public LinearSystem(QNModel qnm, CoMoMBasis basis) throws InternalErrorException {
		this.qnm = qnm;
		this.basis = basis;
		basis.initialiseBasis();	
	}
	
	public void storeNormalisingConstant() throws InternalErrorException {
		BigRational G = basis.getNormalisingConstant();
		System.out.println("G = " + G);
		qnm.setNormalisingConstant(G);			
	}

	public void computePerformanceMeasures() throws InternalErrorException {
		basis.computePerformanceMeasures();		
	}
}
