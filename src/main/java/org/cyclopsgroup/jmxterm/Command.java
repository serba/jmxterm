package org.cyclopsgroup.jmxterm;

import org.cyclopsgroup.jcli.annotation.Option;

public abstract class Command
{
    private boolean help;

    public final boolean isHelp()
    {
        return help;
    }

    @Option( name = "h", longName = "help", description = "Display usage" )
    public final void setHelp( boolean help )
    {
        this.help = help;
    }

    /**
     * Executable command
     * 
     * @param args Arguments
     * @param session Current command line session
     * @throws Exception Allow any exception
     */
    public abstract void execute( Session session )
        throws Exception;
}
