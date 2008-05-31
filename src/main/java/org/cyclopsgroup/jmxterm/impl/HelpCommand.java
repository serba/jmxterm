package org.cyclopsgroup.jmxterm.impl;

import java.util.List;
import java.util.Map;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

public class HelpCommand
    implements Command
{
    private final CommandCenter commandCenter;

    public HelpCommand( CommandCenter commandCenter )
    {
        this.commandCenter = commandCenter;
    }

    /**
     * @inheritDoc
     */
    public void execute( List<String> args, Session session )
    {
        session.getOutput().println( "Following commands are available to use:" );
        for ( Map.Entry<String, Command> entry : commandCenter.getCommands().entrySet() )
        {
            session.getOutput().println( " - " + entry.getKey() );
        }
    }
}
