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
@Cli( name = "open", description = "Open JMX session or display current connection", note = "Without argument this command display current connection. "
    + "URL can be a <PID>, <hostname>:<port> or full qualified JMX service URL. For example\n open localhost:9991,\n open jmx:service:..." )
public class OpenCommand
    extends Command
{
    private String password;

    private String url;

    private String user;

    /**
     * @inheritDoc
     */
    @Override
    public void execute()
        throws IOException
    {
        Session session = getSession();
        if ( url == null )
        {
            Connection con = session.getConnection();
            if ( con == null )
            {
                session.output.printMessage( "not connected" );
                session.output.println( SyntaxUtils.NULL );
            }
            else
            {
                session.output.println( String.format( "%s,%s", con.getConnectorId(), con.getUrl() ) );
            }
            return;
        }
        Map<String, Object> env;
        if ( user != null )
        {
            if ( password == null )
            {
                password = session.input.readMaskedString( "Credential password: " );
            }
            env = new HashMap<String, Object>( 1 );
            String[] credentials = { user, password };
            env.put( JMXConnector.CREDENTIALS, credentials );
        }
        else
        {
            env = null;
        }
        session.connect( SyntaxUtils.getUrl( url ), env );
        session.output.printMessage( "Connection to " + url + " is opened" );
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
     * @param url URL of MBean service to open
     */
    @Argument( )
    public final void setUrl( String url )
    {
        this.url = url;
    }

    /**
     * @param user User name for user authentication
     */
    @Option( name = "u", longName = "user", description = "User name for user/password authentication" )
    public final void setUser( String user )
    {
        this.user = user;
    }
}
