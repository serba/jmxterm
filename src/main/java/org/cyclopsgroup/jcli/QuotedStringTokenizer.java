package org.cyclopsgroup.jcli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class QuotedStringTokenizer
{
    private enum ParsingState
    {
        ESCAPED_OR_QUOTE_END, QUOTED, READY, WORD_STARTED;
    }

    private final char delimiter;

    private final char quotation;

    public QuotedStringTokenizer()
    {
        this( ' ', '\"' );
    }

    public QuotedStringTokenizer( char delimiter, char quotation )
    {
        this.delimiter = delimiter;
        this.quotation = quotation;
    }

    public List<String> parse( String input )
    {
        if ( StringUtils.trimToNull( input ) == null )
        {
            return Collections.emptyList();
        }
        List<String> results = new ArrayList<String>();
        ParsingState state = ParsingState.READY;
        StringBuilder buf = null;
        for ( int i = 0; i < input.length(); i++ )
        {
            char c = input.charAt( i );
            switch ( state )
            {
                case READY:
                    assert buf == null;
                    if ( c == delimiter )
                    {
                        continue;
                    }
                    else if ( c == quotation )
                    {
                        state = ParsingState.QUOTED;
                        buf = new StringBuilder();
                    }
                    else
                    {
                        state = ParsingState.WORD_STARTED;
                        buf = new StringBuilder();
                        buf.append( c );
                    }
                    break;
                case WORD_STARTED:
                    assert buf != null;
                    if ( c == delimiter )
                    {
                        state = ParsingState.READY;
                        results.add( buf.toString() );
                        buf = null;
                    }
                    else
                    {
                        buf.append( c );
                    }
                    break;
                case QUOTED:
                    assert buf != null;
                    if ( c == quotation )
                    {
                        state = ParsingState.ESCAPED_OR_QUOTE_END;
                    }
                    else
                    {
                        buf.append( c );
                    }
                    break;
                case ESCAPED_OR_QUOTE_END:
                    assert buf != null;
                    if ( c == delimiter )
                    {
                        state = ParsingState.READY;
                        results.add( buf.toString() );
                        buf = null;
                    }
                    else
                    {
                        state = ParsingState.QUOTED;
                        buf.append( c );
                    }
                    break;
            }
        }
        if ( buf != null )
        {
            results.add( buf.toString() );
        }
        return results;
    }
}
