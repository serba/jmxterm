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

public class DomainCommandTest
{
    private DomainCommand command;

    private StringWriter output;

    @Before
    public void setUp()
    {
        command = new DomainCommand();
        output = new StringWriter();
    }

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

    @Test
    public void testExecuteWithSettingSomethingValid()
        throws IOException
    {
        Mockery context = new Mockery();
        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        command.setDomain( "something" );
        MockSession session = new MockSession( output, con );
        context.checking( new Expectations()
        {
            {
                one( con ).getDomains();
                will( returnValue( new String[] { "something" } ) );
            }
        } );
        command.execute( session );
        assertEquals( "something", session.getDomain() );
        context.assertIsSatisfied();
    }

    @Test( expected = IllegalArgumentException.class )
    public void testExecuteWithSettingSomethingInvalid()
        throws IOException
    {
        Mockery context = new Mockery();
        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        command.setDomain( "invalid" );
        MockSession session = new MockSession( output, con );
        context.checking( new Expectations()
        {
            {
                one( con ).getDomains();
                will( returnValue( new String[] { "something" } ) );
            }
        } );
        command.execute( session );
    }
}
