package org.cyclopsgroup.jmxterm.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.StringUtils;
import org.cyclopsgroup.jcli.annotation.CliParser;
import org.cyclopsgroup.jcli.jccli.JakartaCommonsCliParser;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

public class CommandCenter
{
    private static final String COMMAND_DELIMITER = "&&";

    private final CliParser cliParser = new JakartaCommonsCliParser( new GnuParser() );

    private final CommandFactory commandFactory;

    private final Lock lock = new ReentrantLock();

    private final Session session = new Session();

    public CommandCenter( PrintWriter output )
        throws ClassNotFoundException, IOException
    {
        commandFactory = new CommandFactory();
        session.setOutput( output );
        output.println( "Welcome to JMX terminal. Type \"help\" for available commands." );
    }

    public void execute( String command )
    {
        try
        {
            doExecute( command );
        }
        catch ( Exception e )
        {
            if ( session.isDebug() )
            {
                e.printStackTrace( session.getOutput() );
            }
            else
            {
                // session.getOutput().println( e.getClass().getSimpleName() + ":" + e.getMessage() );
                e.printStackTrace();
            }
        }
    }

    private void doExecute( String command )
        throws Exception
    {
        if ( command == null || command.trim().length() == 0 )
        {
            return;
        }
        if ( command.indexOf( COMMAND_DELIMITER ) != -1 )
        {
            String[] commands = StringUtils.split( command, COMMAND_DELIMITER );
            for ( String c : commands )
            {
                execute( c );
            }
            return;
        }

        String[] args = StringUtils.split( command, ' ' );
        String commandName = args[0];
        String[] commandArgs = new String[args.length - 1];
        System.arraycopy( args, 1, commandArgs, 0, args.length - 1 );
        Command cmd = commandFactory.createCommand( commandName );
        if ( cmd instanceof HelpCommand )
        {
            ( (HelpCommand) cmd ).setCommandCenter( this );
        }
        cliParser.parse( commandArgs, cmd );
        if ( cmd.isHelp() )
        {
            cliParser.printUsage( cmd.getClass(), session.getOutput() );
            return;
        }
        lock.lock();
        try
        {
            cmd.execute( session );
        }
        finally
        {
            lock.unlock();
        }
    }

    public Set<String> getCommandNames()
    {
        return commandFactory.getCommandTypes().keySet();
    }

    public Class<? extends Command> getCommandType( String name )
    {
        return commandFactory.getCommandTypes().get( name );
    }

    public boolean isClosed()
    {
        return session.isClosed();
    }

    public void prompt()
        throws IOException
    {
        session.getOutput().print( ( session.getConnection() == null ? "?" : ">" ) + "$ " );
        session.getOutput().flush();
    }
}
