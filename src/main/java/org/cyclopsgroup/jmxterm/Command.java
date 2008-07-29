package org.cyclopsgroup.jmxterm;

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
     * @throws Exception Allow any exception
     */
    public abstract void execute( Session session )
        throws Exception;

    /**
     * @return True if help option is on
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
