import java.util.Comparator;

public class URLComparator implements Comparator<Object> {
    /**
     * Compares two WebPages by their url
     */
    public int compare(Object o1, Object o2) {
        WebPage w1 = (WebPage) o1;
        WebPage w2 = (WebPage) o2;

        return w1.getUrl().compareTo(w2.getUrl());
    }
}
