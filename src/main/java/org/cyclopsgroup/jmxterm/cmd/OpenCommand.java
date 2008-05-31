package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.Session;

public class OpenCommand
    implements Command
{
    private static final Log LOG = LogFactory.getLog( OpenCommand.class );

    private static final Pattern PATTERN_HOST_PORT = Pattern.compile( "^(\\w|\\.)+\\:\\d+$" );

    public void execute( List<String> args, Session session )
        throws IOException
    {
        if ( args.isEmpty() )
        {
            Connection con = session.getConnection();
            if ( con == null )
            {
                session.getOutput().println( "Not connected" );
            }
            else
            {

                session.getOutput().println(
                                             String.format( "Connected to: expr=%s, id=%s, url=%s",
                                                            con.getOriginalUrl(), con.getConnector().getConnectionId(),
                                                            con.getUrl() ) );
            }
            return;
        }
        Validate.isTrue( session.getConnection() == null );
        String url = args.get( 0 );
        JMXServiceURL u;
        if ( PATTERN_HOST_PORT.matcher( url ).find() )
        {
            u = new JMXServiceURL( "service:jmx:rmi:///jndi/rmi://" + url + "/jmxrmi" );
        }
        else
        {
            u = new JMXServiceURL( url );
        }
        JMXConnector connector = JMXConnectorFactory.connect( u );
        session.setConnection( new Connection( connector, u, url ) );
        LOG.info( "Connection to " + url + " is opened" );
    }
}
