package org.cyclopsgroup.jmxterm;

import java.io.PrintWriter;

import org.apache.commons.lang.Validate;

/**
 * JMX communication context
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Session
{
    private String bean;

    private boolean closed;

    private Connection connection;

    private boolean debug = true;

    private String domain;

    public final PrintWriter output;

    public Session( PrintWriter output )
    {
        Validate.notNull( output, "Output can't be NULL" );
        this.output = output;
    }

    public void close()
    {
        requireSession();
        if ( connection != null )
        {
            unsetConnection();
        }
        closed = true;
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

    public final boolean isClosed()
    {
        return closed;
    }

    public final boolean isDebug()
    {
        return debug;
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

    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    public void setConnection( Connection connection )
    {
        Validate.notNull( connection, "Connection can't be NULL" );
        requireSession();
        Validate.isTrue( this.connection == null, "Connection already exists" );
        this.connection = connection;
    }

    public final void setDebug( boolean debug )
    {
        this.debug = debug;
    }

    public void setDomain( String domain )
    {
        Validate.notNull( domain, "domain can't be NULL" );
        requireConnector();
        this.domain = domain;
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
