package org.cyclopsgroup.jmxterm.cmd;

import java.util.List;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

public class ListCommand
    implements Command
{
    public void execute( List<String> args, Session session )
        throws Exception
    {
        if ( session.getBean() != null )
        {

        }
        else if ( session.getDomain() != null )
        {
            BeansCommand.listBeans( session );
        }
        else if ( session.getConnection() != null )
        {
            DomainsCommand.listDomains( session );
        }
        else
        {
            session.getOutput().println( "No thing to show" );
        }
    }
}
