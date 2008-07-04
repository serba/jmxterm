package org.cyclopsgroup.jcli.annotation;

import java.beans.IntrospectionException;
import java.io.PrintWriter;

/**
 * Parser that parse string array argument and pass result to given Java bean
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface CliParser
{
    /**
     * Parse arguments and pass result to given Java bean annotated with {@link Cli}
     * 
     * @param args String array of arguments
     * @param bean Java bean where result is set
     * @return True if everything looks good
     * @throws IntrospectionException Thrown when Java bean structure has problem
     */
    boolean parse( String[] args, Object bean )
        throws IntrospectionException;

    /**
     * Print out usage of given Java bean type annotated with {@link Cli}
     * 
     * @param beanType Type of Java bean
     * @param output Output for message
     * @throws IntrospectionException Thrown when Java bean structure has problem
     */
    void printUsage( Class<?> beanType, PrintWriter output )
        throws IntrospectionException;
}
