package QueuingNet;

import Basis.BTFCoMoMBasis;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.BTF.BTFLinearSystem;

public class CoMoMBTFSolver extends CoMoMSolver {

	public CoMoMBTFSolver(QNModel qnm) throws InternalErrorException, BTFMatrixErrorException, InconsistentLinearSystemException {
		super(qnm);
		
		basis =  new BTFCoMoMBasis(qnm);		
		system = new BTFLinearSystem(qnm, basis);
	}

	 /**
     * Prints a short welcome message that says which solver is being used.
     */
    @Override
    public void printWelcome() {
        System.out.println("Using BTF CoMoM");
    }
}
