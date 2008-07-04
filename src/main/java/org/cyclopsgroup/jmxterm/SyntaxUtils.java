package org.cyclopsgroup.jmxterm;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import javax.management.openmbean.CompositeData;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

public final class SyntaxUtils
{
    private SyntaxUtils()
    {
    }

    private static final Pattern PATTERN_INDEX = Pattern.compile( "^\\%\\d+$" );

    private static final String NULL = "null";

    public static boolean isIndex( String s )
    {
        return PATTERN_INDEX.matcher( s ).matches();
    }

    public static int getIndex( String s )
    {
        Validate.isTrue( isIndex( s ), "Malformed index value " + s );
        return Integer.parseInt( s.substring( 1 ) );
    }

    public static boolean isNull( String s )
    {
        return s == null || StringUtils.equalsIgnoreCase( NULL, s );
    }

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

    public static void printValue( PrintWriter output, Object value, int indent, boolean showDescription )
    {
        if ( value == null )
        {
            output.print( "NULL" );
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
            for ( String key : data.getCompositeType().keySet() )
            {
                Object v = data.get( key );
                printExpression( output, key, v, data.getCompositeType().getDescription( key ), indent + 2,
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
}
