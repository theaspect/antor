package com.blazer.exporter;


/**
 * Экспорт процедур
 *
 * @author Constantine
 */
public class Procedure extends AbstractEntity {
    String query =
            "select uo.object_name, dbms_metadata.get_ddl(uo.object_type, uo.object_name) as ddl " +
                    "from user_objects uo where uo.object_type = 'PROCEDURE'";

    public Procedure(DataBase dataBase, String dir) {
        this.dataBase = dataBase;
        this.dir = dir;
    }

    @Override
    public void execute() throws Exception {
        prepareFolder();
        exportObjects(query);
    }
}
