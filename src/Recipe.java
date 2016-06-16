import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


class Recipe {

    /**
     * @author Robbie McLeod (10mcleodr)
     */

    /**
     * Represents the User Input Scanner
     */
    static Scanner sc = new Scanner(System.in);

    /**
     * Represents the name of the recipe.
     */
    static String recipeName = "";

    /**
     * Represents the directory whereby the recipe will be saved.
     */
    //static File file = new File("\\\\W8FSSTU01\\10mcleodr$\\Documents\\GCSE COMPUTING\\a453_recipe_dir", recipeName+"_recipe.txt");
    static File file = new File("C:\\recipe_dir_test", "myRecipe.txt");

    /**
     * Represents the directory whereby the recipe will be locally saved.
     */
    static File localFile = new File("data\\", "recipe_data.txt");

    /**
     * Represents the number of people the recipe will need to serve.
     */
    static Integer people = 0;

    /**
     * Represents the ingredients used to in the recipe.
     */
    static ArrayList < String > Ingredients = new ArrayList < > ();

    /**
     * Represents the amounts of ingredients used in the recipe.
     */
    static ArrayList < String > Amounts = new ArrayList < > ();

    /**
     * Represents the amounts of ingredients used in the recipe.
     */
    static ArrayList < String > Units = new ArrayList < > ();

    /**
     * Represents the position of the current ingredient.
     */
    static int ing_pos = 0;

    /**
     * Represents the position of the current amount.
     */
    static int amt_pos = -1;

    /**
     * Represents the position of the current unit.
     */
    static int unit_pos = -1;

    /**
     * Represents if the user already has a recipe stored in the saving directory.
     */
    static boolean hasRecipeStored;

    /**
     * Represents if a stored recipe has been modified.
     */
    static boolean modified;

    /**
     * The "STAGES" framework
     * @author Robbie
     */
    enum STAGES {

        NAME(),
        READ_WRITE(),
        RECIPE_NAME(),
        NO_OF_PEOPLE(),
        INGREDIENTS_LIST(),
        AMOUNTS_LIST(),
        UNITS_LIST(),
        RECIPE_SAVING();

        STAGES() {}

    }

    /**
     * The "main" method, the first method to be read by the program when it is run.
     */
    public static void main(String args[]) throws IOException {
    	/*
    	 * Call to the beginTrail() method, responsible for the the program's flow.
    	 */
        beginTrail();
    }

    /**
     * The beginTrail() method
     * - Responsible for the program's flow.
     */
    public static void beginTrail() throws IOException {
        System.out.println("[A453 Programming Project - Task Two: Recipe]");
        sleep(2);
        System.out.println("What is your name?");
        System.out.println(handleInputs(STAGES.NAME));
        sleep(1);
        System.out.println("Checking for an existing recipe...");
        sleep(3);
        System.out.println(handleInputs(STAGES.READ_WRITE));
        sleep(1);
        System.out.println("\nPlease enter the name of the recipe you'd like to store:");
        System.out.println(handleInputs(STAGES.RECIPE_NAME));
        sleep(2);
        System.out.println("Now, please enter the number of people you are serving for.");
        System.out.println(handleInputs(STAGES.NO_OF_PEOPLE));
        sleep(1);
        System.out.println("Now you've named your recipe, and chosen how many people you are serving for...");
        sleep(1);
        System.out.println("You will now need to enter the ingredients used for your recipe.");
        sleep(1);
        System.out.println("When you are finished naming all of your ingredients, simply enter 'DONE'.\n");
        sleep(1);
        System.out.println(handleInputs(STAGES.INGREDIENTS_LIST));
        sleep(1);
        System.out.println("Now you need to specify the amount of each ingredient you are going to use in your recipe.");
        sleep(1);
        System.out.println(handleInputs(STAGES.AMOUNTS_LIST));
        sleep(1);
        System.out.println("Now you need to specify the unit for each ingredient you are going to use in your recipe.");
        sleep(1);
        System.out.println(handleInputs(STAGES.UNITS_LIST));
        sleep(1);
        System.out.println(handleInputs(STAGES.RECIPE_SAVING));
        sleep(1);
        saveRecipe();
    }

    /**
     * The handleInputs() method
     */
    public static String handleInputs(STAGES stage) {

        switch (stage) {

            case NAME:
                String name = sc.nextLine();
                sleep(1);
                return "It's nice to meet you, " + name + "!";

            case READ_WRITE:
                hasAnyRecipesStored();
                modifyExistingRecipeCheck();
            return checkModification();

            case RECIPE_NAME:
                String recipeName1 = sc.nextLine();
                recipeName = recipeName1;
                sleep(1);
                return "Thanks for that.\nYou have named this recipe: '" + recipeName + "'.";

            case NO_OF_PEOPLE:
                String amt = sc.nextLine();
                if (validateInput(amt)) {
                    people = Integer.valueOf(amt);
                } else {
                    System.out.println("Please enter an integer value for the number of people you are serving for.");
                    handleInputs(STAGES.NO_OF_PEOPLE);
                }
                return "You've chosen to serve: " + people + " people.";

            case INGREDIENTS_LIST:
                System.out.println("Please enter ingredient number " + grabID() + ": ");
                String firstIngredient = sc.nextLine();
                Ingredients.add(firstIngredient);
                loopIngredients();
                return "Thanks for entering your ingredients list.";

            case AMOUNTS_LIST:
                System.out.println("Please enter the amount (without units) for ingredient '" + getCurrentIngredient() + "': ");
                String firstAmt = sc.nextLine();
                Amounts.add(firstAmt);
                loopAmounts();
                return "Thanks for entering your amounts list.";

            case UNITS_LIST:
                System.out.println("Please enter the unit for ingredient '" + getCurrentIngredient() + "', (amount: " + getCurrentAmount() + "): ");
                String firstUnit = sc.nextLine();
                Units.add(firstUnit);
                loopUnits();
                return "Thanks for entering your units list.";

            case RECIPE_SAVING:
                System.out.println("Would you like to save your recipe?");
                String response = sc.nextLine();
                checkResponse(response);
                return "Attempting to save your recipe...";

            default:
                break;

        }
        return "N/A";
    }

    /**
     * Causes the program to exit if a modification has been made to an existing recipe.
     */
    public static String checkModification() {
        if (modified) {
            System.out.println("\nModification made, program will now exit.");
            sleep(2);
            System.exit(0);
        } else {
            return "";
            //continues trail.
        }
        return null;
    }

    /**
     * Detects if a recipe already exists in the user's directory, and gives the user the choice of modifying it.
     */
    public static boolean modifyExistingRecipeCheck() {
        if (hasRecipeStored) {
            System.out.println("The program has detected that you already have a recipe saved in saving directory - (C:\\recipe_dir_test).");
            sleep(2);
            System.out.println("Would you like to modify this recipe? (In terms of how many people it serves)");
            String response = sc.nextLine();
            if (response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("yes")) {
                modified = true;
                handleModify();
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Handles the modification process (recalculation)
     */
    public static void handleModify() {
        /**
         * Program reads the LOCALLY saved file - and stores it in a list.
         */
        ArrayList < String > text = new ArrayList < > (readFile());

        /* Structure (Method Planning):
         * 
         * Local Save Structure:
         * - Recipe Name
         * - Ingredient one
         * 		- Ingredient one amount
         *		- Ingredient one unit
         * - People served
         */
        sleep(1);
        System.out.println("[Modify]: Modifying recipe: " + text.get(0));
        sleep(1);
        System.out.println("[Modify]: Please enter the NEW value for the number of people you will be serving. (Existing value: " + text.get((text.size() - 1)) + " people)");
        String value = sc.nextLine();
        int newPeople = 0;
        if (validateInput(value)) {
            newPeople = Integer.valueOf(value);
        } else {
            System.out.println("Please enter an integer value for the number of people to recalculate for.");
            handleModify();
        }
        System.out.println("[Modify]: Calculating...");
        sleep(3);
        //newPeople and text are both parsed into the calculateNewValues() method.
        calculateNewValues(newPeople, text);
    }

    /**
     * Logic behind recalculating an existing recipe's values.
     * - ArrayList<String> fileText = the recipe's text read by the FileReader.
     * - int newAmount = the new amount of people to calculate for.
     */
    public static void calculateNewValues(int newAmount, ArrayList < String > fileText) {
        ArrayList < String > ingredientNames = new ArrayList < > ();
        ArrayList < Double > ingredientAmounts = new ArrayList < > ();
        ArrayList < String > ingredientUnits = new ArrayList < > ();
        int people = Integer.valueOf(fileText.get(fileText.size() - 1));
        boolean readName = true, readAmount = false, readUnit = false;
        for (int i = 1; i < (fileText.size() - 1); i++) {
            //will read ingredient id (e.g. IngredientOnE)
            // will read ingredient id's amount
            //  will read ingredient id's unit
            if (readName) {
                ingredientNames.add(fileText.get(i));
                readName = false;
                readAmount = true;
                continue;
            }
            if (readAmount) {
                ingredientAmounts.add(Double.valueOf(fileText.get(i)));
                readAmount = false;
                readUnit = true;
                continue;
            }
            if (readUnit) {
                ingredientUnits.add(fileText.get(i));
                readUnit = false;
                readName = true;
                continue;
            }
        }

        /**
         * Pseudocode:
         * 
         * Calculate each ingredient's amount for 1 person...
         * Then, multiply to determine the amount of each ingredient to serve the new amount of people.
         * 
         * (ingredient amount / no. of people) * new amount of people
         */

        ArrayList < Double > newAmounts = new ArrayList < > ();

        for (int i = 0; i < ingredientAmounts.size(); i++) {
            newAmounts.add(((ingredientAmounts.get(i) / people) * newAmount));
        }
        sleep(1);
        System.out.println("\n[Modify]: Recalculated values for '" + newAmount + "' people.\n");

        for (int i = 0; i < ingredientNames.size(); i++) {
            System.out.println("[Modify] Ingredient: " + ingredientNames.get(i) + ", New Amount: " + newAmounts.get(i) + " " + ingredientUnits.get(i));
            sleep(2);
        }
    }

    /**
     * Reads the existing recipe in the local directory.
     */
    public static ArrayList < String > readFile() {

        ArrayList < String > FILE_TEXT = new ArrayList < > ();

        try (BufferedReader br = new BufferedReader(new FileReader("data\\recipe_data.txt"))) {

            String currentLine;

            /**
             * Reads each line of file, and adds current read line to "FILE_TEXT" list.
             */
            while ((currentLine = br.readLine()) != null) {
                FILE_TEXT.add(currentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return FILE_TEXT;
    }

    /**
     * @param time - Time to sleep (in seconds)
     */
    public static void sleep(int time) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * A simple return statement to check for an existing recipe stored in the user's directory (hasRecipe).
     */
    public static String hasAnyRecipesStored() {
        boolean hasRecipe = new File("C:\\recipe_dir_test", "myRecipe.txt").exists();
        if (hasRecipe) {
            hasRecipeStored = true;
            return "You have a recipe stored.";
        } else {
            hasRecipeStored = false;
            return "You don't have a recipe stored.";
        }

    }

    /**
     * Validates the user's input by attempting to parse it as an integer.
     * - Returns the result of processing (true/false).
     */
    public static boolean validateInput(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
        	/* Catches the exception if necessary (NumberFormatException)*/
            return false;
        }
        if (Integer.valueOf(input) > 0) {
        	return true;
        } else {
        	return false;
        }
    }

    /**
     * Returns the incremented value of the "ing_pos" integer;
     * (existing value + 1)
     */
    public static Integer grabID() {
        ing_pos++;
        return ing_pos;
    }

    /**
     * Returns the ingredient with index equal to the value of the ing_pos integer.
     * @return
     */
    public static String getCurrentIngredient() {
        ing_pos++;
        return Ingredients.get(ing_pos);
    }

    /**
     * Returns the amount with index equal to the value of the amt_pos integer.
     * @return
     */
    public static String getCurrentAmount() {
        amt_pos++;
        return Amounts.get(amt_pos);
    }

    /**
     * Allows the user to input ingredients into the program
     * - Uses the System.in() input stream
     */
    public static void loopIngredients() {
        if (!Ingredients.equals("")) {
            System.out.println("Please now enter ingredient number " + grabID() + ": ");
            String nextIngredient = sc.nextLine();
            if (nextIngredient.equalsIgnoreCase("Done")) {
                ing_pos = -1;
                //resets back to zero.
                return;
            } else {
                Ingredients.add(nextIngredient);
                System.out.println("Ingredients so far: " + Ingredients.toString());
                loopIngredients();
            }
        }
        return;
    }

    /**
     * Allows the user to input the ingredient's amounts into the program
     * - uses the System.in() input stream.
     */
    public static void loopAmounts() {
        if (Ingredients.size() == (ing_pos + 1)) {
            ing_pos = -1;
            //Resets the amount back to its original value.
            return;
        }
        System.out.println("Please now enter the amount (without units) for ingredient '" + getCurrentIngredient() + "': ");
        String nextAmount = sc.nextLine();
        if (nextAmount.equalsIgnoreCase("Done")) {
            return;
        } else {
            Amounts.add(nextAmount);
            loopAmounts();
        }
        return;
    }

    /**
     * Allows the user to input the ingredient's units into the program
     * - uses the System.in() input stream.
     */
    public static void loopUnits() {
        if (Ingredients.size() == (ing_pos + 1)) {
            return;
        }
        System.out.println("Please now enter the unit for ingredient '" + getCurrentIngredient() + "', (amount: " + getCurrentAmount() + "): ");
        String nextUnit = sc.nextLine();
        if (nextUnit.equalsIgnoreCase("Done")) {
            return;
        } else {
            Units.add(nextUnit);
            loopUnits();
        }
        return;
    }

    /**
     * Checks the user's response (System.in()) and acts accordingly.
     */
    public static void checkResponse(String response) {
        if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("Y")) {
            return;
        } else {@
            SuppressWarnings("resource")
            Scanner tempScan = new Scanner(System.in);
            System.out.println("Are you sure you wish to discard this recipe? (The program will exit)");
            String answer = tempScan.nextLine();
            if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("Y")) {
                System.out.println("Recipe discarded.\nThe program will terminate in 5 seconds...");
                sleep(5);
                System.exit(0);
            } else {
                return;
            }
        }
    }

    /**
     * Saved the user's recipe in an easy-accessible directory, as well as saving it locally for reading later by the program.
     */
    public static void saveRecipe() throws IOException {
        Random random = new Random();
        if (file.exists()) {
            file.renameTo(new File("C:\\recipe_dir_test", "new-recipe (UID: " + random.nextInt(50) + ").txt"));
        }
        /**
         * saveInDirectory() and saveLocally() are both synchronized, 
         * meaning that the program will execute them both at exactly the same time.
         */
        saveInDirectory();
        saveLocally();
    }

    /**
     * Saves the program locally for the program to read at next runtime.
     */
    public static synchronized void saveLocally() throws IOException {
        FileWriter fw = new FileWriter(localFile);
        BufferedWriter bw = new BufferedWriter(fw);

        /* Local Save Structure:
         * - Recipe Name
         * - Ingredient one
         * 		- Ingredient one amount
         *		- Ingredient one unit
         * - People served
         */

        bw.write(recipeName + "\r\n");
        for (int i = 0; i < Ingredients.size(); i++) {
            bw.write(Ingredients.get(i) + "\r\n");
            bw.write(Double.valueOf(Amounts.get(i)) * people + "\r\n");
            bw.write(Units.get(i) + "\r\n");
        }
        bw.write("" + people);
        bw.close(); //Closes the BufferedWriter.
    }

    /**
     * Saves the recipe for the user to access in an easily accessible directory for them to read at a later date.
     */
    public static synchronized void saveInDirectory() throws IOException {
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("~ " + recipeName + " ~\r\n");
        bw.write("You will need:\r\n");
        for (int i = 0; i < Ingredients.size(); i++) {
            bw.write("- " + (Double.valueOf(Amounts.get(i))) + " " + Units.get(i) + " of " + Ingredients.get(i) + "\r\n");
        }
        bw.write("In order to make " + recipeName + " for " + people + " people.");
        bw.close(); //Closes the BufferedWriter input stream.
    }


}