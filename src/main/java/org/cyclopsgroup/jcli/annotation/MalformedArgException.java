package org.cyclopsgroup.jcli.annotation;

/**
 * This exception is supposed to be thrown by CliParser implementation when argument format turns out to be wrong
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class MalformedArgException
    extends IllegalArgumentException
{
    private static final long serialVersionUID = 1L;

    /**
     * @param msg String description of message
     * @param cause Cause of exception
     */
    public MalformedArgException( String msg, Throwable cause )
    {
        super( msg, cause );
    }

    /**
     * @param msg String description of message
     */
    public MalformedArgException( String msg )
    {
        super( msg );
    }
}
