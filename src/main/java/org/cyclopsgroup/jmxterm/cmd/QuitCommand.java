package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.Session;

@Cli( name = "quit", description = "Terminate console" )
public class QuitCommand
    extends Command
{
    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws IOException
    {
        Connection con = session.getConnection();
        session.close();
        if ( con != null )
        {
            session.unsetConnection();
        }
        session.msg( "bye" );
        session.ok();
    }
}
