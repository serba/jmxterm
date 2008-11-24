package org.cyclopsgroup.jmxterm.io;

import java.io.PrintWriter;

import org.apache.commons.lang.SystemUtils;

/**
 * General abstract class to output message and values
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class CommandOutput
{
    /**
     * @return PrintWriter of output for more flexible usage
     */
    public abstract PrintWriter getMessageWriter();

    /**
     * Print out value to output without line break
     * 
     * @param output Value to print out
     */
    public abstract void print( String output );

    /**
     * Print out value to output as standalone line
     * 
     * @param output Value to print out
     */
    public void println( String output )
    {
        print( output );
        print( SystemUtils.LINE_SEPARATOR );
    }

    /**
     * Print message to non-standard console for human to read. New line is always appended
     * 
     * @param message Message to print out.
     */
    public abstract void printMessage( String message );

    /**
     * Close the output;
     */
    public void close()
    {
    }
}
