package org.cyclopsgroup.jmxterm.cmd;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.Session;

public class DisConnectCommand
    implements Command
{
    private static final Log LOG = LogFactory.getLog( DisConnectCommand.class );

    /**
     * @inheritDoc
     */
    public void execute( List<String> args, Session session )
        throws Exception
    {
        Connection connection = session.getConnection();
        session.unsetConnection();
        if ( connection != null )
        {
            connection.getConnector().close();
            LOG.info( "Connection to " + connection.getOriginalUrl() + " is closed" );
        }
    }

}
