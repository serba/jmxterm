package org.cyclopsgroup.jmxterm;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

public final class Connection
{
    private final JMXConnector connector;

    private final String originalUrl;

    private final JMXServiceURL url;

    public Connection( JMXConnector connector, JMXServiceURL url, String originalUrl )
    {
        this.connector = connector;
        this.url = url;
        this.originalUrl = originalUrl;
    }

    public JMXConnector getConnector()
    {
        return connector;
    }

    public String getOriginalUrl()
    {
        return originalUrl;
    }

    public JMXServiceURL getUrl()
    {
        return url;
    }
}
