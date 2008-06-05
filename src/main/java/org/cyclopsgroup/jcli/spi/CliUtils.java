package org.cyclopsgroup.jcli.spi;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;

public final class CliUtils
{
    private static final Log LOG = LogFactory.getLog( CliUtils.class );

    private static final String ERROR_MESSAGE = "Couldn't set value %s to %s#%s";

    private CliUtils()
    {
    }

    private static <A extends Annotation> A getAnnotation( PropertyDescriptor descriptor, Class<A> type )
    {
        A a = null;
        if ( descriptor.getWriteMethod() != null )
        {
            a = descriptor.getWriteMethod().getAnnotation( type );
        }
        if ( a == null && descriptor.getReadMethod() != null )
        {
            a = descriptor.getReadMethod().getAnnotation( type );
        }
        return a;
    }

    public static CliDefinition defineCli( Class<?> beanType )
        throws IntrospectionException
    {
        Cli cliAnnotation = beanType.getAnnotation( Cli.class );
        Validate.notNull( cliAnnotation, "Type has to be annotated with @Cli" );
        BeanInfo beanInfo = Introspector.getBeanInfo( beanType );
        CliDefinition cliDefinition = new CliDefinition( cliAnnotation );
        for ( PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors() )
        {
            Method writer = descriptor.getWriteMethod();
            if ( writer == null )
            {
                continue;
            }
            Option option = getAnnotation( descriptor, Option.class );
            if ( option != null )
            {
                OptionDefinition def = new OptionDefinition( option, descriptor );
                cliDefinition.addOption( def );
            }
            Argument argument = getAnnotation( descriptor, Argument.class );
            if ( argument != null )
            {
                cliDefinition.setArgument( new ArgumentDefinition( argument, descriptor ) );
            }
        }
        return cliDefinition;
    }

    public static void setValue( Object bean, PropertyDescriptor prop, String value )
    {
        Object v;
        if ( prop.getPropertyType() == String.class )
        {
            v = value;
        }
        else
        {
            v = ConvertUtils.convert( value, prop.getPropertyType() );
        }
        setValue( bean, prop, v );
    }

    public static void setValue( Object bean, PropertyDescriptor prop, Object value )
    {
        try
        {
            prop.getWriteMethod().invoke( bean, value );
        }
        catch ( IllegalArgumentException e )
        {
            LOG.warn( String.format( ERROR_MESSAGE, value, bean, prop ) );
        }
        catch ( IllegalAccessException e )
        {
            LOG.warn( String.format( ERROR_MESSAGE, value, bean, prop ) );
        }
        catch ( InvocationTargetException e )
        {
            LOG.warn( String.format( ERROR_MESSAGE, value, bean, prop ) );
        }
    }

    public static void setValues( Object bean, PropertyDescriptor prop, String[] values )
    {
        Class<?> type = prop.getPropertyType();
        Object v;
        if ( prop.getPropertyType().isArray() )
        {
            if ( type.getComponentType() == String.class )
            {
                v = values;
            }
            else
            {
                Object[] valueArray = new Object[values.length];
                for ( int i = 0; i < values.length; i++ )
                {
                    valueArray[i] = ConvertUtils.convert( values[i], type.getComponentType() );
                }
                v = valueArray;
            }
        }
        else if ( List.class.isAssignableFrom( type ) )
        {
            v = Arrays.asList( values );
        }
        else
        {
            throw new IllegalStateException( "Argument type " + type + " is illegal" );
        }
        setValue( bean, prop, v );
    }
}
