package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.List;

import javax.management.remote.JMXConnector;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;


public class QuitCommand
    implements Command
{
    public void execute( List<String> args, Session session )
        throws IOException
    {
        JMXConnector connector = session.getConnector();
        session.close();
        if ( connector != null )
        {
            connector.close();
        }
    }
}
