package org.cyclopsgroup.jmxterm.cmd;

import java.util.List;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;


public class DomainsCommand
    implements Command
{
    /**
     * @inheritDoc
     */
    public void execute( List<String> args, Session session )
        throws Exception
    {
        for ( String domain : session.getConnector().getMBeanServerConnection().getDomains() )
        {
            session.getOutput().println( " - " + domain );
        }
    }
}
