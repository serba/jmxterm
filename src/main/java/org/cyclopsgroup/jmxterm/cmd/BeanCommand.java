package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.management.JMException;
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
 * Command to display or set current bean
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "bean", description = "Display or set current bean" )
public class BeanCommand
    extends Command
{
    private static final String STRING_PATTERN_PROPERTIES = "\\w+\\=.+(\\,\\w+\\=.+)*";

    private static final Pattern PATTERN_PROPERTIES = Pattern.compile( "^" + STRING_PATTERN_PROPERTIES + "$" );

    private static final Pattern PATTERN_BEAN_NAME =
        Pattern.compile( "^(\\w|\\.)+\\:" + STRING_PATTERN_PROPERTIES + "$" );

    private String bean;

    private String domain;

    /**
     * Set domain option
     * 
     * @param domain Domain option to set
     */
    @Option( name = "d", longName = "domain", description = "Domain name" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }

    /**
     * Set bean option
     * 
     * @param bean Bean to set
     */
    @Argument
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    /**
     * Get full MBean name with given bean name, domain and session
     * 
     * @param bean Name of bean. It can be NULL so that session#getBean() is returned
     * @param domain Domain for bean
     * @param session Current session
     * @return Full qualified name of MBean
     * @throws JMException Thrown when given MBean name is malformed
     * @throws IOException
     */
    public static String getBeanName( String bean, String domain, Session session )
        throws JMException, IOException
    {
        Validate.notNull( session, "Session can't be NULL" );
        if ( bean == null )
        {
            return session.getBean();
        }
        if ( SyntaxUtils.isNull( bean ) )
        {
            return null;
        }
        MBeanServerConnection con = session.getConnection().getServerConnection();
        if ( PATTERN_BEAN_NAME.matcher( bean ).find() )
        {
            ObjectName name = new ObjectName( bean );
            con.getMBeanInfo( name );
            return bean;
        }
        String domainName = DomainCommand.getDomainName( domain, session );
        if ( domainName == null )
        {
            throw new IllegalArgumentException( "Please specify domain using either -d option or domain command" );
        }
        if ( PATTERN_PROPERTIES.matcher( bean ).find() )
        {
            return domainName + ":" + bean;
        }
        throw new IllegalArgumentException( "Bean name " + bean + " isn't valid" );
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws IOException, JMException
    {
        if ( bean == null )
        {
            if ( session.getBean() == null )
            {
                session.msg( "bean is not set", SyntaxUtils.NULL );
            }
            else
            {
                session.msg( "bean = " + session.getBean(), session.getBean() );
            }
            return;
        }
        String beanName = getBeanName( bean, domain, session );
        if ( beanName == null )
        {
            session.setBean( null );
            session.msg( "bean is unset", SyntaxUtils.OK );
            return;
        }
        ObjectName name = new ObjectName( beanName );
        MBeanServerConnection con = session.getConnection().getServerConnection();
        con.getMBeanInfo( name );
        session.setBean( beanName );
        session.msg( "bean is set to " + beanName, SyntaxUtils.OK );
    }
}
