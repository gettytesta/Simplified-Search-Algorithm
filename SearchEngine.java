import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class SearchEngine {
    public static final String PAGES_FILE = "pages.txt";
    public static final String LINKS_FILE = "links.txt";

    private WebGraph web;

    /**
     * Constructs a SearchEngine by making a WebGraph
     */
    public SearchEngine() {
        web = WebGraph.buildFromFiles(PAGES_FILE, LINKS_FILE);
    }

    /**
     * Allows the user to search a webgraph based on a file.
     * 
     * @param args the command line arguments for the program
     */
    public static void main(String args[]) {
        try {
            System.out.println("Loading WebGraph data...");
            SearchEngine search = new SearchEngine();
            System.out.println("Success!\n");

            Scanner userScanner = new Scanner(System.in);
            String userInput = "";

            boolean isTerminated = false;
            while (!isTerminated) {
                printMainHeader();
                System.out.print("Please select an option: ");
                userInput = userScanner.nextLine();

                if (userInput.toLowerCase().equals("ap")) {
                    System.out.print("Enter a URL: ");
                    String url = userScanner.nextLine();
                    System.out.print("Enter keywords (space-separated): ");
                    ArrayList<String> keywords = new ArrayList<String>(
                            Arrays.asList(userScanner.nextLine().trim().split(" ")));
                    try {
                        search.getWebGraph().addPage(url, keywords);
                        System.out.printf("%s successfully added to the WebGraph!\n", url);
                    } catch (IllegalArgumentException iae) {
                        System.out.printf("Error: %s already exists in the WebGraph. Could not add new WepPage.\n",
                                url);
                    }
                } else if (userInput.toLowerCase().equals("rp")) {
                    System.out.print("Enter a URL: ");
                    String url = userScanner.nextLine();
                    search.getWebGraph().removePage(url);
                    System.out.printf("%s has been removed from the graph!\n", url);
                } else if (userInput.toLowerCase().equals("al")) {
                    try {
                        System.out.print("Enter a source URL: ");
                        String source = userScanner.nextLine();
                        System.out.print("Enter a destination URL: ");
                        String dest = userScanner.nextLine();
                        search.getWebGraph().addLink(source, dest);
                        System.out.printf("Link successfully added from %s to %s!\n", source, dest);
                    } catch (IllegalArgumentException iae) {
                    }
                } else if (userInput.toLowerCase().equals("rl")) {
                    try {
                        System.out.print("Enter a source URL: ");
                        String source = userScanner.nextLine();
                        System.out.print("Enter a destination URL: ");
                        String dest = userScanner.nextLine();
                        search.getWebGraph().removeLink(source, dest);
                        System.out.printf("Link removed from %s to %s!\n", source, dest);
                    } catch (IllegalArgumentException iae) {
                    }
                } else if (userInput.toLowerCase().equals("p")) {
                    printSecondaryHeader();
                    System.out.print("Please select an option: ");
                    userInput = userScanner.nextLine();
                    if (userInput.toLowerCase().equals("i")) {
                        Collections.sort(search.getWebGraph().getPages(), new IndexComparator());
                    } else if (userInput.toLowerCase().equals("u")) {
                        Collections.sort(search.getWebGraph().getPages(), new URLComparator());
                    } else if (userInput.toLowerCase().equals("r")) {
                        Collections.sort(search.getWebGraph().getPages(), new RankComparator());
                    } else {
                        System.out.println("Invalid input.");
                    }
                    search.getWebGraph().printTable();
                } else if (userInput.toLowerCase().equals("s")) {
                    System.out.print("Search keyword: ");
                    String keyword = userScanner.nextLine();
                    ArrayList<WebPage> searchResults = new ArrayList<WebPage>();

                    for (WebPage page : search.getWebGraph().getPages()) {
                        if (page.getKeywords().contains(keyword)) {
                            searchResults.add(page);
                        }
                    }
                    if (searchResults.size() == 0) {
                        System.out.printf("No search results found for the keyword %s.\n", keyword);
                        continue;
                    }
                    Collections.sort(searchResults, new RankComparator());
                    System.out.println("Rank   PageRank    URL\n---------------------------------------------");
                    for (int i = 0; i < searchResults.size(); i++) {
                        System.out.printf("  %-3d|%4s    | %s\n", i + 1, searchResults.get(i).getRank(),
                                searchResults.get(i).getUrl());
                    }
                    System.out.println();
                } else if (userInput.toLowerCase().equals("q")) {
                    isTerminated = true;
                } else {
                    System.out.println("Invalid input.");
                }
                System.out.println();
            }
            userScanner.close();
            System.out.println("\nGoodbye.");
        } catch (IllegalArgumentException iae) {
            System.out.println("ERROR: File does not exist!");
        }
    }

    /**
     * Gets the WebGraph associated to the SearchEngine
     * 
     * @return the WebGraph associated to the SearchEngine
     */
    public WebGraph getWebGraph() {
        return web;
    }

    /**
     * Prints the header for the loop
     */
    public static void printMainHeader() {
        System.out.println("Menu:\n    (AP) - Add a new page to the graph.\n" +
                "    (RP) - Remove a page from the graph.\n    (AL) - Add a link betw" +
                "een pages in the graph.\n    (RL) - Remove a link between pages in t" +
                "he graph.\n    (P)  - Print the graph.\n    (S)  - Search for pages " +
                "with a keyword.\n    (Q)  - Quit.\n");
    }

    /**
     * Prints the secondary header used when printing the WebGraph
     */
    public static void printSecondaryHeader() {
        System.out.println("\n    (I) Sort based on index (ASC)\n    (U) Sort " +
                "based on URL (ASC)\n    (R) Sort based on rank (DSC)\n");
    }
}
