package org.cyclopsgroup.jmxterm;

import java.io.PrintStream;

import javax.management.remote.JMXConnector;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Session
{
    private static final Log LOG = LogFactory.getLog( Session.class );

    private boolean closed;

    private JMXConnector connector;

    private String domain;

    private PrintStream output = System.out;

    private void requireSession()
    {
        Validate.isTrue( !closed, "Session is already closed" );
    }

    private void requireConnector()
    {
        requireSession();
        Validate.notNull( connector, "Connection is not opened yet" );
    }

    private void requireDomain()
    {
        requireConnector();
        Validate.notNull( domain, "Domain is not specified yet" );
    }

    public void close()
    {
        requireSession();
        if ( connector != null )
        {
            unsetConnector();
        }
        closed = true;
        LOG.info( "Session is closed" );
    }

    public final JMXConnector getConnector()
    {
        return connector;
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

    public void setConnector( JMXConnector connection )
    {
        Validate.notNull( connection, "Connection can't be NULL" );
        requireSession();
        Validate.isTrue( this.connector == null, "Connection already exists" );
        this.connector = connection;
        LOG.info( "Connection is set to " + connection );
    }

    public void setDomain( String domain )
    {
        Validate.notNull( domain, "domain can't be NULL" );
        requireConnector();
        this.domain = domain;
        LOG.info( "Domain is set to " + domain );
    }

    public final void setOutput( PrintStream output )
    {
        this.output = output;
    }

    public void unsetConnector()
    {
        requireConnector();
        if ( domain != null )
        {
            unsetDomain();
        }
        connector = null;
        LOG.info( "Connection is unset" );
    }

    public void unsetDomain()
    {
        requireDomain();
        domain = null;
    }
}
