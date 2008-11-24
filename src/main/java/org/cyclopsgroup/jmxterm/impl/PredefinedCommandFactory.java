package org.cyclopsgroup.jmxterm.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.CommandFactory;
import org.cyclopsgroup.jmxterm.ExtendedPropertiesUtils;

/**
 * Factory class of commands which knows how to create Command class with given command name
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class PredefinedCommandFactory
    implements CommandFactory
{
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
        this( "META-INF/cyclopsgroup/jmxterm.properties" );
    }

    /**
     * Constructor which builds up command types
     * 
     * @param configPath Path of configuration file in classpath
     * @throws ClassNotFoundException Thrown when configured command class doesn't exist
     * @throws IOException Thrown when Jar is corrupted
     */
    @SuppressWarnings( "unchecked" )
    public PredefinedCommandFactory( String configPath )
        throws ClassNotFoundException, IOException
    {
        Validate.notNull( configPath, "configPath can't be NULL" );
        ClassLoader classLoader = getClass().getClassLoader();
        ExtendedProperties props = ExtendedPropertiesUtils.loadFromOverlappingResources( configPath, classLoader );
        if ( props == null )
        {
            throw new FileNotFoundException( "Couldn't load configuration from " + configPath
                + ", classpath has problem" );
        }
        props = props.subset( "jmxterm.commands" );
        if ( props == null )
        {
            throw new IOException( "Expected configuration doesn't appear in " + configPath );
        }
        HashMap<String, Class<? extends Command>> commands = new HashMap<String, Class<? extends Command>>();
        for ( String name : props.getStringArray( "name" ) )
        {
            String type = props.getString( name + ".type" );
            Class<? extends Command> commandType = (Class<? extends Command>) classLoader.loadClass( type );
            commands.put( name, commandType );
            String[] aliases = props.getStringArray( name + ".alias" );
            if ( !ArrayUtils.isEmpty( aliases ) )
            {
                for ( String alias : aliases )
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
