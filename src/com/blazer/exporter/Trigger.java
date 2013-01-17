package com.blazer.exporter;


/**
 * Экспорт триггеров
 *
 * @author Constantine
 */
public class Trigger extends AbstractEntity {
    String query =
            "select uo.object_name, dbms_metadata.get_ddl(uo.object_type, uo.object_name) as ddl " +
                    "from user_objects uo where uo.object_type = 'TRIGGER'";

    public Trigger(DataBase dataBase, String dir) {
        this.dataBase = dataBase;
        this.dir = dir;
    }

    @Override
    public void execute() throws Exception {
        prepareFolder();
        exportObjects(query);
    }
}
