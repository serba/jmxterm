package org.cyclopsgroup.jmxterm.cmd;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cyclopsgroup.jcli.AutoCompletable;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.MalformedArgException;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command to change/display console options
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "option", description = "Set options for command session" )
public class OptionCommand
    extends Command
    implements AutoCompletable
{
    private static final List<String> BOOLEAN_VALUES =
        Collections.unmodifiableList( Arrays.asList( Boolean.TRUE.toString(), Boolean.FALSE.toString() ) );

    private static Boolean toBoolean( String value )
    {
        if ( value == null )
        {
            return null;
        }
        if ( value.equalsIgnoreCase( Boolean.toString( true ) ) )
        {
            return Boolean.TRUE;
        }
        else if ( value.equalsIgnoreCase( Boolean.toString( false ) ) )
        {
            return Boolean.FALSE;
        }
        else
        {
            throw new MalformedArgException( "Boolean option value has to be true|false, " + value + " is invalid" );
        }
    }

    private String abbreviated;

    private String verbose;

    /**
     * @inheritDoc
     */
    public List<String> doSuggestOption( String name )
    {
        return BOOLEAN_VALUES;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute()
    {
        Session session = getSession();
        Boolean v = toBoolean( verbose );
        if ( v != null )
        {
            session.setVerbose( v );
            session.output.printMessage( "verbose option is turned " + ( v ? "on" : "off" ) );
        }
        else
        {
            session.output.printMessage( "no change for verbose, verbose = " + session.isVerbose() );
        }

        Boolean a = toBoolean( abbreviated );
        if ( a != null )
        {
            session.setAbbreviated( a );
            session.output.printMessage( "abbreviated option is turned " + ( a ? "on" : "off" ) );
        }
        else
        {
            session.output.printMessage( "no change for abbreviated, abbreviated = " + session.isAbbreviated() );
        }
    }

    /**
     * @param abbreviated Option <code>abbreviated</code>
     */
    @Option( name = "a", longName = "abbreviated", description = "true|false" )
    public final void setAbbreviated( String abbreviated )
    {
        this.abbreviated = abbreviated;
    }

    /**
     * @param verbose Verbose level of session
     */
    @Option( name = "v", longName = "verbose", description = "true|false" )
    public final void setVerbose( String verbose )
    {
        this.verbose = verbose;
    }
}
