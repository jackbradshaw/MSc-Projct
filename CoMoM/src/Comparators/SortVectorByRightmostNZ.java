package Comparators;

import java.util.Comparator;

import DataStructures.PopulationChangeVector;

public class SortVectorByRightmostNZ implements Comparator<PopulationChangeVector> {
	
	 /**
     * Compares two PopulationChangeVector objects.
     * 
     * First vector are compared according to the position of their rightmost non-zero element
     * Then the number of non zero elements (h)
     * Then the value at the non-zero positions, left to right
     *
     * @param v1, v2 The PopulationChangeVectors to be compared.
     * @return -1 if this < o, 0 if this = o, 1 if this > o
     */
   @Override
    public int compare(PopulationChangeVector v1, PopulationChangeVector v2) {    	

    	if (v1.size() < v2.size()) {
            return -1;
        } else if (v1.size() > v2.size()) {
            return 1;
        }else {   	
        	
        	// Vectors have same length
    		// Check 1: vector with rightmost non-zero value is greater
    		for (int j = v1.size() - 1 ; j >= 0; j--) {
    	         if (v1.get(j) == 0 && v2.get(j) > 0 ) {
    	             return -1; // v2 has the right-most non zero
    	         }
    	         if (v1.get(j) > 0 && v2.get(j) == 0) {
    	             return 1;
    		     }
    		}
        	
        	//Check 2: Compare number of non zero elements
        	if(v1.countNonZeroElements() < v2.countNonZeroElements()) {
        		return -1;
        	} else if (v1.countNonZeroElements() > v2.countNonZeroElements()) {
        		return 1;
        	}else {
        		// Vectors have the same number of non zero elements
        		// Check 3: Compare non zero element positions interpreted as binary numbers
        		int value1 = 0, value2 = 0;
        		int size = v1.size();
        		for( int i = 0; i < size - 1; i++) { //sum over first R - 1 positions
        			if(v1.get( size - 2 - i) > 0 ) value1 += (int) Math.pow(2,i);
        			if(v2.get( size - 2 - i) > 0 ) value2 += (int) Math.pow(2,i);
        		}
        		if(value1 < value2) {
        			return -1;
        		} else if(value1 > value2) {
        			return 1;
        		} else {
        			// Vectors have non zero elements in same position
        			// Check 4: Compare values of non zero elements, left to right
        			for( int j = 0; j < v1.size() - 1; j++) { //sum over first R - 1 positions
            			if(v1.get(j) < v2.get(j) ) return -1;
            			if(v1.get(j) > v2.get(j) ) return 1;
            		}   			
        		return 0;	
        		}
        	}
        }
    }
}
