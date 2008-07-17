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

class ConnectionAwareSession
    extends Session
{
    private ConnectionImpl connection;

    ConnectionAwareSession( PrintWriter output )
    {
        super( output );
    }

    /**
     * @inheritDoc
     */
    @Override
    public synchronized boolean isConnected()
    {
        return connection != null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public synchronized Connection getConnection()
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
    public synchronized void connect( String url )
        throws IOException
    {
        Validate.isTrue( connection == null, "Session is already opened" );
        Validate.notNull( url, "URL can't be NULL" );
        JMXServiceURL u = SyntaxUtils.getUrl( url );
        JMXConnector connector = JMXConnectorFactory.connect( u );
        connection = new ConnectionImpl( connector, u, url );
    }

    /**
     * @inheritDoc
     */
    @Override
    public synchronized void disconnect()
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
