package org.cyclopsgroup.jcli;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;

@Cli( name = "sample", description = "A test" )
public class SampleBean
{
    private boolean booleanField;

    private int intField;

    private String stringField1;

    private String stringFIeld2;

    private List<String> values;

    public final int getIntField()
    {
        return intField;
    }

    public final String getStringField1()
    {
        return stringField1;
    }

    public final String getStringFIeld2()
    {
        return stringFIeld2;
    }

    public final List<String> getValues()
    {
        return values;
    }

    public final boolean isBooleanField()
    {
        return booleanField;
    }

    @Option( name = "b", longName = "boolean", description = "Test boolean field" )
    public final void setBooleanField( boolean booleanField )
    {
        this.booleanField = booleanField;
    }

    @Option( name = "i", longName = "tint", description = "Test int value" )
    public final void setIntField( int intField )
    {
        this.intField = intField;
    }

    @Option( name = "f", longName = "field1", required = true )
    public final void setStringField1( String stringField1 )
    {
        this.stringField1 = stringField1;
    }

    @Option( name = "2", longName = "field2" )
    public final void setStringFIeld2( String stringFIeld2 )
    {
        this.stringFIeld2 = stringFIeld2;
    }

    @Argument
    public final void setValues( List<String> values )
    {
        this.values = values;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString( this );
    }
}
