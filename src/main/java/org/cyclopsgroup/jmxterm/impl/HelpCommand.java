package org.cyclopsgroup.jmxterm.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.spi.CliUtils;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

@Cli( name = "help", description = "Display available commands", note = "Run help [command1] [command2] ... to display usage or certain command(s)" )
public class HelpCommand
    extends Command
{
    private CommandCenter commandCenter;

    private String[] argNames = {};

    @Argument
    public final void setArgNames( String[] argNames )
    {
        this.argNames = argNames;
    }

    final void setCommandCenter( CommandCenter commandCenter )
    {
        this.commandCenter = commandCenter;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws Exception
    {
        if ( argNames.length == 0 )
        {
            List<String> commandNames = new ArrayList<String>( commandCenter.getCommandNames() );
            Collections.sort( commandNames );
            session.getOutput().println( "Following commands are available to use:" );
            for ( String commandName : commandNames )
            {
                Class<? extends Command> commandType = commandCenter.getCommandType( commandName );
                Cli cli = CliUtils.defineCli( commandType ).getCli();
                session.getOutput().println( String.format( "- %-16s %s", commandName, cli.description() ) );
            }
        }
        else
        {
            for ( String argName : argNames )
            {
                commandCenter.execute( argName + " -h" );
            }
        }
    }
}
