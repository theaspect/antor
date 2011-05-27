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
 * ����������� ������� ��������� �� ����
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
	 * ���� ��� ��������
	 * @return
	 */	
	public void setPath(String path) {
		this.path = path;
	}

/**
 * ������ ���������� �� ��������� ������	
 */
	public void execute() {
		throw new UnsupportedOperationException("Must implement");
	}

/**
 * ������� DDL �� CLOB	
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
 * ������� �����
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
 * ������� CLOB � ����
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