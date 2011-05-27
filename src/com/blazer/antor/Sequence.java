package com.blazer.antor;


/**
 * Экспорт последовательсностей
 * @author Constantine
 *
 */
public class Sequence extends AbstractEntity{
	String query = 
		"select uo.object_name, dbms_metadata.get_ddl(uo.object_type, uo.object_name) as ddl "+
		"from user_objects uo where uo.object_type = 'SEQUENCE'";

	@Override
	public void execute() {
		prepareFolder();
		exportObjects(query);
	}	
}
