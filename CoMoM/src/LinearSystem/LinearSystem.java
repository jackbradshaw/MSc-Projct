package LinearSystem;

import Basis.CoMoMBasis;
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
}
