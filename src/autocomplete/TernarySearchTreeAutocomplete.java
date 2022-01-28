package autocomplete;

import java.util.*;
import java.lang.CharSequence;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence term : terms) {
            //System.out.println("\n\n==========new term: " + term);
            this.addOne(term);
            //System.out.println(this.overallRoot.printTree(0, ""));
        }
        //System.out.println(this.getAllTerms());
        System.out.println(String.format("%d terms have been added.", terms.size()));
    }

    /**
     * Insert a new term into the tree.
     */
    private void addOne(CharSequence term) {
        Node cur = this.overallRoot;
        for (int i = 0; i < term.length(); i++) {
            //System.out.println("\nfor loop " + i + "; character " + term.charAt(i));
            if (i == 0) {
                cur = this.insertNode(cur, term.charAt(i));
                if (this.overallRoot == null) this.overallRoot = cur;
            } else if (cur.mid == null) {
                //System.out.println("cur mid null, cur="+cur.data);
                cur.mid = this.insertNode(cur.mid, term.charAt(i));
                cur = cur.mid;
            } else {
                //System.out.println("cur mid NOT null, cur="+cur.data);
                cur = this.insertNode(cur.mid, term.charAt(i));
            }
            //System.out.println(this.overallRoot.printTree(0, ""));
            if (i == term.length() - 1) {
                cur.isTerm = true;
                //System.out.println("set term node " + cur);
            }
        }
    }

    /**
     * Insert a new character into the tree under a given node, and return the node assosited with the inserted character.
     */
    private Node insertNode(Node cur, char c) {
        //System.out.println("Inserting...cur: " + cur);
        if (cur == null) {
            //System.out.println("null new case " + c);
            cur = new Node(c);
            return cur;
        }
        if (Character.compare(c, cur.data) == 0) {
            //System.out.println("equal case");
            if (cur.mid == null) cur.mid = new Node(c);
            return cur;
        } else if (Character.compare(c, cur.data) < 0) {
            // these two cases are tricky
            // TODO refactor 
            //System.out.println("smaller case");
            if (cur.left == null) {
                cur.left = new Node(c);
                return cur.left;
            } else {
                return this.insertNode(cur.left, c);
            }
        } else {
            //System.out.println("greater case");
            if (cur.right == null) {
                cur.right = new Node(c);
                return cur.right;
            } else {
                return this.insertNode(cur.right, c);
            }
        }
    }

    /** 
     * Find all terms.
     */
    public ArrayList<CharSequence> getAllTerms() {
        return this.getTerms(this.overallRoot, "");
    }

    /**
     * Find all terms under a given node.
     */
    public ArrayList<CharSequence> getTerms(Node cur, CharSequence curString) {
        if (cur == null) return new ArrayList<CharSequence>();

        ArrayList<CharSequence> output = new ArrayList<CharSequence>();
        String ncurString = curString.toString() + cur.data;
        if (cur.isTerm)        output.add(ncurString);
        if (cur.mid != null)   output.addAll(this.getTerms(cur.mid, ncurString));
        if (cur.right != null) output.addAll(this.getTerms(cur.right, curString));
        if (cur.left != null)  output.addAll(this.getTerms(cur.left, curString));
        return output;
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        Node endNode = this.overallRoot;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i); 
            endNode = this.matchChar(endNode, c);
            //System.out.println("match node: " + endNode);
            if (endNode == null) return new ArrayList<CharSequence>();
            if (i != prefix.length() - 1) endNode = endNode.mid;
        }
        List<CharSequence> output = new ArrayList<>();
        if (endNode.isTerm) output.add(prefix);
        output.addAll(this.getTerms(endNode.mid, prefix));
        return output;
    }

    /**
     * Find the node associated with a given character under a given node.
     */
    private Node matchChar(Node cur, char c) {
        if (cur == null) return null;
        if      (Character.compare(c, cur.data) == 0) return cur;
        else if (Character.compare(c, cur.data) < 0)  return this.matchChar(cur.left, c);
        else                                          return this.matchChar(cur.right, c);
    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }

        public String toString() {
            String m = this.mid != null ? this.mid.data + "" : "";
            String l = this.left != null ? this.left.data + "" : "";
            String r = this.right != null ? this.right.data + "" : "";
            return String.format("<Node data=\"%s\" mid=\"%s\" left=\"%s\" right=\"%s\">", this.data, m, l, r);
            //return "<Node data=\"" + this.data + "\">";
        }

        public String printTree(int level, String pre) {
            String output = "";
            String sep = " ";
            for (int i = 0; i < level * 4; i++) {
                output += sep;
            }
            output += pre;
            output += this.data;
            if (this.isTerm) {
                output += " (term)";
            }
            output += "\n";
            if (this.mid != null) {
                output += this.mid.printTree(level + 1, "mi: ");
            }     
            if (this.left != null) {
                output += this.left.printTree(level + 1, "le: ");
            }    
            if (this.right != null) {
                output += this.right.printTree(level + 1, "ri: ");
            }  
            return output;
        }
    }
}
