package app.parsers.interfaces;

import app.enums.ReportType;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public interface Writer {

    void exportXLSX(File file, ReportType reportType) throws IOException, SQLException;
}
