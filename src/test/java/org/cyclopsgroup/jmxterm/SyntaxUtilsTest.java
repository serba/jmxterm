package org.cyclopsgroup.jmxterm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

/**
 * Test case of {@link SyntaxUtils}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class SyntaxUtilsTest
{
    private static String printValue( Object v )
    {
        StringWriter output = new StringWriter();
        SyntaxUtils.printValue( new PrintWriter( output, true ), v, 0, false );
        return output.toString();
    }

    /**
     * Test how getUrl() figure out MBeanServer URL based on various pattern of input
     * 
     * @throws Exception Thrown when syntax is invalid
     */
    @Test
    public void testGetUrl()
        throws Exception
    {
        assertEquals( "/jndi/rmi://xyz-host.cyclopsgroup.org:12345/jmxrmi",
                      SyntaxUtils.getUrl( "xyz-host.cyclopsgroup.org:12345" ).getURLPath() );
        assertEquals(
                      "/jndi/rmi://xyz-host.cyclopsgroup.org:12345/jmxrmi",
                      SyntaxUtils.getUrl( "service:jmx:rmi:///jndi/rmi://xyz-host.cyclopsgroup.org:12345/jmxrmi" ).getURLPath() );
    }

    /**
     * Verify string expression of type is correctly parsed
     */
    @Test
    public void testParseNormally()
    {
        assertEquals( "x", SyntaxUtils.parse( "x", "java.lang.String" ) );
        assertEquals( 3, SyntaxUtils.parse( "3", "int" ) );
        assertEquals( 3L, SyntaxUtils.parse( "3", "long" ) );
        assertNull( SyntaxUtils.parse( "", "java.util.Date" ) );
        assertNull( SyntaxUtils.parse( "null", "java.lang.String" ) );
    }

    /**
     * Verify that Exception is thrown when type is wrong
     */
    @Test( expected = IllegalArgumentException.class )
    public void testParseWithWrongType()
    {
        SyntaxUtils.parse( "x", "x" );
    }

    /**
     * Print an equation expression
     */
    @Test
    public void testPrintExpression()
    {
        StringWriter output = new StringWriter();
        SyntaxUtils.printExpression( new PrintWriter( output, true ), "a", 123, null, 0, false );
        assertEquals( "\"a\"=123;", output.toString().replaceAll( "\\s", "" ) );
    }

    /**
     * Verify values are correctly printed out
     */
    @Test
    public void testPrintValue()
    {
        assertEquals( "3", printValue( 3 ) );
        assertEquals( "\"x\"", printValue( "x" ) );
        assertEquals( "5.555", printValue( 5.555f ) );
        assertEquals( "( 1, 2, 3 )", printValue( Arrays.asList( 1, 2, 3 ) ) );

        HashMap<String, String> map = new HashMap<String, String>();
        map.put( "a", "qqq" );
        assertEquals( "[1,2,3]", printValue( new int[] { 1, 2, 3 } ).replaceAll( "\\s", "" ) );
        assertEquals( "{\"a\"=\"qqq\";}", printValue( map ).replaceAll( "\\s", "" ) );
    }
}
