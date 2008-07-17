package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class BeanCommandTest
{
    private BeanCommand command = new BeanCommand();

    private StringWriter output = new StringWriter();

    @Test
    public void testExecuteWithGettingNull()
        throws IOException, JMException
    {
        command.execute( new MockSession( output, null ) );
        assertEquals( "null\n", output.toString() );
    }

    @Test
    public void testExecuteWithGettingSomething()
        throws IOException, JMException
    {
        MockSession s = new MockSession( output, null );
        s.setBean( "something" );
        command.execute( s );
        assertEquals( "something\n", output.toString() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testExecuteWithInvalidBean()
        throws IOException, JMException
    {
        command.setBean( "blablabla" );
        command.execute( new MockSession( output, null ) );
    }

    @Test
    public void testExecuteWithSettingNull()
        throws IOException, JMException
    {
        command.setBean( "null" );
        MockSession s = new MockSession( output, null );
        s.setBean( "something" );
        command.execute( s );
        assertEquals( "ok\n", output.toString() );
        assertNull( s.getBean() );
    }

    @Test
    public void testExecuteWithSettingSomething()
        throws IOException, JMException
    {
        Mockery context = new Mockery();
        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        command.setBean( "something:type=x" );
        MockSession s = new MockSession( output, con );
        context.checking( new Expectations()
        {
            {
                atLeast( 1 ).of( con ).getMBeanInfo( new ObjectName( "something:type=x" ) );
            }
        } );
        command.execute( s );
        assertEquals( "ok\n", output.toString() );
        assertEquals( "something:type=x", s.getBean() );
    }

    @Test
    public void testExecuteWithSettingSomethingAndDomain()
        throws IOException, JMException
    {
        Mockery context = new Mockery();
        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        command.setBean( "type=x" );
        MockSession s = new MockSession( output, con );
        s.setDomain( "something" );
        context.checking( new Expectations()
        {
            {
                atLeast( 1 ).of( con ).getMBeanInfo( new ObjectName( "something:type=x" ) );
            }
        } );
        command.execute( s );
        assertEquals( "ok\n", output.toString() );
        assertEquals( "something:type=x", s.getBean() );
    }
}
