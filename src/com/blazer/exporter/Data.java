package com.blazer.exporter;

import java.io.File;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collection;
import java.util.LinkedList;


/**
 * Экспорт данных
 * вложенный тег <name>
 *
 * @author Constantine
 */
public class Data extends AbstractEntity {
    String query = "select * from ";
    Collection<TableName> tables = new LinkedList<TableName>();

    public Data(DataBase dataBase, String path, Collection<TableName> tables) {
        this.dataBase = dataBase;
        this.dir = path;
        this.tables = tables;
    }

    public static class TableName {
        String text;
        String condition = null;

        public TableName(String text) {
            this.text = text;
        }

        public TableName(String text, String condition) {
            this.text = text;
            this.condition = condition;
        }

        public String getCondition() {
            return condition;
        }

        public String getText() {
            return text;
        }
    }

    @Override
    public void execute() throws Exception {
        prepareFolder();
        for (TableName tbl : tables) {
            try {
                String sql = query + tbl.getText();
                if (tbl.getCondition() != null) {
                    sql += " where " + tbl.getCondition();
                }
                PreparedStatement pstmt = dataBase.getConnection().prepareStatement(sql);
                exportTable(tbl, pstmt.executeQuery());
                pstmt.close();
            } catch (Exception e) {
                System.out.println(tbl.getText());
                e.printStackTrace();
            }
        }
    }

    /**
     * Экспортируем содержимое таблицы в CSV файл
     *
     * @param tbl
     * @param rs
     * @throws Exception
     */
    void exportTable(TableName tbl, ResultSet rs) throws Exception {
        ResultSetMetaData rsmd = rs.getMetaData();
        PrintWriter fw = new PrintWriter(new File(dir + "\\" + tbl.getText() + ".csv"), "CP1251");
        while (rs.next()) {
            StringBuffer sb = new StringBuffer();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                sb.append("; ").append(rs.getString(i) == null ? "" : rs.getString(i));
            }
            sb.append("\n");
            fw.write(sb.substring(2));
        }
        fw.close();
    }
}
