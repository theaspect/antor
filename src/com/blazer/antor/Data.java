package com.blazer.antor;

import java.io.File;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import org.apache.tools.ant.Task;


/**
 * Ёкспорт данных
 * вложенный тег <name>
 * @author Constantine
 *
 */
public class Data extends AbstractEntity{
	String query = "select * from ";
	ArrayList<TableName> tables = new ArrayList<TableName>();
	
	public static class TableName extends Task{
		String text;
		String condition;
		
		public String getCondition() {
			return condition;
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public String getText(){
			return text;
		}
		public void addText(String text){
			this.text = text;
		}
	}
	
	public void addConfiguredName(TableName table){
		tables.add(table);
	}
	
	@Override
	public void execute() {
		prepareFolder();
		for(TableName tbl : tables){
			try {
				getProject().log(tbl.getText());
				String sql = query + tbl.getText();
				if(tbl.getCondition() != null){
					sql += " where "+tbl.getCondition();
				}
				//System.out.println(sql);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				exportTable(tbl, pstmt.executeQuery());
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

/**
 * Ёкспортируем содержимое таблицы в CSV файл	
 * @param tbl
 * @param rs
 * @throws Exception
 */
	void exportTable(TableName tbl, ResultSet rs) throws Exception{
		ResultSetMetaData rsmd = rs.getMetaData();
		PrintWriter fw = new PrintWriter(new File(path+"\\"+tbl.getText()+".csv"),"CP1251");
		while(rs.next()){
			StringBuffer sb = new StringBuffer();
			for(int i=1;i<=rsmd.getColumnCount();i++){
				sb.append("; ").append(rs.getString(i)==null?"":rs.getString(i));
			}
			sb.append("\n");
			fw.write(sb.substring(2));
		} 
		fw.close();
	}
}
