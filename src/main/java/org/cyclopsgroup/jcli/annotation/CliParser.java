package org.cyclopsgroup.jcli.annotation;

import java.beans.IntrospectionException;
import java.io.PrintWriter;

public interface CliParser
{
    boolean parse( String[] args, Object bean )
        throws IntrospectionException;

    void printUsage( Class<?> beanType, PrintWriter output )
        throws IntrospectionException;
}
