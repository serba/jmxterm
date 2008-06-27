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
@Cli( name = "beans", description = "List available beans" )
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
        Set<ObjectName> names =
            session.getConnection().getConnector().getMBeanServerConnection().queryNames( queryName, null );
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
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    public void execute( Session session )
        throws MalformedObjectNameException, IOException
    {
        String domainName = DomainCommand.getDomainName( domain, session );
        if ( domainName == null && !domain.equalsIgnoreCase( "null" ) )
        {
            domainName = session.getDomain();
        }
        if ( domainName != null )
        {
            session.getOutput().println( "MBeans under domain " + domainName + ":" );
        }
        else
        {
            session.getOutput().println( "All available MBeans:" );
        }
        int i = 0;
        for ( String bean : getBeans( session, domainName ) )
        {
            session.getOutput().println( String.format( "%%%-3d - %s", i++, bean ) );
        }
    }
}
