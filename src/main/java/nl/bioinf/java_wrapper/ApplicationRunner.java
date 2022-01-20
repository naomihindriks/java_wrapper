package nl.bioinf.java_wrapper;

import org.apache.commons.cli.ParseException;

import java.util.Arrays;

public class ApplicationRunner {
    private int[] instanceValue;
    private String fileValue;

    private CliOptionProvider cliOptionProvider;
    private OptionArgumentParser optionArgumentParser;
    private WekaRunner wekaRunner;


    private ApplicationRunner(String[] args) {
        this.cliOptionProvider = new CliOptionProvider(args);
        this.optionArgumentParser = new OptionArgumentParser();
        try {
            this.wekaRunner = new WekaRunner();
        } catch (Exception e) {
            System.out.println("Something went wrong running the classifier, please make sure installation is correct.");
        }
    }


    /**
     * Returns this.instanceValue as string for easy printing of that value.
     * @return this.instanceValue as string
     */
    private String getInputValueAsString() {
        String arrayAsString = Arrays.toString(this.instanceValue);
        String[] newArray = arrayAsString.substring(1, arrayAsString.length() - 1).split(",");
        for (int i = 0; i < newArray.length; i++) {
//            If array contains -1 (missing value) replace with empty string.
            if(newArray[i].strip().equals("-1")) {
                newArray[i] = "";
            } else {
                newArray[i] = newArray[i].strip();
            }
        }
        return Arrays.toString(newArray);
    }


    /**
     * Classify instance from this.instanceValue and prints result to to cli. If classification fails prints will print
     * error message and help message from this.cliOptionProvider
     */
    private void classifyInstance() {
        try {
            System.out.println(
                    "Given instance with values of: " +
                            this.getInputValueAsString() +
                            " is classified as " +
                            this.wekaRunner.classifyInstance(this.instanceValue)
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.cliOptionProvider.printHelp();
        }
    }


    /**
     * Classifies instances from this.fileValue. Result of classification will be printed to output file, the name
     * of the output file will be printed to the cli. If classification fails will print error message and the
     * help message from this.cliOptionProvider
     */
    private void classifyFromFile() {
        try {
            String labeledFileName = this.wekaRunner.classifyFromFile(this.fileValue);
            System.out.println("instances from file (" + this.fileValue + ") are classified. Classified instances can be found in " + labeledFileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.cliOptionProvider.printHelp();
        }
    }


    /**
     * Method that takes care of running the application, doing the correct action for different inputs.
     */
    private void run() {
        try {
            this.cliOptionProvider.processCommandLine();

            if (this.cliOptionProvider.getHelpRequested()) {
                this.cliOptionProvider.printHelp();
            } else {
                InputType inputType = this.cliOptionProvider.getInputType();
                String inputValue = this.cliOptionProvider.getInputValue();

                optionArgumentParser.parse(inputType, inputValue);

                if (inputType == InputType.INSTANCE) {
                    this.instanceValue = optionArgumentParser.getInstanceOutputValue();
                    this.classifyInstance();
                } else if (inputType == InputType.FILE) {
                    this.fileValue = optionArgumentParser.getFileOutputValue();
                    this.classifyFromFile();
                } else if (inputType == InputType.NONE) {
                    System.out.println("No input instance or file found");
                    this.cliOptionProvider.printHelp();
                }
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            cliOptionProvider.printHelp();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            cliOptionProvider.printHelp();
        }
    }


    /**
     * Entry point of cli application
     * @param args
     */
    public static void main(String[] args) {
        ApplicationRunner app = new ApplicationRunner(args);
        app.run();
    }

}
