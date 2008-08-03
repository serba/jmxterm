package org.cyclopsgroup.jmxterm.boot;

import java.beans.IntrospectionException;
import java.io.IOException;

import org.cyclopsgroup.jmxterm.impl.CommandCenter;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test of {@link Main}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class MainTest
{
    /**
     * Verify commands are parsed and passed to CommandCenter
     * 
     * @throws IntrospectionException
     * @throws IOException
     */
    @Test
    @Ignore
    public void testExecuteNormally()
        throws IntrospectionException, IOException
    {
        Mockery context = new Mockery()
        {
            {
                this.setImposteriser( ClassImposteriser.INSTANCE );
            }
        };
        final CommandCenter cc = context.mock( CommandCenter.class );
        Main main = new Main( cc );
        context.checking( new Expectations()
        {
            {
                allowing( cc ).isClosed();
                one( cc ).setAbbreviated( false );
                one( cc ).execute( "beans" );
                one( cc ).execute( "exit" );
                one( cc ).close();
            }
        } );
        main.execute( new String[] { "-i", "src/test/testscript.jmx" } );
        context.assertIsSatisfied();
    }
}
