package org.cyclopsgroup.jmxterm.jdk6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.JavaProcessManager;
import org.cyclopsgroup.jmxterm.utils.WeakCastUtils;

/**
 * JDK6 specific java process manager
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Jdk6JavaProcessManager
    extends JavaProcessManager
{
    private final StaticLocalVirtualMachine staticVm;

    /**
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    public Jdk6JavaProcessManager()
        throws SecurityException, NoSuchMethodException, ClassNotFoundException
    {
        staticVm =
            WeakCastUtils.staticCast( Class.forName( LocalVirtualMachine.ORIGINAL_CLASS_NAME ),
                                      StaticLocalVirtualMachine.class );
    }

    /**
     * @inheritDoc
     */
    @Override
    public JavaProcess get( int pid )
    {
        Map<Integer, Object> lvms = staticVm.getAllVirtualMachines();
        Object vm = lvms.get( pid );
        if ( vm == null )
        {
            return null;
        }
        try
        {
            return new Jdk6JavaProcess( WeakCastUtils.cast( vm, LocalVirtualMachine.class ) );
        }
        catch ( SecurityException e )
        {
            throw new RuntimeException( "Can't cast " + vm + " to LocalVirtualMachine", e );
        }
        catch ( NoSuchMethodException e )
        {
            throw new RuntimeException( "Can't cast " + vm + " to LocalVirtualMachine", e );
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<JavaProcess> list()
    {
        Map<Integer, Object> lvms = staticVm.getAllVirtualMachines();
        List<JavaProcess> result = new ArrayList<JavaProcess>( lvms.size() );
        for ( Object lvm : lvms.values() )
        {
            LocalVirtualMachine vm;
            try
            {
                vm = WeakCastUtils.cast( lvm, LocalVirtualMachine.class );
                result.add( new Jdk6JavaProcess( vm ) );
            }
            catch ( SecurityException e )
            {
                throw new RuntimeException( "Can't cast " + lvm + " to LocalVirtualMachine", e );
            }
            catch ( NoSuchMethodException e )
            {
                throw new RuntimeException( "Can't cast " + lvm + " to LocalVirtualMachine", e );
            }
        }
        return result;
    }

}
