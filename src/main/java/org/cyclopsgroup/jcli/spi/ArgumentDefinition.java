package org.cyclopsgroup.jcli.spi;

import java.beans.PropertyDescriptor;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;

/**
 * Definition of an argument
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class ArgumentDefinition
{
    private final Argument argument;

    private final PropertyDescriptor descriptor;

    /**
     * @param argument Argument annotation
     * @param descriptor Descriptor of argument property
     */
    public ArgumentDefinition( Argument argument, PropertyDescriptor descriptor )
    {
        Validate.notNull( argument, "argument can't be NULL" );
        Validate.notNull( descriptor, "descriptor can't be NULL" );
        this.argument = argument;
        this.descriptor = descriptor;
    }

    /**
     * @return Argument annotation
     */
    public final Argument getArgument()
    {
        return argument;
    }

    /**
     * @return Descriptor of argument property
     */
    public final PropertyDescriptor getDescriptor()
    {
        return descriptor;
    }
}
