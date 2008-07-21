package org.cyclopsgroup.jmxterm.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

public class ConnectionAwareSessionTest
{
    private ConnectionAwareSession session;

    private Mockery context;

    private JMXConnector con;

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

    @Test
    public void testConnect()
        throws IOException
    {
        session.connect( "localhost:9991", null );
    }
}
