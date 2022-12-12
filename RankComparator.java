import java.util.Comparator;

public class RankComparator implements Comparator<Object> {
    /**
     * Compares two WebPages by their rank
     */
    public int compare(Object o1, Object o2) {
        WebPage w1 = (WebPage) o1;
        WebPage w2 = (WebPage) o2;

        if (w1.getRank() > w2.getRank()) {
            return -1;
        } else if (w1.getRank() == w2.getRank()) {
            return 0;
        } else {
            return 1;
        }
    }
}