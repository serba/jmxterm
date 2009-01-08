package org.cyclopsgroup.jmxterm.jdk6;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.cyclopsgroup.jmxterm.JavaProcess;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test case of Jdk6JavaProcessManager
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Jdk6JavaProcessManagerTest
{
    /**
     * Test to list processes
     * 
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    @Test
    @Ignore( "This test turns out to be too specific to JDK version" )
    public void testList()
        throws SecurityException, NoSuchMethodException, ClassNotFoundException
    {
        Jdk6JavaProcessManager m = new Jdk6JavaProcessManager();
        List<JavaProcess> ps = m.list();
        assertFalse( ps.isEmpty() );
    }
}
