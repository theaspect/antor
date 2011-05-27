package com.blazer.antor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
/**
 * јбстрактный экспорт сущностей из базы
 * @author Constantine
 */
public abstract class AbstractEntity extends Task {
	String path;
	Connection conn;

	public void setConn(String ref) {
		this.conn = (Connection) getProject().getReference(ref);
	}

	public String getPath() {
		return path;
	}

	/**
	 * ѕуть дл€ экспорта
	 * @return
	 */	
	public void setPath(String path) {
		this.path = path;
	}

/**
 * ƒолжен вызыватьс€ из дочернего класса	
 */
	public void execute() {
		throw new UnsupportedOperationException("Must implement");
	}

/**
 * Ёкспорт DDL из CLOB	
 * @param query
 */
	void exportObjects(String query) {
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				saveClob(rs.getString("object_name"), rs.getClob("ddl"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/**
 * ќчистка папки
 */
	void prepareFolder() {
		if(null == path){
			throw new BuildException("Path is null");
		}
		File f = new File(path);
		getProject().log(f.getAbsolutePath());
		if(false == f.exists()){
			f.mkdir();
		}else if (false == f.isDirectory()){			
			throw new BuildException("Path is not dir");
		}else{
			for(File del : f.listFiles()){
				if(del.isFile()){
					del.delete();
				}
			}
		}
	}
/**
 * Ёкспорт CLOB в файл
 * @param filename
 * @param clob
 * @throws IOException
 * @throws SQLException
 */
	void saveClob(String filename, Clob clob) throws IOException, SQLException {
		FileWriter fw = new FileWriter(new File(path+"\\"+filename+".sql"));
		fw.write(clob.getSubString(1, (int) clob.length()).trim());
		fw.close();
	}

}