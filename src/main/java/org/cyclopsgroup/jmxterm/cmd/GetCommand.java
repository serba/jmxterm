package org.cyclopsgroup.jmxterm.cmd;

import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

public class GetCommand
    implements Command
{

    public void execute( List<String> args, Session session )
        throws Exception
    {
        String attributeName = args.get( 0 );
        ObjectName beanName = new ObjectName( session.getBean() );
        MBeanServerConnection con = session.getConnection().getConnector().getMBeanServerConnection();
        Object result = con.getAttribute( beanName, attributeName );
        System.out.println( result );
    }
}
