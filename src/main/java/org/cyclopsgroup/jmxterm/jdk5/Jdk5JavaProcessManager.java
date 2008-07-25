package org.cyclopsgroup.jmxterm.jdk5;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.JavaProcessManager;
import org.cyclopsgroup.jmxterm.WeakCastUtils;

/**
 * JDK5 specific implementation of {@link JavaProcessManager}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Jdk5JavaProcessManager
    extends JavaProcessManager
{
    private final ConnectorAddressLink connectorAddressLink;

    /**
     * Default constructor
     * 
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public Jdk5JavaProcessManager()
        throws SecurityException, NoSuchMethodException, ClassNotFoundException
    {
        connectorAddressLink =
            WeakCastUtils.staticCast( Class.forName( ConnectorAddressLink.ORIGINAL_CLASS_NAME ),
                                      ConnectorAddressLink.class );
    }

    /**
     * @inheritDoc
     */
    @Override
    public JavaProcess get( int pid )
        throws IOException
    {
        Validate.isTrue( pid > 0, "PID " + pid + " isn't valid" );
        String url = connectorAddressLink.importFrom( pid );
        if ( url == null )
        {
            throw new UnsupportedOperationException( "PID " + pid + " is not manageable under JDK5" );
        }
        return new Jdk5JavaProcess( pid, url );
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<JavaProcess> list()
    {
        throw new UnsupportedOperationException( "This feature isn't implemented under JDK5" );
    }
}
