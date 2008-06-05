package org.cyclopsgroup.jcli.jccli;

import java.beans.IntrospectionException;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.StringUtils;
import org.cyclopsgroup.jcli.SampleBean;
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
        System.out.println( b );
    }

    @Test
    public void testPrintHelp()
        throws IntrospectionException
    {
        JakartaCommonsCliParser parser = new JakartaCommonsCliParser( new GnuParser() );
        parser.printUsage( SampleBean.class, System.out );
    }
}
