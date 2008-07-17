package org.cyclopsgroup.jmxterm.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.SelfRecordingCommand;
import org.junit.Before;
import org.junit.Test;

public class CommandCenterTest
{
    private CommandCenter cc;

    private List<Command> executedCommands = new ArrayList<Command>();

    private StringWriter output = new StringWriter();

    private String getArgsFromList( int index )
    {
        SelfRecordingCommand c = (SelfRecordingCommand) executedCommands.get( index );
        return c.getArgs();
    }

    @Before
    public void setUp()
        throws IOException
    {
        Map<String, Class<? extends Command>> commandTypes = new HashMap<String, Class<? extends Command>>();
        commandTypes.put( "test", SelfRecordingCommand.class );
        cc = new CommandCenter( new PrintWriter( output, true ), new TypeMapCommandFactory( commandTypes )
        {
            @Override
            public Command createCommand( String commandName )
                throws InstantiationException, IllegalAccessException, IllegalArgumentException
            {
                return new SelfRecordingCommand( executedCommands );
            }
        } );
    }

    @Test
    public void testExecute()
    {
        cc.execute( "test 1" );
        cc.execute( "test 2 a b && test 3" );
        cc.execute( "# test 4" );
        cc.execute( "test 5 # test 6" );

        assertEquals( 4, executedCommands.size() );
        assertEquals( "1", getArgsFromList( 0 ) );
        assertEquals( "2 a b", getArgsFromList( 1 ) );
        assertEquals( "3", getArgsFromList( 2 ) );
        assertEquals( "5", getArgsFromList( 3 ) );
    }
}
