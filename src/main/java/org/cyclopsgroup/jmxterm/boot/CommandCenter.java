package org.cyclopsgroup.jmxterm.boot;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.cmd.ConnectCommand;
import org.cyclopsgroup.jmxterm.cmd.DisConnectCommand;
import org.cyclopsgroup.jmxterm.cmd.DomainsCommand;
import org.cyclopsgroup.jmxterm.cmd.QuitCommand;


public class CommandCenter
{
    private final Session session = new Session();

    private final Map<String, Command> commands;

    private final Lock lock = new ReentrantLock();

    Map<String, Command> getCommands()
    {
        return commands;
    }

    public CommandCenter( PrintStream output )
    {
        session.setOutput( output );
        HashMap<String, Command> commands = new HashMap<String, Command>();
        commands.put( "quit", new QuitCommand() );
        commands.put( "exit", new QuitCommand() );
        commands.put( "bye", new QuitCommand() );
        commands.put( "connect", new ConnectCommand() );
        commands.put( "con", new ConnectCommand() );
        commands.put( "disconnect", new DisConnectCommand() );
        commands.put( "disc", new DisConnectCommand() );
        commands.put( "domains", new DomainsCommand() );
        commands.put( "help", new HelpCommand( this ) );
        this.commands = Collections.unmodifiableMap( commands );
    }

    public String getPath()
        throws IOException
    {
        StringBuilder s = new StringBuilder();
        lock.lock();
        try
        {
            if ( session.getConnector() != null )
            {
                s.append( session.getConnector().getConnectionId() );
            }
            if ( session.getDomain() != null )
            {
                s.append( '$' ).append( session.getDomain() );
            }
            s.append( '$' );
            return s.toString();
        }
        finally
        {
            lock.unlock();
        }
    }

    public void execute( String command )
        throws Exception
    {
        if ( command == null || command.trim().length() == 0 )
        {
            return;
        }
        String[] args = StringUtils.split( command, ' ' );
        String commandName = args[0];
        Command cmd = commands.get( commandName );
        Validate.notNull( cmd, "Unknown command " + commandName + ", try typing \"type\"" );
        List<String> cmdArgs = new ArrayList<String>( Arrays.asList( args ) );
        cmdArgs.remove( 0 );
        lock.lock();
        try
        {
            cmd.execute( cmdArgs, session );
        }
        finally
        {
            lock.unlock();
        }
    }

    public boolean isClosed()
    {
        return session.isClosed();
    }
}
