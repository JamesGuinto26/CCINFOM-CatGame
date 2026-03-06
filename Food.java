import javax.swing.*;

public class Food {
    private String name;
    private double price;
    private String description;
    private int saturationValue;
    private ImageIcon image;

    public Food (String name, double price, String description, int saturationValue) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.saturationValue = saturationValue;

        switch(name.toLowerCase()) {
            case "tuna_bites":
                image = new ImageIcon("images/Tuna_Bites.png");
                break;
            case "salmon_jelly":
                image = new ImageIcon("images/Salmon_Jelly.png");
                break;
            case "chicken_shreds":
                image = new ImageIcon("images/Chicken_Shreds.png");
                break;
            case "beef_kibble":
                image = new ImageIcon("images/Beef_Kibble.png");
                break;
            case "mackerel_stew":
                image = new ImageIcon("images/Mackerel_Stew.png");
                break;
            case "ocean_treats":
                image = new ImageIcon("images/Ocean_Treats.png");
                break;
            case "fish_biscuits":
                image = new ImageIcon("images/Fish_Biscuits.png");
                break;
            case "seafood_deluxe":
                image = new ImageIcon("images/Seafood_Deluxe.png");
                break;
            case "milk_pudding":
                image = new ImageIcon("images/Milk_Pudding.png");
                break;
            case "shrimp_bowl":
                image = new ImageIcon("images/Shrimp_Bowl.png");
                break;
            default:
                image = new ImageIcon("images/catDefault.png");
                break;
        }
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public int getSaturationValue() {
        return this.saturationValue;
    }

    public ImageIcon getImage() {
        return this.image;
    }
}