package com.blazer.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Абстрактный экспорт сущностей из базы
 *
 * @author Constantine
 */
public abstract class AbstractEntity {
    String dir;
    DataBase dataBase;

    public abstract void execute() throws Exception;

    /**
     * Экспорт DDL из CLOB
     *
     * @param query
     */
    void exportObjects(String query) {
        System.out.println("Exporting to " + dir);
        try {
            PreparedStatement pstmt = dataBase.getConnection().prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                saveClob(rs.getString("object_name"), rs.getClob("ddl"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Очистка папки
     */
    void prepareFolder() throws Exception {
        if (null == dir) {
            throw new Exception("Path is null");
        }
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        } else if (!f.isDirectory()) {
            throw new Exception("Path is not dir");
        } else {
            for (File del : f.listFiles()) {
                if (del.isFile()) {
                    del.delete();
                }
            }
        }
    }

    /**
     * Экспорт CLOB в файл
     *
     * @param filename
     * @param clob
     * @throws IOException
     * @throws SQLException
     */
    void saveClob(String filename, Clob clob) throws IOException, SQLException {
        FileWriter fw = new FileWriter(new File(dir + "\\" + filename + ".sql"));
        fw.write(clob.getSubString(1, (int) clob.length()).trim());
        fw.close();
    }

}
