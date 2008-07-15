package org.cyclopsgroup.jmxterm;

import java.io.IOException;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.Validate;

/**
 * Identifies a JMX connection
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class ConnectionImpl
    implements Connection
{
    private final JMXConnector connector;

    private final String displayUrl;

    private final JMXServiceURL url;

    /**
     * @param connector JMX connector
     * @param url JMX service URL object
     * @param displayUrl The URL user manually input
     */
    ConnectionImpl( JMXConnector connector, JMXServiceURL url, String displayUrl )
    {
        Validate.notNull( connector, "JMX connector can't be NULL" );
        Validate.notNull( url, "JMX service URL can't be NULL" );
        Validate.notNull( displayUrl, "Display URL can't be NULL" );
        this.connector = connector;
        this.url = url;
        this.displayUrl = displayUrl;
    }

    /**
     * @return JMX connector
     */
    public final JMXConnector getConnector()
    {
        return connector;
    }

    /**
     * @inheritDoc
     */
    public final String getDisplayUrl()
    {
        return displayUrl;
    }

    /**
     * @inheritDoc
     */
    public final JMXServiceURL getUrl()
    {
        return url;
    }

    /**
     * @inheritDoc
     */
    public String getConnectorId()
        throws IOException
    {
        return connector.getConnectionId();
    }
}
