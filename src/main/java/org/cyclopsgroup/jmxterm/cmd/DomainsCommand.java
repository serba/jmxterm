package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * List domains for JMX connection
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "domains", description = "List all available domain names" )
public class DomainsCommand
    extends Command
{
    /**
     * Get list of domains for current JMX connection
     * 
     * @param session Current session
     * @return Sorted list of domain names
     * @throws IOException
     */
    public static List<String> getDomains( Session session )
        throws IOException
    {
        String[] domains = session.getConnection().getConnector().getMBeanServerConnection().getDomains();
        List<String> result = new ArrayList<String>( Arrays.asList( domains ) );
        Collections.sort( result );
        return result;
    }

    /**
     * @inheritDoc
     */
    public void execute( Session session )
        throws IOException
    {
        Validate.notNull( session, "Session can't be NULL" );
        if ( session.getConnection() == null )
        {
            throw new IllegalStateException( "There's no open connection right now, use open command" );
        }
        if ( !session.isAbbreviated() )
        {
            session.msg( "following domains are available" );
        }
        int i = 0;
        for ( String domain : getDomains( session ) )
        {
            session.msg( String.format( "#%-3d - %s", i++, domain ), domain );
        }
    }
}