/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyectopr;

import java.util.*;

/**
 *
 * @author Vespertino
 */
public class ProyectoPr {

    public static void main(String[] args) throws InterruptedException{
        Scanner t = new Scanner(System.in);
        int num = 0;
        System.out.println("Escriba un numero");
        num = t.nextInt();
        System.out.println("Leyendo el numero que has pensado");
        for (int i = 10 ; i > 0; i--){
             Thread.sleep(1000);
            System.out.println("en " +i);
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        System.out.println("El numero que has pensado es el " + num);

    }
}
