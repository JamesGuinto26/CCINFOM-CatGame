import javax.swing.*;

public class Furniture
{
    private String name;
    private String type;
    private double price;
    private String description;
    private ImageIcon image;

    public Furniture(String name, String type, double price, String description)
    {
        this.name = name;
        this.type = type;
        this.price = price;
        this.description = description;

        // images for testing only
        switch(type.toLowerCase()) {
            case "pillow":
                image = new ImageIcon("images/pillow.png");
                break;
            case "box":
                image = new ImageIcon("images/box.png");
                break;
            case "cushion":
                image = new ImageIcon("images/cushion.png");
                break;
            case "bucket":
                image = new ImageIcon("images/bucket.png");
                break;
            case "house":
                image = new ImageIcon("images/house.png");
                break;
            case "hamock":
                image = new ImageIcon("images/hamock.png");
                break;
            case "bed":
                image = new ImageIcon("images/bed.png");
                break;
            case "tree":
                image = new ImageIcon("images/tree.png");
                break;
            case "scratcher":
                image = new ImageIcon("images/scratcher.png");
                break;
            case "thunder":
                image = new ImageIcon("images/No_BG/1_Thunder.png");
                break;
            case "maple":
                image = new ImageIcon("images/No_BG/2_Maple.png");
                break;
            case "luna":
                image = new ImageIcon("images/No_BG/3_Luna.png");
                break;
            case "pringles":
                image = new ImageIcon("images/No_BG/4_Pringles.png");
                break;
            case "brook":
                image = new ImageIcon("images/No_BG/5_Brook.png");
                break;
            case "sam":
                image = new ImageIcon("images/No_BG/6_Sam.png");
                break;
            case "oscar":
                image = new ImageIcon("images/No_BG/7_Oscar.png");
                break;
            case "bella":
                image = new ImageIcon("images/No_BG/8_Bella.png");
                break;
            case "simba":
                image = new ImageIcon("images/No_BG/9_Simba.png");
                break;
            case "chloe":
                image = new ImageIcon("images/No_BG/10_Chloe.png");
                break;
            case "leo":
                image = new ImageIcon("images/No_BG/11_Leo.png");
                break;
            case "milo":
                image = new ImageIcon("images/No_BG/12_Milo.png");
                break;
            case "sophie":
                image = new ImageIcon("images/No_BG/13_Sophie.png");
                break;
            case "max":
                image = new ImageIcon("images/No_BG/14_Max.png");
                break;
            case "lily":
                image = new ImageIcon("images/No_BG/15_Lily.png");
                break;
            case "charlie":
                image = new ImageIcon("images/No_BG/16_Charlie.png");
                break;
            case "zoe":
                image = new ImageIcon("images/No_BG/17_Zoe.png");
                break;
            case "tiger":
                image = new ImageIcon("images/No_BG/18_Tiger.png");
                break;
            case "coco":
                image = new ImageIcon("images/No_BG/19_Coco.png");
                break;
            case "jasper":
                image = new ImageIcon("images/No_BG/20_Jasper.png");
                break;

            default:
                image = new ImageIcon("images/furnitureDef.png");
                break;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public double getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public ImageIcon getImage() {
        return this.image;
    }

}
