package org.cyclopsgroup.jmxterm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test case of {@link JavaProcessManager}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class JavaProcessManagerTest
{
    /**
     * Test that getInstance method returns something valid
     */
    @Test
    public void testGetInstance()
    {
        JavaProcessManager jpm = JavaProcessManager.getInstance();
        assertEquals( "jdk6", jpm.getName() );
    }
}
