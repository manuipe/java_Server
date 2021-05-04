package application;

import java.util.ArrayList;

public class Invoice {

    //Attributes
    private ArrayList<Product> products;
    private  double total;

    //Constructor
    public Invoice(ArrayList<Product> products, double total) {
        this.products = products;
        this.total = total;
    }

    //Methods - GETTERS AND SETTERS
    public ArrayList<Product> getProducts(){
        return products;
    }
    //Methods - GETTERS AND SETTERS
    public void  setProducts(ArrayList<Product> products) {
        this.products = products;
    }
    //Methods - GETTERS AND SETTERS
    public  double getTotal() {
        return total;
    }
    //Methods - GETTERS AND SETTERS
    public void setTotal(double total) {
        this.total = total;
    }

    //Method
    public String toString() {

        for (Product p: products) {
            System.out.println(p.toString());
        }

        return "Invoice Total: $" + String.format("%.2f", getTotal()) + "\n";
    }

    static  class Product {

        //Attributes
        private  String name;
        private double price;

        //Constructor
        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        //Methods - Getters and Setters
        public String getName(){
            return name;
        }

        //Methods - Getters and Setters
        public void setName(String name) {
            this.name = name;
        }

        //Methods - Getters and Setters

        public double getPrice() {
            return price;
        }
        //Methods - Getters and Setters
        public void setPrice(double price) {
            this.price = price;
        }

        //Method
        public String toString() {
            return getName() + ": " + "$" + String.format("%.2f", getPrice());
        }
    }
}
