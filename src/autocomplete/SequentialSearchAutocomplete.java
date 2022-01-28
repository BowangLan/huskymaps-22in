package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sequential search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class SequentialSearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> terms;

    /**
     * Constructs an empty instance.
     */
    public SequentialSearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> results = new ArrayList<>();
        String prefixString = prefix.toString();
        for (CharSequence current : this.terms) {
            //if (this.startWith(prefix, current)) {
                //results.add(current); 
            //}
            if (current.toString().startsWith(prefixString)) {
                results.add(current); 
            }
        }
        return results;
    }

    //private boolean startWith(CharSequence prefix, CharSequence word) {
        
    //}
}
