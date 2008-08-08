package org.cyclopsgroup.jmxterm.boot;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import jline.ConsoleReader;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.jccli.JakartaCommonsCliParser;
import org.cyclopsgroup.jmxterm.SyntaxUtils;
import org.cyclopsgroup.jmxterm.impl.CommandCenter;
import org.cyclopsgroup.jmxterm.impl.ConsoleCompletor;

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
            commandCenter.connect( SyntaxUtils.getUrl( options.getUrl() ), null );
        }

        InputStream input;
        boolean closeInputFinally;
        if ( options.getInput().equals( MainOptions.STDIN ) )
        {
            input = System.in;
            closeInputFinally = false;
        }
        else
        {
            input = new FileInputStream( new File( options.getInput() ) );
            closeInputFinally = true;
        }
        try
        {
            ConsoleReader console = new ConsoleReader( input, new OutputStreamWriter( System.out ) );
            console.addCompletor( new ConsoleCompletor( commandCenter ) );
            String line;
            while ( ( line = console.readLine( "$ " ) ) != null )
            {
                commandCenter.execute( line );
                if ( commandCenter.isClosed() )
                {
                    break;
                }
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
