import java.util.ArrayList;

public class WebPage {
    private String url;
    private int index;
    private int rank;
    private ArrayList<String> keywords = new ArrayList<String>();

    /**
     * Empty constructor for the WebPage class
     */
    public WebPage() {
        url = "";
        index = 0;
        rank = 0;
    }

    /**
     * Overloaded constructor for the WebPage class
     * 
     * @param url      the url of the page
     * @param index    the index in pages
     * @param keywords the keywords associated with the page
     */
    public WebPage(String url, int index, ArrayList<String> keywords) {
        this.url = url;
        this.keywords = keywords;
        this.index = index;
    }

    /**
     * Gets the url of the page
     * 
     * @return the url of the page
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the index of the page in pages
     * 
     * @return the index of the page
     */
    public int getIndex() {
        return index;
    }

    /**
     * Decrements the index of the page by one
     */
    public void decrementIndex() {
        index--;
    }

    /**
     * Gets the rank of the page
     * 
     * @return the rank of the page
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets the new rank of the page
     * 
     * @param rank the new rank of the page
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    /**
     * Returns a string representing the WebPage
     * 
     * @return the string representing the WebPage
     */
    public String toString() {
        StringBuilder formattedString = new StringBuilder("  ");
        formattedString.append(String.format("%-3d | %-19s|    %d    |***| ", index, url,
                rank));
        for (int i = 0; i < keywords.size() - 1; i++) {
            formattedString.append(keywords.get(i));
            formattedString.append(", ");
        }
        formattedString.append(keywords.get(keywords.size() - 1));

        return formattedString.toString();

    }
}