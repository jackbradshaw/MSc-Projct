package LinearSystem.Simple;

import static org.junit.Assert.*;

import javax.naming.OperationNotSupportedException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Basis.BTFCoMoMBasis;
import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.MultiplicitiesVector;
import DataStructures.PopulationChangeVector;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InputFileParserException;
import Exceptions.InternalErrorException;
import QueuingNet.ConvolutionSolver;
import Utilities.MiscFunctions;

public class SimpleLinearSystemTest {

	private QNModel qnm;
	private CoMoMBasis basis;
	SimpleLinearSystem system;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		qnm = new QNModel("models\\test_model_4_comp_larger.txt");
//	qnm = new QNModel("models\\test_model_3.txt");
	//qnm = new QNModel("models\\unit_test_model.txt");
		basis =  new BTFCoMoMBasis(qnm);
		system = new SimpleLinearSystem(qnm, basis, 0);
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void Muliplytest() throws BTFMatrixErrorException, InputFileParserException, InternalErrorException, OperationNotSupportedException, InconsistentLinearSystemException {
		
		
		ConvolutionSolver convolution = new ConvolutionSolver(qnm);
		
		BigRational G ;
		
		for(int cur_class = 1; cur_class <= qnm.R; cur_class++) {
			PopulationVector N = qnm.N.copy();
			for(int i = cur_class - 1; i < qnm.R; i++) {
				N.set(i,0);
			}
			
			System.out.print("N: " + N + "\n");			
			
			system.initialiseForClass(N, cur_class);
			N.restore();
			system.update(2);
			System.out.print("N: " + N + "\n");		
						
			//Compute values in previous basis
			basis.startBasisComputation();
			for(int i = 0; i < basis.getOrder().size(); i++) {
				PopulationChangeVector n = basis.getOrder().get(i);
				for(int k = 0; k <= qnm.M; k++) {
					
					MultiplicitiesVector m = qnm.getMultiplicitiesVector();	
					m.plusOne(k);
					
					//compute RHS value
					G = convolution.compute(m, N.changePopulation(n));
					basis.setValue(G.copy(), basis.indexOf(n, k));
				}
			}
			
			//Compute values in new basis:
			basis.startBasisComputation();
			for(int i = 0; i < basis.getOrder().size(); i++) {
				PopulationChangeVector n = basis.getOrder().get(i);
				for(int k = 0; k <= qnm.M; k++) {
					
					MultiplicitiesVector m = qnm.getMultiplicitiesVector();	
					m.plusOne(k);
					
					//compute LHS value
					N.plusOne(cur_class);
					
					G = convolution.compute(m, N.changePopulation(n));
					N.restore();
					basis.setValue(G.copy(), basis.indexOf(n, k));
				}
			}			
			
			//printing lhs and rhs:
			System.out.print(String.format("%40s", "lhs: "));
			System.out.print(String.format("%40s", "rhs: "));
			System.out.print("\n");
			for(int i = 0; i < basis.getSize(); i++) {
				System.out.print(String.format("%40s", basis.getNewValue(i)));
				System.out.print(String.format("%40s", basis.getOldValue(i)));
				System.out.print("\n");
			}
			
			//intialise vector to store results and multiply
			BigRational[] rhs_result = system.multiplyrhs();
			basis.startBasisComputation();
			BigRational[] lhs_result = system.multiplylhs();
			
			
			//printing lhs and rhs results:
			System.out.print(String.format("%40s", "Multiplied lhs: "));
			System.out.print(String.format("%40s", "Multiplied rhs: "));
			System.out.print("\n");
			for(int i = 0; i < basis.getSize(); i++) {
				System.out.print(String.format("%40s", lhs_result[i]));
				System.out.print(String.format("%40s", rhs_result[i]));
				System.out.print("\n");
				assertTrue(lhs_result[i].equals(rhs_result[i]));
			}
		}
	}
	
	@Test
	public void Solvetest() throws BTFMatrixErrorException, InputFileParserException, InternalErrorException, OperationNotSupportedException, InconsistentLinearSystemException {
		
		
		ConvolutionSolver convolution = new ConvolutionSolver(qnm);
		
		BigRational G ;
		
		for(int cur_class = 1; cur_class <= qnm.R; cur_class++) {
			PopulationVector N = qnm.N.copy();
			for(int i = cur_class - 1; i < qnm.R; i++) {
				N.set(i,0);
			}
			
			System.out.print("N: " + N + "\n");			
			
			system.initialiseForClass(N, cur_class);
			N.restore();
			system.update(2);
			System.out.print("N: " + N + "\n");		
			
			//Compute values in previous basis
			basis.startBasisComputation();
			for(int i = 0; i < basis.getOrder().size(); i++) {
				PopulationChangeVector n = basis.getOrder().get(i);
				for(int k = 0; k <= qnm.M; k++) {
					
					MultiplicitiesVector m = qnm.getMultiplicitiesVector();	
					m.plusOne(k);
					
					//compute RHS value
					G = convolution.compute(m, N.changePopulation(n));
					basis.setValue(G.copy(), basis.indexOf(n, k));
				}
			}
			
			//Compute expected values
			BigRational[] result = new BigRational[basis.getSize()];
			for(int i = 0; i < basis.getOrder().size(); i++) {
				PopulationChangeVector n = basis.getOrder().get(i);
				for(int k = 0; k <= qnm.M; k++) {
					
					MultiplicitiesVector m = qnm.getMultiplicitiesVector();	
					m.plusOne(k);
					
					//compute LHS value
					N.plusOne(cur_class);
					
					G = convolution.compute(m, N.changePopulation(n));
					N.restore();
					result[basis.indexOf(n, k)] = G.copy();
				}
			}
			
			//Solve the system, the results will accessable via basis.getNewValue();			
			system.solve();				
			
			
			//printing lhs and rhs results:
			System.out.print(String.format("%40s", "expect: "));
			System.out.print(String.format("%40s", "actual: "));
			System.out.print("\n");
			for(int i = 0; i < basis.getSize(); i++) {
				System.out.print(String.format("%40s", result[i]));
				System.out.print(String.format("%40s", basis.getNewValue(i)));
				System.out.print("\n");
				assertTrue(result[i].equals(basis.getNewValue(i)));
			}
		}
	}
}