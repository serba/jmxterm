package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.List;

import javax.management.MalformedObjectNameException;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

public class ChdirCommand
    implements Command
{
    public void execute( List<String> args, Session session )
        throws IOException, MalformedObjectNameException
    {
        if ( args.isEmpty() )
        {
            return;
        }
        String dir = args.get( 0 );
        if ( dir.equals( ".." ) )
        {
            if ( session.getBean() != null )
            {
                session.setBean( null );
            }
            else if ( session.getDomain() != null )
            {
                session.unsetDomain();
            }
            return;
        }
        if ( session.getDomain() == null )
        {
            DomainCommand.changeDomain( dir, session );
        }
        else if ( session.getBean() == null )
        {
            BeanCommand.selectBean( dir, session );
        }
    }
}
