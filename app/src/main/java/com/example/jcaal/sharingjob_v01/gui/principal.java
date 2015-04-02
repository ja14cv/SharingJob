package com.example.jcaal.sharingjob_v01.gui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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

public class principal extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.TabListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private TipoFragmento fragmentoActual;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        //
        vp = (ViewPager)findViewById(R.id.pager);

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
        onNavigationDrawerItemSelected(tf, new String[0]);
    }

    @Override
    public void onNavigationDrawerItemSelected(TipoFragmento tf, String[] parms) {
        if(fragmentoActual != tf){

            //Dependiendo de la seleccion en el menu izquierdo se abre un fragment
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(fragmentoActual != null){ //No activa la pila con el fragent de navigaction drawer
                transaction.addToBackStack(tf.toString());
                mNavigationDrawerFragment.seleccionarItem(tf);
            }

            fragmentoActual =  tf;
            desactivarTabs();

            if(tf == TipoFragmento.INICIO && fragmentoActual != null){
                transaction.replace(R.id.container, inicio.newInstance(new inicio(), R.layout.fragment_inicio, tf, parms)).commit();
            }else if(tf == TipoFragmento.CUENTA){
                transaction.replace(R.id.container, cuenta.newInstance(new cuenta(), R.layout.fragment_cuenta, tf, parms)).commit();
            }else if(tf == TipoFragmento.NUEVO_EMPLEO){
                transaction.replace(R.id.container, nuevo_empleo.newInstance(new nuevo_empleo(), R.layout.fragment_nuevo_empleo, tf, parms)).commit();
            }else if(tf == TipoFragmento.CONFIG){
                activarTabs(tf);
            }else if(tf == TipoFragmento.LOGIN){
                mNavigationDrawerFragment.desmarcarItem();
                transaction.replace(R.id.container, login.newInstance(new login(), R.layout.fragment_login, tf, parms)).commit();
            }else if(tf == TipoFragmento.REGISTRO){
                mNavigationDrawerFragment.desmarcarItem();
                transaction.replace(R.id.container, registro.newInstance(new registro(), R.layout.fragment_registro, tf, parms)).commit();
            }else if(tf == TipoFragmento.EMPRESA){
                mNavigationDrawerFragment.desmarcarItem();
                transaction.replace(R.id.container, empresa.newInstance(new empresa(), R.layout.fragment_empresa, tf, parms)).commit();
            }else if(tf == TipoFragmento.DIRECCION){
                mNavigationDrawerFragment.desmarcarItem();
                transaction.replace(R.id.container, direccion.newInstance(new direccion(), R.layout.fragment_direccion, tf, parms)).commit();
            }else if(tf == TipoFragmento.PERFIL_COMPLETO){
                mNavigationDrawerFragment.desmarcarItem();
                transaction.replace(R.id.container, perfil_completo.newInstance(new perfil_completo(), R.layout.fragment_perfil_completo, tf, parms)).commit();
            }else{
                transaction.replace(R.id.container, inicio.newInstance(new inicio(), R.layout.fragment_inicio, tf, parms)).commit();
            }
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
        }else if(tf == TipoFragmento.EMPRESA){
            mTitle = "Empresa";
        }else if(tf == TipoFragmento.DIRECCION){
            mTitle = "Dirección";
        }else if(tf == TipoFragmento.PERFIL_COMPLETO){
            mTitle = "Perfil completo";
        }else{
            mTitle = getString(R.string.app_name);
        }

        getSupportActionBar().setTitle(mTitle);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private void desactivarTabs(){
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private void activarTabs(TipoFragmento tf){
        if(tf == TipoFragmento.CONFIG){
            if(sesion.isLogin()){
                final ActionBar actionBar = getSupportActionBar();
                actionBar.removeAllTabs();
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                SectionsPagerAdapter spa = new SectionsPagerAdapter(getSupportFragmentManager());
                for (int i = 0; i < spa.getCount(); i++) {
                    actionBar.addTab(actionBar.newTab().setText(spa.getPageTitle(i)).setTabListener(this));
                }

                vp.setAdapter(spa);
                vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new Fragment()).commit();
            }else{
                onNavigationDrawerItemSelected(TipoFragmento.LOGIN);
            }
        }
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
        if(no_frag > 0){
            List<Fragment> listFragments= getSupportFragmentManager().getFragments();
            FragmentGenerico lastFragment = (FragmentGenerico)listFragments.get(no_frag);
            this.onSectionAttached(lastFragment.tipoFragmento);
            fragmentoActual = lastFragment.tipoFragmento;
            restoreActionBar();

            if(lastFragment.tipoFragmento == TipoFragmento.CONFIG){
                activarTabs(TipoFragmento.CONFIG);
            }else{
                desactivarTabs();
            }
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.i("tab", "selecionada");
        vp.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return cuenta.newInstance(new cuenta(), R.layout.fragment_cuenta, TipoFragmento.CUENTA, new String[0]);
            }else{
                return configuracion.newInstance(new configuracion(), R.layout.fragment_configuracion, TipoFragmento.CONFIG, new String[0]);
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Perfil";
                case 1:
                    return "Direccion";
            }
            return null;
        }
    }
}
