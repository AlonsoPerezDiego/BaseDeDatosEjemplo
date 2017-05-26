/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemploconectarsqlite;

import com.sun.istack.internal.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Diego
 */
public class Conectar {

    Connection conexion;
    Statement stmt;

    public Conectar(String url) {
        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection("jdbc:sqlite:" + url);
            if (conexion != null) {
                System.out.println("Conectado");
            }
        } catch (Exception ex) {
            System.out.println("No se pudo conectar: \n" + ex);
        }
    }

    public void desconecta() {
        try {
            conexion.close();
            System.out.println("Desconectado");
            System.exit(0);
        } catch (SQLException ex) {
            Logger.getLogger(Conectar.class).log(Level.SEVERE, null, ex);
        }
    }

    public void crearTabla() {
        try {
            stmt = conexion.createStatement();
            
            String sql = "CREATE TABLE CLASE "
                    + " (DNI CHAR(9) PRIMARY KEY  NOT NULL,"
                    + " NOMBRE           TEXT   NOT NULL, "
                    + " EDAD             INT    NOT NULL, "
                    + " DIRECCION        CHAR(50), "
                    + " NOTAMEDIA        REAL)";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Table created successfully");
        } catch (Exception e) {
            System.out.println("Deleting table");
            String sql = "DROP TABLE CLASE;";
            try {
                stmt.executeUpdate(sql);
                crearTabla();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(Conectar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Table created successfully");
    }

    public void crearFila(String dni, String nombre, int edad, String direccion, int nota) {
        try {
            stmt = conexion.createStatement();
            String sql = "INSERT INTO CLASE (DNI,NOMBRE,EDAD,DIRECCION,NOTAMEDIA) "
                    + "VALUES ( '" + dni + "', '" + nombre + "', " + edad + ", '" + direccion + "', " + nota + ");";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public void select(JTable tabla) {
        Statement stmt = null;
        int a=0, b=0;
        try {
            stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CLASE;");
            while (rs.next()) {
                tabla.setValueAt(rs.getString("DNI"), a, b);
                b++;
                tabla.setValueAt(rs.getString("Nombre"), a, b);
                b++;
                tabla.setValueAt(rs.getInt("Edad"), a, b);
                b++;
                tabla.setValueAt(rs.getString("Direccion"), a, b);
                b++;
                tabla.setValueAt(rs.getFloat("Notamedia"), a, b);
                b = 0;
                a++;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    public void update(int campo, String valor, int clave, String valorC) {
        Statement stmt = null;
        try {
            stmt = conexion.createStatement();
            String sql = "UPDATE CLASE set " + actualizacion(campo, valor)  + condicion(clave, valorC) + ";";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    private String actualizacion(int campo, String valor){
        String seleccion;
        switch(campo){
            case 1: seleccion="DNI= '" + valor + "'";
                    break;
            case 2: seleccion="NOMBRE= '" + valor + "'";
                    break;
            case 3: seleccion="EDAD= " + Integer.parseInt(valor);
                    break;
            case 4: seleccion="DIRECCION= '" + valor + "'";
                    break;
            case 5: seleccion="NOTAMEDIA= " + Float.parseFloat(valor);
                    break;
            default:seleccion="";
                    break;
        }
        return seleccion;
    }
    
    private String condicion(int campo, String valor){
        String seleccion;
        switch(campo){
            case 1: seleccion=" WHERE DNI= '" + valor + "'";
                    break;
            case 2: seleccion=" WHERE NOMBRE= '" + valor + "'";
                    break;
            case 3: seleccion=" WHERE EDAD= " + Integer.parseInt(valor);
                    break;
            case 4: seleccion=" WHERE DIRECCION= '" + valor + "'";
                    break;
            case 5: seleccion=" WHERE NOTAMEDIA= " + Float.parseFloat(valor);
                    break;
            default:seleccion="";
                    break;
        }
        return seleccion;
    }
    
    public void delete(int campo, String valor){
        Statement stmt = null;
    try {
      stmt = conexion.createStatement();
      String sql = "DELETE from CLASE " + condicion(campo, valor) + ";";
      stmt.executeUpdate(sql);
//      conexion.commit();
      stmt.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Operation done successfully");
    }
    
    public String inString(String s){
        return JOptionPane.showInputDialog(s);
    }
    
    public int inInt(String s){
        try{
            return Integer.parseInt(JOptionPane.showInputDialog(s));
        }catch(Exception ex){
            return 0;
        }
    }
}
