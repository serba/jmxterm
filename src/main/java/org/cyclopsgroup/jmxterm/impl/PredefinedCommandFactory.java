package org.cyclopsgroup.jmxterm.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.CommandFactory;

/**
 * Factory class of commands which knows how to create Command class with given command name
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class PredefinedCommandFactory
    implements CommandFactory
{
    private static final String CONFIG_COMMAND_ENTRY = "jmxterm.commands.";

    private static final String CONFIG_COMMAND_TYPE_SUFFIX = ".type";

    private static final String CONFIG_PATH = "META-INF/cyclopsgroup/jmxterm.properties";

    private final CommandFactory delegate;

    /**
     * Default constructor
     * 
     * @throws ClassNotFoundException Thrown when configured command class doesn't exist
     * @throws IOException Thrown when Jar is corrupted
     */
    PredefinedCommandFactory()
        throws ClassNotFoundException, IOException
    {
        this( CONFIG_PATH );
    }

    /**
     * Constructor which builds up command types
     * 
     * @throws ClassNotFoundException Thrown when configured command class doesn't exist
     * @throws IOException Thrown when Jar is corrupted
     */
    @SuppressWarnings( "unchecked" )
    PredefinedCommandFactory( String configPath )
        throws ClassNotFoundException, IOException
    {
        Validate.notNull( configPath, "configPath can't be NULL" );
        Properties props = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        Enumeration<URL> configs = classLoader.getResources( configPath );
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
        delegate = new TypeMapCommandFactory( commands );
    }

    /**
     * @inheritDoc
     */
    public Command createCommand( String commandName )
        throws Exception
    {
        return delegate.createCommand( commandName );
    }

    /**
     * @inheritDoc
     */
    public Map<String, Class<? extends Command>> getCommandTypes()
    {
        return delegate.getCommandTypes();
    }
}
