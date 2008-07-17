package org.cyclopsgroup.jmxterm;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;

public class MockSession
    extends Session
{
    private boolean connected = true;

    private MockConnection connection;

    public MockSession( Writer output, MBeanServerConnection con )
        throws MalformedURLException
    {
        super( new PrintWriter( output, true ) );
        setAbbreviated( true );
        connection = new MockConnection( SyntaxUtils.getUrl( "localhost:9991" ), con );
    }

    @Override
    public void connect( String url )
        throws IOException
    {
        connected = true;
    }

    @Override
    public void disconnect()
        throws IOException
    {
        connected = false;
    }

    @Override
    public Connection getConnection()
    {
        return connection;
    }

    @Override
    public boolean isConnected()
    {
        return connected;
    }
}
