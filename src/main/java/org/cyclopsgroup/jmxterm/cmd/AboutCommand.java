package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;

import javax.management.JMException;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.JavaProcessManager;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

/**
 * Command to show about page
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "about", description = "Display about page" )
public class AboutCommand
    extends Command
{
    private boolean showDescription;

    /**
     * @inheritDoc
     */
    @Override
    public void execute( Session session )
        throws IOException, JMException
    {
        JavaProcessManager jpm = JavaProcessManager.getInstance();
        SyntaxUtils.printExpression( session.output, "jpm.type", jpm.getClass().getName(),
                                     "Type of JavaProcessManager implementation", 0, showDescription );
        SyntaxUtils.printExpression( session.output, "jpm.name", jpm.getName(),
                                     "Name of JavaProcessManager implementation", 0, showDescription );
        SyntaxUtils.printExpression( session.output, "jpm.description", jpm.getDescription(),
                                     "Description of JavaProcessManager implementation", 0, showDescription );
    }

    /**
     * @param showDescription True to show detail description
     */
    @Option( name = "s", longName = "show", description = "Show detail description" )
    public final void setShowDescription( boolean showDescription )
    {
        this.showDescription = showDescription;
    }
}
