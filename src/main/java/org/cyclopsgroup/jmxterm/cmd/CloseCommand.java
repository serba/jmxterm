package org.cyclopsgroup.jmxterm.cmd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.Session;

@Cli( name = "close", description = "Close JMX session" )
public class CloseCommand
    extends Command
{
    private static final Log LOG = LogFactory.getLog( CloseCommand.class );

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
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
