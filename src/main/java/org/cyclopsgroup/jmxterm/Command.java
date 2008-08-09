package org.cyclopsgroup.jmxterm;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
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

    private Session session;

    /**
     * Execute command
     * 
     * @throws Exception Allow to throw anything
     */
    public abstract void execute()
        throws Exception;

    /**
     * Get candidates of input values
     * 
     * @param optionName Name of option or NULL for arguments
     * @param session Current session
     * @return List of candidates
     */
    public List<String> getInputCandidate( String optionName, Session session )
    {
        return Collections.emptyList();
    }

    /**
     * @return Session where command runs
     */
    public final Session getSession()
    {
        return session;
    }

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

    /**
     * @param session Session where command runs
     */
    public final void setSession( Session session )
    {
        Validate.notNull( session, "Session can't be NULL" );
        this.session = session;
    }
}
