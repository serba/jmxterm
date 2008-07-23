package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.junit.Before;
import org.junit.Test;

public class OpenCommandTest
{
    private OpenCommand command;

    @Before
    public void setUp()
    {
        command = new OpenCommand();
    }

    @Test
    public void testExecuteWithUrl()
        throws IOException
    {
        command.setUrl( "xyz.cyclopsgroup.org:12345" );
        Session session = new MockSession( new StringWriter(), null );
        session.disconnect();
        command.execute( session );
        assertTrue( session.isConnected() );
    }

    @Test
    public void testExecuteWithoutUrl()
        throws IOException
    {
        StringWriter output = new StringWriter();
        Session session = new MockSession( output, null );
        command.execute( session );
        assertEquals( "id,service:jmx:rmi:///jndi/rmi://localhost:9991/jmxrmi\n", output.toString() );
    }
}
