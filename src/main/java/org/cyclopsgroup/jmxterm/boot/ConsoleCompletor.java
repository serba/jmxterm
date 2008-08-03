package org.cyclopsgroup.jmxterm.boot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jline.Completor;
import jline.ConsoleReader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.impl.CommandCenter;

/**
 * JLine completor that handles tab key
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class ConsoleCompletor
    implements Completor
{
    private final CommandCenter commandCenter;

    private final ConsoleReader reader;

    private final List<String> commandNames;

    private Command currentCommand;

    /**
     * Set current command object
     * 
     * @param currentCommand
     */
    public final void setCurrentCommand( Command currentCommand )
    {
        this.currentCommand = currentCommand;
    }

    ConsoleCompletor( CommandCenter commandCenter, ConsoleReader reader )
    {
        Validate.notNull( commandCenter, "Command center can't be NULL" );
        Validate.notNull( reader, "Console reader can't be NULL" );
        this.commandCenter = commandCenter;
        this.reader = reader;
        List<String> commandNames = new ArrayList<String>( commandCenter.getCommandNames() );
        Collections.sort( commandNames );
        this.commandNames = Collections.unmodifiableList( commandNames );
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings( "unchecked" )
    public int complete( String buffer, int position, List candidates )
    {
        String buf = StringUtils.trimToNull( buffer );
        try
        {
            if ( buf == null || buf.indexOf( ' ' ) == -1 )
            {
                return completeCommandName( buf, candidates );
            }
            return 0;
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    private int completeCommandName( String buf, List<String> candidates )
        throws IOException
    {
        if ( buf == null )
        {
            // Nothing is there
            candidates.addAll( commandNames );
        }
        else
        {
            // Partial one word
            List<String> matchedNames = new ArrayList<String>();
            for ( String commandName : commandNames )
            {
                if ( commandName.startsWith( buf ) )
                {
                    matchedNames.add( commandName );
                }
            }
            if ( matchedNames.isEmpty() )
            {
                return 0;
            }
            else if ( matchedNames.size() == 1 )
            {
                String commandName = matchedNames.get( 0 );
                String remaining = commandName.substring( buf.length() ) + " ";
                reader.putString( remaining );
                return remaining.length();
            }
            else
            {
                candidates.addAll( matchedNames );
            }
        }
        return 0;
    }
}
