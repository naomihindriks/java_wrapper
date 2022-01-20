//package nl.bioinf.java_wrapper;
//
//import org.apache.commons.cli.CommandLine;
//
//public class WekaRunner {
//
//    public static void main(String[] args) throws IllegalArgumentException{
//        WekaRunner wekaRun = new WekaRunner();
//        wekaRun.start(args);
//    }
//
//    public void start(String[] args) throws IllegalArgumentException {
//        CliOptionProvider myArgumentParser = new CliOptionProvider(args);
//        CommandLine options = myArgumentParser.parseOptions();
//
//        // If the program has no parameters, the user needs some help because this wont get hem/her/sher/shem any further
//        if (!options.hasOption("file") && !options.hasOption("instance")) {
//            ap.printHelp();
//        }
//
//        // If the user wants to parse an instance
//        if (options.hasOption("instance")) {
//            System.out.println("Single instance selected");
//            // Save the instance parsed by user
//            String newInstance = options.getOptionValue("instance");
//            // Write a temporary file
//            ArffWriter ar = new ArffWriter(newInstance);
//            // parse file to classifier
//            WekaClassifier wC = new WekaClassifier(ar.getFilename());
//            // profit
//        }
//
//        // So the user wants to input a file, the first thing to do is to make sure the file gets to the right place
//        if (options.hasOption("file")) {
//            System.out.println("File input selected");
//            // save filename to variable
//            String filename = options.getOptionValue("file");
//            // parsre filename to classifier
//            WekaClassifier wC = new WekaClassifier(filename);
//            // profit
//        }
//        if (options.hasOption("file") && options.hasOption("instance")){
//            System.out.println("crash you shall");
//        }
//    }
//}
