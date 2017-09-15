
import java.util.*;

public class NaiveInvertedIndex {
    private HashMap<String, List<Integer>> mIndex;

    public NaiveInvertedIndex() {
        mIndex = new HashMap<String, List<Integer>>();
    }

    public void addTerm(String term, int documentID) {
        // TO-DO: add the term to the index hashtable. If the table does not have
        // an entry for the term, initialize a new ArrayList<Integer>, add the 
        // docID to the list, and put it into the map. Otherwise add the docID
        // to the list that already exists in the map, but ONLY IF the list does
        // not already contain the docID.

        if (mIndex.containsKey(term)) {
            List<Integer> postingsList = mIndex.get(term);
            if (!postingsList.contains(documentID)) {
                postingsList.add(documentID);
            }
        }
        else {
            List<Integer> postingsList = new ArrayList<>();
            mIndex.put(term, postingsList); 
            postingsList.add(documentID);
        }

    }

    public List<Integer> getPostings(String term) {
        // TO-DO: return the postings list for the given term from the index map.
        if (mIndex.containsKey(term)) {
            return mIndex.get(term);
        }
        return null;
    }

    public int getTermCount() {
        // TO-DO: return the number of terms in the index.
        return mIndex.size();
    }

    public String[] getDictionary() {
        // TO-DO: fill an array of Strings with all the keys from the hashtable.
        // Sort the array and return it.
        Set<String> keySet = mIndex.keySet();
        String[] dictionary = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(dictionary);
        return dictionary;
    }
}
