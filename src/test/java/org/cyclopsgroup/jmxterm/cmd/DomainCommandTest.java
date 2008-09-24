package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import javax.management.MBeanServerConnection;

import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of {@link DomainCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class DomainCommandTest
{
    private DomainCommand command;

    private StringWriter output;

    /**
     * Set up command to test
     */
    @Before
    public void setUp()
    {
        command = new DomainCommand();
        output = new StringWriter();
    }

    /**
     * Test execution and get empty result
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithGettingNull()
        throws Exception
    {
        command.setSession( new MockSession( output, null ) );
        command.execute();
        assertEquals( "null\n", output.toString() );
    }

    /**
     * Test execution and get valid result
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithGettingSomething()
        throws Exception
    {
        MockSession session = new MockSession( output, null );
        session.setDomain( "something" );
        command.setSession( session );
        command.execute();
        assertEquals( "something\n", output.toString() );
    }

    /**
     * Test execution and set valid value
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithSettingSomethingValid()
        throws Exception
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
        command.setSession( session );
        command.execute();
        assertEquals( "something", session.getDomain() );
        context.assertIsSatisfied();
    }

    /**
     * Test the case where invalid value is declined
     * 
     * @throws Exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void testExecuteWithSettingSomethingInvalid()
        throws Exception
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
        command.setSession( session );
        command.execute();
    }
}
