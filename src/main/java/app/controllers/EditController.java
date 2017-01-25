package app.controllers;

import app.dialogBoxes.Dialog;
import app.dialogBoxes.DialogBox;
import app.product.Product;
import app.product.ProductManager;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class EditController {

    public TextArea descriptionField;
    public TextField typeField;
    public TextField weightTypeField;
    public TextField weightValueField;
    public TextField brandNameField;
    public TextField brandCountryField;
    public TextField nameField;
    public TextField priceField;
    public DatePicker dateField;
    private Product product;
    private Stage modalStage;

    public void initData(Stage owner,Product product) {
        this.modalStage = owner;
        this.product = product;
        this.populateData(product);
    }

    private void populateData(Product product) {
        this.descriptionField.setText(product.getDescription());
        this.typeField.setText(product.getType());
        this.weightTypeField.setText(product.getWeightType());
        this.weightValueField.setText(String.valueOf(product.getWeightValue()));
        this.brandNameField.setText(product.getBrandName());
        this.brandCountryField.setText(product.getBrandCountry());
        this.nameField.setText(product.getName());
        this.priceField.setText(String.valueOf(product.getPrice()));
        Date input = product.getExpirationDate();
        Instant instant = Instant.ofEpochMilli(input.getTime());
        LocalDate expDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .toLocalDate();
        this.dateField.setValue(expDate);
    }

    public void edit() {
        int id = product.getId();
        String description = this.descriptionField.getText().trim();
        String type = this.typeField.getText().trim();
        String weightType = this.weightTypeField.getText().trim();
        Double weightValue = Double.valueOf(this.weightValueField.getText().trim());
        String brandName = this.brandNameField.getText().trim();
        String brandCountry = this.brandCountryField.getText().trim();
        String name = this.nameField.getText().trim();
        Double price = Double.valueOf(this.priceField.getText().trim());
        LocalDate localDate = this.dateField.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date expDate = Date.from(instant);

        try {
            ProductManager manager = new ProductManager();
            manager.edit(id, description, type, weightType,
                    weightValue, brandName, brandCountry, name, price, expDate);
            Double priceDiff = price - product.getPrice();
            manager.updateHistory(id,priceDiff);
            this.modalStage.close();
        } catch (Exception e) {
            Dialog dialog = new DialogBox();
            dialog.errorOperation();
        }
    }
}
