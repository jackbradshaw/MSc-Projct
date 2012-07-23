package Basis;

import java.util.Collections;

import DataStructures.QNModel;

public class SortedCoMoMBasis extends CoMoMBasis {
	public SortedCoMoMBasis(QNModel qnm) {
		super(qnm);
	}
	
	@Override
	protected void sort() {
		Collections.sort(order);
	}

}
