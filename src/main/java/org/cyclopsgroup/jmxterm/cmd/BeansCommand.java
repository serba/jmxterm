package org.cyclopsgroup.jmxterm.cmd;

import java.util.List;
import java.util.Set;

import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

public class BeansCommand
    implements Command
{
    @SuppressWarnings( "unchecked" )
    public void execute( List<String> args, Session session )
        throws Exception
    {
        ObjectName queryName = null;
        if ( session.getDomain() != null )
        {
            queryName = new ObjectName( session.getDomain() + ":*" );
        }
        Set<ObjectName> names =
            session.getConnection().getConnector().getMBeanServerConnection().queryNames( queryName, null );
        for ( ObjectName name : names )
        {
            session.getOutput().println( name );
        }
    }
}
