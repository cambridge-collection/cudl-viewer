/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulcambridge.foundations.viewer.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.model.Properties;

/**
 *
 * @author rekha
 */
public class DatabaseCopy {

    private final String urlLive = Properties.getString("db.jdbc.url.live");
    private final String urlDev = Properties.getString("db.jdbc.url.dev");
    private final String userLive = Properties.getString("db.jdbc.user.live");
    private final String userDev = Properties.getString("db.jdbc.user.dev");
    private final String pwdLive = Properties.getString("db.jdbc.password.live");    
    private final String pwdDev = Properties.getString("db.jdbc.password.dev");
    private final String filepath = Properties.getString("db.filepath");
    private CollectionFactory collectionfactory;
    private static final Logger logger = Logger.getLogger(DatabaseCopy.class.getName());

    public DatabaseCopy(CollectionFactory collectionfactory) {
        this.collectionfactory = collectionfactory;
    }

    /*
     copy items,collections and itemsincollection tables from the dev database to a file in /tmp directory
     The files have the same names as the tables
     */
    
    private Boolean copyToFile(String tablename) {
        Boolean success;
        String url;

        url = urlDev;
        success = writeToFile(tablename, url);

        return success;

    }

    /*
     Called from copyToFile-dumps the database tables to a file
     */
   
    private Boolean writeToFile(String tablename, String url) {
        Connection con = null;
        FileWriter fileWriter = null;
        Boolean success = false;
        int TableRowCount = 0;
        try {
            //get the no of records in the respective database table
            if ("collections".equals(tablename)) {
                TableRowCount = this.collectionfactory.getCollectionsRowCount();

            } else if ("items".equals(tablename)) {
                TableRowCount = this.collectionfactory.getItemsRowCount();

            } else if ("itemsincollection".equals(tablename)) {
                TableRowCount = this.collectionfactory.getItemsInCollectionsRowCount();

            }
            
            con = DriverManager.getConnection(url, userDev, pwdDev);
            CopyManager copyManager = new CopyManager((BaseConnection) con);
            File file = new File(filepath + tablename);
            fileWriter = new FileWriter(file);
            long copyOutRows = copyManager.copyOut("COPY " + tablename + " TO STDOUT ", fileWriter);
            //the no of copied records == no of database table records?
            success = (copyOutRows > 0) && (copyOutRows == TableRowCount);
            if (success == false){
                logger.error("Exception from writeToFile method - failure of (copyOutRows > 0) && (copyOutRows == TableRowCount)");
            }
            fileWriter.flush();

        } catch (SQLException ex) {
            logger.error("Exception from writeToFile method - writing from dev db to file", ex);
            success = false;
        } catch (FileNotFoundException ex) {
            logger.error("Exception from writeToFile method - writing from dev db to file", ex);
            success = false;
        } catch (IOException ex) {
            logger.error("Exception from writeToFile method - writing from dev db to file", ex);
            success = false;
        } catch (NullPointerException ex) {
            logger.error("Exception from writeToFile method - writing from dev db to file", ex);
            success = false;
        } catch (Exception ex) {
            logger.error("Exception from writeToFile method - writing from dev db to file", ex);
            success = false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }

                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException ex) {
                    logger.error("Exception from writing from dev db to file - finally clause - filewriter close ", ex);
                    success = false;
                }
            } catch (SQLException ex) {
                logger.error("Exception from writing from dev db to file - finally clause - connection close ", ex);
                success = false;

            }

        }
        return success;
    }

    /*
     copy the contents of the file(output of the copyToFile function) into the live database
     */
   
    private Boolean copyIn(String tablename, Connection con) {

        Boolean success = true;
        try {

            CopyManager copyManager = new CopyManager((BaseConnection) con);
            File file = new File(filepath + tablename);
            FileReader fileReader = new FileReader(file);
            copyManager.copyIn("COPY " + tablename + " FROM STDIN", fileReader);

        } catch (SQLException ex) {
            logger.error("Exception from writing from file to live db -copy in method", ex);

            success = false;
        } catch (FileNotFoundException ex) {
            logger.error("Exception from writing from file to live db -copy in method", ex);

            success = false;
        } catch (IOException ex) {
            logger.error("Exception from writing from file to live db -copy in method", ex);
            success = false;
        } catch (Exception ex) {
            logger.error("Exception from writing from file to live db -copy in method", ex);

            success = false;
        }

        return success;

    }

    
    public Boolean copy() {
        ArrayList<String> tablename;
        Iterator<String> iterator;
        Connection conlive = null;

        tablename = new ArrayList<String>();

        tablename.add("items");
        tablename.add("collections");
        tablename.add("itemsincollection");

        iterator = tablename.iterator();
        Boolean dbsuccess = false;
        Boolean copysuccess = false;
        Boolean copyfilesuccess = false;
        try {

            //connect to live database
            conlive = DriverManager.getConnection(urlLive, userLive, pwdLive);
            //make sure the commit is disabled-for rollback
            conlive.setAutoCommit(false);
            String table;

            //truncate the live tables before copying over the data
            String sql = "TRUNCATE TABLE items,collections,itemsincollection CASCADE";
            PreparedStatement prepareStatement = conlive.prepareStatement(sql);
            int rowsTruncated = prepareStatement.executeUpdate();
            
            //iterate over the 3 tables to copy them out from the dev database into a file and then copy them into the live database
            while (iterator.hasNext()) {
                //get tablename
                table = iterator.next();

                //write table data from dev db to file
                copyfilesuccess = copyToFile(table);

                if (copyfilesuccess) {//if copy to file has been successful
                    //copy data from file into live database table
                    dbsuccess = copyIn(table, conlive);
                    if (!dbsuccess) {
                        conlive.rollback();//rollback if any issues
                        logger.error("Exception from init method-copyIn failure-rollback done");
                        copysuccess = false;
                        break;//also stop the copy of remaining tables

                    }
                } else {//copy to file not successful rollback
                    conlive.rollback();//rollback if any issues
                    logger.error("Exception from init method-copyOut failure-rollback done");
                    copysuccess = false;
                    break;//also stop the copy of remaining tables
                }
            }
            if (!iterator.hasNext() && dbsuccess) {
                timestampProcess(conlive);
                conlive.commit();
                copysuccess = true;
            }

        } catch (SQLException ex) {
            logger.error("Exception from init method", ex);
            try {
                if (conlive != null) {
                    conlive.rollback();//rollback if any issues
                }
            } catch (SQLException ex1) {
                logger.error("Exception from init method - conlive.rollback", ex);
                copysuccess = false;
            }
            copysuccess = false;
        } catch (Exception ex) {
            logger.error("Exception from init method", ex);
            try {
                if (conlive != null) {
                    conlive.rollback();//rollback if any issues
                }
            } catch (SQLException ex1) {
                logger.error("Exception from init method - conlive.rollback", ex);
                copysuccess = false;
            }
            copysuccess = false;
        } finally {
            try {
                if (conlive != null) {
                    conlive.setAutoCommit(true);
                    conlive.close();
                }

            } catch (SQLException ex) {
                logger.error("Exception from init method - finally clause", ex);
            }
        }

        return copysuccess;

    }
    
    private void timestampProcess(Connection conlive){
        try {
            java.util.Date date= new java.util.Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            String sql = "INSERT INTO timestamp values (?)";
            System.out.println(sql);
            PreparedStatement prepareStatement = conlive.prepareStatement(sql);
            prepareStatement.setTimestamp(1, timestamp);
            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("timestamp process failed");
        }
    }

}
