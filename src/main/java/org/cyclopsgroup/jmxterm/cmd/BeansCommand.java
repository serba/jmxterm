package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command that shows list of beans
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class BeansCommand
    implements Command
{
    /**
     * Get list of bean names under current domain
     * 
     * @param session Current JMX session
     * @return List of bean names
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    @SuppressWarnings( "unchecked" )
    public static List<String> getBeans( Session session )
        throws MalformedObjectNameException, IOException
    {
        ObjectName queryName = null;
        if ( session.getDomain() != null )
        {
            queryName = new ObjectName( session.getDomain() + ":*" );
        }
        Set<ObjectName> names =
            session.getConnection().getConnector().getMBeanServerConnection().queryNames( queryName, null );
        List<String> results = new ArrayList<String>( names.size() );
        for ( ObjectName name : names )
        {
            results.add( name.getCanonicalName() );
        }
        Collections.sort( results );
        return results;
    }

    public static void listBeans( Session session )
        throws MalformedObjectNameException, IOException
    {
        session.getOutput().println( "Following beans are available" );
        int i = 0;
        for ( String bean : getBeans( session ) )
        {
            session.getOutput().println( String.format( "%%%-3d - %s", i++, bean ) );
        }
    }

    /**
     * @inheritDoc
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    public void execute( List<String> args, Session session )
        throws MalformedObjectNameException, IOException
    {
        listBeans( session );
    }
}
