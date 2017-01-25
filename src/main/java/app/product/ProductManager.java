package app.product;


import app.connectors.Connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductManager {

    private final static String UPDATE_QUERY = "UPDATE products SET product_type_id = ?, description = ?, " +
            "weight_type_id = ?, weight_value = ?, " +
            "brand_id = ?, price = ?, expiration_date = ?, name = ?" +
            "WHERE id = ?";
    private final static String DELETE_QUERY = "DELETE FROM products WHERE id = ?";
    private final static String INSERT_QUERY = "INSERT INTO products " +
            "(description, product_type_id, weight_type_id, weight_value, brand_id, price, expiration_date, name) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private final static String UPDATE_HISTORY_QUERY = "INSERT INTO history (product_id,price_difference,changed_on) VALUES (?,?,?);";
    private final static String GET_HISTORY_QUERY = "SELECT " +
            "     p.id, " +
            "     p.description, " +
            "     pt.name AS product_type, " +
            "     w.name AS weight_type, " +
            "     p.weight_value, " +
            "     b.name AS brand_name, " +
            "     b.country AS brand_country, " +
            "     p.name, " +
            "     p.price, " +
            "     p.expiration_date, " +
            "     h.changed_on, " +
            "     h.price_difference " +
            "FROM " +
            "     products AS p " +
            "INNER JOIN " +
            "     product_types as pt " +
            "ON " +
            "     p.product_type_id = pt.id " +
            "INNER JOIN " +
            "     weight_types AS w " +
            "ON " +
            "     p.weight_type_id = w.id " +
            "INNER JOIN " +
            "     brands AS b " +
            "ON " +
            "     p.brand_id = b.id " +
            "INNER JOIN " +
            "history AS h ON h.id = p.id";
    private final static String FIND_ALL_QUERY = "SELECT " +
            "     p.id, " +
            "     p.description, " +
            "     pt.name AS product_type, " +
            "     w.name AS weight_type, " +
            "     p.weight_value, " +
            "     b.name AS brand_name, " +
            "     b.country AS brand_country, " +
            "     p.name, " +
            "     p.price, " +
            "     p.expiration_date " +
            "FROM " +
            "     products AS p " +
            "INNER JOIN " +
            "     product_types as pt " +
            "ON " +
            "     p.product_type_id = pt.id " +
            "INNER JOIN " +
            "     weight_types AS w " +
            "ON " +
            "     p.weight_type_id = w.id " +
            "INNER JOIN " +
            "     brands AS b " +
            "ON " +
            "     p.brand_id = b.id ORDER BY expiration_date ASC";
    private final static String PRODUCT_TYPE_QUERY = "SELECT id FROM product_types WHERE name = ?";
    private final static String WEIGHT_TYPE_QUERY = "SELECT id FROM weight_types WHERE name = ?";
    private final static String BRAND_QUERY = "SELECT id FROM brands WHERE name = ? AND country = ?";

    private final Connection connection;

    public ProductManager() {
        this.connection = Connector.getConnection();
    }

    public void edit(int id, String description, String type, String weightType,
                     double weightValue, String brandName,
                     String brandCountry, String name, Double price,
                     Date expirationDate) throws SQLException {
        Relation relation = this.getRelation(type, weightType, brandName, brandCountry);
        PreparedStatement stmt = this.connection.prepareStatement(UPDATE_QUERY);
        stmt.setInt(1, relation.getProductTypeId());
        stmt.setString(2, description);
        stmt.setInt(3, relation.getWeightTypeId());
        stmt.setDouble(4, weightValue);
        stmt.setInt(5, relation.getBrandId());
        stmt.setDouble(6, price);
        stmt.setDate(7, new java.sql.Date(expirationDate.getTime()));
        stmt.setString(8, name);
        stmt.setInt(9, id);
        stmt.executeUpdate();
    }

    public void remove(int id) throws SQLException {
        PreparedStatement stmt = this.connection.prepareStatement(DELETE_QUERY);
        stmt.setInt(1, id);
        stmt.execute();
    }

    public void add(Product product) throws SQLException {
        Relation relation = this.getRelation(product.getType(), product.getWeightType(), product.getBrandName(), product.getBrandCountry());

        PreparedStatement stmt = this.connection.prepareStatement(INSERT_QUERY);

        stmt.setString(1, product.getDescription());
        stmt.setInt(2, relation.getProductTypeId());
        stmt.setInt(3, relation.getWeightTypeId());
        stmt.setDouble(4, product.getWeightValue());
        stmt.setInt(5, relation.getBrandId());
        stmt.setDouble(6, product.getPrice());
        stmt.setDate(7, new java.sql.Date(product.getExpirationDate().getTime()));
        stmt.setString(8, product.getName());
        stmt.executeUpdate();
    }

    public void updateHistory(int productId, Double priceDiff) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(UPDATE_HISTORY_QUERY);
        statement.setInt(1,productId);
        statement.setDouble(2,priceDiff);
        statement.setDate(3,new java.sql.Date(new Date().getTime()));
        statement.execute();
    }

    public List<String> getHistory() throws SQLException {
        List<String> history = new ArrayList<>();
        ResultSet rs = this.connection.createStatement().executeQuery(GET_HISTORY_QUERY);
        while (rs.next()) {
            StringBuilder sb = new StringBuilder();
            int numberOfColumns = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= numberOfColumns; i++) {
                sb.append(rs.getString(i));
                if (i < numberOfColumns) {
                    sb.append("|");
                }
            }
            history.add(sb.toString());
        }
        return history;
    }

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        ResultSet rs = this.connection.createStatement().executeQuery(FIND_ALL_QUERY);
        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("product_type"),
                    rs.getString("weight_type"),
                    rs.getDouble("weight_value"),
                    rs.getString("brand_name"),
                    rs.getString("brand_country"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getDate("expiration_date")
            );
            products.add(product);
        }

        return products;
    }

    private Relation getRelation(String type, String weightType, String brandName, String brandCountry) throws SQLException {
        PreparedStatement stmt = this.connection.prepareStatement(PRODUCT_TYPE_QUERY);
        stmt.setString(1, type);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int productTypeId = rs.getInt("id");

        stmt = this.connection.prepareStatement(WEIGHT_TYPE_QUERY);
        stmt.setString(1, weightType);
        rs = stmt.executeQuery();
        rs.next();
        int weightTypeId = rs.getInt("id");

        stmt = this.connection.prepareStatement(BRAND_QUERY);
        stmt.setString(1, brandName);
        stmt.setString(2, brandCountry);
        rs = stmt.executeQuery();
        rs.next();
        int brandId = rs.getInt("id");

        return new Relation(weightTypeId, productTypeId, brandId);
    }
}
