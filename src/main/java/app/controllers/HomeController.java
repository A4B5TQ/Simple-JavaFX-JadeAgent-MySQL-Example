package app.controllers;

import app.constants.AppConstants;
import app.constants.ProductConstants;
import app.dialogBoxes.Dialog;
import app.dialogBoxes.DialogBox;
import app.enums.ReportType;
import app.parsers.ExcelReader;
import app.parsers.ExcelWriter;
import app.parsers.interfaces.Writer;
import app.product.Product;
import app.product.ProductManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class HomeController {

    private static final String FX_ALIGN_CENTER = "-fx-alignment: CENTER;";
    private static final String[] PRODUCTS_PROPERTIES = Arrays.stream(Product.class
            .getDeclaredFields())
            .map(Field::getName).toArray(String[]::new);
    private static final Dialog DIALOG = new DialogBox();
    private static final String[] EXCEL_FILE_EXTENSIONS = {"*.xlsx", "*.xls"};
    private final static List<String> options = new ArrayList<String>() {
        {
            add(ProductConstants.ID);
            add(ProductConstants.DESCRIPTION);
            add(ProductConstants.TYPE);
            add(ProductConstants.WEIGHT_TYPE);
            add(ProductConstants.WEIGHT_VALUE);
            add(ProductConstants.BRAND_NAME);
            add(ProductConstants.BRAND_COUNTRY);
            add(ProductConstants.NAME);
            add(ProductConstants.PRICE);
            add(ProductConstants.EXPIRATION_DATE);
        }

    };

    private final static Map<String, Integer> columns = new HashMap<String, Integer>() {
        {
            put("A", 0);
            put("B", 1);
            put("C", 2);
            put("D", 3);
            put("E", 4);
            put("F", 5);
            put("G", 6);
            put("H", 7);
            put("I", 8);
            put("J", 9);
            put("K", 10);
            put("L", 11);
            put("M", 12);
            put("N", 13);
            put("O", 14);
            put("P", 15);
            put("Q", 16);
            put("R", 17);
            put("S", 18);
            put("T", 19);
            put("U", 20);
            put("V", 21);
            put("W", 22);
            put("X", 23);
            put("Y", 24);
            put("Z", 25);
        }
    };

    private Stage currentStage;

    public Button generateReportButton;
    public Button listProductsButton;
    public Button addProductButton;
    public Button importProductsButton;
    public AnchorPane viewPane;
    public Pane addPane;
    public TextArea descriptionField;
    public TextField typeField;
    public TextField weightTypeField;
    public TextField weightValueField;
    public TextField brandNameField;
    public TextField brandCountryField;
    public TextField nameField;
    public TextField priceField;
    public DatePicker dateField;
    public TableView listProduct;
    public HBox listSettings;
    public Button deleteButton;
    public Button editProduct;

    public void generateReport() throws IOException, SQLException {
        this.currentStage = (Stage) this.generateReportButton.getScene().getWindow();
        List<String> choices = new ArrayList<>();
        choices.add("Financial Report");
        choices.add("Product Stock Report");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Financial Report", choices);
        dialog.setTitle("Choice Report");
        dialog.setHeaderText("Export data!");
        dialog.setContentText("Choose your report type:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String choice = result.get();
            switch (choice) {
                case "Financial Report":
                    this.report(ReportType.FINANCIAL);
                    break;
                case "Product Stock Report":
                    this.report(ReportType.STOCK);
                    break;
            }
        }
    }

    public void listProducts() throws IOException, ParseException, SQLException {
        this.clearView();
        this.listProduct = this.populateTable();
        this.currentStage = (Stage) this.listProductsButton.getScene().getWindow();
        this.viewPane.getChildren().add(this.listProduct);
        this.listProduct.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
                    Product product = row.getItem();
                    this.listSettings.setVisible(true);
                    this.deleteButton.setOnAction(delete -> {
                        confirmBox(product);
                    });

                    this.editProduct.setOnAction(edit -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                                    .getResource(AppConstants.EDIT_VIEW_PATH));
                            Parent root = (Parent) loader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.initModality(Modality.APPLICATION_MODAL);
                            EditController controller = loader.getController();
                            controller.initData(stage,product);
                            stage.showAndWait();
                            refreshTable();
                            DIALOG.successfullyOperation();
                        } catch (Exception e) {
                            DIALOG.errorOperation();
                        }
                    });
                }
            });
            return row;
        });
    }


    public void addProduct() throws IOException {
        this.addForm();
    }

    public void importProduct() throws IOException, SQLException {
        this.currentStage = (Stage) this.importProductsButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", EXCEL_FILE_EXTENSIONS));
        File selectedFile = fileChooser.showOpenDialog(currentStage);
        try {
            if (selectedFile != null) {
                ExcelReader reader = new ExcelReader();
                Map<Integer, String> parseCellMap = this.mapObjectDialogBox();
                List<Product> res = reader.importExcelFile(selectedFile, parseCellMap);
                ProductManager manager = new ProductManager();
                for (Product product : res) {
                    manager.add(product);
                }
                DIALOG.successfullyOperation();
            }
        } catch (Exception e) {
            DIALOG.errorOperation();
        }

    }

    public void submit() {
        String description = this.descriptionField.getText().trim();
        this.descriptionField.clear();
        String type = this.typeField.getText().trim();
        this.typeField.clear();
        String weightType = this.weightTypeField.getText().trim();
        this.weightTypeField.clear();
        Double weightValue = Double.valueOf(this.weightValueField.getText().trim());
        this.weightValueField.clear();
        String brandName = this.brandNameField.getText().trim();
        this.brandNameField.clear();
        String brandCountry = this.brandCountryField.getText().trim();
        this.brandCountryField.clear();
        String name = this.nameField.getText().trim();
        this.nameField.clear();
        Double price = Double.valueOf(this.priceField.getText().trim());
        this.priceField.clear();
        LocalDate localDate = this.dateField.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date expDate = Date.from(instant);
        this.dateField.getEditor().clear();
        try {
            Product product = new Product(null, description, type, weightType,
                    weightValue, brandName, brandCountry, name, price, expDate);
            ProductManager manager = new ProductManager();
            manager.add(product);
            DIALOG.successfullyOperation();
        } catch (Exception e) {
            DIALOG.errorOperation();
        }
    }

    private void report(ReportType reportType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File as...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", EXCEL_FILE_EXTENSIONS[0]));
        File file = fileChooser.showSaveDialog(currentStage);
        try {
            Writer writer = new ExcelWriter();
            writer.exportXLSX(file, reportType);
            DIALOG.successfullyOperation();
        } catch (Exception e) {
            DIALOG.errorOperation();
        }
    }

    private void confirmBox(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Permanently deleting a product from database!");
        alert.setContentText("Are you sure you wanna delete product " + product.getName() + " from database?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            deleteProduct(product);
            refreshTable();
        } else {
            DIALOG.errorOperation();
        }
    }

    private void addForm() {
        this.clearView();
        this.addPane.setVisible(true);

    }

    private void clearView() {
        this.viewPane.getChildren().forEach(e -> e.setVisible(false));
    }

    private Map<Integer, String> mapObjectDialogBox() {
        Map<Integer, String> objectMap = new HashMap<>();
        ChoiceDialog<String> dialog = new ChoiceDialog<>("A", columns.keySet());
        dialog.setTitle("Column map dialog");
        dialog.setHeaderText("Choose column that contains current value!");
        Iterator<String> optionsIterator = options.iterator();
        final int[] optionsCounter = {0};
        while (optionsIterator.hasNext()) {
            dialog.setContentText("Please enter column letter that contains " + options.get(optionsCounter[0]) + " value: ");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(column -> {
                int cellIndex = columns.get(column);
                if (!objectMap.containsKey(cellIndex)) {
                    String option = optionsIterator.next();
                    optionsCounter[0]++;
                    objectMap.put(cellIndex, option);
                } else {
                    DIALOG.columnTakenErrorBox(String.valueOf(column));
                }
            });
        }
        return objectMap;
    }

    @SuppressWarnings("unchecked")
    private TableView populateTable() throws ParseException, SQLException {
        ObservableList<Product> data = FXCollections.observableArrayList();
        ProductManager manager = new ProductManager();
        List<Product> products = manager.findAll();
        products.forEach(data::add);

        TableView table = new TableView();
        table.toFront();

        TableColumn idCol = new TableColumn(ProductConstants.ID);
        idCol.setResizable(false);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Product, String>(PRODUCTS_PROPERTIES[0])
        );
        idCol.setStyle(FX_ALIGN_CENTER);

        TableColumn<Product, String> descriptionCol = new TableColumn<>(ProductConstants.DESCRIPTION);
        descriptionCol.setResizable(false);
        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<>(PRODUCTS_PROPERTIES[1])
        );
        descriptionCol.setStyle(FX_ALIGN_CENTER);

        TableColumn<Product, String> typeCol = new TableColumn<>(ProductConstants.TYPE);
        typeCol.setCellValueFactory(
                new PropertyValueFactory<>(PRODUCTS_PROPERTIES[2])
        );
        typeCol.setStyle(FX_ALIGN_CENTER);
        typeCol.setResizable(false);

        TableColumn<Product, String> weightType = new TableColumn<>(ProductConstants.WEIGHT_TYPE);
        weightType.setCellValueFactory(
                new PropertyValueFactory<>(PRODUCTS_PROPERTIES[3])
        );
        weightType.setStyle(FX_ALIGN_CENTER);
        weightType.setResizable(false);

        TableColumn<Product, Double> weightValue = new TableColumn<>(ProductConstants.WEIGHT_VALUE);
        weightValue.setCellValueFactory(
                new PropertyValueFactory<>(PRODUCTS_PROPERTIES[4])
        );
        weightValue.setStyle(FX_ALIGN_CENTER);
        weightValue.setResizable(false);

        TableColumn<Product, String> brandName = new TableColumn<>(ProductConstants.BRAND_NAME);
        brandName.setCellValueFactory(
                new PropertyValueFactory<>(PRODUCTS_PROPERTIES[5])
        );
        brandName.setStyle(FX_ALIGN_CENTER);
        brandName.setResizable(false);

        TableColumn<Product, String> brandCountry = new TableColumn<>(ProductConstants.BRAND_COUNTRY);
        brandCountry.setCellValueFactory(
                new PropertyValueFactory<>(PRODUCTS_PROPERTIES[6])
        );
        brandCountry.setStyle(FX_ALIGN_CENTER);
        brandCountry.setResizable(false);

        TableColumn<Product, String> nameCol = new TableColumn<>(ProductConstants.NAME);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>(PRODUCTS_PROPERTIES[7])
        );
        nameCol.setStyle(FX_ALIGN_CENTER);
        nameCol.setResizable(false);

        TableColumn<Product, Double> priceCol = new TableColumn<>(ProductConstants.PRICE);
        priceCol.setCellValueFactory(
                new PropertyValueFactory<>(PRODUCTS_PROPERTIES[8])
        );
        priceCol.setStyle(FX_ALIGN_CENTER);
        priceCol.setResizable(false);

        TableColumn<Product, Date> expDate = new TableColumn<>(ProductConstants.EXPIRATION_DATE);
        expDate.setCellValueFactory(
                new PropertyValueFactory<>(PRODUCTS_PROPERTIES[9])
        );
        expDate.setStyle(FX_ALIGN_CENTER);
        expDate.setResizable(false);

        table.getColumns().addAll(idCol, descriptionCol, typeCol,
                weightType, weightValue, brandName,
                brandCountry, nameCol, priceCol, expDate);
        table.setItems(data);
        table.prefWidthProperty().bind(((Pane) this.viewPane.getParent()).widthProperty());
        return table;
    }

    private void deleteProduct(Product product) {
        ProductManager manager = new ProductManager();
        try {
            manager.remove(product.getId());
            DIALOG.successfullyOperation();
        } catch (SQLException e) {
            DIALOG.errorOperation();
        }
    }

    private void refreshTable() {
        try {
            this.listProducts();
        } catch (IOException | ParseException | SQLException e) {
            DIALOG.errorOperation();
        }
    }
}
