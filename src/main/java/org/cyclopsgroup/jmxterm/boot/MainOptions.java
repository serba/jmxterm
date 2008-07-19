package org.cyclopsgroup.jmxterm.boot;

import java.io.File;

import org.apache.commons.lang.Validate;
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
    /**
     * Constant <code>stderr</code> that identifies standard error output
     */
    public static final String STDERR = "stderr";

    /**
     * Constant <code>stdin</code> that identifies standard input
     */
    public static final String STDIN = "stdin";

    /**
     * Constant <code>stdout</code> that identifies standard output
     */
    public static final String STDOUT = "stdout";

    private boolean abbreviated;

    private boolean help;

    private String input = STDIN;

    private String output = STDOUT;

    private String url;

    /**
     * @return #setInput(String)
     */
    public final String getInput()
    {
        return input;
    }

    /**
     * @return #setOutput(String)
     */
    public final String getOutput()
    {
        return output;
    }

    /**
     * @return #setUrl(String)
     */
    public final String getUrl()
    {
        return url;
    }

    /**
     * @return {@link #setAbbreviated(boolean)}
     */
    public final boolean isAbbreviated()
    {
        return abbreviated;
    }

    /**
     * @return {@link #setHelp(boolean)}
     */
    public final boolean isHelp()
    {
        return help;
    }

    /**
     * @param abbreviated True to turn on <code>abbreviated</code> flag
     */
    @Option( name = "a", longName = "abbreviated", description = "Flag for printing abbreviated version" )
    public final void setAbbreviated( boolean abbreviated )
    {
        this.abbreviated = abbreviated;
    }

    /**
     * @param help True to turn on <code>help</code> flag
     */
    @Option( name = "h", longName = "help", description = "Show usage of this command line" )
    public final void setHelp( boolean help )
    {
        this.help = help;
    }

    /**
     * @param file Input script path or <code>stdin</code> as default value for console input
     */
    @Option( name = "i", longName = "input", description = "Input script file. There can only be one input file. \"stdin\" is the default value which means console input" )
    public final void setInput( String file )
    {
        Validate.notNull( file, "Input file can't be NULL" );
        Validate.isTrue( new File( file ).isFile(), "File " + file + " doesn't exist" );
        this.input = file;
    }

    /**
     * @param outputFile It can be a file or {@link #STDERR} or {@link #STDERR}
     */
    @Option( name = "o", longName = "output", description = "Output file, stdout or stderr. Default value is stdout" )
    public final void setOutput( String outputFile )
    {
        Validate.notNull( outputFile, "Output file can't be NULL" );
        this.output = outputFile;
    }

    /**
     * @param url MBean server URL
     */
    @Option( name = "u", longName = "url", description = "JMX connection URL" )
    public final void setUrl( String url )
    {
        Validate.notNull( url, "URL can't be NULL" );
        this.url = url;
    }
}
