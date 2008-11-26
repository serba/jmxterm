package org.cyclopsgroup.jmxterm.boot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnector;

import jline.ConsoleReader;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.StringUtils;
import org.cyclopsgroup.jcli.jccli.JakartaCommonsCliParser;
import org.cyclopsgroup.jmxterm.cc.CommandCenter;
import org.cyclopsgroup.jmxterm.cc.ConsoleCompletor;
import org.cyclopsgroup.jmxterm.io.CommandInput;
import org.cyclopsgroup.jmxterm.io.CommandOutput;
import org.cyclopsgroup.jmxterm.io.FileCommandInput;
import org.cyclopsgroup.jmxterm.io.FileCommandOutput;
import org.cyclopsgroup.jmxterm.io.JlineCommandInput;
import org.cyclopsgroup.jmxterm.io.PrintStreamCommandOutput;
import org.cyclopsgroup.jmxterm.utils.SyntaxUtils;

/**
 * Main class invoked directly from command line
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class CliMain
{
    private static final PrintWriter STDOUT_WRITER = new PrintWriter( System.out, true );

    private static final String COMMAND_PROMPT = "$> ";

    /**
     * Main entry
     * 
     * @param args Main command
     * @throws Exception
     */
    public static final void main( String[] args )
        throws Exception
    {
        System.exit( new CliMain().execute( args ) );
    }

    int execute( String[] args )
        throws Exception
    {
        CliMainOptions options = new CliMainOptions();
        JakartaCommonsCliParser parser = new JakartaCommonsCliParser( new GnuParser() );
        parser.parse( args, options );
        if ( options.isHelp() )
        {
            parser.printUsage( CliMainOptions.class, STDOUT_WRITER );
            return 0;
        }
        CommandOutput output;
        if ( StringUtils.equals( options.getOutput(), CliMainOptions.STDOUT ) )
        {
            output = new PrintStreamCommandOutput( System.out, System.err );
        }
        else
        {
            File outputFile = new File( options.getOutput() );
            output = new FileCommandOutput( outputFile );
        }
        try
        {
            CommandInput input;
            if ( options.getInput().equals( CliMainOptions.STDIN ) )
            {
                ConsoleReader consoleReader = new ConsoleReader( System.in, new PrintWriter( System.err, true ) );
                input = new JlineCommandInput( consoleReader, COMMAND_PROMPT );
            }
            else
            {
                File inputFile = new File( options.getInput() );
                if ( !inputFile.isFile() )
                {
                    throw new FileNotFoundException( "File " + inputFile + " is not a valid file" );
                }
                input = new FileCommandInput( new File( options.getInput() ) );
            }
            try
            {
                CommandCenter commandCenter = new CommandCenter( output, input );
                if ( input instanceof JlineCommandInput )
                {
                    ( (JlineCommandInput) input ).getConsole().addCompletor( new ConsoleCompletor( commandCenter ) );
                }
                commandCenter.setAbbreviated( options.isAbbreviated() );
                if ( options.getUrl() != null )
                {
                    Map<String, Object> env;
                    if ( options.getUser() != null )
                    {
                        env = new HashMap<String, Object>( 1 );
                        String password = options.getPassword();
                        if ( password == null )
                        {
                            password = input.readMaskedString( "Authentication password: " );
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
                input.close();
            }
        }
        finally
        {
            output.close();
        }
    }
}
