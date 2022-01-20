# README: Breast Cancer Classifier #

This is a CLI application to classify fine needle aspirates from breast tissue scored as explained in [this document](https://www.rai-light.com/docs/BCD_User_Manual_v01.pdf).

The process in which this classifier was build and the evaluation of this classifier can be found on [this repository](https://github.com/naomihindriks/thema09).

The classifier was trained using the [Breast Cancer Wisconsin
(Original) Data Set](https://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Original%29)


### How do I get set up? ###

- Download this repository to your local computer.
- Unpack the zip file that you downloaded to a place where you can find it. 
- Make sure you have a jvm (java 11) installed.
- Open the folder where you extracted this project in a terminal.
- This is where you run the application.

### Running the application ###

- Open the terminal as described in **How do I get set up?**

#### Get help ####

To get the help in the terminal simply type

`java -jar java_wrapper_weka_classifier.jar --help`

Or

`java -jar java_wrapper_weka_classifier.jar -h`


#### Classify a single instance ####

To classify a single instance run the following command:

`java -jar java_wrapper_weka_classifier.jar -i 1,2,3,4,5,6,7,8,9`

or

`java -jar java_wrapper_weka_classifier.jar --instance 1,2,3,4,5,6,7,8,9`

The result of the classification (either Benign or Malignant) will be printed in the terminal. It is important to give this exact format for your instance. The order of the attributes matters. The order in which the attributes are needed from left to right are:

- Clump Thickness
- Uniformity of Cell Size
- Uniformity of Cell Shape
- Marginal Adhesion
- Single Epithelial Cell Size
- Bare Nuclei
- Bland Chromatin
- Normal Nucleoli
- Mitoses

The score for each attribute should be an integer value belonging to [1,10] interval (or be empty see below).

If you have a missing value for attributes simply skip the number and type the next comma. In the following example the first (Clump Thickness) and fourth (Marginal Adhesion) attribute are missing. If you do this it is important to surround the values with quotation marks.

`java -jar java_wrapper_weka_classifier.jar --i ",2,3,,5,6,7,8,9"`

#### Classification from file  ####

For classifying instances in a file you can use flag `-f` or `--file` followed by the file with the instances that need classifying. This file has to be an ARFF file (read more about ARFF files [here](https://waikato.github.io/weka-wiki/formats_and_processing/arff_stable/)). Only ARFF file formatted exactly like the test.arff file in the test_data folder can be processed (except for the attribute values and amount of instances of course). To classify the test.arff file run the following command:

`java -jar java_wrapper_weka_classifier.jar -f test_data/.arff`

After running this command a new file will be saved in the output_data folder. The file will be like the ARFF input file, but with the class label added to each instance. The name of this file will be: output_<time_stamp>.arff. One output file is already present in this folder. This is the output file generated with the test.arff data found in the test_data folder.

### Contact information ###

If you have questions or problems with the application feel free to send an e-mail to:

n.j.hindriks@st.hanze.nl