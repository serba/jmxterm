package org.cyclopsgroup.jmxterm.boot;

import java.io.IOException;

/**
 * An abstract class that provides command line input line by line
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
abstract class CommandInput
{
    /**
     * @return A line of input
     * @throws IOException
     */
    public abstract String readLine()
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
