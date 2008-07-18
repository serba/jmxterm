package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;

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
        session.connect( url );
        session.msg( "Connection to " + url + " is opened", SyntaxUtils.OK );

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
