package org.cyclopsgroup.jmxterm.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.CliParser;
import org.cyclopsgroup.jcli.jccli.JakartaCommonsCliParser;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Facade class where commands are maintained and executed
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class CommandCenter
{
    private static final String COMMAND_DELIMITER = "&&";

    private final CliParser cliParser = new JakartaCommonsCliParser( new GnuParser() );

    private final CommandFactory commandFactory;

    private final Lock lock = new ReentrantLock();

    private final Session session;

    /**
     * Constructor with given output {@link PrintWriter}
     * 
     * @param output Message output. It can't be NULL
     * @throws ClassNotFoundException Thrown when configured command can't be created
     * @throws IOException Thrown for file access failure
     */
    public CommandCenter( PrintWriter output )
        throws ClassNotFoundException, IOException
    {
        Validate.notNull( output, "Output can't be NULL" );
        session = new Session( output );
        commandFactory = new CommandFactory();
        output.println( "Welcome to JMX terminal. Type \"help\" for available commands." );
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
            cliParser.printUsage( cmd.getClass(), session.output );
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

    /**
     * Execute a command
     * 
     * @param command String command to execute
     */
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
                e.printStackTrace( session.output );
            }
            else
            {
                session.output.println( e.getClass().getSimpleName() + ":" + e.getMessage() );
            }
        }
    }

    /**
     * @return Set of command names
     */
    public Set<String> getCommandNames()
    {
        return commandFactory.commandTypes.keySet();
    }

    /**
     * @param name Command name
     * @return Type of command associated with given name
     */
    public Class<? extends Command> getCommandType( String name )
    {
        return commandFactory.commandTypes.get( name );
    }

    /**
     * @return True if command center is closed
     */
    public boolean isClosed()
    {
        return session.isClosed();
    }

    /**
     * Print out a prompt waiting for command input
     * 
     * @throws IOException Thrown for communication error
     */
    public void prompt()
        throws IOException
    {
        session.output.print( ( session.getConnection() == null ? "?" : ">" ) + "$ " );
        session.output.flush();
    }
}
