package app.product;


import app.constants.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {

    private Integer id;
    private String description;
    private String type;
    private String weightType;
    private double weightValue;
    private String brandName;
    private String brandCountry;
    private String name;
    private double price;
    private Date expirationDate;

    public Product() {
    }

    public Product(Integer id, String description, String type,
                   String weightType,
                   double weightValue, String brandName,
                   String brandCountry, String name, double price,
                   Date expirationDate) {

        this.id = id;
        this.description = description;
        this.type = type;
        this.weightType = weightType;
        this.weightValue = weightValue;
        this.brandName = brandName;
        this.brandCountry = brandCountry;
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public double getWeightValue() {
        return weightValue;
    }

    public void setWeightValue(double weightValue) {
        this.weightValue = weightValue;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandCountry() {
        return brandCountry;
    }

    public void setBrandCountry(String brandCountry) {
        this.brandCountry = brandCountry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> exportData() {
        List<String> datas = new ArrayList<>();
        datas.add(this.getId().toString());
        datas.add(this.getDescription());
        datas.add(this.getType());
        datas.add(this.getWeightType());
        datas.add(String.valueOf(this.getWeightValue()));
        datas.add(this.getBrandName());
        datas.add(this.getBrandCountry());
        datas.add(this.getName());
        datas.add(String.valueOf(this.getPrice()));
        datas.add(new SimpleDateFormat(AppConstants.DATE_FORMAT_PATTERN).format(this.getExpirationDate()));
        return datas;
    }
}

