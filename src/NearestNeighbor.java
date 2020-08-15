import java.io.File;
import java.io.IOException;
import java.util.Scanner;

//------------------------------------------------------------------
// Garrett McCue 
// 08/13/20
// SU2-CPSC-501-002
// Programming Fundamentals
// Program 3
//
// Using the NearestNeighbor algorithm, this reads in a training data
// set (iris-training-data.csv) and predicts the label for the 
// values in the testing data set (iris-testing-data.csv). Calculates
// and returns algorithm accuracy and prints each label and predicted 
// label as well.
//------------------------------------------------------------------
public class NearestNeighbor {

	public static void main(String[] args) throws IOException {
		// Assignment header
		System.out.println("Programming Fundamentals");
		System.out.println("NAME: Garrett McCue");
		System.out.println("PROGRAMMING ASSIGNMENT 3\n");

		// prompts user and gets files from input
		Scanner scanInput = new Scanner(System.in);

		System.out.print("Enter file to train with: "); // make sure files are in project folder
		File trainFile = new File(scanInput.next());

		System.out.print("Enter file to test with: ");
		File testFile = new File(scanInput.next());

		// initializes scanners for each file and data type so data can be read
		Scanner scanTrainLabels = new Scanner(trainFile);
		Scanner scanTestLabels = new Scanner(testFile);
		Scanner scanTrainVals = new Scanner(trainFile);
		Scanner scanTestVals = new Scanner(testFile);

		// initializes String arrays that will hold the labels
		// array sizes were given
		String[] trainLabels = new String[75];
		String[] testLabels = new String[75];
		String[] predictedLabels = new String[75];

		// initializes double arrays that will hold the values
		double[][] trainVals = new double[75][4];
		double[][] testVals = new double[75][4];

		// call getLabels to copy labels into the initialized arrays
		trainLabels = getLabels(scanTrainLabels, trainLabels);
		testLabels = getLabels(scanTestLabels, testLabels);

		// call getValues to copy doubles into corresponding arrays
		trainVals = getVals(scanTrainVals, trainVals);
		testVals = getVals(scanTestVals, testVals);

		// iterates for the specified number of rows and calls
		// getNearestNeighbor to get the NN index
		// uses the index of the NN to find the trainLabel
		// trainLabel is copied into the predictedLabel array at the index of the
		// current loop iteration
		for (int i = 0; i < 75; i++) {
			int indexNN = getNearestNeighbor(i, testVals, trainVals);
			predictedLabels[i] = trainLabels[indexNN];
		}

		// calls getAccuracy to print predicted and true labels
		// also outputs the calculated accuracy
		getAccuracy(predictedLabels, testLabels);

		// close scanners
		scanInput.close();
		scanTrainLabels.close();
		scanTrainVals.close();
		scanTestLabels.close();
		scanTestVals.close();

	}

	// takes in the input file with the labels and the array in which to write them
	// to
	static String[] getLabels(Scanner input, String[] labels) {

		// start the index at the first row
		int labelIndex = 0;
		String[] splitLabels;

		// splits the file one row at a time by the ","
		// assigns the last element of the row (the labels) to the correct label string
		// array
		while (input.hasNextLine()) {
			splitLabels = input.nextLine().split(",");
			labels[labelIndex] = splitLabels[4];
			labelIndex++;

		}
		return labels;

	}

	// takes in the input file with the labels and the array in which to write them
	// to
	// stores
	static double[][] getVals(Scanner input, double[][] values) {
		// start the index at the first row
		int valueRow = 0;

		// creates a 2d string array based on the given data sizes to hold only the
		// values after they have been split since splitting is from the String class
		// and returns a string array
		String[][] splitVals = new String[75][4];

		// loops through the file splitting the values and assigning just the values to
		// the
		// splitVals string before parsing to double in order to get the double value
		// for each
		while (input.hasNextLine()) {
			// creates a new string to hold each line after splitting
			String[] splitLine = input.nextLine().split(",");

			// loops through each value in current row (current split line).
			// i < 4, because there are 4 values to get from the line
			// the values are the first 4 spots, the last is the label
			for (int i = 0; i < 4; i++) {
				// assigns each value in the row to the 2D string array
				splitVals[valueRow][i] = splitLine[i];
				// wraps the splitVals to doubles and assigns to the working doubles array
				// for the values
				values[valueRow][i] = Double.parseDouble(splitVals[valueRow][i]);
			}
			valueRow++;

		}

		// returns the values as doubles in the working 2D double array
		return values;

	}

	// returns the training label index of the nearest neighbor to each testing
	// index
	// calls getDistance to evaluate the distance between the testVals of current
	// row
	// and the rows of trainVals
	static int getNearestNeighbor(int testRow, double[][] test, double[][] train) {
		int indexNN = 0;

		// set the nnDistance equal to the distance of the trainValues first row
		double nnDistance = getDistance(testRow, 0, test, train);

		// loop through all training values (by row) getting the smallest distance for
		// the
		// testRow
		// updates nnDistance if it the new distance is smaller than nnDistance
		// assigns the row index of the new closest row to NNindex in order for the
		// method
		// to return the row index of the NN
		for (int i = 1; i < 75; i++) {
			if (getDistance(testRow, i, test, train) < nnDistance) {

				nnDistance = getDistance(testRow, i, test, train);
				indexNN = i;
			}

		}

		// returns the index of trainVals/trainLabels that is the NN to the testVals at
		// testRow index
		return indexNN;

	}

	// calculates the distance between the given test values and training values
	// takes in the index of both rows in order to calculate. returns the distance
	static double getDistance(int testRow, int trainRow, double[][] test, double[][] train) {
		double distance = Math.sqrt(Math.pow((test[testRow][0] - train[trainRow][0]), 2)
				+ Math.pow((test[testRow][1] - train[trainRow][1]), 2)
				+ Math.pow((test[testRow][2] - train[trainRow][2]), 2)
				+ Math.pow((test[testRow][3] - train[trainRow][3]), 2));

		return distance;

	}

	// prints the header for the columns
	// loops through and prints each row or "EX#"
	// keeps count if the labels match every iteration
	// uses count to calculate the accuracy
	// prints accuracy
	static void getAccuracy(String[] labelPredicted, String[] labelTrue) {
		System.out.println("\nEX#: TRUE LABEL, PREDICTED LABEL ");

		// count for correct prediction
		double correctPredictionNum = 0;
		int exNum = 1;

		for (int i = 0; i < 75; i++) {
			System.out.println(exNum + ": " + labelTrue[i] + " " + labelPredicted[i]);

			if (labelPredicted[i].equals(labelTrue[i])) {
				correctPredictionNum++;
			}
			exNum++;

		}

		// calculates accuracy and prints
		double accuracy = (correctPredictionNum / labelTrue.length);

		System.out.println("ACCURACY: " + accuracy);
	}

}
