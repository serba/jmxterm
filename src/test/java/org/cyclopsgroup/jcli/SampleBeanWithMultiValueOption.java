package org.cyclopsgroup.jcli;

import java.util.List;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;

@Cli( name = "sample" )
public class SampleBeanWithMultiValueOption
{
    private List<String> options;

    public final List<String> getOptions()
    {
        return options;
    }

    @Option( name = "o", longName = "option", description = "Multi value option" )
    public final void setOptions( List<String> options )
    {
        this.options = options;
    }
}
