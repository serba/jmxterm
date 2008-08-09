package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.cyclopsgroup.jcli.AutoCompletable;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command that shows list of beans
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "beans", description = "List available beans under a domain or all domains", note = "Without -d option, current select domain is applied. If there's no domain specified, all beans are listed. Example:\n beans\n beans -d java.lang" )
public class BeansCommand
    extends Command
    implements AutoCompletable
{
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

    private String domain;

    /**
     * @inheritDoc
     */
    @Override
    public void execute()
        throws MalformedObjectNameException, IOException
    {
        Session session = getSession();
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

    /**
     * @param domain Domain under which beans are listed
     */
    @Option( name = "d", longName = "domain", description = "Name of domain under which beans are listed" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }

    /**
     * @inheritDoc
     */
    public List<String> suggestArgument( String partialArgument )
    {
        return null;
    }

    /**
     * @inheritDoc
     */
    public List<String> suggestOption( String optionName, String partialValue )
    {
        if ( optionName.equals( "d" ) && partialValue == null )
        {
            try
            {
                return DomainsCommand.getDomains( getSession() );
            }
            catch ( IOException e )
            {
                getSession().log( e );
            }
        }
        return null;
    }
}
