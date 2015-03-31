package com.example.jcaal.sharingjob_v01.logica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jesus on 30/03/2015.
 */
public class sesion {

    public static String id_sesion = "";

    public void crearSesion(String path) throws IOException {
        //Obteniendo sesion
        id_sesion = getSesion(getFile(path + "/conf.txt"));
    }

    /***
     * Obtiene o crea el archivo de configuaraciones de la app
     * @param name nombre del archivo
     * @return archivo
     */
    private File getFile(String name){
        File f = new File(name);
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return f;
    }

    /***
     * crea las configuraciones en el archivo
     * @param f archivo de configuraciones
     * @throws IOException
     */
    private void crearConfiguraciones(File f) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
        bw.write("sesion:0");
        bw.close();
    }

    /***
     * obtiene la sesion del archivo
     * @param f archivo de configuraciones
     * @return id sesion
     * @throws IOException
     */
    private String getSesion(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        String linea = br.readLine();
        String id;
        if(linea != null && linea.contains("sesion")){
            id = linea.substring(7);
        }else{
            //No se encuentra ninguna linea
            crearConfiguraciones(f);
            id = "0";
        }

        br.close();
        return id;
    }

}
