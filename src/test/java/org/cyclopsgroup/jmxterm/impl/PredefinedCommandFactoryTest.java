package org.cyclopsgroup.jmxterm.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PredefinedCommandFactoryTest
{
    @Test
    public void testConstruction()
        throws Exception
    {
        PredefinedCommandFactory f = new PredefinedCommandFactory();
        assertTrue( f.getCommandTypes().containsKey( "help" ) );
        assertTrue( f.getCommandTypes().containsKey( "open" ) );
        assertTrue( f.getCommandTypes().containsKey( "close" ) );
        assertTrue( f.getCommandTypes().containsKey( "quit" ) );
        assertTrue( f.getCommandTypes().containsKey( "beans" ) );
        assertTrue( f.createCommand( "help" ) instanceof HelpCommand );
    }
}
