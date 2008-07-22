package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.junit.Before;
import org.junit.Test;

public class QuitCommandTest
{
    private QuitCommand command;

    private StringWriter output;

    @Before
    public void setUp()
    {
        command = new QuitCommand();
        output = new StringWriter();
    }

    @Test
    public void testExecute()
        throws IOException
    {
        Session session = new MockSession( output, null );
        command.execute( session );
        assertFalse( session.isConnected() );
        assertTrue( session.isClosed() );
    }
}
