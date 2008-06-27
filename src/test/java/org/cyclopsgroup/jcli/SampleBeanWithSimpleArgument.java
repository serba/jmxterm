package org.cyclopsgroup.jcli;

import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;

@Cli( name = "sample2" )
public class SampleBeanWithSimpleArgument
{
    private String arg;

    @Argument
    public final void setArg( String arg )
    {
        this.arg = arg;
    }

    public final String getArg()
    {
        return arg;
    }
}
