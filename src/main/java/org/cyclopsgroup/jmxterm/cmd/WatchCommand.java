package org.cyclopsgroup.jmxterm.cmd;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command to watch an MBean attribute
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "watch", description = "Watch the value of MBean attribute" )
public class WatchCommand
    extends Command
{
    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
    {
        // TODO Auto-generated method stub

    }
}
