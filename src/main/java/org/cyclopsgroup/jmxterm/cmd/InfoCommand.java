package org.cyclopsgroup.jmxterm.cmd;

import java.io.PrintStream;
import java.util.List;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command that shows information of current selected bean
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class InfoCommand
    implements Command
{
    /**
     * @inheritDoc
     */
    public void execute( List<String> args, Session session )
        throws Exception
    {
        ObjectName name = new ObjectName( session.getBean() );
        MBeanServerConnection con = session.getConnection().getConnector().getMBeanServerConnection();
        MBeanInfo info = con.getMBeanInfo( name );

        PrintStream out = session.getOutput();
        out.println( "MBean " + name );
        out.println( "Class name:" + info.getClassName() );
        out.println( "Attributes:" );
        for ( MBeanAttributeInfo attr : info.getAttributes() )
        {
            out.println( String.format( " - %s(%s)", attr.getName(), attr.getType() ) );
        }
        out.println( "Operations:" );
        for ( MBeanOperationInfo op : info.getOperations() )
        {
            out.println( String.format( " - %s(%s)", op.getName(), op.getReturnType() ) );
        }
    }
}
