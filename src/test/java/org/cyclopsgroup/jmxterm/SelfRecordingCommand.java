package org.cyclopsgroup.jmxterm;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;

@Cli( name = "test" )
public class SelfRecordingCommand
    extends Command
{
    private String[] arguments;

    private final List<Command> records;

    public SelfRecordingCommand( List<Command> records )
    {
        this.records = records;
    }

    @Override
    public void execute( Session session )
        throws Exception
    {
        records.add( this );
    }

    public String getArgs()
    {
        return StringUtils.join( arguments, ' ' );
    }

    public String[] getArguments()
    {
        return arguments;
    }

    @Argument
    public void setArguments( String[] arguments )
    {
        this.arguments = arguments;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + ":" + getArgs();
    }
}
