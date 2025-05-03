package app;

import commands.Help;
import commands.Version;

import java.util.Map;

/**
 * The InternalCommandMapper class maps internal commands to their corresponding actions
 * It provides a method to retrieve the mapping of commands to runnable actions
 */
public class InternalCommandMapper {

    /**
     * A map of internal command names to their corresponding {@link Runnable} actions
     */
    private static final Map<String, Runnable> methodMap = Map.ofEntries(
            Map.entry("Help", Help::printOutHelpOptionMessage),
            Map.entry("Version", Version::printVersion)
    );

    /**
     * Retrieves the map of internal commands and their corresponding actions
     * @return A map of command names to {@link Runnable} actions
     */
    public static Map<String, Runnable> getMethodMap() {
        return methodMap;
    }


}
