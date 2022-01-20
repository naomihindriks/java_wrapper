package nl.bioinf.java_wrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OptionArgumentParser {
    private int[] instanceOutputValue;
    private String fileOutputValue;

    private static String REQUIRED_HEADER = "@relation Filtered.Breast.Cancer.Wisconsin..Original..Data.Set\n" +
            "@attribute clump.thick numeric\n" +
            "@attribute uni.cell.size numeric\n" +
            "@attribute uni.cell.shape numeric\n" +
            "@attribute marg.adhesion numeric\n" +
            "@attribute single.epith.cell.size numeric\n" +
            "@attribute bare.nuclei numeric\n" +
            "@attribute bland.chrom numeric\n" +
            "@attribute norm.nucleoli numeric\n" +
            "@attribute mitoses numeric\n" +
            "@attribute class {'Malignant','Benign'}\n" +
            "@data\n";


    /**
     * validates if inputValue string is valid instance
     * @param inputValue that can potentially be processed to instance
     * @return boolean
     */
    private boolean validateInputInstance(String inputValue) {
        return inputValue.matches("([1-9]?,|(10)?,){8}([1-9]|(10))?");
    }


    /**
     * Process a string to instanceOutputValue (integer array), empty set to -1
     * @param inputValue a string representing an integer
     */
    private void processInputInstance(String inputValue) {
//        Splitting the string on "," if there is no value an empty string will be added because limit is set to -1
        String[] instanceInputArray = inputValue.split(",", -1);
        int[] integerArray = new int[9];

//        Parsing string numbers to integers, if there is an emnpty string number will be set to -1 will be added
        for (int i = 0; i < instanceInputArray.length; i++) {
            if (!instanceInputArray[i].equals("")) {
                integerArray[i] = Integer.parseInt(instanceInputArray[i]);
            } else {
                integerArray[i] = -1;
            }
        }
        this.instanceOutputValue = integerArray;
    }


    private static boolean isArffFile(String fileName) {
        return fileName.toLowerCase().endsWith(".arff");
    }


    private static boolean fileCanBeOpened(String fileName) {
        try {
            File tempFile = new File(fileName);
            Scanner myReader = new Scanner(tempFile);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    private static boolean isValidFileInstance(String instanceInFile) {
        return instanceInFile.matches("([1-9]?,|(10)?,){8}([1-9]|(10))?,\\?");
    }


    private static boolean isValidFileHeader(String headerToTest) {
        return headerToTest.equals(REQUIRED_HEADER);
    }


    private static boolean fileHasCorrectContent(String fileName) {
        StringBuilder header = new StringBuilder();
        boolean allInstancesAreValid =true;

        try {
            File tempFile = new File(fileName);
            Scanner myReader = new Scanner(tempFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
//                Place lines that start with @ in header variable
                if (data.startsWith("@") || data.isBlank()) {
                    header.append(data.strip()).append("\n");
                    continue;
                }
//                Check if instances in file are correctly formatted
                if (!isValidFileInstance(data)) {
                    allInstancesAreValid = false;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }

//        return true if all instances are correct and the header is correct
        if (allInstancesAreValid && isValidFileHeader(header.toString())) {
            return true;
        }
        return false;
    }


    /**
     * Validates whether a string is a valid fileValue and would be allowed to set to fileOutputValue.
     * Checks if file exists and can be read and if content of the file is of correct format.
     * @param inputValue value to be checked
     * @return
     */
    private boolean validateInputFile(String inputValue) {
        if(isArffFile(inputValue) &&
                fileCanBeOpened(inputValue) &&
                fileHasCorrectContent(inputValue)
        ) {
            return true;
        }
        return false;
    }


    /**
     * Sets this.inputValue to inputValue argument
     * @param inputValue String that should be set to this.inputValue
     */
    private void processInputFile(String inputValue) {
        this.fileOutputValue = inputValue;
    }


    /**
     * Checks the input that is given, validates input and if data is valid sends for further processing
     * @param inputType InpuType specifying how inputValue should be handled
     * @param inputValue String value that has to be parsed/processed
     * @throws Exception when input is not valid
     */
    public void parse(InputType inputType, String inputValue) throws Exception {
        if (inputType == InputType.INSTANCE) {
            if (this.validateInputInstance(inputValue)) {
                this.processInputInstance(inputValue);
            } else {
                throw new Exception("Instance does not have the correct format");
            }
        } else if (inputType == InputType.FILE) {
            if (this.validateInputFile(inputValue)) {
                this.processInputFile(inputValue);
            } else {
                throw new Exception("File could not be validated, the file itself or the path to the file might be wrong");
            }
        }
    }


    public int[] getInstanceOutputValue() {
        if (this.instanceOutputValue == null) {
            throw new NullPointerException("No instanceOutputValue can be found. " +
                    "Check if input value has been parsed and inputType is set to INSTANCE");
        }
        return this.instanceOutputValue;
    }


    public String getFileOutputValue() {
        if (this.fileOutputValue == null) {
            throw new NullPointerException("No fileOutputValue can be found. " +
                    "Check if input value has been parsed and inputType is set to FILE");
        }
        return this.fileOutputValue;
    }

}
