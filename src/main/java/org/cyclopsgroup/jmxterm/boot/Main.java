package org.cyclopsgroup.jmxterm.boot;

import java.io.PrintWriter;
import java.nio.ByteBuffer;

import org.cyclopsgroup.jmxterm.impl.CommandCenter;

public class Main
{
    public static final void main( String[] args )
        throws Exception
    {
        CommandCenter commandCenter = new CommandCenter( new PrintWriter( System.out, true ) );
        ByteBuffer buffer = ByteBuffer.allocate( 1 << 16 );
        commandCenter.prompt();
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
                commandCenter.execute( command );
                if ( commandCenter.isClosed() )
                {
                    break;
                }
                else
                {
                    buffer.clear();
                    commandCenter.prompt();
                }
            }
            else
            {
                buffer.put( b );
            }
        }
    }
}
