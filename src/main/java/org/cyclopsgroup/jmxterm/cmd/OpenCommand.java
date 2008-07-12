package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

@Cli( name = "open", description = "Open JMX session", note = "eg. open localhost:9991, or open jmx:service:..." )
public class OpenCommand
    extends Command
{
    private String url;

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
                                : "connected to: expr=%s, id=%s, url=%s", con.getOriginalUrl(),
                                                       con.getConnector().getConnectionId(), con.getUrl() ) );
            }
            return;
        }
        Validate.isTrue( session.getConnection() == null, "Session is already opened" );
        JMXServiceURL u = SyntaxUtils.getUrl( url );
        JMXConnector connector = JMXConnectorFactory.connect( u );
        session.setConnection( new Connection( connector, u, url ) );
        session.msg( "Connection to " + url + " is opened" );
        session.ok();
    }

    public final String getUrl()
    {
        return url;
    }

    @Argument
    public final void setUrl( String url )
    {
        this.url = url;
    }
}
