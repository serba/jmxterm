package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command that shows list of beans
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "beans", description = "List available beans", note = "Example:\nbeans\nbeans -d java.lang" )
public class BeansCommand
    extends Command
{
    private String domain;

    @Option( name = "d", longName = "domain", description = "Domain name or %N domain index" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }

    /**
     * Get list of bean names under current domain
     * 
     * @param session Current JMX session
     * @param Full domain name
     * @return List of bean names
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    @SuppressWarnings( "unchecked" )
    private static List<String> getBeans( Session session, String domainName )
        throws MalformedObjectNameException, IOException
    {
        ObjectName queryName = null;
        if ( domainName != null )
        {
            queryName = new ObjectName( domainName + ":*" );
        }
        Set<ObjectName> names = session.getConnection().getServerConnection().queryNames( queryName, null );
        List<String> results = new ArrayList<String>( names.size() );
        for ( ObjectName name : names )
        {
            results.add( name.getCanonicalName() );
        }
        Collections.sort( results );
        return results;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws MalformedObjectNameException, IOException
    {
        String domainName = DomainCommand.getDomainName( domain, session );
        List<String> domains = new ArrayList<String>();
        if ( domainName == null )
        {
            domains.addAll( DomainsCommand.getDomains( session ) );
        }
        else
        {
            domains.add( domainName );
        }
        int i = 0;
        for ( String d : domains )
        {
            session.msg( "domain = " + d + ":" );
            for ( String bean : getBeans( session, d ) )
            {
                session.msg( String.format( "  %%%-3d - %s", i++, bean ), bean );
            }
        }
    }
}
