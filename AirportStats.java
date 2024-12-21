
// Program description: This program tracks statistics for different airports
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class AirportStats {
    private static String startYear = "2003";
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.print("Enter airport name or code: ");
        String filename = console.nextLine();
        String [] airportName = checkAirPortName(filename);
        if(airportName == null) {
            System.out.println("Airport not found.");
            return;
        }
        checkData(console, airportName);

        
    }

    /**
     * This method checks the airport name input by the user
     * @param filename Airport name input by user
     * @return returns short-hand airport name and full name if true and null if false
     * @throws FileNotFoundException function allows program to continue even if no file is found
     */
    public static String [] checkAirPortName(String filename) throws FileNotFoundException {
        String str;
        String code = null;
        String airport = null;
        File inputFile = new File("airports-code.csv");
        Scanner fileScnr = new Scanner(inputFile);
        while (fileScnr.hasNextLine()) {
            str = fileScnr.nextLine();
            String [] parts1 = str.split(",");
            if (parts1.length == 2) {
                code = parts1[0].toLowerCase().trim();
                airport = parts1[1].toLowerCase().trim();
            }
            else if (parts1.length == 1) {
                code = parts1[0].toLowerCase().trim();
            }

            if (code.contains(filename.toLowerCase()) || airport.contains(filename.toLowerCase())){
                return parts1;
            }
        }
        return null;

    }

    /**
     * The method prompts user for filename and checks if it is valid
     * @param console The scanner console
     * @param parts The array that stores the abbreviated and long hand airport name
     * @throws FileNotFoundException function allows program to continue even if no file is found
     */
    public static void checkData(Scanner console, String [] parts) throws FileNotFoundException  {
        System.out.print("Enter data file name: ");
        String filename = console.nextLine();
        while (!checkFile(filename)) {
            System.out.print("File does not exist, try again: ");
            filename = console.nextLine();
        }
        if (checkFile(filename)) {
            System.out.println(filename + " successfully found.");
        }
        System.out.println();
        System.out.println(parts[0] + " | " + parts[1]);
        System.out.println("Flight Statistics");
        System.out.println();
        File inputFile = new File(filename);
        Scanner fileScnr = new Scanner(inputFile);
        displayStats();
        scanDataCsv(fileScnr, parts[0]);
    }

    /**
     * This method checks if file input by user exists
     * @param filename The file input by user
     * @return returns true or false if file exists or not
     */
    public static boolean checkFile(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    /**
     * This method scan the file input by user and checks if it has valid information
     * @param fileScnr The Scanner that reads the file
     * @param city The name of the city input by user
     */
    public static void scanDataCsv(Scanner fileScnr, String city) {
        int [] statsForYear = new int[4];
        String currentYear = startYear;
        int [] columnTotals = new int[4];
        while(fileScnr.hasNextLine()) {

            String str = fileScnr.nextLine();
            if (!str.isEmpty()) {
                currentYear = processLine(str, city, currentYear, statsForYear, columnTotals);
            }
        }
        printYearStats(currentYear, statsForYear);


        System.out.println("============================================================");
        System.out.printf("%,18d %,13d %,13d %,13d\n", columnTotals[0], columnTotals[1],
                columnTotals[2], columnTotals[3]);
        int grandTotal = columnTotals[0] + columnTotals[1] + columnTotals[2] + columnTotals[3];

        printPercentages(grandTotal, columnTotals);


    }

    /**
     * This method prints the header
     */
    public static void displayStats() {
        System.out.println("============================================================");
        System.out.println("Year     Cancelled       Delayed      Diverted       On Time");
        System.out.println("============================================================");
    }

    /**
     * This method prints the statistics for each year present in file
     * @param currentYear the year at current before it changes
     * @param stats Array the contains the information for each year
     */
    public static void printYearStats(String currentYear, int [] stats) {
        System.out.printf(currentYear + "%,14d  %,12d %,13d %,13d\n", stats[0],
                stats[1], stats[2], stats[3]);
    }

    /**
     *This method processes each line of the file and checks if it has the correct information
     * @param str The line read by the code
     * @param city The name of the city input by the user
     * @param currentYear the current year starting from 2003
     * @param statsForYear Array that holds the statistics for each year
     * @param columnStats Array the holds the sum of numbers in one column
     * @return returns year at present
     */
    public static String processLine(String str, String city, String currentYear,
                                     int [] statsForYear, int [] columnStats) {
        String [] parts2 = str.split(",");

        if(parts2.length == 6 && (parts2[0].equals(city))) {
            String lineYear = parts2[1].substring(0,4);
            if(!lineYear.equals(currentYear)) {
                printYearStats(currentYear, statsForYear);
                restartStats(statsForYear);
                currentYear = lineYear;

            }
            updateColumnStat(parts2, statsForYear, columnStats);

        }
        return currentYear;
    }

    /**
     * This method resets the values for the information when the year changes
     * @param stats Array the holds the information at the start of each year
     */
    public static void restartStats(int [] stats) {
        for (int i = 0; i < stats.length; i++) {
            stats[i] = 0;
        }
    }

    /**
     * This method updates the sum of the values in each column
     * @param parts2 the line read from the file
     * @param statsForYear Array holding the values for each year
     * @param columnStats Array holding the sum of values in each column
     */
    public static void updateColumnStat(String [] parts2, int [] statsForYear, int [] columnStats){
        int cancel = Integer.parseInt(parts2[2].trim());
        int delay = Integer.parseInt(parts2[3].trim());
        int divert = Integer.parseInt(parts2[4].trim());
        int onTime = Integer.parseInt(parts2[5].trim());

        statsForYear[0] += cancel;
        statsForYear[1] += delay;
        statsForYear[2] += divert;
        statsForYear[3] += onTime;


        columnStats[0] += cancel;
        columnStats[1] += delay;
        columnStats[2] += divert;
        columnStats[3] += onTime;
    }

    /**
     * This method finds and prints the percentages of each column based on their sum
     * @param Grand The combined total of the total statistics in each column
     * @param column Array holding total in each column
     */
    public static void printPercentages(int Grand, int [] column) {
        double Canceled = (column[0] * 100.0) / Grand;
        double Delayed = (column[1] * 100.0) / Grand;
        double Diverted = (column[2] * 100.0) / Grand;
        double Ontime = (column[3] * 100.0) / Grand;

        System.out.printf("%17.1f%% %12.1f%% %12.1f%% %12.1f%%\n", Canceled, Delayed,
                Diverted, Ontime);
    }

}
