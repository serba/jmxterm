package org.cyclopsgroup.jmxterm.jdk6;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cyclopsgroup.jmxterm.WeakCastUtils;
import org.junit.Test;

public class LocalVirtualMachineTest
{
    @Test
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
