package LinearSystem;

import javax.naming.OperationNotSupportedException;

import Basis.CoMoMBasis;
import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.PopulationVector;
import DataStructures.QNModel;
import Exceptions.BTFMatrixErrorException;
import Exceptions.InconsistentLinearSystemException;
import Exceptions.InternalErrorException;
import LinearSystem.TopLevelBlocks.B1Block;
import LinearSystem.TopLevelBlocks.B2Block;
import LinearSystem.TopLevelBlocks.CBlock;
import LinearSystem.TopLevelBlocks.XBlock;
import LinearSystem.TopLevelBlocks.YBlock;
import Utilities.MiscFunctions;

public class BTFLinearSystem extends LinearSystem {
	
	//Initial Matrix Components
	XBlock x_block;
	YBlock y_block;
	B1Block b1_block;
	B2Block b2_block;
	CBlock c_block;
	
	//Current Matrix Components
	XBlock x;
	YBlock y;
	B1Block b1;
	B2Block b2;
	CBlock c;
	
	//Vector for storing intermediate solution
	BigRational[] rhs;
	
	public BTFLinearSystem(QNModel qnm, CoMoMBasis basis)
			throws InternalErrorException, BTFMatrixErrorException, InconsistentLinearSystemException {
		
		super(qnm, basis);		
		
		//Create and initialise the component blocks for the final class
		x_block  = new XBlock (qnm, basis);
		x_block.initialise();
		
		y_block  = new YBlock (qnm, basis);
		y_block.initialise();
		
		b1_block = new B1Block(qnm, basis);
		b1_block.initialise();
		
		b2_block = new B2Block(qnm, basis);
		b2_block.initialise();
		
		c_block  = new CBlock (qnm, basis);	
		c_block.initialise();
		
		//Add PCs and CEs to the matrices 
		generate();
		
		//Create rhs vector
		rhs = new BigRational[basis.getSize()];
		
		printFullMatrices();
	}
	
	private void generate() throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		PopulationChangeVector n;
		int row_added;
	   	for(int i = 0; i < MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M); i++)  { //loop over all possible population changes n
	   		n  = basis.getPopulationChangeVector(i).copy(); // To improve bug safety
	   		
	   		if(n.sumHead(qnm.R - 1) < qnm.M) {
	   			for(int k = 1; k <= qnm.M; k++) {
	   				row_added = x_block.addCE(basis.indexOf(n,k), n, k);   				
	   				y_block.addCE(row_added, n, k);
	   				b1_block.addCE(row_added, n, k);	   				
	  			}
	   			for(int s = 1; s < qnm.R; s++) {
	   				n.plusOne(s);
	   				row_added = x_block.addPC(basis.indexOf(n, 1), n, s);	   				
	   				y_block.addPC(row_added, n, s);
	   				b1_block.addPC(row_added, n, s);
	   				n.restore();
	   			}   			    			    			
	   		}
	   	}
	   	x_block.LUPDecompose();
	}
	
	@Override
	public void initialiseMatricesForClass(PopulationVector current_N, int current_class) throws BTFMatrixErrorException, InternalErrorException, InconsistentLinearSystemException {
		
		ComponentBlock.setCurrentClassPopulation(1);		
		
		//create sub-blocks for the current class
		x  = (XBlock)   x_block.subBlock(current_class);
		y  = (YBlock)   y_block.subBlock(current_class);
		b1 = (B1Block) b1_block.subBlock(current_class);
		b2 = (B2Block) b2_block.subBlock(current_class);
		c  = (CBlock)   c_block.subBlock(current_class);
		
		System.out.print("Matrices for class " + current_class + "\n\n\n");
		//printWorkingMatrices();		
	}
	
	@Override
	public void solve() throws OperationNotSupportedException, InconsistentLinearSystemException, InternalErrorException, BTFMatrixErrorException {
	//	System.out.println("Solving System...\n");
		
		basis.startBasisComputation();
		//System.out.println("BEFORE: ");
		//basis.print_values();
		
		//Order of solving is important:
		//B First
		b1.solve(rhs);
		b2.solve(rhs);
		c.solve(rhs);
		
		//Then A; Y then X
		y.solve(rhs);
		//System.out.println("AFTER Y: ");
		//basis.print_values();
		x.solve(rhs);		
		
		//basis.print_values();
	}
	
	@Override
	public void update(int current_class_population ) {
		ComponentBlock.setCurrentClassPopulation(current_class_population);
	}
	
	public void multiply(BigRational[] lhs_result, BigRational[] lhs, BigRational[] rhs_result, BigRational[] rhs) throws BTFMatrixErrorException {
		
		//Multiply each component
		x.multiply(lhs_result, lhs);
		y.multiply(lhs_result, lhs);
		b1.multiply(rhs_result, rhs);
		b2.multiply(rhs_result, rhs);
		c.multiply(rhs_result, rhs);	
		
		
		//achieves identity rows
		for(int i = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M; i < basis.getSize(); i++) {
			lhs_result[i] = lhs[i].copy();
		}
		/*
		for(int i = 0; i < basis.getSize(); i++) {
			lhs_result[i] = lhs[i].copy();
		}
		b1.multiply(rhs_result, rhs);
		b2.multiply(rhs_result, rhs);
		c.multiply(rhs_result, rhs);
		*/
	}
	
	private void printFullMatrices() {
		print(x_block, y_block, b1_block, b2_block, c_block);
	}
	
	private void printWorkingMatrices() {
		print(x, y, b1, b2,c);
	}
	
	private void print(XBlock x_block, YBlock y_block, B1Block b1_block, B2Block b2_block, CBlock c_block) {
		
		//print A
		//calculate the number of rows in the top blocks of the matrices
		int top_half_rows = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M) * qnm.M;
		
		for(int row = 0; row < top_half_rows; row++) {
			x_block.printRow2(row);			
			y_block.printRow2(row);		
			ComponentBlock.newLine();
		}
					
		//print B1
		System.out.print("B1: \n\n");
		for(int row = 0; row < top_half_rows; row++) {
			b1_block.printRow2(row);
			ComponentBlock.newLine();
		}
				
		//print B2
		System.out.print("B2: \n\n");
		for(int row = top_half_rows; row < basis.getSize(); row++) {
			b2_block.printRow2(row);					
			ComponentBlock.newLine();
		}
	}
	
}
