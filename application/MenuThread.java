package application;

import com.google.gson.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class MenuThread extends Thread {

    public void run() {

        while (true) {
            System.out.println("Enter 'R' to generate report, the total of all invoices saved");
            System.out.println("Enter 'D' to delete all invoices from the server");
            System.out.println("Enter 'E' to terminate the server");

            Scanner scanner = new Scanner(System.in);
            System.out.println("\nEnter the task to perform");
            String task = scanner.nextLine().toUpperCase();

            switch (task) {
                case "R":
                    System.out.println(report());
                    break;
                case "D":
                    delete();
                    break;
                case "E":
                    System.exit(0);
                default:
                    System.out.println("Invalid Task\n");
            }

        }
    }


    private String report() {

        ArrayList<Invoice.Product> products = new ArrayList<Invoice.Product>();
        double total = 0;
        Invoice invoice = new Invoice(products, total);
        List<Invoice> invoices = new ArrayList<Invoice>();

        String name = null;
        double price = 0;

        Invoice.Product product = new Invoice.Product(name, price);

        System.out.println("Report\n");

        String path = System.getProperty("user.home") + File.separator + "Invoices";

        File directory = new File(path);
        File fileList[] = directory.listFiles();

        for (File file:fileList) {
            String readThisFile = file.getName().toLowerCase();

            if(readThisFile.endsWith(".json")) {

                try {

                    //create a Gson instance
                    Gson gson = new Gson();

                    //create a reader
                    URI uri = file.toURI();
                    Reader reader = Files.newBufferedReader(Paths.get(uri));

                    //convert JSON string to object
                    JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                    reader.close();

                    products = new ArrayList<Invoice.Product>();

                    JsonArray jsonArray = jsonObject.get("invoice").getAsJsonObject().get("products").getAsJsonObject().get("product").getAsJsonArray();

                    for (JsonElement element : jsonArray) {
                        JsonObject elementAsJsonObject = element.getAsJsonObject();
                        name = elementAsJsonObject.get("name").getAsString();
                        price = elementAsJsonObject.get("price").getAsDouble();

                        product = new Invoice.Product(name, price);
                        products.add(product);
                    }

                    total = jsonObject.get("invoice").getAsJsonObject().get("total").getAsDouble();

                    invoice = new Invoice(products, total);
                    invoices.add(invoice);
                    System.out.println(invoice.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }

            } else if (readThisFile.endsWith(".xml")) {

                try {
                    // make an xml document
                    Document xml = null;

                    xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

                    // search the xml for occurrences of name and price
                    NodeList nameList = xml.getElementsByTagName("name");
                    NodeList priceList = xml.getElementsByTagName("price");
                    products = new ArrayList<Invoice.Product>();
                    for (int i = 0; i < nameList.getLength(); i++) {
                        name = nameList.item(i).getTextContent();
                        price = Double.parseDouble(priceList.item(i).getTextContent());

                        product = new Invoice.Product(name, price);
                        products.add(product);
                    }

                    NodeList nodeList = xml.getElementsByTagName("total");
                    // there is only one total item, so it will be position zero of list
                    total = Double.parseDouble(nodeList.item(0).getTextContent());

                    invoice = new Invoice(products, total);
                    invoices.add(invoice);
                    System.out.println(invoice.toString());

                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
            total = 0;
            for (Invoice i: invoices) {
                total += i.getTotal();
            }
        }

        return "The total amount for " + invoices.size() + " invoices is $" + String.format("%.2f", total) + "\n";
    }

    private void delete() {

        String path = System.getProperty("user.home") + File.separator + "Invoices";

        System.out.println("WARNING! YOU HAVE CHOSEN TO DELETE ALL INVOICES.\n" +
                "THIS WILL DELETE ALL FILES ENDING WITH .xml OR .json FROM " + path);

        System.out.println("THE FOLLOWING FILES WILL BE DELETED: \n");

        File directory = new File(path);
        File fileList[] = directory.listFiles();

        for (File file:fileList) {
            String fileToDelete = file.getName().toLowerCase();
            if (fileToDelete.endsWith(".xml")||fileToDelete.endsWith(".json")) {
                System.out.println(fileToDelete);
            }
        }

        System.out.println("TYPE 'DELETE' AND PRESS ENTER TO PROCEED PT PRESS ENTER TO CANCEL");

        Scanner scanner = new Scanner(System.in);
        String delete = scanner.nextLine().toUpperCase();
        if (delete.equals("DELETE")) {
            for (File file:fileList) {
                String fileToDelete = file.getName().toLowerCase();
                if (fileToDelete.endsWith(".xml")||fileToDelete.endsWith(".json")) {
                    file.delete();
                }
            }
            System.out.println("All invoices deleted\n");
        } else {
            System.out.println("Operation cancelled. No invoices have been deleted\n");
        }
    }
}
