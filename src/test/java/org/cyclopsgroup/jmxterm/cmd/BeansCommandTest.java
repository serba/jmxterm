package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

public class BeansCommandTest
{
    private BeansCommand command = new BeansCommand();

    private MBeanServerConnection conn;

    private Mockery context = new Mockery();

    private StringWriter output = new StringWriter();

    @Before
    public void setUp()
    {
        conn = context.mock( MBeanServerConnection.class );
    }

    @Test
    public void testExecuteWithAllBeans()
        throws JMException, IOException
    {
        context.checking( new Expectations()
        {
            {
                one( conn ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "a:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "a:type=1" ),
                                                                           new ObjectName( "a:type=2" ) ) ) ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "b:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "b:type=1" ) ) ) ) );
            }
        } );
        command.execute( new MockSession( output, conn ) );
        context.assertIsSatisfied();
        assertEquals( "a:type=1\na:type=2\nb:type=1\n", output.toString() );
    }

    @Test
    public void testExecuteWithDomainInArgument()
        throws JMException, IOException
    {
        command.setDomain( "b" );
        context.checking( new Expectations()
        {
            {
                allowing( conn ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "b:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "b:type=1" ) ) ) ) );
            }
        } );
        command.execute( new MockSession( output, conn ) );
        context.assertIsSatisfied();
        assertEquals( "b:type=1\n", output.toString() );
    }

    @Test
    public void testExecuteWithDomainInSession()
        throws JMException, IOException
    {
        context.checking( new Expectations()
        {
            {
                allowing( conn ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "b:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "b:type=1" ) ) ) ) );
            }
        } );
        MockSession session = new MockSession( output, conn );
        session.setDomain( "b" );
        command.execute( session );
        context.assertIsSatisfied();
        assertEquals( "b:type=1\n", output.toString() );
    }

    @Test
    public void testExecuteWithNullDomain()
        throws JMException, IOException
    {
        command.setDomain( "*" );
        context.checking( new Expectations()
        {
            {
                one( conn ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "a:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "a:type=1" ),
                                                                           new ObjectName( "a:type=2" ) ) ) ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "b:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "b:type=1" ) ) ) ) );
            }
        } );
        MockSession session = new MockSession( output, conn );
        session.setDomain( "b" );
        command.execute( session );
        context.assertIsSatisfied();
        assertEquals( "a:type=1\na:type=2\nb:type=1\n", output.toString() );
    }
}
