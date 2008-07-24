package org.cyclopsgroup.jmxterm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JavaProcessManagerTest
{
    @Test
    public void testGetInstance()
    {
        JavaProcessManager jpm = JavaProcessManager.getInstance();
        assertEquals( "jdk6", jpm.getName() );
    }
}
