package org.cyclopsgroup.jmxterm;

import org.cyclopsgroup.jcli.annotation.Option;

/**
 * Base class of all commands. Command is executed in single thread. Extending classes don't need to worry about
 * concurrency.
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class Command
{
    private boolean help;

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

    /**
     * Execute command logic
     * 
     * @param session Current command line session
     * @throws Exception Allow any exception
     */
    public abstract void execute( Session session )
        throws Exception;
}
