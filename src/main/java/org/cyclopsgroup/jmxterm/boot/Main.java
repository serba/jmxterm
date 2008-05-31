package org.cyclopsgroup.jmxterm.boot;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.cyclopsgroup.jmxterm.impl.CommandCenter;

public class Main
{
    public static final void main( String[] args )
        throws Exception
    {
        System.out.println( "Welcome to JMX terminal. Type \"help\" for available commands." );

        CommandCenter commandCenter = new CommandCenter( System.out );
        ByteBuffer buffer = ByteBuffer.allocate( 1 << 16 );
        prompt( commandCenter );
        byte b;
        for ( ;; )
        {
            b = (byte) System.in.read();
            if ( b == 10 )
            {
                byte[] chunk = new byte[buffer.position()];
                buffer.flip();
                buffer.get( chunk );
                String command = new String( chunk );
                try
                {
                    commandCenter.execute( command );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
                if ( commandCenter.isClosed() )
                {
                    break;
                }
                else
                {
                    buffer.clear();
                    prompt( commandCenter );
                }
            }
            else
            {
                buffer.put( b );
            }
        }
        System.out.println( "Bye" );
    }

    private static void prompt( CommandCenter center )
        throws IOException
    {
        System.out.print( center.getPath() + ": " );
    }
}
