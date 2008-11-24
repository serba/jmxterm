package org.cyclopsgroup.jmxterm.io;

import java.io.IOException;

/**
 * An abstract class that provides command line input line by line
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class CommandInput
{
    /**
     * @return A line of input
     * @throws IOException
     */
    public abstract String readLine()
        throws IOException;

    /**
     * Read input without echo'ing back keyboard input
     * 
     * @return A line of input
     * @throws IOException
     */
    public abstract String readMaskedString()
        throws IOException;

    /**
     * Close this input
     * 
     * @throws IOException
     */
    public void close()
        throws IOException
    {
    }
}
