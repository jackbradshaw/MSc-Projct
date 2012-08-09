package LinearSystem;

import static org.junit.Assert.*;

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
import LinearSystem.BTF.ComponentBlock;
import LinearSystem.BTF.Position;
import LinearSystem.BTF.TopLevelBlocks.B1Block;
import LinearSystem.BTF.TopLevelBlocks.B2Block;
import LinearSystem.BTF.TopLevelBlocks.CBlock;
import LinearSystem.BTF.TopLevelBlocks.XBlock;
import LinearSystem.BTF.TopLevelBlocks.YBlock;
import QueuingNet.ConvolutionSolver;
import Utilities.MiscFunctions;

public class LinearSystemTest {

	private QNModel qnm;
	private CoMoMBasis basis;
	
	@Test
	public void test() throws BTFMatrixErrorException, InputFileParserException, InternalErrorException, InconsistentLinearSystemException {
		qnm = new QNModel("models\\test_model_4_comp_larger.txt");
	//	qnm = new QNModel("models\\test_model_3.txt");
		basis =  new BTFCoMoMBasis(qnm);
		
		Position x_position = new Position(0,0);
		XBlock tl_matrix = new XBlock(qnm, basis, x_position);
		
		int Y_starting_col = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M;
		int Y_ending_col = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * (qnm.M + 1);
		Position y_position = new Position(0,Y_starting_col);
		YBlock tr_matrix = new YBlock(qnm, basis, y_position);
		
		B1Block b1_matrix = new B1Block(qnm, basis, x_position);
		
		Position b2_position = new Position(y_position.col, 0);
		
		B2Block b2_matrix = new B2Block(qnm, basis, b2_position);
		
		generate(tl_matrix, tr_matrix, b1_matrix);
		
		//print
		for(int row = 0; row < Y_starting_col; row++) {
			tl_matrix.printRow2(row);			
			tr_matrix.printRow2(row);		
			ComponentBlock.newLine();
		}
				/*
		//print
		for(int row = 0; row < Y_starting_col; row++) {
			tl_matrix.printRow(row, 0, Y_starting_col);
			tr_matrix.printRow(row, Y_starting_col, Y_ending_col);
			
			System.out.print("\n");
		}
		*/
		//print b1
		/*
		System.out.print("B1: \n\n");
		for(int row = 0; row < Y_starting_col; row++) {
			b1_matrix.printRow(row, 0, basis.getSize());
			System.out.print("\n");
		}
		
		//print b2
		System.out.print("B2: \n\n");
		for(int row = b2_position.row; row < basis.getSize(); row++) {
			b2_matrix.printRow(row, 0, basis.getSize());
			
			System.out.print("\n");
		}
		
		//multiply_test(tl_matrix, tr_matrix, b1_matrix, b2_matrix);
		
		
		
		//////////////////////////////////////////////////////////// Lower classes
		int _class = 3;
		XBlock tl_matrix_2 = new XBlock(tl_matrix, _class);
		YBlock tr_matrix_2 = new YBlock(tr_matrix, _class);
		
		//print
		for(int row = 0; row < Y_starting_col; row++) {
			tl_matrix_2.printRow2(row);			
			tr_matrix_2.printRow2(row);		
			ComponentBlock.newLine();
		}		
		
		B2Block b22 = new B2Block(b2_matrix, _class); 
		B1Block b12 = new B1Block(b1_matrix, _class);
		
		//print b1
		System.out.print("B1: \n\n");
		for(int row = 0; row < Y_starting_col; row++) {
			b12.printRow(row, 0, basis.getSize());
			System.out.print("\n");
		}
				
		//print b2
		System.out.print("B2: \n\n");
		for(int row = b2_position.row; row < basis.getSize(); row++) {
			b22.printRow(row, 0, basis.getSize());
					
			System.out.print("\n");
		}
		
		lower_class_test(_class, tl_matrix_2, tr_matrix_2, b12, b22);
		

		Position zerozero = new Position(0,0);
		CBlock C = new CBlock(qnm, basis, zerozero);
		
		int c_class = 3;
		
		CBlock C2 = new CBlock(C, c_class);
		
		carryTest(C2, c_class);
		*/
	}

	private void generate(XBlock tl_matrix, YBlock tr_matrix, B1Block b1_matrix) throws BTFMatrixErrorException, InternalErrorException {
		PopulationChangeVector n;
		int row_added;
	   	for(int i = 0; i < MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M); i++)  { //loop over all possible population changes n
	   		n  = basis.getPopulationChangeVector(i).copy(); // To improve bug safety
	   		
	   		if(n.sumHead(qnm.R - 1) < qnm.M) {
	   			for(int k = 1; k <= qnm.M; k++) {
	   				row_added = tl_matrix.addCE(basis.indexOf(n,k), n, k);
	   				//System.out.println("CE at row: " + row_added);
	   				tr_matrix.addCE(row_added, n, k);
	   				b1_matrix.addCE(row_added, n, k);
	   				
	  			}
	   			for(int s = 1; s < qnm.R; s++) {
	   				n.plusOne(s);
	   				row_added = tl_matrix.addPC(basis.indexOf(n, 1), n, s);
	   				//System.out.println("PC at row: " + row_added);
	   				tr_matrix.addPC(row_added, n, s);
	   				b1_matrix.addPC(row_added, n, s);
	   				n.restore();
	   			}   			    			    			
	   		}
	   	}
	}
	
	private void multiply_test(XBlock X, YBlock Y, B1Block b1, B2Block b2) throws InternalErrorException, BTFMatrixErrorException {
	
		ConvolutionSolver convolution = new ConvolutionSolver(qnm);
		
		BigRational G ;
		
		BigRational[] lhs = new BigRational[basis.getSize()];
		
		BigRational[] rhs = new BigRational[basis.getSize()];	
		
		
		PopulationVector N = qnm.N.copy();
		N.set(qnm.R - 1, 0);	
			
			//generate before and after
			
			for(int i = 0; i < basis.getOrder().size(); i++) {
				PopulationChangeVector n = basis.getOrder().get(i);
				for(int k = 0; k <= qnm.M; k++) {
					
					MultiplicitiesVector m = qnm.getMultiplicitiesVector();	
					m.plusOne(k);
					G = convolution.compute(m,N.changePopulation(n));
					rhs[basis.indexOf(n, k)] = G.copy();
					
					N.plusOne(qnm.R);
					G = convolution.compute(m,N.changePopulation(n));
					N.restore();
					lhs[basis.indexOf(n, k)] = G.copy();
				}
			}	
			
		
			System.out.println("lhs: ");
			MiscFunctions.printMatrix(lhs);
			System.out.println("rhs: ");
			MiscFunctions.printMatrix(rhs);
			
			
			//intialise vector to store results
			BigRational[] lhs_result = new BigRational[basis.getSize()];
			BigRational[] rhs_result = new BigRational[basis.getSize()];
			for(int i = 0; i < lhs_result.length; i++) {
				lhs_result[i] = BigRational.ZERO;
				rhs_result[i] = BigRational.ZERO;
			}
			
			Y.multiply(lhs_result, lhs);
			X.multiply(lhs_result, lhs);
			
			b1.multiply(rhs_result, rhs);
			b2.multiply(rhs_result, rhs);
			
			System.out.println("Multiplied lhs: ");
			MiscFunctions.printMatrix(lhs_result);
			
			System.out.println("Multiplied rhs: ");
			MiscFunctions.printMatrix(rhs_result);
			
	}
	
	private void lower_class_test(int cur_class, XBlock X, YBlock Y, B1Block b1, B2Block b2) throws InternalErrorException, BTFMatrixErrorException {
		
		ConvolutionSolver convolution = new ConvolutionSolver(qnm);
		
		BigRational G ;
		
		BigRational[] lhs = new BigRational[basis.getSize()];
		
		BigRational[] rhs = new BigRational[basis.getSize()];
		
		//for(int cur_class = 1; cur_class <= qnm.R; cur_class++) {
			PopulationVector N = qnm.N.copy();
			for(int i = cur_class - 1; i < qnm.R; i++) {
				N.set(i,0);
			}
			
			System.out.print("N: " + N + "\n");
			
			//system.initialiseForClass(N, cur_class);
			//generate before and after
			//N.restore();
			for(int i = 0; i < basis.getOrder().size(); i++) {
				PopulationChangeVector n = basis.getOrder().get(i);
				for(int k = 0; k <= qnm.M; k++) {
					
					MultiplicitiesVector m = qnm.getMultiplicitiesVector();	
					m.plusOne(k);
					G = convolution.compute(m,N.changePopulation(n));
					rhs[basis.indexOf(n, k)] = G.copy();
					
					N.plusOne(cur_class);
					G = convolution.compute(m,N.changePopulation(n));
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
			
			Y.multiply(lhs_result, lhs);
			X.multiply(lhs_result, lhs);
			
			b1.multiply(rhs_result, rhs);
			b2.multiply(rhs_result, rhs);
			

			//printing lhs and rhs:
			System.out.print(String.format("%32s", "Multiplied lhs: "));
			System.out.print(String.format("%32s", "Multiplied rhs: "));
			System.out.print("\n");
			for(int i = 0; i < lhs.length; i++) {
				System.out.print(String.format("%32s", lhs_result[i]));
				System.out.print(String.format("%32s", rhs_result[i]));
				System.out.print("\n");
			}
	}
	
	private void carryTest(CBlock C, int cur_class) throws InternalErrorException, BTFMatrixErrorException {
		
		//int cur_class = 1;
		
		ConvolutionSolver convolution = new ConvolutionSolver(qnm);
		
		BigRational G ;
		
		BigRational[] lhs = new BigRational[basis.getSize()];
		
		BigRational[] rhs = new BigRational[basis.getSize()];
		
		//for(int cur_class = 1; cur_class <= qnm.R; cur_class++) {
			PopulationVector N = qnm.N.copy();
			for(int i = cur_class - 1; i < qnm.R; i++) {
				N.set(i,0);
			}
			
			System.out.print("N: " + N + "\n");
			
			//system.initialiseForClass(N, cur_class);
			//generate before and after
			//N.restore();
			for(int i = 0; i < basis.getOrder().size(); i++) {
				PopulationChangeVector n = basis.getOrder().get(i);
				for(int k = 0; k <= qnm.M; k++) {
					
					MultiplicitiesVector m = qnm.getMultiplicitiesVector();	
					m.plusOne(k);
					G = convolution.compute(m,N.changePopulation(n));
					rhs[basis.indexOf(n, k)] = G.copy();
					
					N.plusOne(cur_class);
					G = convolution.compute(m,N.changePopulation(n));
					N.restore();
					lhs[basis.indexOf(n, k)] = G.copy();
				}
			}
			
			//intialise vector to store results			
			BigRational[] rhs_result = new BigRational[basis.getSize()];
			for(int i = 0; i < rhs_result.length; i++) {				
				rhs_result[i] = BigRational.ZERO;
			}		
			
			C.multiply(rhs_result, rhs);
			
			//printing lhs and rhs:
			System.out.print(String.format("%32s", "lhs: "));
			System.out.print(String.format("%32s", "rhs: "));
			System.out.print("\n");
			for(int i = 0; i < lhs.length; i++) {
				System.out.print(String.format("%32s", lhs[i]));
				System.out.print(String.format("%32s", rhs[i]));
				System.out.print("\n");
			}
			//printing lhs and rhs:
			System.out.print(String.format("%32s", "lhs: "));
			System.out.print(String.format("%32s", "Multiplied rhs: "));
			System.out.print("\n");
			for(int i = 0; i < lhs.length; i++) {
				System.out.print(String.format("%32s", lhs[i]));
				System.out.print(String.format("%32s", rhs_result[i]));
				System.out.print("\n");
			}
	}
}
