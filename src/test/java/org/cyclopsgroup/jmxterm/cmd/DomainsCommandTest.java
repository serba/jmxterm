package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import javax.management.MBeanServerConnection;

import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

public class DomainsCommandTest
{
    private Mockery context;

    private DomainsCommand command;

    @Before
    public void setUp()
    {
        command = new DomainsCommand();
        context = new Mockery();
    }

    @Test
    public void testExecution()
        throws IOException
    {
        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        StringWriter output = new StringWriter();
        context.checking( new Expectations()
        {
            {
                one( con ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
            }
        } );
        command.execute( new MockSession( output, con ) );
        context.assertIsSatisfied();
        assertEquals( "a\nb\n", output.toString() );
    }
}
