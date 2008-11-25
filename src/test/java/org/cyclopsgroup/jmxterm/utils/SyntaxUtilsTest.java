package org.cyclopsgroup.jmxterm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.cyclopsgroup.jmxterm.utils.SyntaxUtils;
import org.junit.Test;

/**
 * Test case of {@link SyntaxUtils}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class SyntaxUtilsTest
{
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
}
