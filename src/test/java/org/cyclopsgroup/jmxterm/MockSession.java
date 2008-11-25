package org.cyclopsgroup.jmxterm;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXServiceURL;

import org.cyclopsgroup.jmxterm.io.WriterCommandOutput;
import org.cyclopsgroup.jmxterm.utils.SyntaxUtils;

/**
 * Mocked version of {@link Session} implementation for testing purpose only
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class MockSession
    extends Session
{
    private boolean connected = true;

    private MockConnection connection;

    /**
     * @param output Output writer
     * @param con MBean service connection
     * @throws Exception
     */
    public MockSession( Writer output, MBeanServerConnection con )
        throws Exception
    {
        super( new WriterCommandOutput( output, null ), null );
        setAbbreviated( true );
        connection = new MockConnection( SyntaxUtils.getUrl( "localhost:9991" ), con );
    }

    /**
     * @inheritDoc
     */
    @Override
    public void connect( JMXServiceURL url, Map<String, Object> env )
        throws IOException
    {
        connected = true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void disconnect()
        throws IOException
    {
        connected = false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isConnected()
    {
        return connected;
    }
}
