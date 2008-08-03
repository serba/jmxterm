package org.cyclopsgroup.jmxterm.cmd;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.CommandHistoryManager;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command to show command history or run a command in history
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "history", description = "Show command history or run one of them" )
public class HistoryCommand
    extends Command
{
    private int size = -1;

    /**
     * TODO Change it to List<Integer> once jcli fix the type bug
     */
    private List<String> commandIndex = Collections.emptyList();

    /**
     * Default constructor
     */
    public HistoryCommand()
    {
        super( true );
    }

    /**
     * @param size Max size of history
     */
    @Option( name = "s", longName = "size", description = "Reset max size of command history if this option is specified" )
    public final void setSize( int size )
    {
        this.size = size;
    }

    /**
     * @param commandIndex Index of command in history to run
     */
    @Argument( description = "Index of command in history to run", type = Integer.class )
    public final void setCommandIndex( List<String> commandIndex )
    {
        Validate.notNull( commandIndex, "Index can't be NULL" );
        this.commandIndex = commandIndex;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
    {
        Validate.notNull( session, "Session can't be NULL" );
        CommandHistoryManager chm = session.getCommandHistoryManager();
        if ( size > 0 )
        {
            chm.setHistorySize( size );
        }
        List<String> history = chm.getHistory();
        if ( commandIndex.isEmpty() )
        {
            int i = history.size();
            for ( String command : chm.getHistory() )
            {
                session.output.println( String.format( "%-3d %s", --i, command ) );
            }
        }
        else
        {
            for ( String indexValue : commandIndex )
            {
                int index = Integer.parseInt( indexValue );
                String command = history.get( history.size() - index - 1 );
                session.msg( "Run command #" + index + " from history: " + command );
            }
        }
    }
}
