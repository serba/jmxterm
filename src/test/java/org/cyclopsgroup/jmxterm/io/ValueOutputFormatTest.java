package org.cyclopsgroup.jmxterm.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test case for {@link ValueOutputFormat}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
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
        StringWriter out = new StringWriter();
        f.printValue( new WriterCommandOutput( out ), Arrays.asList( "abc", "xyz" ) );
        assertEquals( "( \"abc\", \"xyz\" )", out.toString() );
    }
}
