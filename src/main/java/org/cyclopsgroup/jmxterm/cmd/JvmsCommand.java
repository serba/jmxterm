package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;

import javax.management.JMException;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.JavaProcessManager;

/**
 * Command to list all running local JVM processes
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "jvms", description = "List all running local JVM processes" )
public class JvmsCommand
    extends Command
{
    /**
     * @inheritDoc
     */
    @Override
    public void execute()
        throws IOException, JMException
    {
        JavaProcessManager jpm = JavaProcessManager.getInstance();
        for ( JavaProcess p : jpm.list() )
        {
            getSession().msg(
                              String.format( "%-8d (%s) - %s", p.getProcessId(), p.isManageable() ? "m" : " ",
                                             p.getDisplayName() ), String.valueOf( p.getProcessId() ) );
        }
    }
}
