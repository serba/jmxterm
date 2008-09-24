package org.cyclopsgroup.jmxterm.boot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnector;

import jline.ConsoleReader;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.StringUtils;
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
     * @throws Exception
     */
    public static final void main( String[] args )
        throws Exception
    {
        System.exit( new Main().execute( args ) );
    }

    int execute( String[] args )
        throws Exception
    {
        MainOptions options = new MainOptions();
        JakartaCommonsCliParser parser = new JakartaCommonsCliParser( new GnuParser() );
        parser.parse( args, options );
        if ( options.isHelp() )
        {
            parser.printUsage( MainOptions.class, STDOUT_WRITER );
            return 0;
        }
        commandCenter.setAbbreviated( options.isAbbreviated() );

        Writer output;
        if ( StringUtils.equals( options.getOutput(), MainOptions.STDOUT ) )
        {
            output = new OutputStreamWriter( System.out )
            {
                @Override
                public void close()
                    throws IOException
                {
                }
            };
        }
        else
        {
            File outputFile = new File( options.getOutput() );
            output = new FileWriter( outputFile );
        }

        ConsoleReader consoleReader = new ConsoleReader( System.in, output );
        consoleReader.addCompletor( new ConsoleCompletor( commandCenter ) );

        if ( options.getUrl() != null )
        {
            Map<String, Object> env;
            if ( options.getUser() != null )
            {
                env = new HashMap<String, Object>( 1 );
                String password = options.getPassword();
                if ( password == null )
                {
                    password = consoleReader.readLine( "password:", '*' );
                }
                String[] credentials = { options.getUser(), password };
                env.put( JMXConnector.CREDENTIALS, credentials );
            }
            else
            {
                env = null;
            }
            commandCenter.connect( SyntaxUtils.getUrl( options.getUrl() ), env );
        }

        CommandInput input;
        if ( options.getInput().equals( MainOptions.STDIN ) )
        {
            input = new JlineCommandInput( consoleReader, "$>" );
        }
        else
        {
            input = new FileCommandInput( new File( options.getInput() ) );
        }
        try
        {
            String line;
            int exitCode = 0;
            int lineNumber = 0;
            while ( ( line = input.readLine() ) != null )
            {
                lineNumber++;
                if ( !commandCenter.execute( line ) && options.isExitOnFailure() )
                {
                    exitCode = -lineNumber;
                    break;
                }
                if ( commandCenter.isClosed() )
                {
                    break;
                }
            }
            commandCenter.close();
            return exitCode;
        }
        finally
        {
            output.flush();
            output.close();
            input.close();
        }
    }
}
