package org.cyclopsgroup.jmxterm.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<String> commandNames = new ArrayList<String>( commandCenter.getCommands().keySet() );
        Collections.sort( commandNames );
        session.getOutput().println( "Following commands are available to use:" );
        for ( String commandName : commandNames )
        {
            session.getOutput().println( " - " + commandName );
        }
    }
}
