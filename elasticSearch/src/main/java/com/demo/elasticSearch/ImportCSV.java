package com.demo.elasticSearch;

import com.opencsv.CSVReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class ImportCSV {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/product_dataset";
        String user = "root";
        String password = "root";
        String csvFilePath = "/home/dev1024/Harshil/Learning/SpringBoot/elasticSearch/src/main/resources/electronics_product.csv";

        // Adjusted query based on your CSV headers
        String query = "INSERT INTO product (name, main_category, sub_category, image, link, ratings, no_of_ratings, discount_price, actual_price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            PreparedStatement pstmt = conn.prepareStatement(query);

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                pstmt.setString(1, nextLine[0]);  // name
                pstmt.setString(2, nextLine[1]);  // main_category
                pstmt.setString(3, nextLine[2]);  // sub_category
                pstmt.setString(4, nextLine[3]);  // image
                pstmt.setString(5, nextLine[4]);  // link
                pstmt.setDouble(6, Double.parseDouble(nextLine[5]));  // ratings
                pstmt.setInt(7, Integer.parseInt(nextLine[6]));  // no_of_ratings
                pstmt.setDouble(8, Double.parseDouble(nextLine[7]));  // discount_price
                pstmt.setDouble(9, Double.parseDouble(nextLine[8]));  // actual_price

                pstmt.addBatch();  // Add this to batch for more efficient insertion
            }

            pstmt.executeBatch();  // Execute the batch of inserts
            System.out.println("Data imported successfully.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
