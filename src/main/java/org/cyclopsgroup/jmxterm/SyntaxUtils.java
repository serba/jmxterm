package org.cyclopsgroup.jmxterm;

import java.util.regex.Pattern;

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
}
