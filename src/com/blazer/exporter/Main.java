package com.blazer.exporter;

import org.apache.commons.cli.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class Main {

    public static final String SYNTAX = "java -jar antor.jar -l HR -p \"pass\" -u \"jdbc:oracle:thin:@//localhost:1521/XE\"  -lang en -reg EN -d \"c:\\antor\\out\"";

    public static void main(String args[]) {
        Options options = new Options();
        options.addOption("l", "login", true, "Database login: -l HR");
        options.getOption("l").setRequired(true);
        options.addOption("p", "password", true, "Database password: -p MYPASS");
        options.getOption("p").setRequired(true);
        options.addOption("u", "url", true, "Connection url: -u jdbc:oracle:thin:@//localhost:1521/hr");
        options.getOption("u").setRequired(true);
        options.addOption("d", "dir", true, "Output dir: -d \"c:\\antor\\out\"");
        options.getOption("d").setRequired(true);
        options.addOption("reg", "region", true, "Connection region (optional): -reg EN");
        options.addOption("lang", "language", true, "Connection language (optional): -lang en");
        options.addOption("data", true, "Export table data to CVS (optional): -data table1 -data \"table2 table2.col1 > 5\"");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Cannot parse params " + e.getMessage());
            new HelpFormatter().printHelp(SYNTAX, options);
            return;
        }

        if (cmd.getOptions().length == 0) {
            new HelpFormatter().printHelp(SYNTAX, options);
            return;
        }

        DataBase dataBase = new DataBase();
        dataBase.setLanguage(cmd.getOptionValue("lang"));
        dataBase.setRegion(cmd.getOptionValue("reg"));
        dataBase.setLogin(cmd.getOptionValue("l"));
        dataBase.setPassword(cmd.getOptionValue("p"));
        dataBase.setUrl(cmd.getOptionValue("u"));

        String dir = cmd.getOptionValue("d");

        try {
            dataBase.connect();

            List<AbstractEntity> entities = new LinkedList<AbstractEntity>(Arrays.asList(
                    new Index(dataBase, dir + "\\index"),
                    new Package(dataBase, dir + "\\package"),
                    new PackageBody(dataBase, dir + "\\package_body"),
                    new Procedure(dataBase, dir + "\\procedure"),
                    new Sequence(dataBase, dir + "\\sequence"),
                    new Synonym(dataBase, dir + "\\synonym"),
                    new Table(dataBase, dir + "\\table"),
                    new Trigger(dataBase, dir + "\\trigger"),
                    new Type(dataBase, dir + "\\type"),
                    new View(dataBase, dir + "\\view")
            ));

            if (cmd.hasOption("data")) {
                Collection<Data.TableName> tableNames = new LinkedList<Data.TableName>();
                for (String t : cmd.getOptionValues("data")) {
                    String table[] = t.split("\\s", 2);
                    if (table.length == 1) {
                        tableNames.add(new Data.TableName(table[0]));
                    } else {
                        tableNames.add(new Data.TableName(table[0], table[1]));
                    }
                }
                entities.add(new Data(dataBase, dir + "\\data", tableNames));
            }

            for (AbstractEntity entity : entities) {
                try {
                    entity.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }

    }
}
