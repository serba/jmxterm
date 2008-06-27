package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

@Cli( name = "domain", description = "Display or set current domain" )
public class DomainCommand
    extends Command
{
    public static final Log LOG = LogFactory.getLog( DomainCommand.class );

    /**
     * Get domain name from given domain expression
     * 
     * @param domain Domain expression, which can be a name or %N
     * @param session Current JMX session
     * @return String name of domain
     * @throws IOException
     */
    public static String getDomainName( String domain, Session session )
        throws IOException
    {
        if ( SyntaxUtils.isNull( domain ) )
        {
            return null;
        }
        else if ( SyntaxUtils.isIndex( domain ) )
        {
            int index = SyntaxUtils.getIndex( domain );
            List<String> domains = DomainsCommand.getDomains( session );
            if ( index >= domains.size() )
            {
                throw new IllegalArgumentException( "There're only " + domains.size() + " domains, " + domain +
                    " doesn't exist" );
            }
            return domains.get( index );
        }
        else
        {
            HashSet<String> domains = new HashSet<String>( DomainsCommand.getDomains( session ) );
            if ( !domains.contains( domain ) )
            {
                throw new IllegalArgumentException( "Domain " + domain + " doesn't exist, check your spelling" );
            }
            return domain;
        }
    }

    private String domain;

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws Exception
    {
        if ( domain == null )
        {
            if ( session.getDomain() == null )
            {
                session.getOutput().println( "Domain is not set" );
            }
            else
            {
                session.getOutput().println( "Domain is " + session.getDomain() );
            }
        }
        else
        {
            String domainName = getDomainName( domain, session );
            if ( domainName == null )
            {
                session.unsetDomain();
                session.getOutput().println( "Domain is unset" );
            }
            else
            {
                session.setDomain( domainName );
                session.getOutput().println( "Domain is set to " + session.getDomain() );
            }
        }
    }

    @Argument
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }
}
