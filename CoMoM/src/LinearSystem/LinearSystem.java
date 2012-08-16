package LinearSystem;

import javax.naming.OperationNotSupportedException;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;

public abstract class LinearSystem {

	protected  CoMoMBasis basis;
	
	protected QNModel qnm;
	
	public LinearSystem(QNModel qnm, CoMoMBasis basis) throws InternalErrorException {
		this.qnm = qnm;
		this.basis = basis;
		basis.initialiseBasis();	
	}

	public void computePerformanceMeasures() throws InternalErrorException {
		basis.computePerformanceMeasures();		
	}
	
	public abstract void update(int current_class_population ); 
	
	public abstract void solve() throws OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException, BTFMatrixErrorException;
	
	public final void initialiseForClass(PopulationVector current_N, int current_class) 
			throws InternalErrorException, OperationNotSupportedException, BTFMatrixErrorException, InconsistentLinearSystemException {
		basis.initialiseForClass(current_class);
		initialiseMatricesForClass(current_N, current_class);
	}
	
	protected abstract void initialiseMatricesForClass(PopulationVector current_N, int current_class) 
			throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException, OperationNotSupportedException;		
}
