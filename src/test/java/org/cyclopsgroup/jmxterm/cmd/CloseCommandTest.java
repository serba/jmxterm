package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.io.StringWriter;

import org.cyclopsgroup.jmxterm.MockSession;
import org.junit.Test;

public class CloseCommandTest
{
    private CloseCommand command = new CloseCommand();

    private StringWriter output = new StringWriter();

    @Test
    public void testExecute()
        throws IOException
    {
        MockSession session = new MockSession( output, null );
        command.execute( session );
        assertFalse( session.isConnected() );
    }
}
