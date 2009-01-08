package org.cyclopsgroup.jmxterm.jdk6;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cyclopsgroup.jmxterm.utils.WeakCastUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test of LocalVirtualMachine
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class LocalVirtualMachineTest
{
    /**
     * Test run
     * 
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    @Test
    @Ignore( "This test turns out to be too specific to JDK version" )
    public void testRun()
        throws SecurityException, NoSuchMethodException, ClassNotFoundException
    {
        Class<?> type = Class.forName( "sun.tools.jconsole.LocalVirtualMachine" );
        StaticLocalVirtualMachine s = WeakCastUtils.staticCast( type, StaticLocalVirtualMachine.class );
        Map<Integer, Object> vms = s.getAllVirtualMachines();
        List<LocalVirtualMachine> lvms = new ArrayList<LocalVirtualMachine>( vms.size() );
        for ( Object vm : vms.values() )
        {
            LocalVirtualMachine m = WeakCastUtils.cast( vm, LocalVirtualMachine.class );
            lvms.add( m );
        }
        assertTrue( lvms.size() != 0 );
    }
}
