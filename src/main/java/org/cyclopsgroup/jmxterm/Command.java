package org.cyclopsgroup.jmxterm;

import java.util.List;

public interface Command
{
    /**
     * Executable command
     * 
     * @param args Arguments
     * @param session Current command line session
     * @throws Exception Allow any exception
     */
    void execute( List<String> args, Session session )
        throws Exception;
}
