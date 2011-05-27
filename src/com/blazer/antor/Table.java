package com.blazer.antor;


/**
 * Ёкспорт таблиц
 * @author Constantine
 *
 */
public class Table extends AbstractEntity{
	String query = 
		"select uo.object_name, dbms_metadata.get_ddl(uo.object_type, uo.object_name) as ddl "+
		"from user_objects uo where "+
		"uo.generated = 'N' and "+ //ORA-31603
		"uo.object_name not in "+
		"(select table_name from user_all_tables where nested='YES') "+ 
		"and uo.object_type = 'TABLE'";

	@Override
	public void execute() {
		prepareFolder();
		exportObjects(query);
	}
}
