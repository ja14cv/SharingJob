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
    private static String path;

    public void crearSesion(String _path) throws IOException {
        //Obteniendo sesion
        path = _path + "/conf.txt";
        id_sesion = getSesionFromFile(getFile(path));
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
    private String getSesionFromFile(File f) throws IOException {
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

    /***
     * Setea el id de sesion en el archivo y ejecucion
     * @param id id se sesion
     * @throws IOException
     */
    public static void setSesion(String id) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write("sesion:" + id);
        bw.close();

        id_sesion = id;
    }

    /***
     * Verifica si el id de sesion es valido
     * @return
     */
    public static boolean isLogin(){
        return !id_sesion.equals("0");
    }

    /***
     * Cierra la sesion actual
     */
    public static void logout() throws IOException {
        setSesion("0");
    }

}
