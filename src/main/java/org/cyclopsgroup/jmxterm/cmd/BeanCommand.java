package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;
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
    private static final String STRING_PATTERN_PROPERTIES = "\\w+\\=\\w+(\\,\\w+\\=\\w+)*";

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

    public static String getBeanName( String bean, String domain, Session session )
        throws MalformedObjectNameException, IOException
    {
        if ( SyntaxUtils.isNull( bean ) )
        {
            return session.getBean();
        }
        if ( SyntaxUtils.isIndex( bean ) )
        {
            return null;
        }
        if ( PATTERN_BEAN_NAME.matcher( bean ).find() )
        {
            return bean;
        }
        String domainName = DomainCommand.getDomainName( domain, session );
        if ( domainName == null && !StringUtils.equalsIgnoreCase( domain, "null" ) )
        {
            domainName = session.getDomain();
        }
        if ( PATTERN_PROPERTIES.matcher( bean ).find() && domainName != null )
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
                session.output.println( "Bean is not set" );
            }
            else
            {
                session.output.println( "Bean = " + session.getBean() );
            }
        }
        else
        {
            String beanName = getBeanName( bean, domain, session );
            ObjectName name = new ObjectName( beanName );
            MBeanServerConnection con = session.getConnection().getConnector().getMBeanServerConnection();
            con.getMBeanInfo( name );
            session.setBean( beanName );
            session.output.println( "Bean is set to " + beanName );
        }
    }
}
