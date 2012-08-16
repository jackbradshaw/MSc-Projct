/**
 * Copyright (C) 2010, Michail Makaronidis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package DataStructures;

import Exceptions.IllegalValueInInputFileException;
import Exceptions.InputFileParserException;
import Exceptions.InternalErrorException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.naming.OperationNotSupportedException;

import Utilities.MiscFunctions;

/**
 * This class contains an implementation of the input file reader and parser,
 * as well as a storage data structure for the model and performance evaluation 
 * values. It also implements some useful operations using the model, such as
 * various estimations.
 * 
 * @author Michail Makaronidis, 2010
 */
public class QNModel {

    /**
     * Number of classes R
     */
    /**
     * Number of queues M
     */
    public int R, M;
    /**
     * Contains total number of jobs for each class
     */
    public PopulationVector N;
    /**
     * Contains the mean delays (think time) for each class
     */
    private EnhancedVector Z;
    /**
     * Contains the multiplicities for each queue
     */
    public MultiplicitiesVector multiplicities;
    /**
     * Contains the service demands for each class at each queue
     */
    private Integer[][] D;
    private BigRational[] X;
    private BigRational[][] Q;
    private BigRational G;
    private boolean isNormalisingCOnstantComputed = false;
    private boolean arePerformanceMeasuresComputed = false;

    /**
     * Creates a new QNModel object and initialises it according to an input file.
     *
     * @param filename The path to the input file
     * @throws InputFileParserException An exception is thrown if any error is encountered during input file parsing
     *
     */
    public QNModel(String filename) throws InputFileParserException {
        super();
        R = M = 0;
        N = new PopulationVector();
        Z = new EnhancedVector();
        multiplicities = new MultiplicitiesVector();
        readModel(filename);
    }

    /**
     * Creates a new QNModel object and initialises in according to the given
     * arguments.
     * @param R The number of classes
     * @param M The number of queue types
     * @param N The array containing the population per class
     * @param Z The array containing the total delay per class
     * @param multiplicities The array containing the queue multiplicities
     * @param D The array containing the service demands
     * @throws Exception Thrown if the arguments' sizes do not match.
     */
    public QNModel(int R, int M, Integer[] N, Integer[] Z, Integer[] multiplicities, Integer[][] D) throws Exception {
        this.R = R;
        this.M = M;
        if ((N.length != R) || (Z.length != R) || (multiplicities.length != M) || (D.length != M) || (D[0].length != R)) {
            throw new Exception("Model sizes do not match");
        }

        this.N = new PopulationVector(N);
        this.Z = new EnhancedVector(Z);
        this.multiplicities = new MultiplicitiesVector(multiplicities);
        this.D = D;
    }

    /**
     * Returns the service demand of a class at a queue. Indexes start from 0.
     * @param k Queue index
     * @param r Class index
     * @return The corresponding service demand
     */
    public Integer getDemand(int k, int r) {
        return D[k][r];
    }

    /**
     * Returns the service demand of a class at a queue as a BigDecimal object.
     * Indexes start from 0.
     *
     * @param k Queue index
     * @param r Class index
     * @return The corresponding service demand
     */
    public BigDecimal getDemandAsBigDecimal(int k, int r) {
        return new BigDecimal(getDemand(k, r));
    }

    /**
     * Returns the service demand of a class at a queue as a BigInteger object.
     * Indexes start from 0.
     *
     * @param k Queue index
     * @param r Class index
     * @return The corresponding service demand
     */
    public BigInteger getDemandAsBigInteger(int k, int r) {
        return new BigInteger(getDemand(k, r).toString());
    }

    /**
     * Returns the service demand of a class at a queue as a BigRational object.
     * Indexes start from 0.
     *
     * @param k Queue index
     * @param r Class index
     * @return The corresponding service demand
     */
    public BigRational getDemandAsBigRational(int k, int r) {
        return new BigRational(getDemand(k, r));
    }

    /**
     * Returns the delay (think time) of a class. Indexes start from 0.
     *
     * @param r Class index
     * @return The corresponding delay (think time)
     */
    public Integer getDelay(int r) {
        return Z.get(r);
    }

    /**
     * Returns the delay (think time) of a class as a BigDecimal object .
     * Indexes start from 0.
     *
     * @param r Class index
     * @return The corresponding delay (think time)
     */
    public BigDecimal getDelayAsBigDecimal(int r) {
        return new BigDecimal(getDelay(r));
    }

    /**
     * Returns the delay (think time) of a class as a BigInteger object .
     * Indexes start from 0.
     *
     * @param r Class index
     * @return The corresponding delay (think time)
     */
    public BigInteger getDelayAsBigInteger(int r) {
        return new BigInteger(getDelay(r).toString());
    }

    /**
     * Returns the delay (think time) of a class as a BigRational object .
     * Indexes start from 0.
     *
     * @param r Class index
     * @return The corresponding delay (think time)
     */
    public BigRational getDelayAsBigRational(int r) {
        return new BigRational(getDelay(r));
    }

    /**
     * Initialises the current QNModel object according to an input file.
     * Decimal numbers are approximated as integers.
     *
     * @param filename The path to the input file
     */
    private void readModel(String filename) throws InputFileParserException {
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            StreamTokenizer stok = new StreamTokenizer(br);
            stok.parseNumbers();
            // Begin reading file
            // The first line stores the number of classes (R)
            stok.nextToken();
            if ((stok.ttype != StreamTokenizer.TT_EOF) && (stok.ttype == StreamTokenizer.TT_NUMBER)) {
                Double readR = stok.nval;
                if (hasNoFractionalPart(readR)) {
                    this.R = readR.intValue();
                } else {
                    throw new IllegalValueInInputFileException("Cannot have a decimal value as number of classes (R).");
                }
            } else {
                throw new InputFileParserException("Bad format of input file.");
            }
            // The second line stores the number of jobs of each class circulating (N)
            for (int i = 0; i < this.R; i++) {
                stok.nextToken();
                if ((stok.ttype != StreamTokenizer.TT_EOF) && (stok.ttype == StreamTokenizer.TT_NUMBER)) {
                    Double readN = stok.nval;
                    if (hasNoFractionalPart(readN)) {
                        N.add(readN.intValue());
                    } else {
                        throw new IllegalValueInInputFileException("Cannot have a decimal value as number of jobs for a class (N).");
                    }
                } else {
                    throw new InputFileParserException("Bad format of input file.");
                }
            }
            // The third line stores the delay (think time) for each class (Z)
            for (int i = 0; i < this.R; i++) {
                stok.nextToken();
                if ((stok.ttype != StreamTokenizer.TT_EOF) && (stok.ttype == StreamTokenizer.TT_NUMBER)) {
                    Double readZ = stok.nval;
                    if (hasNoFractionalPart(readZ)) {
                        Z.add(readZ.intValue());
                    } else {
                        throw new IllegalValueInInputFileException("Cannot have a decimal value as delay time (Z).");
                    }
                } else {
                    throw new InputFileParserException("Bad format of input file.");
                }
            }
            // The fourth line contains the number of queues (M)
            stok.nextToken();
            if ((stok.ttype != StreamTokenizer.TT_EOF) && (stok.ttype == StreamTokenizer.TT_NUMBER)) {
                Double readM = stok.nval;
                if (readM == 0) {
                    throw new IllegalValueInInputFileException("Cannot have zero as number of queues (M).");
                } else if (hasNoFractionalPart(readM)) {
                    this.M = readM.intValue();
                } else {
                    throw new IllegalValueInInputFileException("Cannot have a decimal value as number of queues (M).");
                }
            } else {
                throw new InputFileParserException("Bad format of input file.");
            }
            D = new Integer[M][R];
            // And lastly there is a (noOfQueues)x(1+noOfClasses) matrix containing the "multiplicities"
            // on the first column and the service demands (D) on the next ones
            for (int i = 0; i < this.M; i++) {
                stok.nextToken();
                if ((stok.ttype != StreamTokenizer.TT_EOF) && (stok.ttype == StreamTokenizer.TT_NUMBER)) {
                    Double readMultiplicity = stok.nval;
                    if (hasNoFractionalPart(readMultiplicity)) {
                        multiplicities.add(readMultiplicity.intValue());
                    } else {
                        throw new IllegalValueInInputFileException("Cannot have a decimal value as multiplicity.");
                    }
                } else {
                    throw new InputFileParserException("Bad format of input file.");
                }
                for (int j = 0; j < this.R; j++) {
                    stok.nextToken();
                    if ((stok.ttype != StreamTokenizer.TT_EOF) && (stok.ttype == StreamTokenizer.TT_NUMBER)) {
                        Double readD = stok.nval;
                        if (hasNoFractionalPart(readD)) {
                            D[i][j] = readD.intValue();
                        } else {
                            throw new IllegalValueInInputFileException("Cannot have a decimal value as demand.");
                        }
                    } else {
                        throw new InputFileParserException("Bad format of input file.");
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            throw new InputFileParserException("File not found.");
        } catch (IOException ex) {
            throw new InputFileParserException("I/O exception");
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                throw new InputFileParserException("I/O exception");
            }
        }
    }

    /**
     * Prints the parsed data of the current QNModel object.
     */
    public void printModel() {
        System.out.println(this.R);
        System.out.println(this.N.toString());
        System.out.println(this.Z.toString());
        System.out.println(this.M);
        for (int i = 0; i < this.M; i++) {
            System.out.print(this.multiplicities.get(i) + "\t");
            for (int j = 0; j < this.R; j++) {
                System.out.print(this.getDemand(i, j) + " ");
            }
            System.out.println();
        }
    }

    /**
     * Stores the performance measures (mean throughputs X and mean queue
     * lengths Q) in the model description object (QNModel). The function can
     * only be used once.
     * @param Q Mean queue lengths matrix
     * @param X Mean throughputs matrix
     * @throws InternalErrorException Thrown on attempt to re-assign performance measures
     */
    public void setPerformanceMeasures(BigRational[][] Q, BigRational[] X) throws InternalErrorException {
        if (!arePerformanceMeasuresComputed) {
            this.X = X;
            this.Q = Q;
            arePerformanceMeasuresComputed = true;
        } else {
            throw new InternalErrorException("Performance Measures have already been computed.");
        }
    }

    /**
     * Stores the normalising constant (G) in the model description object
     * (QNModel). The function can only be used once.
     * @param G The normalising constant
     * @throws InternalErrorException Thrown on attempt to re-assign performance measures
     */
    public void setNormalisingConstant(BigRational G) throws InternalErrorException {
        if (!isNormalisingCOnstantComputed) {
            this.G = G;
            isNormalisingCOnstantComputed = true;
        } else {
            throw new InternalErrorException("Normalising Constant has already been computed.");
        }
    }

    /**
     * Returns a string containing the computed normalising constant G.
     * @return A string containing the computed normalising constant G
     */
    public String getPrettyNormalisingConstant() {
        if (isNormalisingCOnstantComputed) {
            if (G.isBigDecimal()) {
                return G.asBigDecimal().toString();
            } else {
                return G.toString();
            }
        } else {
            throw new UnsupportedOperationException("Normalising Constant has not been computed yet.");
        }
    }

    /**
     * Returns the computed normalising constant G.
     * @return The computed normalising constant G
     */
    public BigRational getNormalisingConstant() {
        if (isNormalisingCOnstantComputed) {
            return G;
        } else {
            throw new UnsupportedOperationException("Normalising Constant has not been computed yet.");
        }
    }

    /**
     * Returns the computed mean throughputs X of the current model.
     * @return The computed mean throughputs X
     */
    public BigRational[] getMeanThroughputs() {
        if (arePerformanceMeasuresComputed) {
            return X;
        } else {
            throw new UnsupportedOperationException("Performance Measures have not been computed yet.");
        }
    }

    /**
     * Returns the computed mean queue lengths Q of the current model.
     * @return The computed mean queue lengths Q
     */
    public BigRational[][] getMeanQueueLengths() {
        if (arePerformanceMeasuresComputed) {
            return Q;
        } else {
            throw new UnsupportedOperationException("Performance Measures have not been computed yet.");
        }
    }

    private int getMaxDemand() {
        int max = 0;
        for (int k = 0; k < this.M; k++) {
            for (int r = 0; r < this.R; r++) {
                if (D[k][r] > max) {
                    max = D[k][r];
                }
            }
        }
        return max;
    }

    /**
     * Returns the maximum possible normalising constant of the queueing network.
     * @return The maximum value as a BigInteger object
     * @throws OperationNotSupportedException Thrown if the maximum normalising constant cannot be computed
     */
    public BigInteger getMaxG() throws OperationNotSupportedException {
        int Dmax = Z.max();
        for (int k = 0; k < this.M; k++) {
            for (int r = 0; r < this.R; r++) {
                if (D[k][r] > Dmax) {
                    Dmax = D[k][r];
                }
            }
        }
        int Ntot = N.sum();
        int nmax = (int) (Math.round(Math.ceil(Math.log10(Dmax * (Ntot + M + R)))) * Ntot);
        return BigInteger.TEN.pow(nmax + 1);
    }

    /**
     * Returns the maximum value of the queueing network model.
     * @return The maximum value
     * @throws OperationNotSupportedException Thrown if the maximum value cannot be computed
     */
    public int getMaxModelValue() throws OperationNotSupportedException {
        int max = N.max();
        max = (max < Z.max()) ? Z.max() : max;
        max = (max < getMaxDemand()) ? getMaxDemand() : max;
        return max;
    }

    /**
     * Checks whether a double has a zero fractional part, i.e. it represents an
     * integer number.
     *
     * @param d The number to check
     * @return True if it is an integer, false otherwise
     */
    public boolean hasNoFractionalPart(double d) {
        if (d == (int) d) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method returns a copy of the current MultiplicitiesVector with the values
     * contained in this QNModel object (quening network model).
     *
     * @return The MultiplicitiesVector object
     */
    public MultiplicitiesVector getMultiplicitiesVector() {
        return this.multiplicities.copy();
    }

    /**
     * This method returns a copy of the current PopulationVector with the values
     * contained in this QNModel object (quening network model).
     *
     * @return The PopulationVector object
     */
    public PopulationVector getPopulationVector() {
    	PopulationVector to_return = new PopulationVector();
    	for(int i = 0; i < R; i++) {
    		to_return.add(this.N.get(i));
    	}
        return to_return;
    }

    /**
     * Returns the mean queue lengths as an MxR array of doubles.
     * @return The double[M][R] array.
     */
    public double[][] getMeanQueueLengthsAsDoubles() {
        double[][] toReturn = new double[M][R];
        for (int k = 0; k < M; k++) {
            for (int r = 0; r < R; r++) {
                toReturn[k][r] = Q[k][r].approximateAsDouble();
            }
        }
        return toReturn;
    }

    /**
     * Returns the mean throughputs as an array of R doubles.
     * @return The double[R] array.
     */
    public double[] getMeanThroughputsAsDoubles() {
        double[] toReturn = new double[R];
        for (int r = 0; r < R; r++) {
            toReturn[r] = X[r].approximateAsDouble();
        }
        return toReturn;
    }
    
    /**
     * Added by Jack Bradshaw
     * Prints the preformance measures computed for the model.
     */
    public void printPerformaceMeasrues() {
    	System.out.println("\nMean Throughputs:");
    	MiscFunctions.printMatrix(X);
    	
    	System.out.println("Mean Queue Lengths:");
    	MiscFunctions.printMatrix(Q);
    }
    
    public void setM(int m) {
    	this.M = m;
    }
    
    public void setR(int r) {
    	this.R = r;
    }


}
