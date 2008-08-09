package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.Map;

import javax.management.JMException;

import org.apache.commons.collections.ExtendedProperties;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.ExtendedPropertiesUtils;
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
    @SuppressWarnings( "unchecked" )
    @Override
    public void execute()
        throws IOException, JMException
    {
        Session session = getSession();
        // output predefined about properties
        ExtendedProperties props =
            ExtendedPropertiesUtils.loadFromOverlappingResources( "META-INF/cyclopsgroup/jmxterm.properties",
                                                                  getClass().getClassLoader() );
        for ( Object entryObject : props.subset( "jmxterm.about" ).entrySet() )
        {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) entryObject;
            SyntaxUtils.printExpression( session.output, entry.getKey(), entry.getValue(), null, 0, false );
        }

        // output Java runtime properties
        for ( Map.Entry<Object, Object> entry : System.getProperties().entrySet() )
        {
            String keyName = entry.toString();
            if ( keyName.startsWith( "java." ) )
            {
                SyntaxUtils.printExpression( session.output, keyName, entry.getValue(), null, 0, false );
            }
        }

        // output runtime JPM configurations
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
