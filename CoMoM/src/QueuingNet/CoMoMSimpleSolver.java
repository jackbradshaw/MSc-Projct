package QueuingNet;

import javax.naming.OperationNotSupportedException;

import Basis.BTFCoMoMBasis;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;
import LinearSystem.SimpleLinearSystem;

public class CoMoMSimpleSolver extends CoMoMSolver {

	public CoMoMSimpleSolver(QNModel qnm, int num_threads) throws InternalErrorException {
		super(qnm);
		
		basis =  new BTFCoMoMBasis(qnm);		
		system = new SimpleLinearSystem(qnm, basis, num_threads);
	}

	 /**
     * Prints a short welcome message that says which solver is being used.
     */
    @Override
    public void printWelcome() {
        System.out.println("Using Simple CoMoM");
    }
}
