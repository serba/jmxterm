package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.List;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;


public class ConnectCommand
    implements Command
{
    public void execute( List<String> args, Session session )
        throws IOException
    {
        Validate.isTrue( args.size() == 1, "Only one parameter is expected" );
        String url = args.get( 0 );
        JMXServiceURL u = new JMXServiceURL( url );
        JMXConnector connector = JMXConnectorFactory.connect( u );
        session.setConnector( connector );
    }
}
