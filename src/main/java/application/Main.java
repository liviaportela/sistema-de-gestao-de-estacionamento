package application;

import db.DB;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection conn = null;
        conn = DB.getConnection();

    }
}