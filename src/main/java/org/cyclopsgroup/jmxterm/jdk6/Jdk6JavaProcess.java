package org.cyclopsgroup.jmxterm.jdk6;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.JavaProcess;

/**
 * JDK6 specific implementation of {@link JavaProcess}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Jdk6JavaProcess
    implements JavaProcess
{
    private final LocalVirtualMachine vm;

    /**
     * @param vm Local VM
     */
    public Jdk6JavaProcess( LocalVirtualMachine vm )
    {
        Validate.notNull( vm, "VM can't be NULL" );
        this.vm = vm;
    }

    /**
     * @inheritDoc
     */
    public String getDisplayName()
    {
        return vm.displayName();
    }

    /**
     * @inheritDoc
     */
    public int getProcessId()
    {
        return vm.vmid();
    }

    /**
     * @inheritDoc
     */
    public boolean isManageable()
    {
        return vm.isManageable();
    }

    /**
     * @inheritDoc
     */
    public void startManagementAgent()
    {
        vm.startManagementAgent();
    }

    /**
     * @inheritDoc
     */
    public String toUrl()
    {
        return vm.connectorAddress();
    }

}
