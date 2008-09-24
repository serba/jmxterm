package org.cyclopsgroup.jmxterm.boot;

import java.io.IOException;

import jline.ConsoleReader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * Implementation of input that reads command from jloin console input
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class JlineCommandInput
    extends CommandInput
{
    private final ConsoleReader console;

    private final String prompt;

    /**
     * @param console Jline console reader
     * @param prompt Prompt string
     */
    JlineCommandInput( ConsoleReader console, String prompt )
    {
        Validate.notNull( console, "Jline console reader can't be NULL" );
        this.console = console;
        this.prompt = StringUtils.trimToEmpty( prompt );
    }

    /**
     * @inheritDoc
     */
    @Override
    public String readLine()
        throws IOException
    {
        return console.readLine( prompt );
    }

}
