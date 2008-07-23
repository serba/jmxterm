package org.cyclopsgroup.jmxterm.jdk6;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.cyclopsgroup.jmxterm.JavaProcess;
import org.junit.Test;

public class Jdk6JavaProcessManagerTest
{
    @Test
    public void testList()
        throws Exception
    {
        Jdk6JavaProcessManager m = new Jdk6JavaProcessManager();
        List<JavaProcess> ps = m.list();
        assertFalse( ps.isEmpty() );
    }
}
