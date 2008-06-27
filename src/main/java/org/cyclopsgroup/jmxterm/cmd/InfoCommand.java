package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command that shows information of current selected bean
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "info", description = "Display detail about an MBean" )
public class InfoCommand
    extends Command
{
    private static final Comparator<MBeanAttributeInfo> ATTRIBUTE_COMPARATOR = new Comparator<MBeanAttributeInfo>()
    {
        public int compare( MBeanAttributeInfo o1, MBeanAttributeInfo o2 )
        {
            return o1.getName().compareTo( o2.getName() );
        }
    };

    private static final Comparator<MBeanOperationInfo> OPERATION_COMPARATOR = new Comparator<MBeanOperationInfo>()
    {
        public int compare( MBeanOperationInfo o1, MBeanOperationInfo o2 )
        {
            return o1.getName().compareTo( o2.getName() );
        }
    };

    private static List<MBeanAttributeInfo> getAttributes( MBeanInfo beanInfo )
        throws JMException, IOException
    {
        MBeanAttributeInfo[] attrInfos = beanInfo.getAttributes();
        List<MBeanAttributeInfo> result = new ArrayList<MBeanAttributeInfo>( Arrays.asList( attrInfos ) );
        Collections.sort( result, ATTRIBUTE_COMPARATOR );
        return result;
    }

    private static List<MBeanOperationInfo> getOperations( MBeanInfo beanInfo )
    {
        MBeanOperationInfo[] operationInfos = beanInfo.getOperations();
        List<MBeanOperationInfo> result = new ArrayList<MBeanOperationInfo>( Arrays.asList( operationInfos ) );
        Collections.sort( result, OPERATION_COMPARATOR );
        return result;
    }

    public static List<MBeanAttributeInfo> getAttributes( Session session )
        throws JMException, IOException
    {
        Validate.notNull( session, "Session can't be NULL" );
        return getAttributes( getMBeanInfo( session ) );
    }

    private static MBeanInfo getMBeanInfo( Session session )
        throws IOException, JMException
    {
        ObjectName name = new ObjectName( session.getBean() );
        MBeanServerConnection con = session.getConnection().getConnector().getMBeanServerConnection();
        return con.getMBeanInfo( name );
    }

    private String bean;

    @Option( name = "b", longName = "bean", description = "Name of bean" )
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws IOException, JMException
    {
        MBeanInfo info = getMBeanInfo( session );
        PrintWriter out = session.getOutput();
        out.println( "MBean " + session.getBean() );
        out.println( "Class name:" + info.getClassName() );
        out.println( "Attributes:" );
        int index = 0;
        for ( MBeanAttributeInfo attr : getAttributes( info ) )
        {
            out.println( String.format( "%%%-3d - %s(%s)", index++, attr.getName(), attr.getType() ) );
        }
        out.println( "Operations:" );
        index = 0;
        for ( MBeanOperationInfo op : getOperations( info ) )
        {
            MBeanParameterInfo[] paramInfos = op.getSignature();
            List<String> paramTypes = new ArrayList<String>( paramInfos.length );
            for ( MBeanParameterInfo paramInfo : paramInfos )
            {
                paramTypes.add( paramInfo.getType() + " " + paramInfo.getName() );
            }
            out.println( String.format( "%%%-3d - %s(%s) returns %s", index++, op.getName(),
                                        StringUtils.join( paramTypes, ',' ), op.getReturnType() ) );
        }
    }
}
