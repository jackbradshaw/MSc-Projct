package Basis;

import java.util.HashSet;
import java.util.Set;
import DataStructures.BigRational;
import DataStructures.QNModel;
import Exceptions.InternalErrorException;

public abstract class Basis {

	/**
	 * The Queuing Network Model under study
	 */
	protected QNModel qnm;
	
	/**
	 * Variables to store qnm fields for easy access	
	 **/	
	protected int R;
	protected int M;
	
	/**
	 * The basis vector of normalising constants
	 */
	protected BigRational[] basis;
	
	/**
	 * The previous basis vector of normalising constants in the sequence of computation
	 */
	protected BigRational[] previous_basis;
	
	/**
	 * Not really sure what this is for yet, but MoM uses it...
	 */
	protected Set<Integer> uncomputables;
	
	/**
	 * Variable to store the size of the basis, due to frequent use
	 */
	protected int size;	
	
	/**
	 * Constructor
	 * @param qnm The Queuing Network Model under study
	 */
	public Basis(QNModel qnm) {
		this.qnm = qnm;
		R = qnm.R;
		M = qnm.M;
		setSize();		
		basis = new BigRational[size];
		previous_basis = new BigRational[size];
		
		uncomputables = new HashSet<Integer>();
	}
	
	/**
	 * Initialises the basis for population (0,...0)
	 * @throws InternalErrorException 
	 */	
	public abstract void initialiseBasis() throws InternalErrorException;
	
	public abstract void initialiseForClass(int current_class) throws InternalErrorException;
	
	/**
	 * Calculates the size of the basis to be store in variable size
	 */
	protected abstract void setSize();
	
	/**
	 * @return The size of the basis
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Returns the basis vector for mutation
	 */
	public BigRational[] getBasis() {
		return basis;
	}
	
	/**
	 * Returns the previous basis vector 
	 */
	public BigRational[] getPreviousBasis() {
		return previous_basis;
	}
	
	/**
	 * Computes Mean Throughput and Mean Queue Length performance indices
	 * and stores them in the queueing network model object, qnm
	 * @throws InternalErrorException
	 */
	public abstract void computePerformanceMeasures() throws InternalErrorException;
	
	
	/**
	 * Returns the Normalising Constant for the current computed population  
	 * @throws InternalErrorException 
	 */
	public abstract BigRational getNormalisingConstant() throws InternalErrorException;
	
	/**
	 * Sets the basis vector 
	 */
	//TODO think about this, copies references, garbage collection....
	public void setBasis(BigRational[] v) {
		for(int i = 0; i < basis.length; i++) {	
			previous_basis[i] = basis[i].copy();
		}
		basis = v;
	}
	
	public void reset_uncomputables() {
		for(int i = 0; i < size; i++) {
			uncomputables.add(i);
		}
	}
	
	public void computatble(int i) {
		uncomputables.remove(i);
	}
	
	public Set<Integer> getUncomputables() {
		return uncomputables;
	}
	
	public BigRational getOldValue(int index) {
		return previous_basis[index].copy();
	}
	
	public BigRational getNewValue(int index) {
		return basis[index].copy();
	}
	
	public void setValue(BigRational value, int index) {
		basis[index] = value.copy();
	}
	
	public void startBasisComputation() {
		for(int i = 0; i < basis.length; i++) {	
			previous_basis[i] = basis[i].copy();
		}
	}
}
