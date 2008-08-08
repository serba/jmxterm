package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.AutoCompletable;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

/**
 * Get or set current selected domain
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "domain", description = "Display or set current selected domain. ", note = "With a parameter, parameter defined domain is selected, otherwise it displays current selected domain."
    + " eg. domain java.lang" )
public class DomainCommand
    extends Command
    implements AutoCompletable
{
    /**
     * @inheritDoc
     */
    public List<String> suggestArgument( String partialArgument )
    {
        if ( partialArgument != null )
        {
            return null;
        }
        try
        {
            return DomainsCommand.getDomains( getSession() );
        }
        catch ( IOException e )
        {
            getSession().log( e );
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    public List<String> suggestOption( String optionName, String partialOption )
    {
        return null;
    }

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
        throws IOException
    {
        Validate.notNull( session, "Session can't be NULL" );
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
            session.msg( "domain is unset", SyntaxUtils.OK );
        }
        else
        {
            session.setDomain( domainName );
            session.msg( "domain is set to " + session.getDomain(), SyntaxUtils.OK );
        }
    }

    /**
     * @param domain Domain to select
     */
    @Argument
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }
}
