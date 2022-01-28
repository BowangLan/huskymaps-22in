package autocomplete;

import java.util.*;

class SimpleExample {
    public static void main(String[] args) {
        System.out.println("This is a simple example.");
        List<CharSequence> terms = new ArrayList<>();
        terms.add("alpha");
        terms.add("delta");
        terms.add("do");
        terms.add("cats");
        terms.add("doagy");
        terms.add("pilot");
        terms.add("dodg");

        // Choose your Autocomplete implementation.
        //Autocomplete autocomplete = new SequentialSearchAutocomplete();
        Autocomplete autocomplete = new BinarySearchAutocomplete();
        autocomplete.addAll(terms);
        // Choose your prefix string.
        CharSequence prefix = "do";
        List<CharSequence> matches = autocomplete.allMatches(prefix);
        System.out.println(matches.toString());
        //for (CharSequence match : matches) {
            //System.out.println(match);
        //}
        //System.out.println(autocomplete.getAllTerms().toString());
        //System.out.println(autocomplete.getTerms(autocomplete.overallRoot, "").toString());
        //System.out.println(autocomplete.hello());
    }
}
