package QueuingNet;

import javax.naming.OperationNotSupportedException;

import Basis.BTFCoMoMBasis;
import Basis.CoMoMBasis;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.LinearSystem;
import LinearSystem.Simple.SimpleLinearSystem;

public class CoMoMSolver extends QNSolver {

private static int M,R;	
	
	private static PopulationVector current_N;
	private static PopulationVector target_N;
	
	protected static LinearSystem system; 
	
	protected static CoMoMBasis basis;
	
	public CoMoMSolver(QNModel qnm) throws InternalErrorException {
		super(qnm);
		
		M = qnm.M;
		R = qnm.R;	
		target_N = qnm.N;
		
		System.out.println("Model under study:\n");
		qnm.printModel();
		System.out.println("\n");		
	}

    @Override
    public void computeNormalisingConstant() throws InternalErrorException, OperationNotSupportedException, InconsistentLinearSystemException, BTFMatrixErrorException {
		
		current_N = new PopulationVector(0,R);
		
				
		for(int current_class = 1; current_class <= R; current_class++) {
			System.out.println("Working on class " + current_class);
			System.out.println("Current Population: " + current_N);
			
			system.initialiseForClass(current_N, current_class);
		
			solveForClass(current_class);			
		}						
		
		//Store the computed normalsing constant
		system.storeNormalisingConstant();				
	}
    
    private  void solveForClass(int current_class) throws InternalErrorException, OperationNotSupportedException, InconsistentLinearSystemException, BTFMatrixErrorException {
		/*
		//If no jobs of current_class in target population, move onto next class
		if(target_N.get(current_class - 1) == 0) {
			return;
		}				
		*/
		for(int current_class_population = current_N.get(current_class - 1); 
				current_class_population <= target_N.get(current_class - 1); 
				current_class_population++ ) {
			
			
			System.out.println("Solving for population: " + current_N);
			System.out.println(current_class_population);
			
			system.update(current_class_population);
			
			system.solve();
						
			if(current_class_population < target_N.get(current_class - 1)) {
				//System.out.println("Updated A: ");
				//A.update();  Updating now done in solver
				//A.print();
				
				current_N.plusOne(current_class);
			}
		}		
	}
    
    @Override
    public void computePerformanceMeasures() throws InternalErrorException {
    	system.computePerformanceMeasures();
    }
}
