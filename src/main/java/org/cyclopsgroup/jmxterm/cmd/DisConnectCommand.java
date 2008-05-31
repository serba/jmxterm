package org.cyclopsgroup.jmxterm.cmd;

import java.util.List;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;


public class DisConnectCommand
    implements Command
{

    /**
     * @inheritDoc
     */
    public void execute( List<String> args, Session session )
        throws Exception
    {
        session.unsetConnector();
    }

}
