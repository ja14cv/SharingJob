package com.example.jcaal.sharingjob_v01;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class principal extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private int seleccionActual = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if(seleccionActual != position){

            //Dependiendo de la seleccion en el menu izquierdo se abre un fragment
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(seleccionActual != -1){ //No activa la pila con el fragent de navigaction drawer
                transaction.addToBackStack(null);
            }

            if(position == 0) {
                transaction.replace(R.id.container, inicio.newInstance(new inicio(), R.layout.fragment_inicio, position, new String[0])).commit();
            }
            if(position == 1){
                transaction.replace(R.id.container, inicio.newInstance(new mi_cuenta(), R.layout.fragment_mi_cuenta, position, new String[0])).commit();
            }
            if(position == 2){
                transaction.replace(R.id.container, inicio.newInstance(new nuevo_empleo(), R.layout.fragment_nuevo_empleo, position, new String[0])).commit();
            }
            if(position == 3){
                transaction.replace(R.id.container, inicio.newInstance(new configuracion(), R.layout.fragment_configuracion, position, new String[0])).commit();
            }

            seleccionActual =  position;
        }
    }

    @Override
    public void seleccion(int _seleccion) {
        Log.v("LOG", "clic " + _seleccion);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section0);
                break;
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
            restoreActionBar();
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
            this.onSectionAttached(lastFragment.seleccion);
            seleccionActual = lastFragment.seleccion;
            restoreActionBar();
        }
        super.onBackPressed();
    }
}
