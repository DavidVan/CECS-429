
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

/**
  A very simple search engine. Uses an inverted index over a folder of TXT files.
  */
public class SimpleEngine {

    public static void main(String[] args) throws IOException {
        final Path currentWorkingPath = Paths.get("").toAbsolutePath();

        // the inverted index
        final NaiveInvertedIndex index = new NaiveInvertedIndex();

        // the list of file names that were processed
        final List<String> fileNames = new ArrayList<String>();


        // This is our standard "walk through all .txt files" code.
        Files.walkFileTree(currentWorkingPath, new SimpleFileVisitor<Path>() {
            int mDocumentID  = 0;

            public FileVisitResult preVisitDirectory(Path dir,
                    BasicFileAttributes attrs) {
                // make sure we only process the current working directory
                if (currentWorkingPath.equals(dir)) {
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.SKIP_SUBTREE;
            }

            public FileVisitResult visitFile(Path file,
                    BasicFileAttributes attrs) throws FileNotFoundException {
                // only process .txt files
                if (file.toString().endsWith(".txt")) {
                    // we have found a .txt file; add its name to the fileName list,
                    // then index the file and increase the document ID counter.
                    System.out.println("Indexing file " + file.getFileName());


                    fileNames.add(file.getFileName().toString());
                    indexFile(file.toFile(), index, mDocumentID);
                    mDocumentID++;
                }
                return FileVisitResult.CONTINUE;
            }

            // don't throw exceptions if files are locked/other errors occur
            public FileVisitResult visitFileFailed(Path file,
                    IOException e) {

                return FileVisitResult.CONTINUE;
            }

        });

        printResults(index, fileNames);

        // Implement the same program as in Homework 1: ask the user for a term,
        // retrieve the postings list for that term, and print the names of the 
        // documents which contain the term.

        Scanner scanner = new Scanner(System.in);
        String userInput;
        do {
            System.out.print("Enter a term to search for: ");
            userInput = scanner.nextLine();
            if (userInput.equals("quit")) {
                System.out.println("Bye!");
                break;
            }
            List<Integer> postingsList = index.getPostings(userInput);
            System.out.println("These documents contain that term: ");
            if (postingsList != null) {
                for (int i : postingsList) {
                    System.out.print(i + " ");
                } 
            }
            System.out.println("\n");
        } while (!userInput.equals("quit"));
    }

    /**
      Indexes a file by reading a series of tokens from the file, treating each 
      token as a term, and then adding the given document's ID to the inverted
      index for the term.
      @param file a File object for the document to index.
      @param index the current state of the index for the files that have already
      been processed.
      @param docID the integer ID of the current document, needed when indexing
      each term from the document.
      */
    private static void indexFile(File file, NaiveInvertedIndex index, 
            int docID) throws FileNotFoundException {
        // TO-DO: finish this method for indexing a particular file.
        // Construct a SimpleTokenStream for the given File.
        // Read each token from the stream and add it to the index.

        SimpleTokenStream tokenStream = new SimpleTokenStream(file);
        while (tokenStream.hasNextToken()) {
            index.addTerm(tokenStream.nextToken(), docID);
        }

    }

    private static void printResults(NaiveInvertedIndex index, 
            List<String> fileNames) {

        // TO-DO: print the inverted index.
        // Retrieve the dictionary from the index. (It will already be sorted.)
        // For each term in the dictionary, retrieve the postings list for the
        // term. Use the postings list to print the list of document names that
        // contain the term. (The document ID in a postings list corresponds to 
        // an index in the fileNames list.)

        // Print the postings list so they are all left-aligned starting at the
        // same column, one space after the longest of the term lengths. Example:
        // 
        // as:      document0 document3 document4 document5
        // engines: document1
        // search:  document2 document4  
        
        String[] dictionary = index.getDictionary();
        int longestTerm = 0;
        for (String term : dictionary) {
            longestTerm = term.length() > longestTerm ? term.length() : longestTerm;
        }
        for (String term : dictionary) {
            List<Integer> postingsList = index.getPostings(term);
            System.out.print(term + ":");
            for (int i = 0; i < longestTerm - term.length(); i++) {
                System.out.print(" ");
            }
            for (int i : postingsList) {
                System.out.print("document" + i + " ");
            }
            System.out.print("\n");
        }
    }
}
