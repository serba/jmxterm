package org.cyclopsgroup.jmxterm.cmd;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

@Cli( name = "close", description = "Close JMX session" )
public class CloseCommand
    extends Command
{
    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws Exception
    {
        if ( session.disconnect() )
        {
            session.msg( "JMX connection is closed", "ok" );
        }
    }
}
