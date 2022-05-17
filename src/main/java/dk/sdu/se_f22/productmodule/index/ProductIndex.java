package dk.sdu.se_f22.productmodule.index;


// Requires Product from group 4.4 and works it gets approved and merged

import java.sql.*;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import dk.sdu.se_f22.sharedlibrary.models.Product;

public class ProductIndex implements IProductIndex, IProductIndexDataAccess {

    private int categoryHits = 0;
    private int nameHits = 0;
    private int descriptionHits = 0;
    private List<Product> sortedList = new ArrayList<>();
    private static ProductIndex instance;
    private ProductIndex(){

    }

    public static ProductIndex getInstance(){
        if (instance == null) {
            instance = new ProductIndex();
        }
        return instance;
    }


    // Method for finding amount of hits within a product by a token, then returning an indexed list by the hits
    public List<Product> indexProductsByToken(List<Product> products, List<String> tokens) {

        for(Product p : products){
            String[] categoryWords = p.getCategory().toLowerCase().split("/");
            String[] nameWords = p.getName().toLowerCase().split(" ");
            String[] descriptionWords = p.getDescription().toLowerCase().split(" ");

            int totalHits = 0;

            for(String s : tokens){
                s = s.toLowerCase();
                totalHits = findHits(categoryWords, s) + findHits(nameWords, s) + findHits(descriptionWords, s);
            }

            p.setHitNum(totalHits);
            sortedList.add(p);
            Collections.sort(sortedList);
        }
        return sortedList;

    }

    public int findHits(String[] info, String token){
        int output = 0;
        for(String i : info){
            if(i == token){
                output++;
            }
        }
        return output;
    }




    public void updateProduct(String id, Product product){
        try {
            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE product set averageUserReview = ?, instock = ?, ean = ?, price = ?, publisheddate = ?, expirationdate = ?, category = ?, name = ?, description = ?, weight = ? WHERE id = ?");
            updateStatement.setDouble(1, product.getAverageUserReview());
            updateStatement.setArray(2, (Array) product.getInStock());
            updateStatement.setLong(3, product.getEan());
            updateStatement.setDouble(4, product.getPrice());
            updateStatement.setString(5, String.valueOf(product.getPublishedDate()));
            updateStatement.setString(6, String.valueOf(product.getExpirationDate()));
            updateStatement.setString(7, product.getCategory());
            updateStatement.setString(8, product.getName());
            updateStatement.setString(9, product.getDescription());
            updateStatement.setDouble(10, product.getWeight());
            updateStatement.setString(11, String.valueOf(id));
            updateStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(String id){
        try {
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM product WHERE id = ?");
            deleteStatement.setString(1, id);
            deleteStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void createProduct(Product product){
        // calling stored procedure within db
        try {
            CallableStatement cs = connection.prepareCall("CALL insertproduct(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            cs.setString(1,product.getUuid().toString());
            cs.setDouble(2,product.getAverageUserReview());
            cs.setArray(3,connection.createArrayOf("VARCHAR",product.getInStock().toArray()));
            cs.setLong(4,product.getEan());
            cs.setDouble(5,product.getPrice());
            cs.setTimestamp(6,Timestamp.from(product.getPublishedDate()));
            cs.setTimestamp(7,Timestamp.from(product.getExpirationDate()));
            cs.setString(8,product.getCategory());
            cs.setString(9,product.getName());
            cs.setString(10,product.getDescription());
            cs.setString(11,product.getSize());
            cs.setDouble(12,product.getClockspeed());
            cs.setDouble(13,product.getWeight());

            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Product> getProducts(){
        List<Product> productList = new ArrayList<>();

        try {
            // query all products

            PreparedStatement p = connection.prepareStatement("select product.productid," +
                    "averageuserreview,ean,price,publisheddate,expirationdate,categories.category," +
                    "name,description,storage.size,specs.clockspeed,product.weight from product\n" +
                    "LEFT JOIN  productspecs on product.productid = productspecs.productid\n" +
                    "LEFT JOIN  specs on productspecs.specid = specs.specid\n" +
                    "LEFT JOIN  productstorage on productstorage.productid = product.productid\n" +
                    "LEFT JOIN  storage on productstorage.storageid = storage.storageid\n" +
                    "LEFT JOIN categories on categories.categoryid = product.categoryid;");
            ResultSet resultSet = p.executeQuery();

            while(resultSet.next()){
                List<String> citylist = new ArrayList<>();

                //iterate through cities in the city column of stock table for all products
                PreparedStatement pcity = connection.prepareStatement("select city from stock" +
                        " inner join productstock on productstock.stockid = stock.stockid " +
                        "INNER JOIN product on product.productid = productstock.productid" +
                        " WHERE product.productid = ?");
                pcity.setString(1,resultSet.getString(1));
                ResultSet citySet = pcity.executeQuery();

                while(citySet.next()){
                    citylist.add(citySet.getString(1));
                }

                // create product object
                productList.add(new Product(UUID.fromString(resultSet.getString(1)),
                        resultSet.getDouble(2), citylist,resultSet.getInt(3),
                        resultSet.getDouble(4),resultSet.getTimestamp(5).toInstant(),
                        resultSet.getTimestamp(6).toInstant(), resultSet.getString(7),
                        resultSet.getString(8), resultSet.getString(9),
                        resultSet.getString(10), resultSet.getDouble(11),
                        resultSet.getDouble(12)
                ));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}






        /*
        for (int i = 0; i < products.size(); i++) {
            for (int n = 0; n < token.size(); n++) {
                String[] categoryWords = products.get(i).getCategory().toLowerCase().split("/");
                String[] nameWords = products.get(i).getName().toLowerCase().split(" ");
                String[] descriptionWords = products.get(i).getDescription().toLowerCase().split(" ");


                for (String cateElem : categoryWords) {
                    if (cateElem.contains(token.get(n).toLowerCase())) {
                        categoryHits += 1;
                    }
                }
                for (String nameElem : nameWords) {
                    if (nameElem.contains(token.get(n).toLowerCase())) {
                        nameHits += 1;
                    }
                }
                for (String descElem : descriptionWords) {
                    if (descElem.contains(token.get(n).toLowerCase())) {
                        descriptionHits += 1;
                    }
                }
            }
            int total = categoryHits + nameHits + descriptionHits;

            System.out.printf("Product: " + i +
                            "\nCategory hit counter: %4d " +
                            "\nName hit counter: %8d " +
                            "\nDescription hit counter: %d " +
                            "\nTotal: %19d\n\n",
                            categoryHits, nameHits, descriptionHits, total);

            products.get(i).setHitNum(total);
            sortedList.add(products.get(i));
            Collections.sort(sortedList);


            categoryHits = 0;
            nameHits = 0;
            descriptionHits = 0;
        }


        return sortedList;

        */