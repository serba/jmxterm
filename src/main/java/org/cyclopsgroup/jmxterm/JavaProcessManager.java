package org.cyclopsgroup.jmxterm;

import java.util.List;

/**
 * Java process manager
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface JavaProcessManager
{
    /**
     * List all running Java processes
     * 
     * @return List of running processes
     */
    List<JavaProcess> list();

    /**
     * Get JVM process
     * 
     * @param pid Process ID
     * @return Process or NULL
     */
    JavaProcess get( int pid );
}
