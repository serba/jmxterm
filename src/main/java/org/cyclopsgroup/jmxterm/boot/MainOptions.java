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
@Cli( name = "jmxterm", description = "Main executable of JMX terminal CLI tool" )
public final class MainOptions
{
    public static final String STDERR = "stderr";

    public static final String STDIN = "stdin";

    public static final String STDOUT = "stdout";

    private boolean help;

    private String input = STDIN;

    private String output = STDOUT;

    private String url;

    public String getInput()
    {
        return input;
    }

    public String getOutput()
    {
        return output;
    }

    public String getUrl()
    {
        return url;
    }

    public boolean isHelp()
    {
        return help;
    }

    @Option( name = "h", longName = "help", description = "Show usage of this command line" )
    public void setHelp( boolean help )
    {
        this.help = help;
    }

    @Option( name = "i", longName = "input", description = "Input script" )
    public void setInput( String file )
    {
        Validate.notNull( file, "Input file can't be NULL" );
        Validate.isTrue( new File( file ).isFile(), "File " + file + " doesn't exist" );
        this.input = file;
    }

    @Option( name = "o", longName = "output", description = "Output file" )
    public void setOutput( String outputFile )
    {
        Validate.notNull( outputFile, "Output file can't be NULL" );
        this.output = outputFile;
    }

    @Option( name = "u", longName = "url", description = "JMX connection URL" )
    public void setUrl( String url )
    {
        Validate.notNull( url, "URL can't be NULL" );
        this.url = url;
    }
}
