package org.cyclopsgroup.jcli.spi;

import java.beans.PropertyDescriptor;

import org.cyclopsgroup.jcli.annotation.Argument;

public class ArgumentDefinition
{
    private final Argument argument;

    private final PropertyDescriptor descriptor;

    public ArgumentDefinition( Argument argument, PropertyDescriptor descriptor )
    {
        this.argument = argument;
        this.descriptor = descriptor;
    }

    public final Argument getArgument()
    {
        return argument;
    }

    public final PropertyDescriptor getDescriptor()
    {
        return descriptor;
    }
}
