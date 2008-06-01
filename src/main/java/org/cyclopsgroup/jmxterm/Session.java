package org.cyclopsgroup.jmxterm;

import java.io.PrintStream;

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

    private String domain;

    private PrintStream output = System.out;

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

    public final PrintStream getOutput()
    {
        return output;
    }

    public final boolean isClosed()
    {
        return closed;
    }

    private void requireConnector()
    {
        requireSession();
        Validate.notNull( connection, "Connection is not opened yet" );
    }

    private void requireDomain()
    {
        requireConnector();
        Validate.notNull( domain, "Domain is not specified yet" );
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

    public void setDomain( String domain )
    {
        Validate.notNull( domain, "domain can't be NULL" );
        requireConnector();
        this.domain = domain;
    }

    public final void setOutput( PrintStream output )
    {
        this.output = output;
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
        requireDomain();
        bean = null;
        domain = null;
    }
}
