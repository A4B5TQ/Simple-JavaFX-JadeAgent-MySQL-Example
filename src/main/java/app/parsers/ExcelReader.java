package app.parsers;

import app.constants.AppConstants;
import app.constants.ProductConstants;
import app.parsers.interfaces.Reader;
import app.product.Product;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExcelReader implements Reader {

    private final static String XLSX = "xlsx";
    private final static String XLS = "xls";
    private final static String INCORRECT_DATE_FORMAT = "Incorrect date format!";

    public List<Product> importExcelFile(File chooseFile, Map<Integer, String> cellMap) throws IOException {

        FileInputStream stream = new FileInputStream(chooseFile);
        int dotIndex = chooseFile.getName().lastIndexOf(".");
        String fileType = chooseFile.getName().substring(dotIndex + 1);
        Workbook workbook = null;

        switch (fileType) {
            case XLSX:
                workbook = new XSSFWorkbook(stream);
                break;
            case XLS:
                workbook = new HSSFWorkbook(stream);
                break;
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<Product> products = new ArrayList<>();
        for (Row row : sheet) {

            if (row.getRowNum() == 0) continue;
            cellMap.entrySet().forEach(product -> {
                Product currentProduct = new Product();
                switch (product.getValue()) {
                    case ProductConstants.ID:
                        double id = Double.parseDouble(row.getCell(product.getKey()).toString());
                        currentProduct.setId((int) id);
                        break;
                    case ProductConstants.DESCRIPTION:
                        currentProduct.setDescription(row.getCell(product.getKey()).toString());
                        break;
                    case ProductConstants.TYPE:
                        currentProduct.setType(row.getCell(product.getKey()).toString());
                        break;
                    case ProductConstants.WEIGHT_TYPE:
                        currentProduct.setWeightType(row.getCell(product.getKey()).toString());
                        break;
                    case ProductConstants.WEIGHT_VALUE:
                        currentProduct.setWeightValue(Double.parseDouble(row.getCell(product.getKey()).toString()));
                        break;
                    case ProductConstants.BRAND_NAME:
                        currentProduct.setBrandName(row.getCell(product.getKey()).toString());
                        break;
                    case ProductConstants.BRAND_COUNTRY:
                        currentProduct.setBrandCountry(row.getCell(product.getKey()).toString());
                        break;
                    case ProductConstants.NAME:
                        currentProduct.setName(row.getCell(product.getKey()).toString());
                        break;
                    case ProductConstants.PRICE:
                        currentProduct.setPrice(Double.parseDouble(row.getCell(product.getKey()).toString()));
                        break;
                    case ProductConstants.EXPIRATION_DATE:
                        SimpleDateFormat format = new SimpleDateFormat(AppConstants.DATE_FORMAT_PATTERN, Locale.ROOT);
                        try {
                            String dateString = row.getCell(product.getKey()).toString();
                            currentProduct.setExpirationDate(format.parse(dateString));
                        } catch (ParseException e) {
                            System.out.println(INCORRECT_DATE_FORMAT);
                        }
                        break;
                }
                products.add(currentProduct);
            });
        }
        return products;
    }
}
