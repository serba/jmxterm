package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

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
        session.disconnect();
        session.msg( "bye", SyntaxUtils.OK );
    }
}
