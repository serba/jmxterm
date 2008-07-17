package org.cyclopsgroup.jmxterm;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * JMX communication context. This class exists for the whole lifecycle of a command execution. The runner of session
 * make sure all calls are synchronized.
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class Session
{
    private boolean abbreviated;

    private String bean;

    private boolean closed;

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
     * Close JMX terminal session. It is unnecessarily synchronized
     */
    public synchronized void close()
    {
        if ( closed )
        {
            return;
        }
        closed = true;
    }

    /**
     * Connect to MBean server
     * 
     * @param url URL to connect
     * @throws IOException
     */
    public abstract void connect( String url )
        throws IOException;

    /**
     * Close JMX connector
     * 
     * @throws IOException Thrown when connection can't be closed
     */
    public abstract void disconnect()
        throws IOException;

    public final String getBean()
    {
        return bean;
    }

    public abstract Connection getConnection();

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

    public abstract boolean isConnected();

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

    public final void setAbbreviated( boolean abbreviated )
    {
        this.abbreviated = abbreviated;
    }

    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    public void setDomain( String domain )
    {
        Validate.notNull( domain, "domain can't be NULL" );
        this.domain = domain;
    }

    public final void setVerbose( boolean debug )
    {
        this.verbose = debug;
    }

    public void unsetDomain()
    {
        bean = null;
        domain = null;
    }
}
