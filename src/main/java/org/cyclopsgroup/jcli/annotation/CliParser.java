package org.cyclopsgroup.jcli.annotation;

import java.beans.IntrospectionException;
import java.io.OutputStream;

public interface CliParser
{
    boolean parse( String[] args, Object bean )
        throws IntrospectionException;

    void printUsage( Class<?> beanType, OutputStream output )
        throws IntrospectionException;
}
