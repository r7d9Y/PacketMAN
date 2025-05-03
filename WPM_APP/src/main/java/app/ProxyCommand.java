package app;

import picocli.CommandLine.Command;
import java.io.IOException;

/**
 * The ProxyCommand class represents a subcommand that can be executed either internally
 * or externally (externally means an added module). It implements the {@link Runnable} interface
 */
@Command
public class ProxyCommand implements Runnable {

    /**
     * The path to the external executable file, needed only for external commands
     */
    private final String path;

    /**
     * The name of the internal command
     */
    private String command;

    /**
     * Indicates whether the command is internal
     */
    private final boolean isInternal;

    /**
     * Constructs a new ProxyCommand instance
     *
     * @param executablePath The path to the external executable file
     * @param command        The name of the internal command
     * @param isInternal     Whether the command is internal
     */
    public ProxyCommand(String executablePath,String command, boolean isInternal) {
        this.path = executablePath;
        this.isInternal = isInternal;
        this.command = command;
    }


    /**
     * Executes the command. If the command is internal, it runs the corresponding action
     * If the command is external, it starts the external process
     */
    @Override
    public void run() {
        if (!isInternal) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(path);
                processBuilder.inheritIO(); // Pass input/output to the current process
                Process process = processBuilder.start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                System.err.println("Couldn't start subcommand external (sub)command -> " + e.getMessage());
            }
        } else {
            InternalCommandMapper.getMethodMap().get(command).run();
        }
    }
}
