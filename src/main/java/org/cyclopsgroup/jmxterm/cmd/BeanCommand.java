package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.management.JMException;
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

/**
 * Command to display or set current bean
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "bean", description = "Display or set current selected MBean. ", note = "Without any parameter, it displays current selected bean, "
    + "otherwise it selects the bean defined by the first parameter. eg. bean java.lang:type=Memory" )
public class BeanCommand
    extends Command
{
    private static final String PARTIAL_PATTERN_PROPERTIES = "\\w+\\=.+(\\,\\w+\\=.+)*";

    private static final Pattern PATTERN_BEAN_NAME =
        Pattern.compile( "^(\\w|\\.)+\\:" + PARTIAL_PATTERN_PROPERTIES + "$" );

    private static final Pattern PATTERN_PROPERTIES = Pattern.compile( "^" + PARTIAL_PATTERN_PROPERTIES + "$" );

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
     * Get list of candidate beans
     * 
     * @param session Session
     * @return List of bean names
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    static List<String> getCandidateBeanNames( Session session )
        throws MalformedObjectNameException, IOException
    {
        ArrayList<String> results = new ArrayList<String>( BeansCommand.getBeans( session, null ) );
        String domain = session.getDomain();
        if ( domain != null )
        {
            List<String> beans = BeansCommand.getBeans( session, domain );
            for ( String bean : beans )
            {
                results.add( bean.substring( domain.length() + 1 ) );
            }
        }
        return results;
    }

    private String bean;

    private String domain;

    /**
     * @inheritDoc
     */
    public List<String> doSuggestArgument()
        throws IOException, MalformedObjectNameException
    {
        return getCandidateBeanNames( getSession() );
    }

    /**
     * @inheritDoc
     */
    public List<String> doSuggestOption( String optionName )
        throws IOException
    {
        if ( optionName.equals( "d" ) )
        {
            return DomainsCommand.getCandidateDomains( getSession() );
        }
        else
        {
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute()
        throws IOException, JMException
    {
        Session session = getSession();
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
     * Set domain option
     * 
     * @param domain Domain option to set
     */
    @Option( name = "d", longName = "domain", description = "Domain name" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }
}
