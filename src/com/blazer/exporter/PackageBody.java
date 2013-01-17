package com.blazer.exporter;


/**
 * Экспорт тел пакетов
 *
 * @author Constantine
 */
public class PackageBody extends AbstractEntity {
    String query =
            "select uo.object_name, dbms_metadata.get_ddl('PACKAGE_BODY', uo.object_name) as ddl " +
                    "from user_objects uo where uo.object_type = 'PACKAGE BODY'";

    public PackageBody(DataBase dataBase, String dir) {
        this.dataBase = dataBase;
        this.dir = dir;
    }

    @Override
    public void execute() throws Exception {
        prepareFolder();
        exportObjects(query);
    }
}
