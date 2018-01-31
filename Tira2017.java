/*
  Data Structures 2017
  Valtteri Vuori
  415642

  This is the main class of the assignment. Run with "javac Tira2017.java".
 */

import java.io.*;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import DataStruct.*;



public class Tira2017 {

    public static void main(String[] args) {
        Tira2017 ht = new Tira2017();
        

        // Welcome the user, and explain the program.
        System.out.println(" ***** Welcome, human! ***** ");
        System.out.println("This is the course work for Data Structures 2017.");
        System.out.println("This program reads two files setA.txt and setB.txt and provides three output " +
                "files or.txt, and.txt and xor.txt to the user.");
        System.out.println("Please do not feed me numbers higher or lower than 2 147 483 647. I'm sure you know why.");


        // Prompt user for filenames.
        Integer[] setA;
        Integer[] setB;

        System.out.println("For starters, I'll need the two files from you =)");
        System.out.print("File #1: ");
        do {
            setA = ht.readInput(ht.getInput());
        } while (setA == null);

        System.out.print("File #2: ");
        do {
            setB = ht.readInput(ht.getInput());
        } while (setB == null);

        // Info about files, and prompt user if it wants to delete anything as per instructions.
        System.out.println("\nThe files contain " + (setA.length + setB.length) + " items in total.");
        System.out.println("Would you like to delete any items from the output tables? (yes/no)");
        boolean skipDelete = false;
        String answer = ht.getInput();
        if (answer.toLowerCase().equals("no")) {
            System.out.println("Alrighty! Generating AND, OR & XOR files.");
            skipDelete = true;
        }

        // MARK: Actions
        // Goes as follows for each (AND, OR, XOR)
        // 1. Generate table
        // 2. Prompt user if (s)he wants to remove any items from the generated table
        // (2.1. Delete said stuff)
        // 3. Write to corresponding file

        Hashtable table;

        // OR
        table = ht.OR(setA, setB);
        if (!skipDelete){
            System.out.println("An empty input will stop the removal.\n");
            ht.removeFromTable(table, "OR");
        }
        ht.writeOutput(table, "or.txt");

        // AND
        table = ht.AND(setA, setB);
        if (!skipDelete){
            System.out.println("An empty input will stop the removal.\n");
            ht.removeFromTable(table, "AND");
        }
        ht.writeOutput(table, "and.txt");

        // XOR
        table = ht.XOR(setA, setB);
        if (!skipDelete){
            System.out.println("An empty input will stop the removal.\n");
            ht.removeFromTable(table, "XOR");
        }
        ht.writeOutput(table, "xor.txt");
    }


    /**
     * Performs the logical operation union for the two parameter arrays.
     * The value (=second column) contains the information how many time an integer appears in input files.
     * Returns an array with the union of given arrays a and b.
     */
    private Hashtable OR(Integer setA[], Integer setB[]) {

        Hashtable or = new Hashtable();
        addOR(setA, or);
        addOR(setB, or);

        return or;
    }

    /**
     * A helper function to add a set to the OR-table.
     * Increases the number of occurences by 1 if finds the same key already in the table.
     */
    private void addOR(Integer set[], Hashtable hashtable){
        for (Integer index : set)
            if (!hashtable.contains(index))
                hashtable.put(index, 1);
            else
                hashtable.put(index, hashtable.remove(index) + 1);
    }

    /**
     * Performs the intersection for the two parameter arrays.
     * The value in the key-value pairs of the hashtable is the line number the key occurred in the input file for the first time.
     */
    private Hashtable AND(Integer setA[], Integer setB[]) {
        // The final hashtable
        Hashtable and = new Hashtable();
        Hashtable setAtable = new Hashtable();

        // Generate a hashtable containing all items from setA.
        for(int i = 0; i < setA.length; i++)
            if (!setAtable.contains(setA[i]))
                setAtable.put(setA[i], i + 1);

        // Then iterate through the setB. If the hashtable generated previously contains
        // the current item, add it to the "and"-table.
        for (Integer setBindex : setB)
            if (setAtable.contains(setBindex))
                and.put(setBindex, setAtable.get(setBindex));

        return and;
    }

    /**
     * Generates a table from the two sets (A & B) with the logical operator "XOR".
     * The value in the key-value pairs of the hashtable represents which of the two arrays the key is from.
     * 1 = the first, 2 = the second.
     */
    private Hashtable XOR(Integer setA[], Integer setB[]) {
        Hashtable xor = new Hashtable();

        // Generate a hashtable from each set.
        Hashtable aHash = new Hashtable();
        for (Integer x : setA)
            if (!aHash.contains(x))
                aHash.put(x, 1);

        Hashtable bHash = new Hashtable();
        for (Integer x : setB)
            if (!bHash.contains(x))
                bHash.put(x, 2);

        // Iterate through both hashtables. Add the keys that exist only either to the xor-hashtable.
        int[] aKeys = aHash.keys();
        for (int aKey : aKeys)
            if (!bHash.contains(aKey))
                xor.put(aKey, aHash.get(aKey));

        int[] bKeys = bHash.keys();
        for (int bKey : bKeys)
            if (!aHash.contains(bKey))
                xor.put(bKey, bHash.get(bKey));

        return xor;
    }
  
    /**
     * This function performs the deletion of a value from the generated table. Done before writing on file.
     */
    private void removeFromTable(Hashtable table, String name) {
        // Keep bugging the user until no input is given, or the user dies.
        boolean gaveInput = true;
        while(gaveInput) {

            // If the table isn't empty, move forward.
            if (!table.isEmpty()) {
                String input = "";
                int idx = 0;
                boolean acceptedInput = false;


                //Again, keep asking until a valid input (integer or null this time) is given.
                while(!acceptedInput) {
                    System.out.print("Enter a key to be removed from " + name + "-table: ");
                    input = getInput();

                    // Checks if the input is an integer, or a empty line.
                    // After we try to parseInt the user input.
                    // If it's an empty line, it's still accepted as an input!
                    if(!input.isEmpty()) try {
                        idx = Integer.parseInt(input);
                        acceptedInput = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Error! Integers only.");
                    } else {
                        acceptedInput = true;
                        gaveInput = false;
                    }
                }

                // Only if the input contained an integer, do we delete anything.
                if(!input.isEmpty())
                    table.remove(idx);

            } else {
                // If the table is empty, there's not much we can delete, no?
                gaveInput = false;
                System.out.println("The " + name + "-table has no items to remove.");
            }
        }
    }


    /**
     * Reads the numbers from the given file. File is assumed to be in the same folder.
     * Formatting of numbers is assumed to be with one number per line, seperated by a newline.
     *
     */
    private Integer[] readInput(String filename) {
        // A dynamic list to store each item into. Easy to add values to a normal array, and return it.
        ArrayList<Integer> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            // While line has something (eg. file isn't over) split current line into an array,
            // and add each item to the ArrayList
            while(line != null) {
                list.add(Integer.parseInt(line.trim()));
                line = br.readLine();
            }
        } catch(IOException e) {
            System.out.println("Error. Given file not found. Are you sure it is in the correct folder?");
            System.out.print("Try again: ");
        }
        Integer[] arr = new Integer[list.size()];
        list.toArray(arr);
        if (arr.length > 0) {
            System.out.println("Success!");
            return arr;
        }
        else
            return null;
    }

    /**
     * Writes the given hashtable into a given file.
     * One line per key-value pair.
     */
    private void writeOutput(Hashtable hashtable, String filename) {
        // Get keys from given hashtable and sort them.
        int keys[] = hashtable.keys();
        Arrays.sort(keys);


        // Create a buffered writer (from the skeleton file) and write one key-value pair per line
        System.out.println("Writing file...");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for (int key : keys) {
                String outputrow = String.format("%4d %4d", key, hashtable.get(key));
                bw.write(outputrow);
                bw.newLine();
            }

            // Close writer and let user know how many items were written and on what file.
            bw.close();
            System.out.println("Done! File \"" + filename + "\" contains " + hashtable.size() + " items.\n");
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private String getInput(){
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }
}
