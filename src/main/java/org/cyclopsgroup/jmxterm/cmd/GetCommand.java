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
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

@Cli( name = "get", description = "Get value of attribute(s)" )
public class GetCommand
    extends Command
{
    public void displayAttributes( Session session )
        throws IOException, JMException
    {
        List<String> values = new ArrayList<String>( attributes.length + 1 );
        values.add( new SimpleDateFormat( "mm:HH:ss" ).format( new Date() ) );

        ObjectName beanName = new ObjectName( bean == null ? session.getBean() : bean );

        MBeanServerConnection con = session.getConnection().getConnector().getMBeanServerConnection();
        for ( String arg : attributes )
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
        session.output.println( StringUtils.join( values, ',' ) );
    }

    private String[] attributes = {};

    private String bean;

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws JMException, IOException
    {
        displayAttributes( session );
    }

    @Argument( requires = 1 )
    public final void setAttributes( String[] attributes )
    {
        this.attributes = attributes;
    }

    @Option( name = "b", longName = "bean" )
    public final void setBean( String bean )
    {
        this.bean = bean;
    }
}
