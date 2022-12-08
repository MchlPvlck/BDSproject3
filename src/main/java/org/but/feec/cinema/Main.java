package org.but.feec.cinema;

import org.but.feec.cinema.config.DataSourceConfig;

public class Main {
    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            DataSourceConfig.initializeDataSource(args[0]);
        } else {
            DataSourceConfig.initializeDataSource(null);
        }
        App.main(args);
    }
}