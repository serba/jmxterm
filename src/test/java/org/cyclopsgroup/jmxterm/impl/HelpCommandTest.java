package org.cyclopsgroup.jmxterm.impl;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;

import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.SelfRecordingCommand;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link HelpCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class HelpCommandTest
{
    private HelpCommand command;

    private Mockery context;

    private StringWriter output;

    /**
     * Set up objects to test
     */
    @Before
    public void setUp()
    {
        command = new HelpCommand();
        output = new StringWriter();
        context = new Mockery();
        context.setImposteriser( ClassImposteriser.INSTANCE );
    }

    /**
     * Test execution with several options
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithOption()
        throws Exception
    {
        command.setArgNames( new String[] { "a", "b" } );
        final CommandCenter cc = context.mock( CommandCenter.class );
        command.setCommandCenter( cc );

        context.checking( new Expectations()
        {
            {
                one( cc ).getCommandType( "a" );
                will( returnValue( SelfRecordingCommand.class ) );
                one( cc ).printUsage( SelfRecordingCommand.class );
                one( cc ).getCommandType( "b" );
                will( returnValue( SelfRecordingCommand.class ) );
                one( cc ).printUsage( SelfRecordingCommand.class );
            }
        } );
        command.setSession( new MockSession( output, null ) );
        command.execute();
        context.assertIsSatisfied();
    }

    /**
     * Test execution without option
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithoutOption()
        throws Exception
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
        command.setSession( new MockSession( output, null ) );
        command.execute();
        assertEquals( "a:desc\nb:desc\n", output.toString() );
    }
}
