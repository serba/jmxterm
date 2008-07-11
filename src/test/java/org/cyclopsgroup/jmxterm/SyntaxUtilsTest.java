package org.cyclopsgroup.jmxterm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SyntaxUtilsTest
{
    @Test
    public void testParseNormally()
    {
        assertEquals( "x", SyntaxUtils.parse( "x", "java.lang.String" ) );
        assertEquals( 3, SyntaxUtils.parse( "3", "int" ) );
        assertEquals( 3L, SyntaxUtils.parse( "3", "long" ) );
        assertNull( SyntaxUtils.parse( "", "java.util.Date" ) );
        assertNull( SyntaxUtils.parse( "null", "java.lang.String" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testParseWithWrongType()
    {
        SyntaxUtils.parse( "x", "x" );
    }
}
