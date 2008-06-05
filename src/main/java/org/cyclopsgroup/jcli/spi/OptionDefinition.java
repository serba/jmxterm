package org.cyclopsgroup.jcli.spi;

import java.beans.PropertyDescriptor;
import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Option;

public class OptionDefinition
{
    private final PropertyDescriptor descriptor;

    private final boolean flag;

    private final boolean multiValue;

    private final Option option;

    public OptionDefinition( Option option, PropertyDescriptor descriptor )
    {
        Validate.notNull( option, "Option can't be NULL" );
        Validate.notNull( descriptor, "Descriptor can't be NULL" );
        this.option = option;
        this.descriptor = descriptor;
        Class<?> type = descriptor.getPropertyType();
        this.flag = type == Boolean.TYPE || type == Boolean.class;
        this.multiValue = Collection.class.isAssignableFrom( type ) || type.isArray();
    }

    public final PropertyDescriptor getDescriptor()
    {
        return descriptor;
    }

    public String getName()
    {
        return option.name();
    }

    public final Option getOption()
    {
        return option;
    }

    public final boolean isFlag()
    {
        return flag;
    }

    public final boolean isMultiValue()
    {
        return multiValue;
    }
}
