package org.cyclopsgroup.jcli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * String tokenizer which split string into segments considering quotation and character escaping
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class QuotedStringTokenizer
{
    private enum ParsingState
    {
        /**
         * Ready for escaping next character or end a quoted segment
         */
        ESCAPED_OR_QUOTE_END,
        /**
         * Quotation started
         */
        QUOTED,
        /**
         * Ready for new word
         */
        READY,
        /**
         * Word started without quotation
         */
        WORD_STARTED;
    }

    private final char delimiter;

    private final char quotation;

    /**
     * Default constructor that uses white space as delimiter and &quot; as quotation character
     */
    public QuotedStringTokenizer()
    {
        this( ' ', '\"' );
    }

    /**
     * @param delimiter Delimiter character
     * @param quotation Quotation character
     */
    public QuotedStringTokenizer( char delimiter, char quotation )
    {
        this.delimiter = delimiter;
        this.quotation = quotation;
    }

    /**
     * Parse given string into segments
     * 
     * @param input Given string input
     * @return List of segments
     */
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
                        buf.append( c );
                        state = ParsingState.QUOTED;
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
