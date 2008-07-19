package org.cyclopsgroup.jcli;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class QuotedStringTokenizerTest
{
    @Test
    public void testParse()
    {
        QuotedStringTokenizer t = new QuotedStringTokenizer();
        assertEquals( "a,b,c d ,e,f,g\"h", StringUtils.join( t.parse( "a b \"c d \" e  f    g\"h " ), ',' ) );
        assertEquals( "123,4\",56", StringUtils.join( t.parse( "  123   4\" 56" ), ',' ) );
        assertEquals( "1,22\"2,34", StringUtils.join( t.parse( "1 \"22\"\"2\" \"34" ), ',' ) );
    }
}
