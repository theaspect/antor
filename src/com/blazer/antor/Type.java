package com.blazer.antor;


/**
 * Ёкспорт типов
 * @author Constantine
 *
 */
public class Type extends AbstractEntity{
	String query = 
		"select uo.object_name, dbms_metadata.get_ddl(uo.object_type, uo.object_name) as ddl "+
		"from user_objects uo where uo.object_type = 'TYPE'";

	@Override
	public void execute() {
		prepareFolder();
		exportObjects(query);
	}	
}
