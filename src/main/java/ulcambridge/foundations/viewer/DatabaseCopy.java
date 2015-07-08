/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulcambridge.foundations.viewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import ulcambridge.foundations.viewer.model.Properties;

/**
 *
 * @author rekha
 */
public class DatabaseCopy {

    private final String urlLive = Properties.getString("db.jdbc.url.live");
    private final String urlDev = Properties.getString("db.jdbc.url.dev");
    private final String userLive = Properties.getString("db.jdbc.user.live");
    private final String pwdLive = Properties.getString("db.jdbc.password.live");
    private final String userDev = Properties.getString("db.jdbc.user.dev");
    private final String pwdDev = Properties.getString("db.jdbc.password.dev");
    private CollectionFactory collectionfactory;

    public DatabaseCopy(CollectionFactory collectionfactory) {
        this.collectionfactory = collectionfactory;
    }

    /*
     copy items,collections and itemsincollection tables from the dev database to a file in /tmp directory
     The files have the same names as the tables
     */
    public Boolean copyToFile(String tablename, String db) {
        Boolean success = false;
        String url;
        if (null != db) {
            url = urlDev;
            success = writeToFile(tablename, url, "devTables");
        }
        return success;

    }

    /*
     Called from copyToFile-dumps the database tables to a file
     */
    public Boolean writeToFile(String tablename, String url, String folder) {
        Connection con = null;
        FileWriter fileWriter = null;
        Boolean success = false;
        int TableRowCount = 0;
        try {
            if ("collections".equals(tablename)) {
                TableRowCount = this.collectionfactory.getCollectionsRowCount();

            } else if ("items".equals(tablename)) {
                TableRowCount = this.collectionfactory.getItemsRowCount();

            } else if ("itemsincollection".equals(tablename)) {
                TableRowCount = this.collectionfactory.getItemsInCollectionsRowCount();

            }

            con = DriverManager.getConnection(url, userDev, pwdDev);
            CopyManager copyManager = new CopyManager((BaseConnection) con);
            File file = new File("/tmp/" + folder + "/" + tablename);
            fileWriter = new FileWriter(file);
            long copyOutRows = copyManager.copyOut("COPY " + tablename + " TO STDOUT ", fileWriter);

            success = (copyOutRows > 0) && (copyOutRows == TableRowCount);
            fileWriter.flush();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (IOException ex) {
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (NullPointerException ex) {
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (Exception ex) {
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
                    success = false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
                success = false;

            }

        }
        return success;
    }

    /*
     copy the contents of the file(output of the copyToFile function) into the live database
     */
    public Boolean copyIn(String tablename, Connection con) {

        Boolean success = true;
        try {

            CopyManager copyManager = new CopyManager((BaseConnection) con);
            File file = new File("/tmp/devTables/" + tablename);
            FileReader fileReader = new FileReader(file);
            copyManager.copyIn("COPY " + tablename + " FROM STDIN", fileReader);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);

            success = false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);

            success = false;
        } catch (IOException ex) {
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);

            success = false;
        }

        return success;

    }

    public Boolean init() {
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
            conlive.setAutoCommit(false);
            String table;        

            
            //truncate the live tables before copying over the data
            String sql = "TRUNCATE TABLE items,collections,itemsincollection CASCADE";
            PreparedStatement prepareStatement = conlive.prepareStatement(sql);
            int rowsTruncated = prepareStatement.executeUpdate();
           
            

            //if (rowsTruncated != 0) {
            //iterate over the 3 tables to copy them out from the dev database into a file and then copy them into the live database
            while (iterator.hasNext()) {
                //get tablename
                table = iterator.next();

                //write table data to file
                copyfilesuccess = copyToFile(table, "dev");

                if (copyfilesuccess) {//if copy to file has been successful
                    //copy data from file into live database table
                    dbsuccess = copyIn(table, conlive);
                    if (!dbsuccess) {
                        conlive.rollback();//rollback if any issues
                        copysuccess = false;
                        break;//also stop the copy incase of any issues

                    }
                } else {
                    conlive.rollback();//rollback if any issues
                    copysuccess = false;
                    break;//also stop the copy incase of any issues
                }
            }
            if (!iterator.hasNext() && dbsuccess) {
                conlive.commit();
                copysuccess = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex){
            Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                if (conlive != null) {
                    conlive.setAutoCommit(true);
                    conlive.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseCopy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return copysuccess;

    }

}
