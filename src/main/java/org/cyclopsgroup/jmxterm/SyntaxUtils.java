package org.cyclopsgroup.jmxterm;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class for syntax checking
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public final class SyntaxUtils
{
    public static final String NULL = "null";

    private static final Pattern PATTERN_HOST_PORT = Pattern.compile( "^(\\w|\\.)+\\:\\d+$" );

    public static JMXServiceURL getUrl( String url )
        throws MalformedURLException
    {
        if ( PATTERN_HOST_PORT.matcher( url ).find() )
        {
            return new JMXServiceURL( "service:jmx:rmi:///jndi/rmi://" + url + "/jmxrmi" );
        }
        return new JMXServiceURL( url );
    }

    /**
     * Check if string value is <code>null</code>
     * 
     * @param s String value
     * @return True if value is <code>null</code>
     */
    public static boolean isNull( String s )
    {
        return StringUtils.equalsIgnoreCase( NULL, s );
    }

    /**
     * Parse given string expression to expected type of value
     * 
     * @param expression String expression
     * @param type Target type
     * @return Object of value
     */
    public static Object parse( String expression, String type )
    {
        if ( StringUtils.isEmpty( expression ) || isNull( expression ) )
        {
            return null;
        }
        Class<?> c;
        try
        {
            c = ClassUtils.getClass( type );
        }
        catch ( ClassNotFoundException e )
        {
            throw new IllegalArgumentException( "Type " + type + " isn't valid", e );
        }
        return ConvertUtils.convert( expression, c );
    }

    /**
     * Print out <code>name = value;</code> expression considering various possible types of value
     * 
     * @param output Output writer where expression is printed
     * @param name Variable name
     * @param value Variable value
     * @param description Description of expression
     * @param indent Starting indentation length
     * @param showDescription True if description needs to be printed
     */
    public static void printExpression( PrintWriter output, Object name, Object value, String description, int indent,
                                        boolean showDescription )
    {
        output.print( StringUtils.repeat( " ", indent ) );
        printValue( output, name, indent, showDescription );
        output.print( " = " );
        printValue( output, value, indent, showDescription );
        output.print( ";" );
        if ( showDescription && description != null )
        {
            output.print( " (" + description + ")" );
        }
        output.println();
    }

    /**
     * Print out expression of given value considering various possible types of value
     * 
     * @param output Output writer where value is printed
     * @param value Object value which can be anything
     * @param indent Starting indentation length
     * @param showDescription True if description needs to be printed
     */
    public static void printValue( PrintWriter output, Object value, int indent, boolean showDescription )
    {
        if ( value == null )
        {
            output.print( NULL );
        }
        else if ( value.getClass().isArray() )
        {
            int length = Array.getLength( value );
            output.print( "[ " );
            for ( int i = 0; i < length; i++ )
            {
                if ( i != 0 )
                {
                    output.print( ", " );
                }
                printValue( output, Array.get( value, i ), indent, showDescription );
            }
            output.print( " ]" );
        }
        else if ( Collection.class.isAssignableFrom( value.getClass() ) )
        {
            boolean start = true;
            output.print( "( " );
            for ( Object obj : ( (Collection<?>) value ) )
            {
                if ( !start )
                {
                    output.print( ", " );
                }
                start = false;
                printValue( output, obj, indent, showDescription );
            }
            output.print( " )" );
        }
        else if ( Map.class.isAssignableFrom( value.getClass() ) )
        {
            output.println( "{ " );
            for ( Map.Entry<?, ?> entry : ( (Map<?, ?>) value ).entrySet() )
            {
                printExpression( output, entry.getKey(), entry.getValue(), null, indent + 2, showDescription );
            }
            output.print( StringUtils.repeat( " ", indent ) + " }" );
        }
        else if ( CompositeData.class.isAssignableFrom( value.getClass() ) )
        {
            output.println( "{ " );
            CompositeData data = (CompositeData) value;
            for ( Object key : data.getCompositeType().keySet() )
            {
                Object v = data.get( (String) key );
                printExpression( output, key, v, data.getCompositeType().getDescription( (String) key ), indent + 2,
                                 showDescription );
            }
            output.print( StringUtils.repeat( " ", indent ) + " }" );
        }
        else if ( value instanceof String )
        {
            output.print( "\"" + value + "\"" );
        }
        else
        {
            output.print( value );
        }
    }

    private SyntaxUtils()
    {
    }
}
