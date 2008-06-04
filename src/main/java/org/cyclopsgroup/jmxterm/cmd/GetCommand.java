package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

public class GetCommand
    implements Command
{
    public static void displayAttributes( List<String> args, Session session )
        throws IOException, JMException
    {
        List<String> values = new ArrayList<String>( args.size() + 1 );
        values.add( new SimpleDateFormat( "mm:HH:ss" ).format( new Date() ) );

        ObjectName beanName = new ObjectName( session.getBean() );
        MBeanServerConnection con = session.getConnection().getConnector().getMBeanServerConnection();
        for ( String arg : args )
        {
            String attrName;
            if ( SyntaxUtils.isIndex( arg ) )
            {
                int index = SyntaxUtils.getIndex( arg );
                attrName = InfoCommand.getAttributes( session ).get( index ).getName();
            }
            else
            {
                attrName = arg;
            }
            Object result = con.getAttribute( beanName, attrName );
            values.add( result == null ? "null" : result.toString() );
        }
        session.getOutput().println( StringUtils.join( values, ',' ) );
    }

    public void execute( List<String> args, Session session )
        throws JMException, IOException
    {
        displayAttributes( args, session );
    }
}
