import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Item x = new Item (12, "fdr", "fda", "htr",10,20,30,true);
        System.out.println(x.getDescription());
        Inventory y = new Inventory();
        y.addItem(x);
        System.out.println(y.size());

    }
}
