package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> terms;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
        //for (CharSequence term : terms) {
            //if (this.terms.contains(term)) continue;
            //this.terms.add(term);
            ////System.out.println("Term added: " + term);
        //}
        Collections.sort(this.terms, CharSequence::compare);
        System.out.println(String.format("%d terms added", terms.size()));
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> output = new ArrayList<>();

        int index = Collections.binarySearch(this.terms, prefix, CharSequence::compare);
        if (index < 0) index = -(index + 1);
        for (int i = index; i < this.terms.size(); i++) {
            CharSequence curr = this.terms.get(i);
            if (!curr.toString().startsWith(prefix.toString())) return output;
            else                                                output.add(curr);
        }
        return output;
    }
}
