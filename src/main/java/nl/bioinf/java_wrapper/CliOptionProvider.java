package nl.bioinf.java_wrapper;


import org.apache.commons.cli.*;

public class CliOptionProvider {

    private final String[] arguments;
    private Options helpOptions;
    private Options options;
    private CommandLine commandLine;

//    TODO write better descriptions
//    Create the option objects, so changing names etc will only be necessary in this place
    private static final Option helpOption = new Option("h", "help", false, "Prints help message");
    private static final Option instanceOption = new Option("i", "instance", true, "Instance to classify");
    private static final Option fileOption = new Option("f", "file", true, "File location with instances to classify");

    private boolean helpRequested = false;
    private InputType inputType = InputType.NONE;
    private String inputValue = "";

    /**
     * constructs with the command line arguments.
     *
     * @param arguments the CL array
     */
    public CliOptionProvider(String[] arguments) {
        this.arguments = arguments;
        this.AddOptions();
    }


    /**
     * Method that adds the all the options to this.helpOptions and this.options
     */
    private void AddOptions() {
//        Using two options objects so input can be required, except when needing help
        this.helpOptions = new Options();
        this.options = new Options();


//        Make option group for input (instance or file)
        OptionGroup inputGroup = new OptionGroup();
        inputGroup.addOption(instanceOption);
        inputGroup.addOption(fileOption);
        inputGroup.setRequired(true);

//        Add the options to the Options objects (both hold all the options to avoid ParseException,
//        and get all the options specified when printing help)
        options.addOption(helpOption);
        options.addOptionGroup(inputGroup);

        helpOptions.addOption(helpOption);
//        In the help option the instanceOption and fileOption are not required (nor processed) so the inputGroup is
//        not used (that would only cause a parseError when requesting help)
        helpOptions.addOption(instanceOption);
        helpOptions.addOption(fileOption);
    }


    /**
     * Processes the options in this.options, checks wich option is present and sets this.inputType and
     * this.inputValue accordingly
     * @throws ParseException
     */
    private void processInput() throws ParseException {
        CommandLineParser parser = new DefaultParser();
        this.commandLine = parser.parse(this.options, this.arguments);

//            Print Warning message when there are loose arguments given in the commandline.
        if (commandLine.getArgList().size() != 0) {
            throw new ParseException("Illegal loose argument found in cli args.");
        }

        if (commandLine.hasOption(CliOptionProvider.instanceOption.getLongOpt())) {
            this.inputType = InputType.INSTANCE;
            this.inputValue = commandLine.getOptionValue(CliOptionProvider.instanceOption.getLongOpt());
        } else if(commandLine.hasOption(CliOptionProvider.fileOption.getLongOpt())) {
            this.inputType = InputType.FILE;
            this.inputValue = commandLine.getOptionValue(CliOptionProvider.fileOption.getLongOpt());
        }

    }

    /**
     * Checks if help is requested, sets the helpRequested attribute
     * @throws ParseException
     */
    private void processHelpRequested() throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(this.helpOptions, this.arguments, false);

        if (cmd.hasOption(CliOptionProvider.helpOption.getOpt()) || cmd.hasOption(CliOptionProvider.helpOption.getLongOpt())) {
            this.helpRequested = true;
        }
    }

    /**
     * processes the command line arguments. If the argument -h or --help is set the other aguments are not
     * parsed anymore.
     */
    public void processCommandLine() throws ParseException {
        this.processHelpRequested();
        if (!this.helpRequested) {
            this.processInput();
            this.processInput();
        }
    }

    /**
     * returns helpRequested attribute
     * @return a boolean indicating if the helpOption is set in the command line
     */
    public boolean getHelpRequested() {
        return this.helpRequested;
    }

    public InputType getInputType() {
        return this.inputType;
    }

    public String getInputValue() {
        return this.inputValue;
    }


    /**
     * Prints help message
     */
    public void printHelp() {
//        TODO write better help
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java wrapper\n", this.options);
    }

}
