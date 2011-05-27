package com.blazer.antor;
import java.util.Locale;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.JDBCTask;

public class DataBase extends JDBCTask{
	String ref;
	String region="US";
	String language="en";

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public void setRegion(String region) {
		this.region=region;
	}

	public void setLanguage(String language) {
		this.language=language;
	}

	@Override
	public void execute() throws BuildException {
		if(language!=null && region !=null){
			Locale.setDefault(new Locale(language, region));
		}
		super.execute();
		getProject().addReference(ref, super.getConnection());
	}
}
