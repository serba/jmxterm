package org.cyclopsgroup.jmxterm;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * JMX communication context. This class exists for the whole lifecycle of a command execution. It is NOT thread safe.
 * The caller(CommandCenter) makes sure all calls are synchronized.
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class Session
{
    private boolean abbreviated;

    private String bean;

    private boolean closed;

    private String domain;

    /**
     * Output field
     */
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
     * Close JMX terminal console. Supposedly, process terminates after this call
     */
    public void close()
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

    /**
     * @return Current selected bean
     */
    public final String getBean()
    {
        return bean;
    }

    /**
     * @return Current open JMX server connection
     */
    public abstract Connection getConnection();

    /**
     * @return Current domain
     */
    public final String getDomain()
    {
        return domain;
    }

    /**
     * @return True if output is abbreviated
     */
    public final boolean isAbbreviated()
    {
        return abbreviated;
    }

    /**
     * @return True if {@link #close()} has been called
     */
    public final boolean isClosed()
    {
        return closed;
    }

    /**
     * @return True if there's a open connection to JMX server
     */
    public abstract boolean isConnected();

    /**
     * @return True if <code>verbose</code> option is turned on
     */
    public final boolean isVerbose()
    {
        return verbose;
    }

    /**
     * Output a message based on abbreviated option
     * 
     * @param msg Message to output
     */
    public void msg( String msg )
    {
        msg( msg, null );
    }

    /**
     * Output a string message
     * 
     * @param msg Message to output when <code>abbreviated</code> option is off
     * @param abbr Message to output when <code>abbreviated</code> option is on
     */
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

    /**
     * @param abbreviated True if <code>abbreviated</code> option is to be turned on
     */
    public final void setAbbreviated( boolean abbreviated )
    {
        this.abbreviated = abbreviated;
    }

    /**
     * Set current selected bean
     * 
     * @param bean Bean to select
     */
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    /**
     * Set current selected domain
     * 
     * @param domain Domain to select
     */
    public final void setDomain( String domain )
    {
        Validate.notNull( domain, "domain can't be NULL" );
        this.domain = domain;
    }

    /**
     * Set verbose option
     * 
     * @param verbose Verbose option
     */
    public final void setVerbose( boolean verbose )
    {
        this.verbose = verbose;
    }

    /**
     * Set domain and bean to be NULL
     */
    public void unsetDomain()
    {
        bean = null;
        domain = null;
    }
}
