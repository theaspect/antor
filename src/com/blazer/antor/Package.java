package com.blazer.antor;


/**
 * Ёкспорт пакетов
 * @author Constantine
 *
 */
public class Package extends AbstractEntity{
	String query = 
		"select uo.object_name, dbms_metadata.get_ddl(uo.object_type, uo.object_name) as ddl "+
		"from user_objects uo where uo.object_type = 'PACKAGE'";

	@Override
	public void execute() {
		prepareFolder();
		exportObjects(query);
	}	
}
