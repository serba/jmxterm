package org.cyclopsgroup.jmxterm.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.spi.CliUtils;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command that display a help message
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "help", description = "Display available commands or usage of a command", note = "Run \"help [command1] [command2] ...\" to display usage or certain command(s). Help without argument shows list of available commands" )
public class HelpCommand
    extends Command
{
    private CommandCenter commandCenter = null;

    private String[] argNames = {};

    /**
     * @param argNames Array of arguments
     */
    @Argument
    public final void setArgNames( String[] argNames )
    {
        Validate.notNull( argNames, "argNames can't be NULL" );
        this.argNames = argNames;
    }

    /**
     * @param commandCenter CommandCenter object that calls this help command
     */
    final void setCommandCenter( CommandCenter commandCenter )
    {
        Validate.notNull( commandCenter, "commandCenter can't be NULL" );
        this.commandCenter = commandCenter;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws Exception
    {
        Validate.notNull( commandCenter, "Command center hasn't been set yet" );
        if ( argNames.length == 0 )
        {
            List<String> commandNames = new ArrayList<String>( commandCenter.getCommandNames() );
            Collections.sort( commandNames );
            session.msg( "following commands are available to use:" );
            for ( String commandName : commandNames )
            {
                Class<? extends Command> commandType = commandCenter.getCommandType( commandName );
                Cli cli = CliUtils.defineCli( commandType ).getCli();
                session.msg( String.format( "- %-16s %s", commandName, cli.description() ), commandName + ":"
                    + cli.description() );
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
