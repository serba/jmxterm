package org.cyclopsgroup.jmxterm;

import java.io.IOException;
import java.io.PrintWriter;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * JMX communication context. This class exists for the whole lifecycle of a command execution
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Session
{
    private boolean abbreviated;

    private String bean;

    private boolean closed;

    private ConnectionImpl connection;

    private String domain;

    public final PrintWriter output;

    private boolean verbose = true;

    /**
     * @param output Output destination
     */
    public Session( PrintWriter output )
    {
        Validate.notNull( output, "Output can't be NULL" );
        this.output = output;
    }

    /**
     * Close JMX terminal session
     */
    public synchronized void close()
    {
        if ( closed )
        {
            return;
        }
        if ( connection != null )
        {
            unsetConnection();
        }
        closed = true;
    }

    public synchronized boolean connect( String url )
        throws IOException
    {
        Validate.isTrue( connection == null, "Session is already opened" );
        Validate.notNull( url, "URL can't be NULL" );
        JMXServiceURL u = SyntaxUtils.getUrl( url );
        JMXConnector connector = JMXConnectorFactory.connect( u );
        connection = new ConnectionImpl( connector, u, url );
        return true;
    }

    /**
     * Close JMX connector
     * 
     * @throws IOException Thrown when connection can't be closed
     */
    public synchronized boolean disconnect()
        throws IOException
    {
        ConnectionImpl conn = connection;
        unsetConnection();
        if ( conn != null )
        {
            conn.getConnector().close();
            return true;
        }
        return false;
    }

    public final String getBean()
    {
        return bean;
    }

    public final Connection getConnection()
    {
        return connection;
    }

    public final String getDomain()
    {
        return domain;
    }

    /**
     * Get connection to MBean server
     * 
     * @return MBean server connection
     * @throws IOException Thrown when server connection can't be retrieved
     */
    public MBeanServerConnection getServerConnection()
        throws IOException
    {
        requireConnector();
        return connection.getConnector().getMBeanServerConnection();
    }

    public final boolean isAbbreviated()
    {
        return abbreviated;
    }

    public final boolean isClosed()
    {
        return closed;
    }

    public final boolean isVerbose()
    {
        return verbose;
    }

    /**
     * Output a message based on abbreviated option
     * 
     * @param msg
     */
    public void msg( String msg )
    {
        msg( msg, null );
    }

    public void msg( String msg, String abbr )
    {
        if ( abbreviated )
        {
            if ( StringUtils.isNotEmpty( abbr ) )
            {
                output.println( abbr );
            }
        }
        else
        {
            if ( StringUtils.isNotEmpty( msg ) )
            {
                output.println( msg );
            }
        }
    }

    /**
     * Print out <code>ok</code> if abbreviated option is true
     */
    public void ok()
    {
        if ( abbreviated )
        {
            output.println( "ok" );
        }
    }

    private void requireConnector()
    {
        requireSession();
        Validate.notNull( connection, "Connection is not opened yet" );
    }

    private void requireSession()
    {
        Validate.isTrue( !closed, "Session is already closed" );
    }

    public final void setAbbreviated( boolean abbreviated )
    {
        this.abbreviated = abbreviated;
    }

    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    public void setConnection( ConnectionImpl connection )
    {
        Validate.notNull( connection, "Connection can't be NULL" );
        requireSession();
        Validate.isTrue( this.connection == null, "Connection already exists" );
        this.connection = connection;
    }

    public void setDomain( String domain )
    {
        Validate.notNull( domain, "domain can't be NULL" );
        requireConnector();
        this.domain = domain;
    }

    public final void setVerbose( boolean debug )
    {
        this.verbose = debug;
    }

    public void unsetConnection()
    {
        if ( domain != null )
        {
            unsetDomain();
        }
        connection = null;
    }

    public void unsetDomain()
    {
        bean = null;
        domain = null;
    }
}
