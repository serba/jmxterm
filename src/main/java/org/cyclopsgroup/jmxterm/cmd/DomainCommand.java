package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

@Cli( name = "domain", description = "Display or set current domain" )
public class DomainCommand
    extends Command
{
    /**
     * Get domain name from given domain expression
     * 
     * @param domain Domain expression, which can be a name or NULL
     * @param session Current JMX session
     * @return String name of domain coming from given parameter or current session
     * @throws IOException
     */
    public static String getDomainName( String domain, Session session )
        throws IOException
    {
        Validate.notNull( session, "Session can't be NULL" );
        Validate.isTrue( session.getConnection() != null, "Session isn't opened" );
        if ( domain == null )
        {
            return session.getDomain();
        }
        if ( SyntaxUtils.isNull( domain ) )
        {
            return null;
        }
        HashSet<String> domains = new HashSet<String>( DomainsCommand.getDomains( session ) );
        if ( !domains.contains( domain ) )
        {
            throw new IllegalArgumentException( "Domain " + domain + " doesn't exist, check your spelling" );
        }
        return domain;
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
                session.msg( "domain is not set", SyntaxUtils.NULL );
            }
            else
            {
                session.msg( "domain = " + session.getDomain(), session.getDomain() );
            }
            return;
        }
        String domainName = getDomainName( domain, session );
        if ( domainName == null )
        {
            session.unsetDomain();
            session.msg( "domain is unset" );
        }
        else
        {
            session.setDomain( domainName );
            session.msg( "domain is set to " + session.getDomain() );
        }
        session.ok();
    }

    @Argument
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }
}
