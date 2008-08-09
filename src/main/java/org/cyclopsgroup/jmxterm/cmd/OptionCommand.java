package org.cyclopsgroup.jmxterm.cmd;

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
{
    private String verbose;

    private String abbreviated;

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
            throw new MalformedArgException( "Boolean option value has to be yes|no, " + value + " is invalid" );
        }
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
            session.msg( "verbose option is turned " + ( v ? "on" : "off" ) );
        }
        else
        {
            session.msg( "no change for verbose, verbose = " + ( session.isVerbose() ? "yes" : "no" ) );
        }

        Boolean a = toBoolean( abbreviated );
        if ( a != null )
        {
            session.setAbbreviated( a );
            session.msg( "abbreviated option is turned " + ( a ? "on" : "off" ) );
        }
        else
        {
            session.msg( "no change for abbreviated, abbreviated = " + ( session.isAbbreviated() ? "yes" : "no" ) );
        }
        session.ok();
    }
}
