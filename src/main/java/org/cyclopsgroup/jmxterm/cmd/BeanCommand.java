package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

public class BeanCommand
    implements Command
{
    private static final String STRING_PATTERN_PROPERTIES = "\\w\\=\\w(\\,\\w\\=\\w)*";

    private static final Pattern PATTERN_PROPERTIES = Pattern.compile( "^" + STRING_PATTERN_PROPERTIES + "$" );

    private static final Pattern PATTERN_BEAN_NAME =
        Pattern.compile( "^(\\w|\\.)+\\:" + STRING_PATTERN_PROPERTIES + "$" );

    private static final Log LOG = LogFactory.getLog( BeanCommand.class );

    public static void selectBean( String bean, Session session )
        throws MalformedObjectNameException, IOException
    {
        if ( SyntaxUtils.isNull( bean ) )
        {
            session.setBean( null );
        }
        else if ( SyntaxUtils.isIndex( bean ) )
        {
            String beanName = BeansCommand.getBeans( session ).get( SyntaxUtils.getIndex( bean ) );
            session.setBean( beanName );
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
     * @throws IOException
     * @throws MalformedObjectNameException
     * @inheritDoc
     */
    public void execute( List<String> args, Session session )
        throws MalformedObjectNameException, IOException
    {
        if ( args.isEmpty() )
        {
            if ( session.getBean() == null )
            {
                session.getOutput().println( "Bean is not set" );
            }
            else
            {
                session.getOutput().println( "Current bean is " + session.getBean() );
            }
            return;
        }
        selectBean( args.get( 0 ), session );
    }
}
