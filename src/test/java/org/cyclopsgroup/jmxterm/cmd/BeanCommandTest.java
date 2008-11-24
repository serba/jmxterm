package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Ignore;
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
     * @throws Exception
     */
    @Test
    @Ignore
    public void testExecuteWithGettingNull()
        throws Exception
    {
        command.setSession( new MockSession( output, null ) );
        command.execute();
        assertEquals( "null\n", output.toString() );
    }

    /**
     * Test execution with some result
     * 
     * @throws Exception
     */
    @Test
    @Ignore
    public void testExecuteWithGettingSomething()
        throws Exception
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
     * @throws Exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void testExecuteWithInvalidBean()
        throws Exception
    {
        command.setBean( "blablabla" );
        command.setSession( new MockSession( output, null ) );
        command.execute();
    }

    /**
     * Test the case where NULL is get
     * 
     * @throws Exception
     */
    @Test
    @Ignore
    public void testExecuteWithSettingNull()
        throws Exception
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
     * @throws Exception
     */
    @Test
    @Ignore
    public void testExecuteWithSettingSomething()
        throws Exception
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
     * @throws Exception
     */
    @Test
    @Ignore
    public void testExecuteWithSettingSomethingAndDomain()
        throws Exception
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
