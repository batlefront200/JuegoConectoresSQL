/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

import implementacion.SQLiteImpl;
import java.util.*;

/**
 *
 * @author Vespertino
 */
public class pruebaSQLite {
    public static void main(String[] args) {
        SQLiteImpl prueba = new SQLiteImpl();
        
        String [] pruebas = prueba.getConfig();
        System.out.println(Arrays.toString(pruebas));
    }
}
