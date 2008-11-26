package org.cyclopsgroup.jmxterm.utils;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.management.remote.JMXServiceURL;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.JavaProcessManager;

/**
 * Utility class for syntax checking
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public final class SyntaxUtils
{
    /**
     * String <code>null</code> that identifies empty value
     */
    public static final String NULL = "null";

    private static final Pattern PATTERN_HOST_PORT = Pattern.compile( "^(\\w|\\.|\\-)+\\:\\d+$" );

    /**
     * @param url String expression of MBean server URL or abbreviation like localhost:9991
     * @return Parsed JMXServerURL
     * @throws IOException IO error
     */
    public static JMXServiceURL getUrl( String url )
        throws IOException
    {
        if ( StringUtils.isEmpty( url ) )
        {
            throw new IllegalArgumentException( "Empty URL is not allowed" );
        }
        else if ( NumberUtils.isDigits( url ) )
        {
            Integer pid = Integer.parseInt( url );
            JavaProcess p = JavaProcessManager.getInstance().get( pid );
            if ( p == null )
            {
                throw new NullPointerException( "No such PID " + pid );
            }
            if ( !p.isManageable() )
            {
                p.startManagementAgent();
                if ( !p.isManageable() )
                {
                    throw new IllegalStateException( "Managed agent for PID " + pid + " couldn't start. PID " + pid
                        + " is not manageable" );
                }
            }
            return new JMXServiceURL( p.toUrl() );
        }
        else if ( PATTERN_HOST_PORT.matcher( url ).find() )
        {
            return new JMXServiceURL( "service:jmx:rmi:///jndi/rmi://" + url + "/jmxrmi" );
        }
        else
        {
            return new JMXServiceURL( url );
        }
    }

    /**
     * Check if string value is <code>null</code>
     * 
     * @param s String value
     * @return True if value is <code>null</code>
     */
    public static boolean isNull( String s )
    {
        return StringUtils.equalsIgnoreCase( NULL, s ) || StringUtils.equals( "*", s );
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

    private SyntaxUtils()
    {
    }
}