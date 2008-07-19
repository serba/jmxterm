package org.cyclopsgroup.jmxterm.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

/**
 * Implementation of {@link Session} which keeps a {@link ConnectionImpl}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class ConnectionAwareSession
    extends Session
{
    private ConnectionImpl connection;

    /**
     * @param output Output result
     */
    public ConnectionAwareSession( PrintWriter output )
    {
        super( output );
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isConnected()
    {
        return connection != null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Connection getConnection()
    {
        if ( connection == null )
        {
            throw new IllegalStateException( "Connection isn't open yet. Run open command to open a connection" );
        }
        return connection;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void connect( String url )
        throws IOException
    {
        Validate.isTrue( connection == null, "Session is already opened" );
        Validate.notNull( url, "URL can't be NULL" );
        JMXServiceURL u = SyntaxUtils.getUrl( url );
        JMXConnector connector = doConnect( u );
        connection = new ConnectionImpl( connector, u, url );
    }

    protected JMXConnector doConnect( JMXServiceURL url )
        throws IOException
    {
        return JMXConnectorFactory.connect( url );
    }

    /**
     * @inheritDoc
     */
    @Override
    public void disconnect()
        throws IOException
    {
        if ( connection == null )
        {
            return;
        }
        connection.close();
        connection = null;
    }
}
