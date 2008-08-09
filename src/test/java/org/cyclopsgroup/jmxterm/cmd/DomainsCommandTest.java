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

/**
 * Test case of {@link DomainsCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class DomainsCommandTest
{
    private Mockery context;

    private DomainsCommand command;

    /**
     * Set up objects to test
     */
    @Before
    public void setUp()
    {
        command = new DomainsCommand();
        context = new Mockery();
    }

    /**
     * Test normal execution
     * 
     * @throws IOException
     */
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
        command.setSession( new MockSession( output, con ) );
        command.execute();
        context.assertIsSatisfied();
        assertEquals( "a\nb\n", output.toString() );
    }
}
