package org.cyclopsgroup.jmxterm;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.cyclopsgroup.jmxterm.WeakCastUtils;
import org.junit.Test;

public class WeakCastUtilsTest
{
    @Test
    public void testCase()
        throws SecurityException, NoSuchMethodException
    {
        SizeAware s = WeakCastUtils.cast( new ArrayList<Integer>( Arrays.asList( 1, 2, 3 ) ), SizeAware.class );
        assertEquals( 3, s.size() );
    }
}
