package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

@Cli( name = "bean", description = "Display or set current bean" )
public class BeanCommand
    extends Command
{
    private static final String STRING_PATTERN_PROPERTIES = "\\w\\=\\w(\\,\\w\\=\\w)*";

    private static final Pattern PATTERN_PROPERTIES = Pattern.compile( "^" + STRING_PATTERN_PROPERTIES + "$" );

    private static final Pattern PATTERN_BEAN_NAME =
        Pattern.compile( "^(\\w|\\.)+\\:" + STRING_PATTERN_PROPERTIES + "$" );

    private static final Log LOG = LogFactory.getLog( BeanCommand.class );

    private String bean;

    private String domain;

    @Option( name = "d", longName = "domain", description = "Domain name" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }

    @Argument
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    public static void selectBean( String bean, Session session )
        throws MalformedObjectNameException, IOException
    {
        if ( SyntaxUtils.isNull( bean ) )
        {
            session.setBean( null );
        }
        else if ( PATTERN_PROPERTIES.matcher( bean ).matches() && session.getDomain() != null )
        {
            session.setBean( session.getDomain() + ":" + bean );
        }
        else if ( PATTERN_BEAN_NAME.matcher( bean ).matches() )
        {
            ObjectName beanName = new ObjectName( bean );
            session.setDomain( beanName.getDomain() );
            session.setBean( bean );
        }
        else
        {
            throw new IllegalArgumentException( "Bean name " + bean + " isn't valid" );
        }
        LOG.info( "Bean is set to " + session.getBean() );
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws MalformedObjectNameException, IOException
    {
        if ( bean == null )
        {
            if ( session.getBean() == null )
            {
                session.getOutput().println( "Bean is not set" );
            }
            else
            {
                session.getOutput().println( "Bean " + session.getBean() + " is selected currently" );
            }
        }
        else
        {
            selectBean( bean, session );
        }
    }
}
