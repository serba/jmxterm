package org.cyclopsgroup.jmxterm;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Java process manager
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class JavaProcessManager
{
    private static final JavaProcessManager INSTANCE;

    private static final Log LOG = LogFactory.getLog( JavaProcessManager.class );
    static
    {
        JavaProcessManager jpm = null;
        try
        {
            jpm = createInstance();
        }
        catch ( IOException e )
        {
            LOG.error( "JavaProcessManager couldn't be initialized", e );
        }
        finally
        {
            INSTANCE = jpm;
        }
    }

    private static JavaProcessManager createInstance()
        throws IOException
    {
        ExtendedProperties extProps =
            ExtendedPropertiesUtils.loadFromOverlappingResources( "META-INF/cyclopsgroup/jmxterm.properties",
                                                                  JavaProcessManager.class.getClassLoader() );
        extProps = extProps.subset( "jmxterm.jpm" );
        String[] names = extProps.getStringArray( "name" );
        ClassLoader classLoader = JavaProcessManager.class.getClassLoader();
        JavaProcessManager jpm = null;
        for ( String name : names )
        {
            String type = extProps.getString( name + ".type" );
            String description = extProps.getString( name + ".description" );

            try
            {
                jpm = (JavaProcessManager) classLoader.loadClass( type ).newInstance();
                jpm.setName( name );
                jpm.setDescription( description );
                break;
            }
            catch ( InstantiationException e )
            {
                continue;
            }
            catch ( IllegalAccessException e )
            {
                continue;
            }
            catch ( ClassNotFoundException e )
            {
                continue;
            }
        }
        return jpm;
    }

    /**
     * @return Valid JavaProcessManager implementation or throw exception
     * @throws UnsupportedOperationException Implementation can't be found at runtime
     */
    public static JavaProcessManager getInstance()
    {
        if ( INSTANCE == null )
        {
            throw new UnsupportedOperationException( "Java environment doesn't support this feature" );
        }
        return INSTANCE;
    }

    private String description;

    private String name;

    /**
     * Get JVM process
     * 
     * @param pid Process ID
     * @return Process or NULL
     * @throws Exception All any exception
     */
    public abstract JavaProcess get( int pid )
        throws Exception;

    /**
     * @return Description of this implementation
     */
    public final String getDescription()
    {
        return description;
    }

    /**
     * @return Name of this implementation
     */
    public final String getName()
    {
        return name;
    }

    /**
     * List all running Java processes
     * 
     * @return List of running processes
     */
    public abstract List<JavaProcess> list();

    private void setDescription( String description )
    {
        this.description = description;
    }

    private void setName( String name )
    {
        this.name = name;
    }
}
