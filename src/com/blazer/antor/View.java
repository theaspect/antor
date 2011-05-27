package com.blazer.antor;


/**
 * Ёкспорт представлений
 * @author Constantine
 *
 */
public class View extends AbstractEntity{
	String query = 
		"select uo.object_name, dbms_metadata.get_ddl(uo.object_type, uo.object_name) as ddl "+
		"from user_objects uo where uo.object_type = 'VIEW'";

	@Override
	public void execute() {
		prepareFolder();
		exportObjects(query);
	}	
}
