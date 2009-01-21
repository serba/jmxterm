package org.cyclopsgroup.jmxterm.cmd;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
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

    private Mockery context;

    private StringWriter output;

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

    @Test
    public void testExecuteNormally()
        throws IOException, JMException
    {
        setValueAndVerify("33", "int", 33);
    }

    @Test
    public void testExecuteWithAnEmptyString()
        throws IOException, JMException
    {
        setValueAndVerify("\"\"", String.class.getName(), "");
    }

    @Test
    public void testExecuteWithNullString()
        throws IOException, JMException
    {
        setValueAndVerify("null", String.class.getName(), null);
    }

    @Test
    public void testExecuteWithControlCharacters()
        throws IOException, JMException
    {
        setValueAndVerify("hello\\n", String.class.getName(), "hello\n");
    }


    private void setValueAndVerify(String expr, final String type, final Object expected)
        throws IOException, JMException
    {
        command.setBean( "a:type=x" );
        command.setArguments( Arrays.asList( "var", expr ) );

        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        final MBeanInfo beanInfo = context.mock( MBeanInfo.class );
        final MBeanAttributeInfo attributeInfo = context.mock( MBeanAttributeInfo.class );
        final AtomicReference<Attribute> setAttribute = new AtomicReference<Attribute>();
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
                will( returnValue( type ) );
                atLeast( 1 ).of( attributeInfo ).isWritable();
                will( returnValue( true ) );
                one( con ).setAttribute( (ObjectName) with(equal(new ObjectName( "a:type=x" ))), (Attribute) with(a(Attribute.class)) );
                will(doAll(new CustomAction("SetAttribute"){

                    public Object invoke(Invocation invocation) throws Throwable
                    {
                        setAttribute.set((Attribute)invocation.getParameter(1));
                        return null;
                    }
                }));
            }
        } );

        command.setSession( new MockSession( output, con ) );
        command.execute();
        context.assertIsSatisfied();

        assertNotNull(setAttribute.get());
        assertEquals("var", setAttribute.get().getName());
        assertEquals(expected, setAttribute.get().getValue());
    }
}
