package org.cyclopsgroup.jcli.jccli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.beans.IntrospectionException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.StringUtils;
import org.cyclopsgroup.jcli.SampleBean;
import org.cyclopsgroup.jcli.SampleBeanWithMultiValueOption;
import org.cyclopsgroup.jcli.SampleBeanWithSimpleArgument;
import org.junit.Test;

public class JakartaCommonsCliParserTest
{
    @Test
    public void testParse()
        throws IntrospectionException
    {
        JakartaCommonsCliParser parser = new JakartaCommonsCliParser( new GnuParser() );
        SampleBean b = new SampleBean();
        parser.parse( StringUtils.split( "--tint 34123 -f 00907 -b 3453 34 52345", ' ' ), b );
        assertEquals( "00907", b.getStringField1() );
        assertEquals( 34123, b.getIntField() );
        assertTrue( b.isBooleanField() );
        assertArrayEquals( new String[] { "3453", "34", "52345" }, b.getValues().toArray() );
    }

    @Test
    public void testParseWithMultiValueOption()
        throws IntrospectionException
    {
        JakartaCommonsCliParser parser = new JakartaCommonsCliParser( new GnuParser() );
        SampleBeanWithMultiValueOption bean = new SampleBeanWithMultiValueOption();
        parser.parse( StringUtils.split( "-o aaa -o bbb -o ccc" ), bean );
        System.out.println( bean.getOptions() );
    }

    @Test
    public void testParseWithSingleArgument()
        throws IntrospectionException
    {
        JakartaCommonsCliParser parser = new JakartaCommonsCliParser( new GnuParser() );
        SampleBeanWithSimpleArgument bean = new SampleBeanWithSimpleArgument();
        parser.parse( StringUtils.split( "12345 23456", ' ' ), bean );
        assertEquals( "12345", bean.getArg() );
    }

    @Test
    public void testPrintHelp()
        throws IntrospectionException
    {
        JakartaCommonsCliParser parser = new JakartaCommonsCliParser( new GnuParser() );
        StringWriter stringOutput = new StringWriter();
        PrintWriter output = new PrintWriter( stringOutput );
        parser.printUsage( SampleBean.class, output );
        output.flush();
        String result = stringOutput.toString();
        assertTrue( result.indexOf( "sample [-2 <val>] [-b] -f <val> [-i <val>]" ) != -1 );
    }
}
