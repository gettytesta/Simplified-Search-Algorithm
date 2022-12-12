import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class WebGraph {
    public static final int MAX_PAGES = 40;

    private ArrayList<WebPage> pages = new ArrayList<WebPage>();
    private int[][] edges = new int[MAX_PAGES][MAX_PAGES];

    /**
     * Constructs a WebGraph object using the indicated files as the source for
     * pages and edges.
     * 
     * @param pagesFile the file containing the pages and keywords
     * @param linksFile the file containing the links from each page
     * @return the WebGraph represented by the two files
     * @throws IllegalArgumentException if the files don't exist
     */
    public static WebGraph buildFromFiles(String pagesFile, String linksFile) throws IllegalArgumentException {
        try {
            Scanner scanner = new Scanner(new File(pagesFile));

            WebGraph fileGraph = new WebGraph();

            String[] pageText;
            while (scanner.hasNextLine()) {
                pageText = scanner.nextLine().trim().split(" ");
                ArrayList<String> keywords = new ArrayList<String>();
                for (int i = 1; i < pageText.length; i++) {
                    keywords.add(pageText[i]);
                }
                fileGraph.addPage(pageText[0], keywords);
            }

            scanner = new Scanner(new File(linksFile));
            while (scanner.hasNextLine()) {
                pageText = scanner.nextLine().trim().split(" ");
                fileGraph.addLink(pageText[0], pageText[1]);
            }

            return fileGraph;

        } catch (FileNotFoundException fnfe) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Adds a page to the WebGraph
     * 
     * @param url      the url of the page to add
     * @param keywords the keywords associated to the page
     * @throws IllegalArgumentException if the url already exists in the WebGraph
     */
    public void addPage(String url, ArrayList<String> keywords) throws IllegalArgumentException {
        if (url == null || keywords == null) {
            throw new IllegalArgumentException();
        }
        for (WebPage page : pages) {
            if (page.getUrl().equals(url)) {
                throw new IllegalArgumentException();
            }
        }

        pages.add(new WebPage(url, pages.size(), keywords));
        updatePageRanks();
    }

    /**
     * Adds a link to the page
     * 
     * @param source      the source link
     * @param destination the destination link
     * @throws IllegalArgumentException if either link doesn't exist in the graph
     */
    public void addLink(String source, String destination) throws IllegalArgumentException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException();
        }
        WebPage sourcePage = null;
        WebPage destPage = null;
        for (WebPage page : pages) {
            if (page.getUrl().equals(source)) {
                sourcePage = page;
            } else if (page.getUrl().equals(destination)) {
                destPage = page;
            }
        }
        if (sourcePage == null) {
            System.out.printf("ERROR: %s could not be found\n", source);
            throw new IllegalArgumentException();
        } else if (destPage == null) {
            System.out.printf("ERROR: %s could not be found\n", destination);
            throw new IllegalArgumentException();
        }
        if (edges[sourcePage.getIndex()][destPage.getIndex()] == 1) {
            System.out.println("ERROR: Link was already established");
            throw new IllegalArgumentException();
        }
        edges[sourcePage.getIndex()][destPage.getIndex()] = 1;
        updatePageRanks();
    }

    /**
     * Removes a page from the graph
     */
    public void removePage(String url) {
        Collections.sort(pages, new IndexComparator());
        int indexToRemove = -1;
        for (int i = 0; i < pages.size(); i++) {
            if (pages.get(i).getUrl().equals(url)) {
                indexToRemove = i;
            }
            if (indexToRemove != -1) {
                pages.get(i).decrementIndex();
            }
        }
        pages.remove(indexToRemove);

        for (int i = 0; i <= pages.size(); i++) {
            for (int j = indexToRemove; j <= pages.size(); j++) {
                edges[i][j] = edges[i][j + 1];
            }
        }
        for (int i = 0; i <= pages.size(); i++) {
            for (int j = indexToRemove; j <= pages.size(); j++) {
                edges[j][i] = edges[j + 1][i];
            }
        }
        updatePageRanks();
    }

    /**
     * Removes a link from the graph
     * 
     * @param source      the source link
     * @param destination the destination link
     * @throws IllegalArgumentException if either link doesn't exist in the graph
     */
    public void removeLink(String source, String destination) throws IllegalArgumentException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException();
        }
        WebPage sourcePage = null;
        WebPage destPage = null;
        for (WebPage page : pages) {
            if (page.getUrl().equals(source)) {
                sourcePage = page;
            } else if (page.getUrl().equals(destination)) {
                destPage = page;
            }
        }
        if (sourcePage == null) {
            System.out.printf("ERROR: %s could not be found\n", source);
            throw new IllegalArgumentException();
        } else if (destPage == null) {
            System.out.printf("ERROR: %s could not be found\n", destination);
            throw new IllegalArgumentException();
        }

        edges[sourcePage.getIndex()][destPage.getIndex()] = 0;
        updatePageRanks();
    }

    /**
     * Updates the ranking of each page in the graph based on the simplified
     * PageRank algorithm.
     */
    public void updatePageRanks() {
        for (int i = 0; i < pages.size(); i++) {
            int currentRank = 0;
            for (int j = 0; j < pages.size(); j++) {
                currentRank += edges[j][i];
            }
            pages.get(i).setRank(currentRank);
        }
    }

    /**
     * Prints the WebGraph in a tabular form
     */
    public void printTable() {
        System.out.println("Index     URL               PageRank  Links               Keywords\n" +
                "--------------------------------------------------------------------" +
                "-------------------------------");
        for (int i = 0; i < pages.size(); i++) {
            StringBuilder links = new StringBuilder("");
            int numCommas = 0;
            for (int j = 0; j < pages.size(); j++) {
                if (edges[pages.get(i).getIndex()][j] == 1) {
                    if (numCommas != 0) {
                        links.append(", ");
                    }
                    numCommas++;
                    links.append(j);
                }
            }
            String formattedLinks = String.format(" %-18s", links.toString());
            System.out.println(pages.get(i).toString().replace("***", formattedLinks));
        }
    }

    /**
     * Prints out the actual contents of the adjancency matrix
     */
    public void printGraph() {
        System.out.print("  _");
        for (int i = 0; i < pages.size(); i++) {
            System.out.print(i + "_");
        }
        System.out.println();

        for (int i = 0; i < pages.size(); i++) {
            System.out.print(i + "| ");
            for (int j = 0; j < pages.size(); j++) {
                System.out.print(edges[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Gets the pages in the WebGraph
     * 
     * @return the pages in the WebGraph
     */
    public ArrayList<WebPage> getPages() {
        return pages;
    }
}
