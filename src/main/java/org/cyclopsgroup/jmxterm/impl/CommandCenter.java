package org.cyclopsgroup.jmxterm.impl;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.remote.JMXServiceURL;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.QuotedStringTokenizer;
import org.cyclopsgroup.jcli.annotation.CliParser;
import org.cyclopsgroup.jcli.jccli.JakartaCommonsCliParser;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.CommandFactory;
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

    private final QuotedStringTokenizer argTokenizer = new QuotedStringTokenizer();

    /**
     * Constructor with given output {@link PrintWriter}
     * 
     * @param output Message output. It can't be NULL
     * @throws ClassNotFoundException Thrown when configured command can't be created
     * @throws IOException Thrown for file access failure
     */
    public CommandCenter( PrintWriter output )
        throws IOException, ClassNotFoundException
    {
        this( output, new PredefinedCommandFactory() );
    }

    /**
     * This constructor is for testing purpose only
     * 
     * @param output Output result
     * @param commandFactory Given command factory
     */
    public CommandCenter( PrintWriter output, CommandFactory commandFactory )
    {
        Validate.notNull( output, "Output can't be NULL" );
        Validate.notNull( commandFactory, "Command factory can't be NULL" );
        this.session = new ConnectionAwareSession( output );
        this.commandFactory = commandFactory;
        session.msg( "Welcome to JMX terminal. Type \"help\" for available commands." );
    }

    /**
     * Close session
     */
    public void close()
    {
        session.close();
    }

    /**
     * @param url MBeanServer location. It can be <code>AAA:###</code> or full JMX server URL
     * @param env Environment variables
     * @throws IOException Thrown when connection can't be established
     */
    public void connect( JMXServiceURL url, Map<String, Object> env )
        throws IOException
    {
        Validate.notNull( url, "URL can't be NULL" );
        session.connect( url, env );
    }

    private void doExecute( String command )
        throws Exception
    {
        command = StringUtils.trimToNull( command );
        // Ignore empty line
        if ( command == null )
        {
            return;
        }
        // Ignore line comment
        if ( command.startsWith( "#" ) )
        {
            return;
        }
        // Truncate command if there's # character
        int commandEnds = command.indexOf( '#' );
        if ( commandEnds != -1 )
        {
            command = command.substring( 0, commandEnds );
        }
        // If command includes multiple segments, call them one by one using recursive call
        if ( command.indexOf( COMMAND_DELIMITER ) != -1 )
        {
            String[] commands = StringUtils.split( command, COMMAND_DELIMITER );
            for ( String c : commands )
            {
                execute( c );
            }
            return;
        }

        // Take the first argument out since it's command name
        String[] args = argTokenizer.parse( command ).toArray( ArrayUtils.EMPTY_STRING_ARRAY );
        String commandName = args[0];
        // Leave the rest of arguments for command
        String[] commandArgs = new String[args.length - 1];
        System.arraycopy( args, 1, commandArgs, 0, args.length - 1 );
        // Call command with parsed command name and arguments
        doExecute( commandName, commandArgs );
    }

    void printUsage( Class<? extends Command> commandType )
        throws IntrospectionException
    {
        cliParser.printUsage( commandType, session.output );
    }

    private void doExecute( String commandName, String[] commandArgs )
        throws Exception
    {
        Command cmd = commandFactory.createCommand( commandName );
        if ( cmd instanceof HelpCommand )
        {
            ( (HelpCommand) cmd ).setCommandCenter( this );
        }
        cliParser.parse( commandArgs, cmd );

        // Print out usage if help option is specified
        if ( cmd.isHelp() )
        {
            printUsage( cmd.getClass() );
            return;
        }

        // Make sure concurrency and run command
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
     * Execute a command. Command can be a valid full command, a comment, command followed by comment or empty
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
            if ( session.isVerbose() )
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
        return commandFactory.getCommandTypes().keySet();
    }

    /**
     * @param name Command name
     * @return Type of command associated with given name
     */
    public Class<? extends Command> getCommandType( String name )
    {
        return commandFactory.getCommandTypes().get( name );
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
        if ( !session.isAbbreviated() )
        {
            session.output.print( ( session.isConnected() ? ">" : "?" ) + "$ " );
            session.output.flush();
        }
    }

    /**
     * Set <code>abbreviated</code> option
     * 
     * @param abbreviated Value of <code>abbreviated</code> option
     */
    public void setAbbreviated( boolean abbreviated )
    {
        session.setAbbreviated( abbreviated );
    }
}
