package org.cyclopsgroup.jmxterm.io;

/**
 * Dynamic config for {@link VerboseCommandOutput}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface VerboseCommandOutputConfig
{
    /**
     * @return True if detail message is expected to be printed out
     */
    boolean isVerbose();
}
