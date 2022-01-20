package nl.bioinf.java_wrapper;

import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.*;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WekaRunner {
    private final String modelFile = "src/main/resources/cost_sensitive_voting_naiveBayes_IBk_randomForest.model";
    private final String dataFile = "src/main/resources/template_instance.arff";

    CostSensitiveClassifier classifier;

    public WekaRunner() throws Exception {
        try {
            this.classifier = (CostSensitiveClassifier) SerializationHelper.read(this.modelFile);
        } catch (Exception e) {
            throw new Exception("Could not load classifier");
        }
    }


    /**
     * Classifies an instance based on input array. Array should consist of 9 integer values ranging from 1 to 10 and -1
     * @param instanceValue Array should consist of 9 integer values may range from 1 to 10 or be -1
     * @return
     * @throws Exception
     */
    public String classifyInstance(int[] instanceValue) throws Exception {
        try {
            Instances unknownInstances = loadArff(this.dataFile);
            Instance newInstance = new DenseInstance(10);
            unknownInstances.add(newInstance);

            for (int i = 0; i < 9; i++) {
                if (instanceValue[i] == -1) {
                    continue;
                } else {
                    unknownInstances.instance(unknownInstances.numInstances() - 1).setValue(i, instanceValue[i]);
                }
            }
//            Classify instance
            double classification = this.classifier.classifyInstance(unknownInstances.instance(0));
            unknownInstances.instance(0).setClassValue(classification);
//            Return the class value
            return unknownInstances.instance(0).toString(unknownInstances.classIndex());
        } catch (Exception e) {
            throw new Exception("Could not classify instance");
        }
    }


    /**
     * Classifies instances from input arff file without class label and saves those instances with the class label
     * added to them to a new output file.
     * @param arffInputFile arff file with unclassified instances to classify
     * @throws Exception throws exception when something goes wring in reading from arff input file,
     *          or if something goes wring with opening or writing to the output file
     */
    public String classifyFromFile(String arffInputFile) throws Exception {
        Instances labeled;
        String outputFileName = "output_data/output_" + getDate() + ".arff";

        labeled = getLabeled(arffInputFile);
        saveLabeledInstancesToFile(labeled, outputFileName);

        return outputFileName;
    }


    /**
     * Saves labeled instances in arff file
     * @param labeled An Instances object with labeled instances
     * @param outputFileName pathname of output file where labeled instances should be stored
     * @return name of output file where the instances are saved.
     * @throws Exception
     */
    private void saveLabeledInstancesToFile(Instances labeled, String outputFileName) throws Exception {
        ArffSaver saver;

//        Try saving the labeled instances to output file
        try {
            saver = new ArffSaver();

//            Change error stream to avoid ugly messages in cli
            PrintStream oldErr = System.err;
            PrintStream newErr = new PrintStream(new ByteArrayOutputStream());
            System.setErr(newErr);
//            set instances
            saver.setInstances(labeled);
//            change error stream back to normal
            System.setErr(oldErr);
//
            File outputFile = new File(outputFileName);
            if (outputFile.createNewFile()) {
                saver.setFile(outputFile);
                saver.writeBatch();
            } else {
                throw new Exception("Output file already exists");
            }
        } catch (Exception e) {
            throw new Exception("Could not open a file with the name of " + outputFileName);
        }
    }


    /**
     * gets instances from input file and returns labeled instances as Instances object.
     * @param arffFileName arff file with unlabeled instances
     * @return Instances with class label added
     * @throws Exception
     */
    private Instances getLabeled(String arffFileName) throws Exception {
        Instances labeled;
//        Try loading the given file name
        try {
            Instances unknownInstances = this.loadArff(arffFileName);
            labeled = new Instances(unknownInstances);

//            Label the instances from the file
            for (int i = 0; i < unknownInstances.numInstances(); i++) {
                double classLabel = this.classifier.classifyInstance(unknownInstances.instance(i));
                labeled.instance(i).setClassValue(classLabel);
            }
        } catch (IOException e) {
            throw new Exception("Could not read arff file, please make sure the path is correct.");
        } catch (Exception e) {
            throw new Exception("Unexpected error occurred");
        }
        return labeled;
    }


    /**
     * Gets information from arff file and transforms that information to an Instances object that is returned.
     * @param datafile arff file
     * @return Instances read from the arff file
     * @throws IOException
     */
    private Instances loadArff(String datafile) throws IOException {
        try {
            DataSource source = new DataSource(datafile);
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            return data;
        } catch (Exception e)  {
            throw new IOException("could not read from file");
        }
    }


    /**
     * Gets current date and time in a fomrat that can be inserted in a file name.
     * @return String with date and time in format yyyy_MM_dd_HH_mm_ss
     */
    private static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
