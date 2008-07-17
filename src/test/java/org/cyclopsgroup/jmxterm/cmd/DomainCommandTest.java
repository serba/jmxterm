package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.cyclopsgroup.jmxterm.MockSession;
import org.junit.Test;

public class DomainCommandTest
{
    private DomainCommand command = new DomainCommand();

    private StringWriter output = new StringWriter();

    @Test
    public void testExecuteWithGettingNull()
        throws IOException
    {
        command.execute( new MockSession( output, null ) );
        assertEquals( "null\n", output.toString() );
    }

    @Test
    public void testExecuteWithGettingSomething()
        throws IOException
    {
        MockSession session = new MockSession( output, null );
        session.setDomain( "something" );
        command.execute( session );
        assertEquals( "something\n", output.toString() );
    }
}
