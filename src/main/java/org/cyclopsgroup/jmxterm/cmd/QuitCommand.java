package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.List;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.Session;

public class QuitCommand
    implements Command
{
    public void execute( List<String> args, Session session )
        throws IOException
    {
        Connection con = session.getConnection();
        session.close();
        if ( con != null )
        {
            session.unsetConnection();
        }
        System.out.println( "Bye" );
    }
}
