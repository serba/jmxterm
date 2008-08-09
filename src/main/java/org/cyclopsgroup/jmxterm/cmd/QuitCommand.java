package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

/**
 * Command to terminate the console
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "quit", description = "Terminate console and exit" )
public class QuitCommand
    extends Command
{
    /**
     * @inheritDoc
     */
    @Override
    public void execute()
        throws IOException
    {
        Session session = getSession();
        session.disconnect();
        session.close();
        session.msg( "bye", SyntaxUtils.OK );
    }
}
