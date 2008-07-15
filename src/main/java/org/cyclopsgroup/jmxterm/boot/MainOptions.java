package org.cyclopsgroup.jmxterm.boot;

import java.io.File;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;

/**
 * Options for main class
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "jmxterm", description = "Main executable of JMX terminal CLI tool", note = "Without any option, this command opens an interactive command line based console. With a given input file, commands in file will be executed and process ends after file is processed" )
public class MainOptions
{
    public static final String STDERR = "stderr";

    public static final String STDIN = "stdin";

    public static final String STDOUT = "stdout";

    private boolean abbreviated;

    private boolean help;

    private String input = STDIN;

    private String output = STDOUT;

    private String url;

    /**
     * @see #setInput(String)
     */
    public final String getInput()
    {
        return input;
    }

    /**
     * @see #setOutput(String)
     */
    public final String getOutput()
    {
        return output;
    }

    /**
     * @see #setUrl(String)
     */
    public final String getUrl()
    {
        return url;
    }

    public final boolean isAbbreviated()
    {
        return abbreviated;
    }

    public final boolean isHelp()
    {
        return help;
    }

    @Option( name = "a", longName = "abbreviated", description = "Flag for printing abbreviated version" )
    public final void setAbbreviated( boolean abbreviated )
    {
        this.abbreviated = abbreviated;
    }

    @Option( name = "h", longName = "help", description = "Show usage of this command line" )
    public final void setHelp( boolean help )
    {
        this.help = help;
    }

    /**
     * @param file Input script path or <code>stdin</code> as default value for console input
     */
    @Argument( description = "Input script file. There can only be one input file. \"stdin\" is the default value which means console input" )
    public final void setInput( String file )
    {
        Validate.notNull( file, "Input file can't be NULL" );
        Validate.isTrue( new File( file ).isFile(), "File " + file + " doesn't exist" );
        this.input = file;
    }

    @Option( name = "o", longName = "output", description = "Output file" )
    public final void setOutput( String outputFile )
    {
        Validate.notNull( outputFile, "Output file can't be NULL" );
        this.output = outputFile;
    }

    @Option( name = "u", longName = "url", description = "JMX connection URL" )
    public final void setUrl( String url )
    {
        Validate.notNull( url, "URL can't be NULL" );
        this.url = url;
    }
}
