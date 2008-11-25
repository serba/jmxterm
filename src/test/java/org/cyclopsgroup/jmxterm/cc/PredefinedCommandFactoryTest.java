package org.cyclopsgroup.jmxterm.cc;

import static org.junit.Assert.assertTrue;

import org.cyclopsgroup.jmxterm.cc.HelpCommand;
import org.cyclopsgroup.jmxterm.cc.PredefinedCommandFactory;
import org.junit.Test;

/**
 * Test case of {@link PredefinedCommandFactory}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class PredefinedCommandFactoryTest
{
    /**
     * Test that object is constructed
     * 
     * @throws Exception
     */
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
