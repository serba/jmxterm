package org.cyclopsgroup.jmxterm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * Manages command history
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public final class CommandHistoryManager
{
    private final LinkedList<String> history = new LinkedList<String>();

    private int historySize = 100;

    private final List<String> readOnlyHistory = Collections.unmodifiableList( history );

    /**
     * Append a command
     * 
     * @param command Command to append
     */
    public void append( String command )
    {
        command = StringUtils.trimToNull( command );
        if ( command == null || command.charAt( 0 ) == '#' )
        {
            return;
        }
        if ( !history.isEmpty() )
        {
            String previous = history.getLast();
            if ( previous.equals( command ) )
            {
                return;
            }
        }
        history.addLast( command );
        while ( history.size() > historySize )
        {
            history.removeFirst();
        }
    }

    /**
     * @return Command history
     */
    public List<String> getHistory()
    {
        return readOnlyHistory;
    }

    /**
     * @return Max size of command history
     */
    public int getHistorySize()
    {
        return historySize;
    }

    /**
     * @param historySize Max size of command history
     */
    public void setHistorySize( int historySize )
    {
        Validate.isTrue( historySize > 0, "Invalid history size " + historySize );
        this.historySize = historySize;
    }
}
