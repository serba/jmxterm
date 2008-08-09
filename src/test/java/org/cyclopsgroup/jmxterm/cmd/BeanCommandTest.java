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

/**
 * Test case for {@link BeanCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class BeanCommandTest
{
    private BeanCommand command = new BeanCommand();

    private StringWriter output = new StringWriter();

    /**
     * Test execution with NULL result
     * 
     * @throws IOException
     * @throws JMException
     */
    @Test
    public void testExecuteWithGettingNull()
        throws IOException, JMException
    {
        command.setSession( new MockSession( output, null ) );
        command.execute();
        assertEquals( "null\n", output.toString() );
    }

    /**
     * Test execution with some result
     * 
     * @throws IOException
     * @throws JMException
     */
    @Test
    public void testExecuteWithGettingSomething()
        throws IOException, JMException
    {
        MockSession s = new MockSession( output, null );
        s.setBean( "something" );
        command.setSession( s );
        command.execute();
        assertEquals( "something\n", output.toString() );
    }

    /**
     * Test the case where an illegal bean is requested
     * 
     * @throws IOException
     * @throws JMException
     */
    @Test( expected = IllegalArgumentException.class )
    public void testExecuteWithInvalidBean()
        throws IOException, JMException
    {
        command.setBean( "blablabla" );
        command.setSession( new MockSession( output, null ) );
        command.execute();
    }

    /**
     * Test the case where NULL is get
     * 
     * @throws IOException
     * @throws JMException
     */
    @Test
    public void testExecuteWithSettingNull()
        throws IOException, JMException
    {
        command.setBean( "null" );
        MockSession s = new MockSession( output, null );
        s.setBean( "something" );
        command.setSession( s );
        command.execute();
        assertEquals( "ok\n", output.toString() );
        assertNull( s.getBean() );
    }

    /**
     * Test the case where domain is set
     * 
     * @throws IOException
     * @throws JMException
     */
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
        command.setSession( s );
        command.execute();
        assertEquals( "ok\n", output.toString() );
        assertEquals( "something:type=x", s.getBean() );
    }

    /**
     * Test the case where a domain is set
     * 
     * @throws IOException
     * @throws JMException
     */
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
        command.setSession( s );
        command.execute();
        assertEquals( "ok\n", output.toString() );
        assertEquals( "something:type=x", s.getBean() );
    }
}
