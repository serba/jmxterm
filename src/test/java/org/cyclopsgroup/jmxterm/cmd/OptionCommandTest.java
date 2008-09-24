package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.cyclopsgroup.jcli.annotation.MalformedArgException;
import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link OptionCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class OptionCommandTest
{
    private OptionCommand command;

    private StringWriter output;

    /**
     * Set up object to test
     */
    @Before
    public void setUp()
    {
        command = new OptionCommand();
        output = new StringWriter();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExecuteNormally()
        throws Exception
    {
        Session session = new MockSession( output, null );
        command.setAbbreviated( "false" );
        command.setVerbose( "true" );
        command.setSession( session );
        command.execute();
        assertFalse( session.isAbbreviated() );
        assertTrue( session.isVerbose() );
        command.setAbbreviated( "true" );
        command.setVerbose( "false" );
        command.execute();
        assertTrue( session.isAbbreviated() );
        assertFalse( session.isVerbose() );
    }

    /**
     * @throws Exception
     */
    @Test( expected = MalformedArgException.class )
    public void testExecuteWithInvalidVerbose()
        throws Exception
    {
        Session session = new MockSession( output, null );
        command.setVerbose( "xyz" );
        command.setSession( session );
        command.execute();
        assertEquals( "ok\n", output.toString() );
    }
}
