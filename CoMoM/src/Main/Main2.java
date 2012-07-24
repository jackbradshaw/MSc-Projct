

 package Main;
 



import javax.naming.OperationNotSupportedException;

import Basis.BTFCoMoMBasis;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InputFileParserException;
import Exceptions.InternalErrorException;
import LinearSystem.BTFLinearSystem;
import LinearSystem.LinearSystem;
import LinearSystemSolver.SimpleSolver;
import Matrix.StandardMatrix;

public class Main2 {
	
	private static QNModel qnm;
	private static int M,R;	
	
	private static PopulationVector current_N;
	private static PopulationVector target_N;
	
	private static BTFLinearSystem system; 
	
	private static CoMoMBasis basis;
	
	public static void main(String[] args) throws InputFileParserException, InternalErrorException, BTFMatrixErrorException, InconsistentLinearSystemException {
		
		qnm = new QNModel("models\\test_model_4_comp_larger.txt");
		//qnm = new QNModel("models\\test_model_5.txt");
		//qnm = new QNModel("models\\test_model_2.txt");
		//qnm = new QNModel("models\\test_model_7.txt");
		//qnm = new QNModel("models\\model8.txt");
		//qnm = new QNModel("models\\big_model.txt");
		M = qnm.M;
		R = qnm.R;	
		target_N = qnm.N;
		
		System.out.println("Model under study:\n");
		qnm.printModel();
		System.out.println("\n");
		
		basis =  new BTFCoMoMBasis(qnm);
		system = new BTFLinearSystem(qnm, basis);
		
		
		try {
			computeNormalisingConstant();
		} catch (OperationNotSupportedException ex) {
            ex.printStackTrace();
            throw new InternalErrorException("Error in linear system solver.");
        } catch (InconsistentLinearSystemException ex) {
            ex.printStackTrace();
            throw new InternalErrorException(ex.getMessage());
        }
		
		system.computePerformanceMeasures();		
		qnm.printPerformaceMeasrues();
		
	}
	
	public static void computeNormalisingConstant() throws InternalErrorException, OperationNotSupportedException, InconsistentLinearSystemException, BTFMatrixErrorException {
		
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
	
	public static void solveForClass(int current_class) throws InternalErrorException, OperationNotSupportedException, InconsistentLinearSystemException, BTFMatrixErrorException {
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
}

