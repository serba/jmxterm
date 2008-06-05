package org.cyclopsgroup.jcli.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Cli;

public class CliDefinition
{
    private ArgumentDefinition argument;

    private final Cli cli;

    private final Map<String, OptionDefinition> options = new HashMap<String, OptionDefinition>();

    public CliDefinition( Cli cli )
    {
        this.cli = cli;
    }

    public void addOption( OptionDefinition opt )
    {
        Validate.notNull( opt, "Option can't be NULL" );
        options.put( opt.getName(), opt );
    }

    public final ArgumentDefinition getArgument()
    {
        return argument;
    }

    public final Cli getCli()
    {
        return cli;
    }

    public final Map<String, OptionDefinition> getOptions()
    {
        return Collections.unmodifiableMap( options );
    }

    public final void setArgument( ArgumentDefinition argument )
    {
        this.argument = argument;
    }
}
