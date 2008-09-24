package org.cyclopsgroup.jmxterm.boot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.apache.commons.lang.Validate;

/**
 * Implementation of CommandInput with given File
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class FileCommandInput
    extends CommandInput
{
    private final LineNumberReader in;

    /**
     * Read input from a given file
     * 
     * @param inputFile Given input file
     * @throws FileNotFoundException Thrown when file doesn't exist
     */
    FileCommandInput( File inputFile )
        throws FileNotFoundException
    {
        Validate.notNull( inputFile, "Input can't be NULL" );
        this.in = new LineNumberReader( new FileReader( inputFile ) );
    }

    /**
     * @inheritDoc
     */
    @Override
    public String readLine()
        throws IOException
    {
        return in.readLine();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void close()
        throws IOException
    {
        in.close();
    }
}
