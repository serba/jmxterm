package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import javax.management.Attribute;
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

/**
 * Test case of {@link SetCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class SetCommandTest
{
    private SetCommand command;

    private StringWriter output;

    private Mockery context;

    /**
     * Set up objects to test
     */
    @Before
    public void setUp()
    {
        command = new SetCommand();
        output = new StringWriter();
        context = new Mockery();
        context.setImposteriser( ClassImposteriser.INSTANCE );
    }

    /**
     * Test normal execution
     * 
     * @throws IOException
     * @throws JMException
     */
    @Test
    public void testExecuteNormally()
        throws IOException, JMException
    {
        command.setBean( "a:type=x" );
        command.setArguments( Arrays.asList( "var", "33" ) );

        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        final MBeanInfo beanInfo = context.mock( MBeanInfo.class );
        final MBeanAttributeInfo attributeInfo = context.mock( MBeanAttributeInfo.class );
        context.checking( new Expectations()
        {
            {
                atLeast( 1 ).of( con ).getMBeanInfo( new ObjectName( "a:type=x" ) );
                will( returnValue( beanInfo ) );
                atLeast( 1 ).of( beanInfo ).getAttributes();
                will( returnValue( new MBeanAttributeInfo[] { attributeInfo } ) );
                atLeast( 1 ).of( attributeInfo ).getName();
                will( returnValue( "var" ) );
                atLeast( 1 ).of( attributeInfo ).getType();
                will( returnValue( "int" ) );
                atLeast( 1 ).of( attributeInfo ).isWritable();
                will( returnValue( true ) );
                one( con ).setAttribute( new ObjectName( "a:type=x" ), new Attribute( "var", 33 ) );
            }
        } );

        command.setSession( new MockSession( output, con ) );
        command.execute();
        context.assertIsSatisfied();
        assertEquals( "ok\n", output.toString() );
    }
}
