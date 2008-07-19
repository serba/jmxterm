package org.cyclopsgroup.jmxterm.impl;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;

import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.SelfRecordingCommand;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

public class HelpCommandTest
{
    private HelpCommand command;

    private StringWriter output;

    private Mockery context;

    @Before
    public void setUp()
    {
        command = new HelpCommand();
        output = new StringWriter();
        context = new Mockery();
        context.setImposteriser( ClassImposteriser.INSTANCE );
    }

    @Test
    public void testExecuteWithoutOption()
        throws MalformedURLException
    {
        final CommandCenter cc = context.mock( CommandCenter.class );
        command.setCommandCenter( cc );

        context.checking( new Expectations()
        {
            {
                one( cc ).getCommandNames();
                will( returnValue( new HashSet<String>( Arrays.asList( "a", "b" ) ) ) );
                one( cc ).getCommandType( "a" );
                will( returnValue( SelfRecordingCommand.class ) );
                one( cc ).getCommandType( "b" );
                will( returnValue( SelfRecordingCommand.class ) );
            }
        } );
        command.execute( new MockSession( output, null ) );
        assertEquals( "a:desc\nb:desc\n", output.toString() );
    }
}
