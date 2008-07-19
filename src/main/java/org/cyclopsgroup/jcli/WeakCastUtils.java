package org.cyclopsgroup.jcli;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.apache.commons.lang.Validate;

/**
 * Utility that cast object into given interface(s) even though class doesn't implement interface(s)
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public final class WeakCastUtils
{
    private WeakCastUtils()
    {
    }

    /**
     * Cast object into multiple interfaces
     * 
     * @param from Object to cast 
     * @param interfaces Interfaces to cast to
     * @param classLoader ClassLoader to load methods for invocation
     * @return Result that implements given interfaces
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static Object cast( final Object from, final Class<?>[] interfaces, ClassLoader classLoader )
        throws SecurityException, NoSuchMethodException
    {
        Validate.notNull( from, "Invocation target can't be NULL" );
        Validate.notNull( interfaces, "Interfaces can't be NULL" );
        Validate.notNull( classLoader, "ClassLoader can't be NULL" );
        final Map<Method, Method> methodMap = new HashMap<Method, Method>();
        for ( Class<?> interfase : interfaces )
        {
            Validate.isTrue( interfase.isInterface(), interfase + " is not an interface" );
            for ( Method fromMethod : interfase.getMethods() )
            {
                Method toMethod = from.getClass().getMethod( fromMethod.getName(), fromMethod.getParameterTypes() );
                methodMap.put( fromMethod, toMethod );
            }
        }
        return Proxy.newProxyInstance( classLoader, interfaces, new InvocationHandler()
        {
            public Object invoke( Object proxy, Method method, Object[] args )
                throws Throwable
            {
                Method toMethod = methodMap.get( method );
                if ( toMethod == null )
                {
                    throw new OperationNotSupportedException( "Method " + method + " isn't implemented in "
                        + from.getClass() );
                }
                return toMethod.invoke( from, args );
            }
        } );
    }

    /**
     * Cast object to one given interface
     * 
     * @param <T> Type of interface
     * @param from Object to cast
     * @param interfase Interface to cast to
     * @param classLoader Class loader to load invocation methods
     * @return Result that implements interface
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings( "unchecked" )
    public static <T> T cast( Object from, Class<T> interfase, ClassLoader classLoader )
        throws SecurityException, NoSuchMethodException
    {
        Validate.notNull( interfase, "Interface can't be NULL" );
        return (T) cast( from, new Class<?>[] { interfase }, classLoader );
    }

    /**
     * Cast object to one given interface using ClassLoader of interface
     * 
     * @param <T> Type of interface
     * @param from Object to cast
     * @param interfase Interface to cast to
     * @return Result that implements interface
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static <T> T cast( Object from, Class<T> interfase )
        throws SecurityException, NoSuchMethodException
    {
        return cast( from, interfase, interfase.getClassLoader() );
    }
}
