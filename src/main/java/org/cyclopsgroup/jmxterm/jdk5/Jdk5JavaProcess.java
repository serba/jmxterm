package org.cyclopsgroup.jmxterm.jdk5;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jmxterm.JavaProcess;

/**
 * JDK5 specific implementation of {@link JavaProcess}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class Jdk5JavaProcess
    implements JavaProcess
{
    private final int processId;

    private final String url;

    Jdk5JavaProcess( int processId, String url )
    {
        Validate.isTrue( processId > 0, "Invalid process ID " + processId );
        Validate.notNull( url, "URL can't be NULL" );
        this.processId = processId;
        this.url = url;
    }

    /**
     * @inheritDoc
     */
    public String getDisplayName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @inheritDoc
     */
    public int getProcessId()
    {
        return processId;
    }

    /**
     * @inheritDoc
     */
    public boolean isManageable()
    {
        return true;
    }

    /**
     * @inheritDoc
     */
    public void startManagementAgent()
    {

    }

    /**
     * @inheritDoc
     */
    public String toUrl()
    {
        return url;
    }
}
