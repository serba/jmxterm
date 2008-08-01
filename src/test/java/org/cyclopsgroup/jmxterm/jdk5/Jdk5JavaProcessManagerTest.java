package org.cyclopsgroup.jmxterm.jdk5;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.cyclopsgroup.jmxterm.JavaProcess;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test case of {@link Jdk5JavaProcessManager}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Jdk5JavaProcessManagerTest
{
    /**
     * Dummy test case
     * 
     * @throws Exception For any constrocutor exception
     */
    @Test
    @Ignore
    public void testConstruction()
        throws Exception
    {
        Jdk5JavaProcessManager jpm = new Jdk5JavaProcessManager();
        List<JavaProcess> ps = jpm.list();
        assertFalse( ps.isEmpty() );
    }
}
