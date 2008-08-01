package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

/**
 * Command to set an attribute
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "set", description = "Set value of an MBean attribute" )
public class SetCommand
    extends Command
{
    private List<String> arguments = Collections.emptyList();

    private String bean;

    private String domain;

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws JMException, IOException
    {
        Validate.isTrue( arguments.size() >= 2, "At least two arguments are required" );
        String attributeName = arguments.get( 0 );

        String beanName = BeanCommand.getBeanName( bean, domain, session );
        ObjectName name = new ObjectName( beanName );

        MBeanServerConnection con = session.getConnection().getServerConnection();
        MBeanInfo beanInfo = con.getMBeanInfo( new ObjectName( beanName ) );
        MBeanAttributeInfo attributeInfo = null;
        for ( MBeanAttributeInfo i : beanInfo.getAttributes() )
        {
            if ( i.getName().equals( attributeName ) )
            {
                attributeInfo = i;
                break;
            }
        }
        if ( attributeInfo == null )
        {
            throw new IllegalArgumentException( "Attribute " + attributeName + " is not sepcified" );
        }
        if ( !attributeInfo.isWritable() )
        {
            throw new IllegalArgumentException( "Attribute " + attributeName + " is not writable" );
        }
        Object value = SyntaxUtils.parse( arguments.get( 1 ), attributeInfo.getType() );
        con.setAttribute( name, new Attribute( attributeName, value ) );
        session.msg( "Value of attribute " + attributeName + " is set to " + value, SyntaxUtils.OK );
    }

    /**
     * @param arguments Argument list. The first argument is attribute name
     */
    @Argument( description = "name, value, value2..." )
    public final void setArguments( List<String> arguments )
    {
        Validate.notNull( arguments, "Arguments can't be NULL" );
        this.arguments = arguments;
    }

    /**
     * @param bean Bean where the attribute is
     */
    @Option( name = "b", longName = "bean", description = "MBean name where the attribute is. Optional if bean has been set" )
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    /**
     * @param domain Domain where the bean is
     */
    @Option( name = "d", longName = "domain", description = "Domain under which the bean is" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }
}
