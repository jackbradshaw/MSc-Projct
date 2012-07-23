package Basis;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Utilities.MiscFunctions;

import DataStructures.BigRational;
import DataStructures.PopulationChangeVector;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public class CoMoMBasisTest {

	private QNModel qnm;
	private CoMoMBasis basis;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		qnm = new QNModel("jack_test.txt");
		basis = new CoMoMBasis(qnm);
			}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Ensures all required vectors are produced by checking they are all positive, and bounded above by M,
	 * are all distinct and of the correct number
	 */
	@Test
	public void testGenerate() {
		ArrayList<PopulationChangeVector> order = basis.getOrder();
		Iterator<PopulationChangeVector> it = order.iterator();
		HashSet<PopulationChangeVector> set = new HashSet<PopulationChangeVector>();
		PopulationChangeVector v;
		int amount = 0;
		while(it.hasNext()) {
			v = it.next();
			for(int i = 0; i < v.size(); i++) {
				if(v.get(i) < 0 ) fail("A vector with a negative element was generated!");
			}
			if(v.sum() > qnm.M) fail("A vector with too greater sum was generated!");
			amount++;		
			set.add(v);
			
		}
		int correct_size =  MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1, qnm.M);
		
		//correct number of vectors generated
		assertEquals("Incorrect amount of vectors generated!", correct_size ,amount);
		
		//vectors are distinct
		assertEquals("Vectors not distinct!", correct_size, set.size());		
	}
	
	/**
	 * Warning: Test order specific, but method its test is not
	 */
	@Test
	public void testIndexOf() {
		PopulationChangeVector n = new PopulationChangeVector(0,qnm.R);		
		
		try {
			//n = [0,0]
			assertEquals("Error at no queues added!", 6, basis.indexOf(n,0));
			assertEquals("Error at queue 2 added!",   3, basis.indexOf(n,2));
			assertEquals("Error at queue 1 added!",   0, basis.indexOf(n,1));			
			
			n.plusOne(1);
			//n = [1,0]
			assertEquals("Error at no queues added!", 7, basis.indexOf(n,0));
			assertEquals("Error at queue 2 added!",   4, basis.indexOf(n,2));
			assertEquals("Error at queue 1 added!",   1, basis.indexOf(n,1));
			
			n.plusOne(1);
			//n = [2,0]
			assertEquals("Error at no queues added!", 8, basis.indexOf(n,0));
			assertEquals("Error at queue 2 added!",   5, basis.indexOf(n,2));
			assertEquals("Error at queue 1 added!",   2, basis.indexOf(n,1));
			
			
		} catch (InternalErrorException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Warning: Test order specific, but method its test is not
	 */
	@Test 
	public void testInitialiseBasis() {
		try {
			int size = MiscFunctions.binomialCoefficient(qnm.M + qnm.R - 1 , qnm.M);
			basis.initialiseBasis();
			BigRational[] values = basis.getBasis();		
			PopulationChangeVector v; 
			for(int i = 0; i < values.length; i++) {
				if(values[i].equals(BigRational.ONE)) {
					v = basis.getPopulationChangeVector(i % (size));
					assertEquals("Non-zero population constant initialised to 1", 0, v.sum()); 
				} else if(values[i].equals(BigRational.ZERO)) {
					v = basis.getPopulationChangeVector(i % (size));
					assertTrue("Zero population constant initialised to 1", v.sum() > 0); 
				} else {
					fail("Initial condition not zero or one!");
				}
			}		
		} catch (InternalErrorException e) {		
			e.printStackTrace();
		}
	}

}
