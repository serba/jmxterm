package org.cyclopsgroup.jmxterm.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.Command;

/**
 * Factory class of commands which knows how to create Command class with given command name
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class CommandFactory
{
    final Map<String, Class<? extends Command>> commandTypes;

    private static final String CONFIG_COMMAND_ENTRY = "jmxterm.commands.";

    private static final String CONFIG_COMMAND_TYPE_SUFFIX = ".type";

    private static final String CONFIG_PATH = "META-INF/cyclopsgroup/jmxterm.properties";

    /**
     * Constructor which builds up command types
     * 
     * @throws ClassNotFoundException Thrown when configured command class doesn't exist
     * @throws IOException Thrown when Jar is corrupted
     */
    @SuppressWarnings( "unchecked" )
    CommandFactory()
        throws ClassNotFoundException, IOException
    {
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
        HashMap<String, Class<? extends Command>> commands = new HashMap<String, Class<? extends Command>>();
        for ( Map.Entry<Object, Object> entry : props.entrySet() )
        {
            String name = (String) entry.getKey();
            if ( !name.startsWith( CONFIG_COMMAND_ENTRY ) || !name.endsWith( CONFIG_COMMAND_TYPE_SUFFIX ) )
            {
                continue;
            }
            String commandConfig = name.substring( 0, name.length() - CONFIG_COMMAND_TYPE_SUFFIX.length() );
            String commandName = commandConfig.substring( CONFIG_COMMAND_ENTRY.length() );
            Class<? extends Command> commandType =
                (Class<? extends Command>) classLoader.loadClass( (String) entry.getValue() );
            commands.put( commandName, commandType );
            String aliases = props.getProperty( commandConfig + ".alias" );
            if ( StringUtils.isNotEmpty( aliases ) )
            {
                for ( String alias : StringUtils.split( aliases, ',' ) )
                {
                    commands.put( alias, commandType );
                }
            }
        }
        commands.put( "help", HelpCommand.class );
        this.commandTypes = Collections.unmodifiableMap( commands );
    }

    /**
     * Create command instance for given command name
     * 
     * @param commandName Name of command
     * @return Instance of command. It won't be NULL
     * @throws InstantiationException Thrown when command instance couldn't be created
     * @throws IllegalAccessException Thrown when command instance couldn't be created
     * @throws IllegalArgumentException Thrown when commandName is NULL or unknown
     */
    Command createCommand( String commandName )
        throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        Validate.notNull( commandName, "commandName can't be NULL" );
        Class<? extends Command> commandType = commandTypes.get( commandName );
        if ( commandType == null )
        {
            throw new IllegalArgumentException( "Command " + commandName
                + " isn't valid, run help to see available commands" );
        }
        return commandType.newInstance();
    }
}
