package org.cyclopsgroup.jmxterm.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import org.cyclopsgroup.jmxterm.SyntaxUtils;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case of {@link ConnectionAwareSession}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class ConnectionAwareSessionTest
{
    private ConnectionAwareSession session;

    private Mockery context;

    private JMXConnector con;

    /**
     * Set up objects to test
     */
    @Before
    public void setUp()
    {
        context = new Mockery();
        con = context.mock( JMXConnector.class );
        session = new ConnectionAwareSession( new PrintWriter( new StringWriter() ) )
        {

            @Override
            protected JMXConnector doConnect( JMXServiceURL url, Map<String, Object> env )
                throws IOException
            {
                return con;
            }
        };
    }

    /**
     * Verify connect() runs correctly
     * 
     * @throws Exception
     */
    @Test
    public void testConnect()
        throws Exception
    {
        session.connect( SyntaxUtils.getUrl( "localhost:9991" ), null );
    }
}
