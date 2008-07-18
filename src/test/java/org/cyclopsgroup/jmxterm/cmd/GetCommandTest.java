package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

public class GetCommandTest
{
    private GetCommand command;

    private Mockery context;

    private StringWriter output;

    @Before
    public void setUp()
    {
        command = new GetCommand();
        context = new Mockery();
        context.setImposteriser( ClassImposteriser.INSTANCE );
        output = new StringWriter();
    }

    @Test
    public void testExecuteNormally()
        throws IOException, JMException
    {
        command.setDomain( "a" );
        command.setBean( "type=x" );
        command.setAttributes( Arrays.asList( "a" ) );

        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        final MBeanInfo beanInfo = context.mock( MBeanInfo.class );
        final MBeanAttributeInfo attributeInfo = context.mock( MBeanAttributeInfo.class );
        context.checking( new Expectations()
        {
            {
                one( con ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
                one( con ).getMBeanInfo( new ObjectName( "a:type=x" ) );
                will( returnValue( beanInfo ) );
                one( beanInfo ).getAttributes();
                will( returnValue( new MBeanAttributeInfo[] { attributeInfo } ) );
                allowing( attributeInfo ).getName();
                will( returnValue( "a" ) );
                allowing( attributeInfo ).isReadable();
                will( returnValue( true ) );
                one( con ).getAttribute( new ObjectName( "a:type=x" ), "a" );
                will( returnValue( "bingo" ) );
            }
        } );
        command.execute( new MockSession( output, con ) );
        context.assertIsSatisfied();
        assertEquals( "\"bingo\"\n", output.toString() );
    }
}
