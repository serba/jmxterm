package org.cyclopsgroup.jcli;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class QuotedStringTokenizerTest
{
    @Test
    public void testParse()
    {
        List<String> result = new QuotedStringTokenizer().parse( "a b \"c d \" e  f    g\"h " );
        assertEquals( "a,b,c d ,e,f,g\"h", StringUtils.join( result, ',' ) );
    }
}
