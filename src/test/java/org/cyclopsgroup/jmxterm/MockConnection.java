package org.cyclopsgroup.jmxterm;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXServiceURL;

public class MockConnection
    implements Connection
{
    private final MBeanServerConnection con;

    private final JMXServiceURL url;

    public MockConnection( JMXServiceURL url, MBeanServerConnection con )
    {
        this.url = url;
        this.con = con;
    }

    public String getConnectorId()
        throws IOException
    {
        return "id";
    }

    public String getDisplayUrl()
    {
        return "url";
    }

    public MBeanServerConnection getServerConnection()
        throws IOException
    {
        return con;
    }

    public JMXServiceURL getUrl()
    {
        return url;
    }
}
