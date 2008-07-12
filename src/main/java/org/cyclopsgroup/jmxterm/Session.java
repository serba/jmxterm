package org.cyclopsgroup.jmxterm;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * JMX communication context
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Session
{
    private boolean abbreviated;

    private String bean;

    private boolean closed;

    private Connection connection;

    private boolean verbose = true;

    private String domain;

    public final PrintWriter output;

    public Session( PrintWriter output )
    {
        Validate.notNull( output, "Output can't be NULL" );
        this.output = output;
    }

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

    public void setConnection( Connection connection )
    {
        Validate.notNull( connection, "Connection can't be NULL" );
        requireSession();
        Validate.isTrue( this.connection == null, "Connection already exists" );
        this.connection = connection;
    }

    public final void setVerbose( boolean debug )
    {
        this.verbose = debug;
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
