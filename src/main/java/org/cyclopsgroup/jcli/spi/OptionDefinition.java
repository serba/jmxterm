package org.cyclopsgroup.jcli.spi;

import java.beans.PropertyDescriptor;
import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Option;

/**
 * Definition of an option
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class OptionDefinition
{
    private final PropertyDescriptor descriptor;

    private final boolean flag;

    private final boolean multiValue;

    private final Option option;

    /**
     * @param option Option deifinition
     * @param descriptor PropertyDescriptor for property associated to this option
     */
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

    /**
     * @return PrpertyDescriptor for property associated to this option
     */
    public final PropertyDescriptor getDescriptor()
    {
        return descriptor;
    }

    /**
     * @return Option name
     */
    public String getName()
    {
        return option.name();
    }

    /**
     * @return Option annotation
     */
    public final Option getOption()
    {
        return option;
    }

    /**
     * @return True if option is a boolean type
     */
    public final boolean isFlag()
    {
        return flag;
    }

    /**
     * @return True if option takes multiple values
     */
    public final boolean isMultiValue()
    {
        return multiValue;
    }
}
