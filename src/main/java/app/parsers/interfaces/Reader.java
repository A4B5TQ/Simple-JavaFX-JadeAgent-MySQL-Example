package app.parsers.interfaces;

import app.product.Product;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Reader {

    List<Product> importExcelFile(File chooseFile, Map<Integer, String> cellMap) throws IOException;
}
