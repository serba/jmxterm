package org.cyclopsgroup.jmxterm.cmd;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

public class DomainCommand
    implements Command
{
    public static final Log LOG = LogFactory.getLog( DomainCommand.class );

    public void execute( List<String> args, Session session )
        throws Exception
    {
        if ( args.isEmpty() )
        {
            if ( session.getDomain() == null )
            {
                session.getOutput().println( "Domain is not set" );
            }
            else
            {
                session.getOutput().println( "Domain is " + session.getDomain() );
            }
            return;
        }
        String domain = args.get( 0 );
        if ( domain.equalsIgnoreCase( NULL ) )
        {
            session.unsetDomain();
        }
        else
        {
            session.setDomain( domain );
        }
        LOG.info( "Domain is set to " + domain );
    }
}
