package org.cyclopsgroup.jcli.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Cli;

/**
 * Command level definition
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class CliDefinition
{
    private ArgumentDefinition argument;

    private final Cli cli;

    private final Map<String, OptionDefinition> options = new HashMap<String, OptionDefinition>();

    /**
     * @param cli Command line annotation
     */
    public CliDefinition( Cli cli )
    {
        this.cli = cli;
    }

    /**
     * Add a definition of option
     * 
     * @param opt Option definition to add
     */
    public void addOption( OptionDefinition opt )
    {
        Validate.notNull( opt, "Option can't be NULL" );
        options.put( opt.getName(), opt );
    }

    /**
     * @return Definition of argument
     */
    public final ArgumentDefinition getArgument()
    {
        return argument;
    }

    /**
     * @return Command line annotation
     */
    public final Cli getCli()
    {
        return cli;
    }

    /**
     * @return Map of option definitions
     */
    public final Map<String, OptionDefinition> getOptions()
    {
        return Collections.unmodifiableMap( options );
    }

    /**
     * @param argument Argument definition to set
     */
    public final void setArgument( ArgumentDefinition argument )
    {
        this.argument = argument;
    }
}
