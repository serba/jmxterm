package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.net.MalformedURLException;

import org.cyclopsgroup.jcli.annotation.MalformedArgException;
import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.junit.Before;
import org.junit.Test;

public class OptionCommandTest
{
    private OptionCommand command;

    private StringWriter output;

    @Before
    public void setUp()
    {
        command = new OptionCommand();
        output = new StringWriter();
    }

    @Test
    public void testExecuteNormally()
        throws MalformedURLException
    {
        Session session = new MockSession( output, null );
        command.setAbbreviated( "false" );
        command.setVerbose( "true" );
        command.execute( session );
        assertFalse( session.isAbbreviated() );
        assertTrue( session.isVerbose() );
        command.setAbbreviated( "true" );
        command.setVerbose( "false" );
        command.execute( session );
        assertTrue( session.isAbbreviated() );
        assertFalse( session.isVerbose() );
    }

    @Test( expected = MalformedArgException.class )
    public void testExecuteWithInvalidVerbose()
        throws MalformedURLException
    {
        Session session = new MockSession( output, null );
        command.setVerbose( "xyz" );
        command.execute( session );
        assertEquals( "ok\n", output.toString() );
    }
}
