package LinearSystem.BTF;

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

public class BTFLinearSystemTest {

	private QNModel qnm;
	private CoMoMBasis basis;
	BTFLinearSystem system;
	
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
		basis =  new BTFCoMoMBasis(qnm);
		system = new BTFLinearSystem(qnm, basis);
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void solveTest() throws OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException, BTFMatrixErrorException {
		basis.initialiseBasis();
		//Dummy Population Vector
		PopulationVector N = qnm.N.copy();
		
		system.initialiseForClass(N, 1);
		
		
		system.solve();
	}
	
	@Test
	public void Muliplytest() throws BTFMatrixErrorException, InputFileParserException, InternalErrorException, OperationNotSupportedException, InconsistentLinearSystemException {
		
		
		ConvolutionSolver convolution = new ConvolutionSolver(qnm);
		
		BigRational G ;
		
		BigRational[] lhs = new BigRational[basis.getSize()];
		
		BigRational[] rhs = new BigRational[basis.getSize()];
		
		for(int cur_class = 1; cur_class <= qnm.R; cur_class++) {
			PopulationVector N = qnm.N.copy();
			for(int i = cur_class - 1; i < qnm.R; i++) {
				N.set(i,0);
			}
			
			System.out.print("N: " + N + "\n");			
			
			system.initialiseForClass(N, cur_class);
			N.restore();
			
			
			for(int i = 0; i < basis.getOrder().size(); i++) {
				PopulationChangeVector n = basis.getOrder().get(i);
				for(int k = 0; k <= qnm.M; k++) {
					
					MultiplicitiesVector m = qnm.getMultiplicitiesVector();	
					m.plusOne(k);
					
					//compute RHS value
					G = convolution.compute(m, N.changePopulation(n));
					rhs[basis.indexOf(n, k)] = G.copy();
					
					//compute LHS value
					N.plusOne(cur_class);
					
					G = convolution.compute(m, N.changePopulation(n));
					N.restore();
					lhs[basis.indexOf(n, k)] = G.copy();
				}
			}
			
			//intialise vector to store results
			BigRational[] lhs_result = new BigRational[basis.getSize()];
			BigRational[] rhs_result = new BigRational[basis.getSize()];
			for(int i = 0; i < lhs_result.length; i++) {
				lhs_result[i] = BigRational.ZERO;
				rhs_result[i] = BigRational.ZERO;
			}
			
			//printing lhs and rhs:
			System.out.print(String.format("%40s", "lhs: "));
			System.out.print(String.format("%40s", "rhs: "));
			System.out.print("\n");
			for(int i = 0; i < lhs.length; i++) {
				System.out.print(String.format("%40s", lhs[i]));
				System.out.print(String.format("%40s", rhs[i]));
				System.out.print("\n");
			}
			
			system.multiply(lhs_result, lhs, rhs_result, rhs);	
			
			
			//printing lhs and rhs results:
			System.out.print(String.format("%40s", "Multiplied lhs: "));
			System.out.print(String.format("%40s", "Multiplied rhs: "));
			System.out.print("\n");
			for(int i = 0; i < lhs.length; i++) {
				System.out.print(String.format("%40s", lhs_result[i]));
				System.out.print(String.format("%40s", rhs_result[i]));
				System.out.print("\n");
			}
		}
	}

}
