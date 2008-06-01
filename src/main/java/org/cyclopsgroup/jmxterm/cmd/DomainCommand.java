package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

public class DomainCommand
    implements Command
{
    public static final Log LOG = LogFactory.getLog( DomainCommand.class );

    public static void changeDomain( String domain, Session session )
        throws IOException
    {
        if ( SyntaxUtils.isNull( domain ) )
        {
            session.unsetDomain();
        }
        else if ( SyntaxUtils.isIndex( domain ) )
        {
            int index = SyntaxUtils.getIndex( domain );
            session.setDomain( DomainsCommand.getDomains( session ).get( index ) );
        }
        else
        {
            session.setDomain( domain );
        }
        LOG.info( "Domain is set to " + session.getDomain() );
    }

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
        changeDomain( args.get( 0 ), session );
    }
}
