package org.cyclopsgroup.jmxterm;

import java.io.IOException;

import javax.management.JMException;

import org.cyclopsgroup.jcli.annotation.Option;

/**
 * Base class of all commands. Command is executed in single thread. Extending classes don't need to worry about
 * concurrency. Command is transient, every command in console creates a new instance of Command object which is
 * disposed after execution finishes.
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class Command
{
    private boolean help;

    /**
     * Execute command logic
     * 
     * @param session Current console session
     * @throws IOException IOException is allowed
     * @throws JMException JMException can be caused by user's mistake
     */
    public abstract void execute( Session session )
        throws IOException, JMException;

    /**
     * @see #setHelp(boolean)
     */
    public final boolean isHelp()
    {
        return help;
    }

    /**
     * @param help True to display usage
     */
    @Option( name = "h", longName = "help", description = "Display usage" )
    public final void setHelp( boolean help )
    {
        this.help = help;
    }
}
