package org.cyclopsgroup.jmxterm.boot;

import java.beans.IntrospectionException;
import java.io.IOException;

import org.cyclopsgroup.jmxterm.impl.CommandCenter;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class MainTest
{
    @Test
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
                allowing( cc ).prompt();
                allowing( cc ).isClosed();
                one( cc ).execute( "beans" );
                one( cc ).execute( "exit" );
                one( cc ).close();
            }
        } );
        main.execute( new String[] { "-i", "src/test/testscript.jmx" } );
        context.assertIsSatisfied();
    }
}
