package Exceptions;

/**
 * This class represents an exception that can be thrown if an internal error is
 * encountered somewhere in the program.
 *
 * @author Michail Makaronidis, 2010
 */
public class BTFMatrixErrorException extends Exception {

    /**
     * Creates an InternalErrorException with an exception message.
     *
     * @param string The exception message
     */
    public BTFMatrixErrorException(String string) {
        super(string);
    }

}