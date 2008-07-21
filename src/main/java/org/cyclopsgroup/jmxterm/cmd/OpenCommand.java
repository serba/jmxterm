package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnector;

import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

/**
 * Command to open JMX connection
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "open", description = "Open JMX session", note = "eg. open localhost:9991, or open jmx:service:..." )
public class OpenCommand
    extends Command
{
    private String url;

    private String user;

    private String password;

    /**
     * @param user User name for user authentication
     */
    @Option( name = "u", longName = "user", description = "User name for user/password authentication" )
    public final void setUser( String user )
    {
        this.user = user;
    }

    /**
     * @param password Password for user authentication
     */
    @Option( name = "p", longName = "password", description = "Password for user/password authentication" )
    public final void setPassword( String password )
    {
        this.password = password;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws IOException
    {
        if ( url == null )
        {
            Connection con = session.getConnection();
            if ( con == null )
            {
                session.msg( "not connected", SyntaxUtils.NULL );
            }
            else
            {
                session.output.println( String.format( session.isAbbreviated() ? "%s,%s,%s"
                                : "connected to: expr=%s, id=%s, url=%s", con.getDisplayUrl(), con.getConnectorId(),
                                                       con.getUrl() ) );
            }
            return;
        }
        Map<String, Object> env;
        if ( user != null )
        {
            env = new HashMap<String, Object>( 1 );
            env.put( JMXConnector.CREDENTIALS, new String[] { user, password } );
        }
        else
        {
            env = null;
        }
        session.connect( url, env );
        session.msg( "Connection to " + url + " is opened", SyntaxUtils.OK );

    }

    /**
     * @param url URL of MBean service to open
     */
    @Argument
    public final void setUrl( String url )
    {
        this.url = url;
    }
}
