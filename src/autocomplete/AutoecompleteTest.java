package autocomplete;

import java.lang.*;
import java.util.*;
import java.io.*;

class AutocompleteTest {
    public static void main(String[] args) {
        System.out.println("This is the autocomplete test.");
        Autocomplete autocomplete0 = new TreeSetAutocomplete();
        //Autocomplete autocomplete3 = new TernarySearchTreeAutocomplete();
        Autocomplete autocomplete3 = new SequentialSearchAutocomplete();
        //Autocomplete autocomplete3 = new BinarySearchAutocomplete();

        runTests(autocomplete0, autocomplete3, 500000, 5, 10, 0);
    }

    /**
     * Run a number of tests for addAll and allMatches
     */
    private static void runTests(Autocomplete auto1, Autocomplete auto2, int count, int min, int max, int test_count) {
        Random random = new Random();

        List<String> testData = generateTermList(count, min, max);

        long start = 0l, time = 0l;
        double totalAddTime = 0d;

        start = System.nanoTime();
        auto1.addAll(testData);
        time = System.nanoTime() - start;
        totalAddTime += (double) time / 1_000_000_000;
        System.out.println(String.format("Autocomplete 1: add %d terms in %.4f seconds", testData.size(), totalAddTime));

        start = System.nanoTime();
        auto2.addAll(testData);
        time = System.nanoTime() - start;
        totalAddTime += (double) time / 1_000_000_000;
        System.out.println(String.format("Autocomplete 2: add %d terms in %.4f seconds", testData.size(), totalAddTime));

        System.out.println("Start test");
        
        int pass_count = 0;

        for (int i = 0; i < test_count; i++) {

            // generate a random term between min and max
            //int len = random.nextInt(max-min) + min;
            //String prefix = generateTerm(len);

            // get a random slice of a random term from testData
            String prefix = getRandomTermSlice(testData);

            System.out.println("\nTest term: " + prefix);
            List<String> m1 = convertCharSequenceArrayListToString(auto1.allMatches(prefix));
            Collections.sort(m1);
            List<String> m2 = convertCharSequenceArrayListToString(auto2.allMatches(prefix));
            Collections.sort(m2);
            boolean pass = m1.equals(m2);
            if (pass) {
                System.out.println(" - \033[92m"+pass+"\033[0m"); 
                pass_count += 1;  
            } else {
                System.out.println(" - \033[91m"+pass+"\033[0m");
                System.out.println(m1.size());
                System.out.println(m1);
                System.out.println(m2.size());
                System.out.println(m2);
            } 
        }
        System.out.println(String.format("Test stats: %d of %d tests passed.", pass_count, test_count));
    }

    /**
     * Generate a list of random string.
     */
    private static List<String> generateTermList(int count, int min, int max) {
        List<String> output = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int len = random.nextInt(max-min) + min;
            output.add(generateTerm(len));
        } 
        return output;
    }

    /**
     * Generate a random string of a given length.
     */
    private static String generateTerm(int len) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder output = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = chars.charAt(random.nextInt(chars.length()));
            output.append(c);
        }
        return output.toString();
    }

    /** 
     * Convert a list of CharSequence to a list of String
     */
    private static List<String> convertCharSequenceArrayListToString(List<CharSequence> list) {
        List<String> output = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            output.add(list.get(i).toString());
        }
        return output;
    }

    private static String getRandomTermSlice(List<String> list) {
        Random random = new Random();
        int index = random.nextInt(list.size());
        String term = list.get(index).toString();
        int slice = random.nextInt(term.length()-1);
        while (slice == 0) {
            slice = random.nextInt(term.length()-1);
        }
        term = term.substring(0, slice);
        return term;
    }

    private static void testAddAll(int num_trials, Autocomplete autocomplete, List<String> dataset) throws IOException {
        String test_dir = "auto_test";
        new File(test_dir).mkdir();
        String name = "test";
        PrintStream out = new PrintStream(test_dir + "/" + name + ".csv");

        // Record the total runtimes for this dataset.
        double totalAddTime = 0.0;
        double totalQueryTime = 0.0;

        for (int i = 0; i < num_trials; i += 1) {
            //Autocomplete autocomplete = implementations.get(name).get();
            // First, measure addAll.
            long addStart = System.nanoTime();
            autocomplete.addAll(dataset);
            long addTime = System.nanoTime() - addStart;
            // Convert from nanoseconds to seconds and add to total time.
            totalAddTime += (double) addTime / 1_000_000_000;

            // With the dataset loaded, measure allMatches.
            long queryStart = System.nanoTime();
            String prefix = getRandomTermSlice(dataset);
            autocomplete.allMatches(prefix);
            long queryTime = System.nanoTime() - queryStart;
            totalQueryTime += (double) queryTime / 1_000_000_000;
        }

        // Output the averages to 10 decimal places.
        out.printf("%.10f", totalAddTime / num_trials);
        out.print(',');
        out.printf("%.10f", totalQueryTime / num_trials);
        out.println();


    }
}
