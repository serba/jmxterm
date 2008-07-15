package org.cyclopsgroup.jmxterm;

import java.io.IOException;

import javax.management.remote.JMXServiceURL;

public class MockConnection
    implements Connection
{
    private final JMXServiceURL url;

    public MockConnection( JMXServiceURL url )
    {
        this.url = url;
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

    public JMXServiceURL getUrl()
    {
        return url;
    }
}
