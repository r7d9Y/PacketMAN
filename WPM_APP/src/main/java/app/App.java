package app;

import org.json.JSONObject;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command(
        name = "WPM",
        mixinStandardHelpOptions = true
)
public class App implements Runnable {

    static final Path APP_DIR = getAppDir();
    static final String PATH_TO_CONFIG = APP_DIR + "\\config\\configuration.json";
    static final int EXIT_CODE_IF_NO_CONFIGURATION_FILE = 10;
    private static String APP_NAME = "WPM_APP";
    private static String VERSION;

    private static String VERSION_DATE;

    static JSONObject configFile = readInConfigurationFile();

    //------------------------------------------------------------------------------------------------------------------

    public static String getAppName() {
        return APP_NAME;
    }

    public static String getVERSION() {
        return VERSION;
    }

    public static String getVersionDate() {
        return VERSION_DATE;
    }

    //------------------------------------------------------------------------------------------------------------------

    static Path getAppDir() {
        // Get the path of the running .jar or .class file
        Path jarPath;
        try {
            jarPath = Paths.get(App.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("File Path: " + jarPath);

        /* Get the directory where the file is saved and return */
        return jarPath.getParent();
    }

    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new App());
        Map<String, String> subcommands = new HashMap<>();

        loadInternalSubcommands(subcommands, commandLine);
        LoadExternalSubcommands(subcommands, commandLine);

        System.out.println(subcommands);

        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }

    public void run() {

        String AppInfo = String.format("%s %s", APP_NAME, VERSION);
        System.out.println(AppInfo);

    }


    //------------------------------------------------------------------------------------------------------------------
    private static void LoadExternalSubcommands(Map<String, String> subcommands, CommandLine commandLine) {
        Path path = Paths.get(APP_DIR.toString() + "\\" + configFile.getString("PATH_OF_SUBCOMMANDS_CONFIGURATION_FILE"));
        try {
            String jsonContent = new String(Files.readAllBytes(path));

            // Get all entries from subcommand json file
            JSONObject subcommandsFound = new JSONObject(jsonContent).getJSONObject("subcommands");

            for (String key : subcommandsFound.keySet()) {
                String value = subcommandsFound.getString(key);

                String executablePath = APP_DIR + "\\" + subcommandsFound.get(key);
                System.out.println("exec path " + executablePath);
                commandLine.addSubcommand(key, new ProxyCommand(executablePath, "", false));


                subcommands.put(key, value);
            }
        } catch (IOException eIO) {
            System.err.println("Problem with reading in subcommand info file -> " + eIO.getMessage());
        }
    }

    private static void loadInternalSubcommands(Map<String, String> subcommands, CommandLine commandLine) {
        List<String> internalCommands = new ArrayList<>(InternalCommandMapper.getMethodMap().keySet());
        for (String command : internalCommands) {
            //System.out.println("internal option :" + command);
            subcommands.put(command, "");
            commandLine.addSubcommand(command, new ProxyCommand("", command, true));
        }
    }

    private static JSONObject readInConfigurationFile() {

        try {
            Path configPath = Paths.get(PATH_TO_CONFIG);
            String jsonContent = new String(Files.readAllBytes(configPath));
            JSONObject configurationJsonObj = new JSONObject(jsonContent);
            APP_NAME = configurationJsonObj.getString("APP_NAME");
            VERSION = configurationJsonObj.getString("VERSION");
            VERSION_DATE = configurationJsonObj.getString("VERSION_DATE");
            return configurationJsonObj;
        } catch (IOException e) {
            System.err.println("Couldn't find or read in " + APP_NAME + " configuration file, exiting... ");
            System.exit(EXIT_CODE_IF_NO_CONFIGURATION_FILE);
        }
        return null;
    }
}