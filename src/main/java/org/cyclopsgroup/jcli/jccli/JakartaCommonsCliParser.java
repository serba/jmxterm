package org.cyclopsgroup.jcli.jccli;

import java.beans.IntrospectionException;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jcli.annotation.CliParser;
import org.cyclopsgroup.jcli.spi.CliDefinition;
import org.cyclopsgroup.jcli.spi.CliUtils;
import org.cyclopsgroup.jcli.spi.OptionDefinition;

public class JakartaCommonsCliParser
    implements CliParser
{
    private static final Log LOG = LogFactory.getLog( JakartaCommonsCliParser.class );

    private static CliDefinition defineBean( Class<?> beanType, Options options )
        throws IntrospectionException
    {
        CliDefinition cliDefinition = CliUtils.defineCli( beanType );
        for ( OptionDefinition def : cliDefinition.getOptions().values() )
        {
            Option opt =
                new Option( def.getName(), def.getOption().longName(), !def.isFlag(), def.getOption().description() );
            opt.setRequired( def.getOption().required() );
            opt.setOptionalArg( !def.getOption().required() );
            opt.setArgName( def.getOption().displayName() );
            opt.setType( def.getDescriptor().getPropertyType() );
            options.addOption( opt );
        }
        return cliDefinition;
    }

    private final Parser jcParser;

    public JakartaCommonsCliParser( Parser jcParser )
    {
        this.jcParser = jcParser;
    }

    public boolean parse( String[] args, Object bean )
        throws IntrospectionException
    {
        Options options = new Options();
        CliDefinition cliDefinition = defineBean( bean.getClass(), options );

        CommandLine cli;
        try
        {
            cli = jcParser.parse( options, args );
        }
        catch ( ParseException e )
        {
            LOG.info( e );
            return false;
        }
        for ( OptionDefinition def : cliDefinition.getOptions().values() )
        {
            if ( def.isFlag() )
            {
                CliUtils.setValue( bean, def.getDescriptor(), cli.hasOption( def.getName() ) );
                continue;
            }
            if ( def.getOption().required() && !cli.hasOption( def.getName() ) )
            {
                LOG.warn( "Required option " + def.getName() + " is missing" );
                return false;
            }
            if ( def.isMultiValue() )
            {
                String[] values = cli.getOptionValues( def.getName() );
                CliUtils.setValues( bean, def.getDescriptor(), values );
                continue;
            }
            if ( !cli.hasOption( def.getName() ) && StringUtils.isEmpty( def.getOption().defaultValue() ) )
            {
                continue;
            }
            String value = cli.getOptionValue( def.getName(), def.getOption().defaultValue() );
            CliUtils.setValue( bean, def.getDescriptor(), value );
        }
        if ( cliDefinition.getArgument() != null )
        {
            CliUtils.setValues( bean, cliDefinition.getArgument().getDescriptor(), cli.getArgs() );
        }
        return true;
    }

    public void printUsage( Class<?> beanType, OutputStream output )
        throws IntrospectionException
    {
        Options options = new Options();
        CliDefinition cli = defineBean( beanType, options );
        HelpFormatter format = new HelpFormatter();
        format.printHelp( cli.getCli().description(), options, true );
    }
}
