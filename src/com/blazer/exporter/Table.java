package com.blazer.exporter;


/**
 * Экспорт таблиц
 *
 * @author Constantine
 */
public class Table extends AbstractEntity {
    String query =
            "select uo.object_name, dbms_metadata.get_ddl(uo.object_type, uo.object_name) as ddl " +
                    "from user_objects uo where " +
                    "uo.generated = 'N' and " + //ORA-31603
                    "uo.object_name not in " +
                    "(select table_name from user_all_tables where nested='YES') " +
                    "and uo.object_type = 'TABLE'";

    public Table(DataBase dataBase, String dir) {
        this.dataBase = dataBase;
        this.dir = dir;
    }

    @Override
    public void execute() throws Exception {
        prepareFolder();
        exportObjects(query);
    }
}
