package Basis;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Utilities.MiscFunctions;

import DataStructures.QNModel;

public class CoMoMReorderingBasisTest {

	private QNModel qnm;
	private CoMoMReorderingBasis basis;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		qnm = new QNModel("jack_test.txt");
		basis = new CoMoMReorderingBasis(qnm);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdateDelta() {
		
		
		int [] order_delta = new int[basis.getSize()];
		
		basis.updateOffset(1);		
		int [] previous_order_offset = new int[basis.getSize()];
		System.arraycopy(basis.getOrderOffset(),0,previous_order_offset,0,basis.getSize());
		MiscFunctions.printMatrix(previous_order_offset);
		
		basis.updateOffset(2);
		int [] order_offset = new int[basis.getSize()];
		System.arraycopy(basis.getOrderOffset(),0,order_offset,0,basis.getSize());
		MiscFunctions.printMatrix(order_offset);
		
		for(int i = 0; i < basis.getSize(); i++) {
			//System.out.println(order_offset[i]);
			//System.out.println(order_delta[i]);
			order_delta[i] = order_offset[i] - previous_order_offset[i];	
			//System.out.println(order_delta[i]);
		}
		
		MiscFunctions.printMatrix(order_delta);
		
	}

}
