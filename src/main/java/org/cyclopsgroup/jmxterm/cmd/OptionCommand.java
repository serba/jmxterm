package org.cyclopsgroup.jmxterm.cmd;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

@Cli( name = "option", description = "Set options for command session" )
public class OptionCommand
    extends Command
{
    private String verbose;

    /**
     * @param verbose Verbose level of session
     */
    @Option( name = "v", longName = "verbose", description = "yes|no" )
    public final void setVerbose( String verbose )
    {
        this.verbose = verbose;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws Exception
    {
        if ( verbose == null )
        {
            session.output.println( "No change, verbose = " + ( session.isDebug() ? "yes" : "no" ) );
            return;
        }
        if ( verbose.equalsIgnoreCase( "yes" ) )
        {
            session.setDebug( true );
            session.output.println( "Verbose is turned on" );
        }
        else if ( verbose.equalsIgnoreCase( "no" ) )
        {
            session.setDebug( false );
            session.output.println( "Verbose if turned off" );
        }
        else
        {
            throw new IllegalArgumentException( "Verbose option value has to be yes|no, " + verbose + " is invalid" );
        }
    }
}
