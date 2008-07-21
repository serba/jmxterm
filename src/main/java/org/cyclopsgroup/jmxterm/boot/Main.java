package org.cyclopsgroup.jmxterm.boot;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.jccli.JakartaCommonsCliParser;
import org.cyclopsgroup.jmxterm.impl.CommandCenter;

/**
 * Main class invoked directly from command line
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Main
{
    private static final PrintWriter STDOUT_WRITER = new PrintWriter( System.out, true );

    private final CommandCenter commandCenter;

    Main( CommandCenter commandCenter )
    {
        Validate.notNull( commandCenter, "CommandCenter can't be NULL" );
        this.commandCenter = commandCenter;
    }

    private Main()
        throws IOException, ClassNotFoundException
    {
        this( new CommandCenter( STDOUT_WRITER ) );
    }

    /**
     * Main entry
     * 
     * @param args Main command
     * @throws IntrospectionException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static final void main( String[] args )
        throws IntrospectionException, IOException, ClassNotFoundException
    {
        Main main = new Main();
        main.execute( args );
    }

    void execute( String[] args )
        throws IntrospectionException, IOException
    {
        MainOptions options = new MainOptions();
        JakartaCommonsCliParser parser = new JakartaCommonsCliParser( new GnuParser() );
        parser.parse( args, options );
        if ( options.isHelp() )
        {
            parser.printUsage( MainOptions.class, STDOUT_WRITER );
            return;
        }
        commandCenter.setAbbreviated( options.isAbbreviated() );
        if ( options.getUrl() != null )
        {
            commandCenter.connect( options.getUrl(), null );
        }

        Reader input;
        boolean closeInputFinally;
        if ( options.getInput().equals( MainOptions.STDIN ) )
        {
            input = new InputStreamReader( System.in );
            closeInputFinally = false;
        }
        else
        {
            input = new FileReader( new File( options.getInput() ) );
            closeInputFinally = true;
        }
        try
        {
            LineNumberReader in = new LineNumberReader( input );
            commandCenter.prompt();
            String line = in.readLine();
            while ( line != null )
            {
                commandCenter.execute( line );
                if ( commandCenter.isClosed() )
                {
                    break;
                }
                commandCenter.prompt();
                line = in.readLine();
            }
            commandCenter.close();
        }
        finally
        {
            if ( closeInputFinally )
            {
                input.close();
            }
        }
    }
}
