package app.parsers;

import app.enums.ReportType;
import app.parsers.interfaces.Writer;
import app.product.Product;
import app.product.ProductManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

public class ExcelWriter implements Writer {

    private final static String MARGIN = "Margin: ";
    private final static String SUM_FORMULA = "sum(L1:L";
    private final static String CLOSE_BRACKET = ")";
    private final static String PRICE_FORMAT = "#0.0###";

    private final static String[] FINANCIAL_HEADERS = new String[]{
            "Id",
            "Description",
            "Type",
            "Weight Type",
            "Weight Value",
            "Brand Name",
            "Brand Country",
            "Name",
            "Price",
            "Expiration Date",
            "Changed On",
            "Price Difference"

    };

    private final static String[] STOCK_HEADERS = new String[]{
            "Id",
            "Description",
            "Type",
            "Weight Type",
            "Weight Value",
            "Brand Name",
            "Brand Country",
            "Name",
            "Price",
            "Expiration Date"
    };

    public void exportXLSX(File file, ReportType reportType) throws IOException, SQLException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        ProductManager manager = new ProductManager();
        CellStyle floatingPointStyle = workbook.createCellStyle();
        floatingPointStyle.setDataFormat(
                workbook.getCreationHelper().createDataFormat().getFormat(PRICE_FORMAT));
        switch (reportType) {
            case STOCK:
                List<Product> productData = manager.findAll();
                for (int row = 0; row <= productData.size(); row++) {
                    Row excelRow = sheet.createRow(row);
                    if (row == 0) {
                        this.writeHeaders(excelRow, STOCK_HEADERS, sheet);
                        continue;
                    }
                    for (int column = 0; column < productData.get(row - 1).exportData().size(); column++) {
                        Cell cell = excelRow.createCell(column);
                        sheet.autoSizeColumn(column);
                        if (column == 4 || column == 8) {
                            cell.setCellValue(Double.valueOf(productData.get(row - 1).exportData().get(column)));
                            cell.setCellStyle(floatingPointStyle);
                        } else {
                            cell.setCellValue(productData.get(row - 1).exportData().get(column));
                        }
                    }
                }
                break;
            case FINANCIAL:
                List<String> financialData = manager.getHistory();
                for (int row = 0; row <= financialData.size(); row++) {
                    Row excelRow = sheet.createRow(row);
                    if (row == 0) {
                        this.writeHeaders(excelRow, FINANCIAL_HEADERS, sheet);
                        continue;
                    }
                    String[] data = financialData.get(row - 1).split("\\|");
                    for (int column = 0; column < data.length; column++) {
                        Cell cell = excelRow.createCell(column);
                        sheet.autoSizeColumn(column);
                        if (column == 4 || column == 8 || column == 11) {
                            cell.setCellValue(Double.valueOf(data[column]));
                            cell.setCellStyle(floatingPointStyle);
                        } else {
                            cell.setCellValue(String.valueOf(data[column]));
                        }
                    }
                }
                int lastRow = financialData.size() + 1;
                int cellIndex = 11;
                Row formulaRow = sheet.createRow(lastRow);
                Cell marginCell = formulaRow.createCell(cellIndex - 1);
                marginCell.setCellValue(MARGIN);
                Cell sumCell = formulaRow.createCell(cellIndex);
                sumCell.setCellFormula(SUM_FORMULA + lastRow + CLOSE_BRACKET);
                sumCell.setCellStyle(floatingPointStyle);
                break;
        }

        file.createNewFile();
        OutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    private void writeHeaders(Row excelRow, String[] headers, Sheet sheet) {
        for (int column = 0; column < headers.length; column++) {
            Cell cell = excelRow.createCell(column);
            cell.setCellValue(headers[column]);
            sheet.autoSizeColumn(column);
        }
    }
}
