/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scoirproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONObject;

/**
 *
 * @author jaghaul
 */
public class SCOIRproject {

    /**
     * @param args the command line arguments
     *
     */
    public static ArrayList<Error> _errorList = new ArrayList();

    public static void main(String[] args) throws FileNotFoundException {

        String inputD = "D:\\Desktop\\SCOIRproject\\src\\input directory\\";
        File inputDirectory = new File(inputD);

        //lists all the files in the input directory
        File[] inputList = inputDirectory.listFiles();

        String outputD = "D:\\Desktop\\SCOIRproject\\src\\output directory\\";
        File outputDirectory = new File(outputD);

        //loop through the input files and see if they exist in the output directory. 
        //if they do not exist, then we process them.
        for (int i = 0; i < inputList.length; i++) {
            String fileName = inputList[i].getName();

            //checks if the file exists in the output directory
            boolean doesExist = new File(outputD, fileName).exists();
          

            if (!doesExist) {
                File in = new File(inputD + fileName + "\\");
                Scanner key = new Scanner(in);
                int j = 1;
                key.nextLine();
                while (key.hasNextLine()) {
                    readHeaders(in, key.nextLine(), j);
                    j++;
                }
                writeToErrorFile(fileName);
                //delete file after we are done reading it
                in.delete();
            }

        }

    }
    /*
     This Method uses the input file as a parameter.
     It reads the headers and splits it up into different fields to construct a JSON object. 
     Once the JSON object is read, the object is written to the output file in the output directory
     Also, errors are saved to the error array list. 
    
     */

    public static void readHeaders(File input, String line, int j) throws FileNotFoundException {

        String InputFileName = input.getName();

        String[] nameSplit = InputFileName.split(".c");
        String outputFileName = nameSplit[0];

        //parses each column of the header into seperate strings
        String[] fields = line.split(",");

        String id = fields[0];
        int idVal;
        try {
            idVal = Integer.parseInt(id);
        } catch (NumberFormatException n) {
            idVal = 1;
            Error error = new Error("NumberFormatException: id consists of characters and not just numbers", j);
            _errorList.add(error);

        }
        if (idVal <= 0) {

            Error error = new Error("Negative value or zero value for id", j);
            _errorList.add(error);
        }
        int maxIdLength = 8;
        if (id.length() < maxIdLength || id.length() > maxIdLength) {
            Error error = new Error("Impproper id length", j);
            _errorList.add(error);

        }
        String firstName = fields[1];
        int maxFirstNameLength = 15;
        if (firstName.length() >= maxFirstNameLength) {

            Error error = new Error("First name too long", j);
            _errorList.add(error);
        }
        if (firstName.equals("")) {

            Error error = new Error("No first name given", j);
            _errorList.add(error);
        }
        String middleName = fields[2];
        int maxmiddleNameLength = 15;
        if (middleName.length() >= maxmiddleNameLength) {

            Error error = new Error("Middle name too long", j);
            _errorList.add(error);
        }
        String lastName = fields[3];
        int maxLastNameLength = 15;
        if (lastName.length() >= maxLastNameLength) {
            Error error = new Error("Last name too long", j);
            _errorList.add(error);
        }
        if (lastName.equals("")) {
            Error error = new Error("No last name given", j);
            _errorList.add(error);
        }
        String phoneNumber = fields[4];
        int numberLength = 12;
        char spotFour = phoneNumber.charAt(3);
        char spotEight = phoneNumber.charAt(7);
        if (phoneNumber.length() < numberLength) {

            Error error = new Error("phone number is too long", j);
            _errorList.add(error);
        }
        if (spotFour != '-' || spotEight != '-') {

            Error error = new Error("Forgot hyphens in spots four and seven", j);
            _errorList.add(error);
        }

        //process of turning header into a JSON object 
        JSONObject user = new JSONObject();

        user.put("id", id);

        JSONObject name = new JSONObject();
        name.put("first", firstName);
        name.put("middle", middleName);
        name.put("last", lastName);

        //adds the name object to the user object
        user.put("name", name);

        user.put("phone", phoneNumber);

        PrintWriter p = new PrintWriter("D:\\Desktop\\SCOIRproject\\src\\output directory\\" + outputFileName + ".json");
        p.write(user.toString());

        p.close();
    }
    /*
     This method creates an error file in the error directory and adds all of the errors from the error list to the file. 
     */

    public static void writeToErrorFile(String fileName) throws FileNotFoundException {

        String[] nameSplit = fileName.split(".cs");

        String outputFileName = nameSplit[0];

        File errorDirectory = new File("D:\\Desktop\\SCOIRproject\\src\\error directory\\");
        String errorD = "D:\\Desktop\\SCOIRproject\\src\\error directory\\";

        if (_errorList.size() > 0) {

            //creates a writer for the error csv. 
            File errors = new File(errorD + outputFileName + ".csv\\");
            PrintWriter e = new PrintWriter(errors);

            e.print("Line_NUM,");
            e.print("ERROR_MSG \n");
            int i = _errorList.size() - 1;
            while (i > 0) {
                e.print(_errorList.get(i).getMessage() + ",");
                e.print(_errorList.get(i).getLineNum() + "\n");

                i--;
            }

            e.println("Total Errors," + _errorList.size());
            e.close();
        }
    }
}
