package org.cyclopsgroup.jmxterm;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case of {@link CommandHistoryManager}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class CommandHistoryManagerTest
{
    private CommandHistoryManager chm;

    /**
     * Initialize command history manager to test
     */
    @Before
    public void setUp()
    {
        chm = new CommandHistoryManager();
        chm.setHistorySize( 3 );
    }

    /**
     * Test command is appended and verify result
     */
    @Test
    public void testAppend()
    {
        assertTrue( chm.getHistory().isEmpty() );
        chm.append( "a" );
        assertArrayEquals( new String[] { "a" }, chm.getHistory().toArray() );
        chm.append( "b" );
        chm.append( "#c" );
        chm.append( " " );
        assertArrayEquals( new String[] { "a", "b" }, chm.getHistory().toArray() );
        chm.append( "b" );
        assertArrayEquals( new String[] { "a", "b" }, chm.getHistory().toArray() );
        chm.append( "c" );
        chm.append( "d" );
        assertArrayEquals( new String[] { "b", "c", "d" }, chm.getHistory().toArray() );
    }
}
