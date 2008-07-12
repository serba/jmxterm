package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.management.JMException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

@Cli( name = "run", description = "Invocate MBean action" )
public class RunCommand
    extends Command
{
    private List<String> parameters = Collections.emptyList();

    private String bean;

    private String domain;

    private boolean measure;

    @Option( name = "m", longName = "measure", description = "Measure the time spent on the invocation of operation" )
    public final void setMeasure( boolean measure )
    {
        this.measure = measure;
    }

    @Option( name = "d", longName = "domain", description = "Domain of MBean to invoke" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }

    @Option( name = "b", longName = "bean", description = "MBean to invoke" )
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    @Argument( requires = 1 )
    public final void setParameters( List<String> parameters )
    {
        Validate.notNull( parameters, "Parameters can't be NULL" );
        this.parameters = parameters;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws MalformedObjectNameException, IOException, JMException
    {
        String beanName = BeanCommand.getBeanName( bean, domain, session );
        if ( beanName == null )
        {
            throw new IllegalArgumentException( "Please specify MBean to invoke either using -b option or bean command" );
        }

        Validate.isTrue( parameters.size() > 0, "At least one parameter is needed" );
        String operationName = parameters.get( 0 );
        ObjectName name = new ObjectName( beanName );
        MBeanServerConnection con = session.getConnection().getConnector().getMBeanServerConnection();
        MBeanInfo beanInfo = con.getMBeanInfo( name );
        MBeanOperationInfo operationInfo = null;
        for ( MBeanOperationInfo info : beanInfo.getOperations() )
        {
            if ( operationName.equals( info.getName() ) && info.getSignature().length == parameters.size() - 1 )
            {
                operationInfo = info;
                break;
            }
        }
        if ( operationInfo == null )
        {
            throw new IllegalArgumentException( "Operation " + operationName + " with " + ( parameters.size() - 1 ) +
                " parameters doesn't exist in bean " + beanName );
        }
        Object[] params = new Object[parameters.size() - 1];
        MBeanParameterInfo[] paramInfos = operationInfo.getSignature();
        Validate.isTrue( params.length == paramInfos.length,
                         String.format( "%d parameters are expected but %d are provided", paramInfos.length,
                                        params.length ) );
        String[] signatures = new String[paramInfos.length];
        for ( int i = 0; i < paramInfos.length; i++ )
        {
            MBeanParameterInfo paramInfo = paramInfos[i];
            params[i] = SyntaxUtils.parse( parameters.get( i + 1 ), paramInfo.getType() );
            signatures[i] = paramInfo.getType();
        }
        session.msg( String.format( "calling operation %s of mbean %s", operationName, beanName ) );
        Object result;
        if ( measure )
        {
            long start = System.currentTimeMillis();
            try
            {
                result = con.invoke( name, operationName, params, signatures );
            }
            finally
            {
                long latency = System.currentTimeMillis() - start;
                session.msg( latency + "ms is taken by invocation" );
            }
        }
        else
        {
            result = con.invoke( name, operationName, params, signatures );
        }
        session.msg( "operation returns: " );
        SyntaxUtils.printValue( session.output, result, 0, false );
        session.output.println();
    }
}
