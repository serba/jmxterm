package org.cyclopsgroup.jmxterm.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

public class CommandCenter
{
    private static final String COMMAND_DELIMITER = "&&";

    private static final String CONFIG_COMMAND_ENTRY = "jmxterm.commands.";

    private static final String CONFIG_COMMAND_TYPE_SUFFIX = ".type";

    private static final String CONFIG_PATH = "META-INF/cyclopsgroup/jmxterm.properties";

    private final Map<String, Command> commands;

    private final Lock lock = new ReentrantLock();

    private final Session session = new Session();

    public CommandCenter( PrintStream output )
        throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        session.setOutput( output );
        Properties props = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        Enumeration<URL> configs = classLoader.getResources( CONFIG_PATH );
        while ( configs.hasMoreElements() )
        {
            InputStream in = configs.nextElement().openStream();
            try
            {
                props.load( in );
            }
            finally
            {
                in.close();
            }
        }
        HashMap<String, Command> commands = new HashMap<String, Command>();
        for ( Map.Entry<Object, Object> entry : props.entrySet() )
        {
            String name = (String) entry.getKey();
            if ( !name.startsWith( CONFIG_COMMAND_ENTRY ) || !name.endsWith( CONFIG_COMMAND_TYPE_SUFFIX ) )
            {
                continue;
            }
            String commandConfig = name.substring( 0, name.length() - CONFIG_COMMAND_TYPE_SUFFIX.length() );
            String commandName = commandConfig.substring( CONFIG_COMMAND_ENTRY.length() );
            Command command = (Command) classLoader.loadClass( ( (String) entry.getValue() ).trim() ).newInstance();
            commands.put( commandName, command );
            String aliases = props.getProperty( commandConfig + ".alias" );
            if ( StringUtils.isNotEmpty( aliases ) )
            {
                for ( String alias : StringUtils.split( aliases, ',' ) )
                {
                    commands.put( alias, command );
                }
            }
        }
        commands.put( "help", new HelpCommand( this ) );
        this.commands = Collections.unmodifiableMap( commands );
        output.println( "Welcome to JMX terminal. Type \"help\" for available commands." );
    }

    public void execute( String command )
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
        Command cmd = commands.get( commandName );
        Validate.notNull( cmd, "Unknown command " + commandName + ", try typing \"help\"" );
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

    Map<String, Command> getCommands()
    {
        return commands;
    }

    public boolean isClosed()
    {
        return session.isClosed();
    }

    public void prompt()
        throws IOException
    {
        session.getOutput().print( ( session.getConnection() == null ? "?" : ">" ) + "$ " );
    }
}
