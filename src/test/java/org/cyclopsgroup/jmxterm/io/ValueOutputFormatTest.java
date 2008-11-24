package org.cyclopsgroup.jmxterm.io;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class ValueOutputFormatTest
{
    /**
     * Test {@link ValueOutputFormat#printValue(CommandOutput, Object, int)}
     * 
     * @throws IOException IO error
     */
    @Test
    public void testPrintValue()
        throws IOException
    {
        ValueOutputFormat f = new ValueOutputFormat();
        f.printValue( new PrintStreamCommandOutput(), Arrays.asList( "abc", 123, "xyz" ) );
    }
}
