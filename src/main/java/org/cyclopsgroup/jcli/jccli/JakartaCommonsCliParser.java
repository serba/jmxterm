package org.cyclopsgroup.jcli.jccli;

import java.beans.IntrospectionException;
import java.io.PrintWriter;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.CliParser;
import org.cyclopsgroup.jcli.annotation.MalformedArgException;
import org.cyclopsgroup.jcli.spi.ArgumentDefinition;
import org.cyclopsgroup.jcli.spi.CliDefinition;
import org.cyclopsgroup.jcli.spi.CliUtils;
import org.cyclopsgroup.jcli.spi.OptionDefinition;

/**
 * Implementation of {@link CliParser} that uses a given Jakarta Commons CLI {@link Parser}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class JakartaCommonsCliParser
    implements CliParser
{
    private static CliDefinition defineBean( Class<?> beanType, Options options )
        throws IntrospectionException
    {
        CliDefinition cliDefinition = CliUtils.defineCli( beanType );
        for ( OptionDefinition def : cliDefinition.getOptions().values() )
        {
            Option opt =
                new Option( def.getName(), def.getOption().longName(), !def.isFlag(), "  "
                    + def.getOption().description() );
            opt.setRequired( def.getOption().required() );
            opt.setOptionalArg( !def.getOption().required() );
            opt.setArgName( def.getOption().displayName() );
            opt.setType( def.getDescriptor().getPropertyType() );
            options.addOption( opt );
        }
        return cliDefinition;
    }

    private final Parser jcParser;

    private int width = 80;

    /**
     * Constructor with given Jakarta Commons CLI parser
     * 
     * @param jcParser Jakarta commons CLI parser implementation
     */
    public JakartaCommonsCliParser( Parser jcParser )
    {
        Validate.notNull( jcParser, "jcParser can't be NULL" );
        this.jcParser = jcParser;
    }

    /**
     * @return Width of text box where usage is printed
     */
    public final int getWidth()
    {
        return width;
    }

    /**
     * @inheritDoc
     */
    public boolean parse( String[] args, Object bean )
        throws IntrospectionException
    {
        Validate.notNull( args, "Arg list can't be NULL" );
        Options options = new Options();
        CliDefinition cliDefinition = defineBean( bean.getClass(), options );

        CommandLine cli;
        try
        {
            cli = jcParser.parse( options, args );
        }
        catch ( ParseException e )
        {
            throw new MalformedArgException( "Apache Jakarta commons-cli couldn't parse arguments "
                + Arrays.asList( args ), e );
        }
        boolean changed = false;
        for ( OptionDefinition def : cliDefinition.getOptions().values() )
        {
            if ( def.isMultiValue() )
            {
                // If argument can take multi value, assume input is multi value and assign it to option
                String[] values = cli.getOptionValues( def.getName() );
                CliUtils.setValues( bean, def.getDescriptor(), values );
                changed = true;
                continue;
            }
            else
            {
                // If argument can not take multi value but input is multi value, throw MalfomedArgException
                String[] values = cli.getOptionValues( def.getName() );
                if ( values != null && values.length > 1 )
                {
                    throw new MalformedArgException( "Option " + def + " can only take 1 value while " + values.length
                        + " are assigned" );
                }
            }
            if ( def.isFlag() )
            {
                // If option is a boolean flag, check if option is specified
                CliUtils.setValue( bean, def.getDescriptor(), cli.hasOption( def.getName() ) );
                changed = true;
                continue;
            }
            if ( def.getOption().required() && !cli.hasOption( def.getName() ) )
            {
                // If option is required but not provided, throw MalformedArgException
                throw new MalformedArgException( "Required option " + def.getName() + " is missing" );
            }
            if ( !cli.hasOption( def.getName() ) && StringUtils.isEmpty( def.getOption().defaultValue() ) )
            {
                // If option isn't specified, and there's no default value defined in option, skip it
                continue;
            }
            String value = cli.getOptionValue( def.getName(), def.getOption().defaultValue() );
            CliUtils.setValue( bean, def.getDescriptor(), value );
            changed = true;
        }
        // Assign the rest of arguments to @Argment annotated field
        ArgumentDefinition argDef = cliDefinition.getArgument();
        if ( argDef != null )
        {
            if ( argDef.getArgument().requires() > 0 && cli.getArgs().length < argDef.getArgument().requires() )
            {
                throw new MalformedArgException( argDef.getArgument().requires()
                    + " arguments are required while there are only " + cli.getArgs().length );
            }
            changed = true;
            CliUtils.setValues( bean, cliDefinition.getArgument().getDescriptor(), cli.getArgs() );
        }
        return changed;
    }

    /**
     * @inheritDoc
     */
    public void printUsage( Class<?> beanType, PrintWriter output )
        throws IntrospectionException
    {
        Options options = new Options();
        Cli cli = defineBean( beanType, options ).getCli();
        HelpFormatter format = new HelpFormatter();
        format.printHelp( output, width, cli.name(), cli.description(), options, 2, 0, cli.note(), true );
    }

    /**
     * @param width Width of text box where usage is printed
     */
    public final void setWidth( int width )
    {
        Validate.isTrue( width > 0, "Invalid width " + width );
        this.width = width;
    }
}
