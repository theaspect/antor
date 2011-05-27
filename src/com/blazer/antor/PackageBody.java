package com.blazer.antor;


/**
 * Ёкспорт тел пакетов
 * @author Constantine
 *
 */
public class PackageBody extends AbstractEntity{
	String query = 
		"select uo.object_name, dbms_metadata.get_ddl('PACKAGE_BODY', uo.object_name) as ddl "+
		"from user_objects uo where uo.object_type = 'PACKAGE BODY'";

	@Override
	public void execute() {
		prepareFolder();
		exportObjects(query);
	}	
}
