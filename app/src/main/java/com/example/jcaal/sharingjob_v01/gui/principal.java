package com.example.jcaal.sharingjob_v01.gui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.logica.sesion;

import java.io.IOException;
import java.util.List;


public class principal extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private TipoFragmento fragmentoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        //Creando Session
        sesion s = new sesion();
        try {
            s.crearSesion(getFilesDir().getPath());
            Log.i("Login", sesion.id_sesion);
            Toast.makeText(this, "Sesion " + sesion.id_sesion, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.i("Login", "Fallido " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(TipoFragmento tf) {
        if(fragmentoActual != tf){

            //Dependiendo de la seleccion en el menu izquierdo se abre un fragment
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(fragmentoActual != null){ //No activa la pila con el fragent de navigaction drawer
                mNavigationDrawerFragment.seleccionarItem(tf);
                transaction.addToBackStack(null);
            }

            if(tf == TipoFragmento.INICIO && fragmentoActual != null){
                transaction.replace(R.id.container, inicio.newInstance(new inicio(), R.layout.fragment_inicio, tf, new String[0])).commit();
            }else if(tf == TipoFragmento.CUENTA){
                transaction.replace(R.id.container, cuenta.newInstance(new cuenta(), R.layout.fragment_cuenta, tf, new String[0])).commit();
            }else if(tf == TipoFragmento.NUEVO_EMPLEO){
                transaction.replace(R.id.container, nuevo_empleo.newInstance(new nuevo_empleo(), R.layout.fragment_nuevo_empleo, tf, new String[0])).commit();
            }else if(tf == TipoFragmento.CONFIG){
                transaction.replace(R.id.container, configuracion.newInstance(new configuracion(), R.layout.fragment_configuracion, tf, new String[0])).commit();
            }else if(tf == TipoFragmento.LOGIN){
                mNavigationDrawerFragment.desmarcarItem();
                transaction.replace(R.id.container, login.newInstance(new login(), R.layout.fragment_login, tf, new String[0])).commit();
            }else if(tf == TipoFragmento.REGISTRO){
                mNavigationDrawerFragment.desmarcarItem();
                transaction.replace(R.id.container, registro.newInstance(new registro(), R.layout.fragment_registro, tf, new String[0])).commit();
            }else{
                transaction.replace(R.id.container, inicio.newInstance(new inicio(), R.layout.fragment_inicio, tf, new String[0])).commit();
            }

            fragmentoActual =  tf;
        }
    }

    @Override
    public void seleccion(TipoFragmento tf) {
        Log.v("LOG", "clic " + tf);
    }

    public void onSectionAttached(TipoFragmento tf) {
        if(tf == TipoFragmento.INICIO){
            mTitle = getString(R.string.app_name);
        }else if(tf == TipoFragmento.CUENTA){
            mTitle = getString(R.string.title_section1);
        }else if(tf == TipoFragmento.NUEVO_EMPLEO){
            mTitle = getString(R.string.title_section2);
        }else if(tf == TipoFragmento.CONFIG){
            mTitle = getString(R.string.title_section3);
        }else if(tf == TipoFragmento.LOGIN){
            mTitle = "Login";
        }else if(tf == TipoFragmento.REGISTRO){
            mTitle = "Registro";
        }else{
            mTitle = getString(R.string.app_name);
        }

        //restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.principal, menu);
            //restoreActionBar();
            return true;

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        int no_frag = getSupportFragmentManager().getBackStackEntryCount();
        if(no_frag != 0){
            List<android.support.v4.app.Fragment> listFragments= getSupportFragmentManager().getFragments();
            FragmentGenerico lastFragment = (FragmentGenerico)listFragments.get(no_frag);
            this.onSectionAttached(lastFragment.tipoFragmento);
            fragmentoActual = lastFragment.tipoFragmento;
            restoreActionBar();
        }
        super.onBackPressed();
    }
}
