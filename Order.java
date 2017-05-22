package Cinema;
/**
 * Created by TaioYui on 25.11.2015.
 */
public class Order {
    Films chosenFilm;
    int amount;
    double totalPrice;



    public Order(Films chosenFilm, int amount, double totalPrice) {
        this.amount = amount;
        this.chosenFilm = chosenFilm;
        this.totalPrice = totalPrice;

    }


}
