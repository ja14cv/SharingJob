package com.example.jcaal.sharingjob_v01.gui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.example.jcaal.sharingjob_v01.logica.elementoPila;
import com.example.jcaal.sharingjob_v01.logica.sesion;

import java.io.IOException;
import java.util.Stack;

public class principal extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.TabListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private ViewPager vp;
    private Stack<elementoPila> pilaFragmentos = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Seteando datos
        vp = (ViewPager)findViewById(R.id.pager);
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
        onNavigationDrawerItemSelected(tf, new String[0]);
    }

    @Override
    public void onNavigationDrawerItemSelected(TipoFragmento tf, String[] parms) {
        //Verificar pila
        if(!pilaFragmentos.isEmpty()){
            if(pilaFragmentos.lastElement().getTipoFragmento() != tf && tf != TipoFragmento.PROBLEMA){
                inflarFragment(tf, parms);
                pilaFragmentos.push(new elementoPila(tf, parms));
            }else{
                inflarFragment(tf, parms);
            }
        }else{
            inflarFragment(tf, parms);
            pilaFragmentos.push(new elementoPila(tf, parms));
        }
    }

    private void inflarFragment(TipoFragmento tf, String[] parms){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        desactivarTabs();
        if(tf == TipoFragmento.INICIO){
            transaction.replace(R.id.container, inicio.newInstance(new inicio(), R.layout.fragment_inicio, tf, parms)).commit();
        }else if(tf == TipoFragmento.CUENTA){
            transaction.replace(R.id.container, cuenta.newInstance(new cuenta(), R.layout.fragment_cuenta, tf, parms)).commit();
        }else if(tf == TipoFragmento.NUEVO_EMPLEO){
            transaction.replace(R.id.container, nuevo_empleo.newInstance(new nuevo_empleo(), R.layout.fragment_nuevo_empleo, tf, parms)).commit();
        }else if(tf == TipoFragmento.CONFIG){
            transaction.replace(R.id.container, configuracion.newInstance(new configuracion(), R.layout.fragment_configuracion, tf, parms)).commit();
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
        }else if(tf == TipoFragmento.ADD_EMPLEO_REALIZADO){
            mNavigationDrawerFragment.desmarcarItem();
            transaction.replace(R.id.container, add_empleo_realizado.newInstance(new add_empleo_realizado(), R.layout.fragment_add_empleo_realizado, tf, parms)).commit();
        }else if(tf == TipoFragmento.ADD_ESTUDIO_REALIZADO){
            mNavigationDrawerFragment.desmarcarItem();
            transaction.replace(R.id.container, add_estudio_realizado.newInstance(new add_estudio_realizado(), R.layout.fragment_add_estudio_realizado, tf, parms)).commit();
        }else if(tf == TipoFragmento.PROBLEMA){
            mNavigationDrawerFragment.desmarcarItem();
            transaction.replace(R.id.container, problema.newInstance(new problema(), R.layout.fragment_problema, tf, parms)).commit();
        }else if(tf == TipoFragmento.TAB_PERFIL || tf == TipoFragmento.TAB_EMPRESA){
            mNavigationDrawerFragment.desmarcarItem();
            activarTabs(tf);
        }else if(tf == TipoFragmento.RESULTADO_BUSQUEDA){
            mNavigationDrawerFragment.desmarcarItem();
            transaction.replace(R.id.container, resultado_busqueda.newInstance(new resultado_busqueda(), R.layout.fragment_resultado_busqueda, tf, parms)).commit();
        }else if(tf == TipoFragmento.EMPLEOS_TOMADOS){
            mNavigationDrawerFragment.desmarcarItem();
            transaction.replace(R.id.container, empleos_tomados.newInstance(new empleos_tomados(), R.layout.fragment_empleos_tomados, tf, parms)).commit();
        }else if(tf == TipoFragmento.ESTUDIOS_REALIZADOS){
            mNavigationDrawerFragment.desmarcarItem();
            transaction.replace(R.id.container, estudios_realizados.newInstance(new estudios_realizados(), R.layout.fragment_estudios_realizados, tf, parms)).commit();
        }else if(tf == TipoFragmento.EMPLEOS_PUBLICADOS){
            mNavigationDrawerFragment.desmarcarItem();
            transaction.replace(R.id.container, empleos_publicados.newInstance(new empleos_publicados(), R.layout.fragment_empleos_publicados, tf, parms)).commit();
        }else if(tf == TipoFragmento.EMPLEO){
            mNavigationDrawerFragment.desmarcarItem();
            transaction.replace(R.id.container, empleo.newInstance(new empleo(), R.layout.fragment_empleo, tf, parms)).commit();
        }else if(tf == TipoFragmento.EMPLEOS_APLICADOS){
            mNavigationDrawerFragment.desmarcarItem();
            transaction.replace(R.id.container, empleos_aplicados.newInstance(new empleos_aplicados(), R.layout.fragment_empleos_aplicados, tf, parms)).commit();
        }
        else{
            transaction.replace(R.id.container, inicio.newInstance(new inicio(), R.layout.fragment_inicio, tf, parms)).commit();
        }

        onSectionAttached(tf);
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
        }else if(tf == TipoFragmento.ADD_EMPLEO_REALIZADO){
            mTitle = "Empleos Realizados";
        }else if(tf == TipoFragmento.ADD_ESTUDIO_REALIZADO){
            mTitle = "Estudios Realizados";
        }else if(tf == TipoFragmento.PROBLEMA){
            //
        }else if(tf == TipoFragmento.TAB_PERFIL){
            mTitle = "Perfil";
        }else if(tf == TipoFragmento.TAB_EMPRESA){
            mTitle = "Empresa";
        }else if(tf == TipoFragmento.RESULTADO_BUSQUEDA){
            mTitle = "Resultados";
        }else if(tf == TipoFragmento.EMPLEOS_TOMADOS){
            mTitle = "Empleos tomados";
        }else if(tf == TipoFragmento.ESTUDIOS_REALIZADOS){
            mTitle = "Estudios realizados";
        }else if(tf == TipoFragmento.EMPLEOS_PUBLICADOS){
            mTitle = "Empleos publicados";
        }else if(tf == TipoFragmento.EMPLEOS_APLICADOS){
            mTitle = "Empleos aplicados";
        }
        else{
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
        if(vp != null)
            vp.removeAllViews();
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private void activarTabs(TipoFragmento tf){
        if(sesion.isLogin()){
            final ActionBar actionBar = getSupportActionBar();
            actionBar.removeAllTabs();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            if(tf == TipoFragmento.TAB_PERFIL){
                SectionsPagerAdapterPerfil spa = new SectionsPagerAdapterPerfil(getSupportFragmentManager());
                for (int i = 0; i < spa.getCount(); i++) {
                    actionBar.addTab(actionBar.newTab().setText(spa.getPageTitle(i)).setTabListener(this));
                }

                vp.setAdapter(spa);
            }else{
                SectionsPagerAdapterEmpresa spa = new SectionsPagerAdapterEmpresa(getSupportFragmentManager());
                for (int i = 0; i < spa.getCount(); i++) {
                    actionBar.addTab(actionBar.newTab().setText(spa.getPageTitle(i)).setTabListener(this));
                }

                vp.setAdapter(spa);
            }

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

    public void loadLastFragment(){
        onNavigationDrawerItemSelected(pilaFragmentos.lastElement().getTipoFragmento(), pilaFragmentos.lastElement().getParametros());
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
        pilaFragmentos.pop();   //Saca elemento de la pila
        if(pilaFragmentos.size() > 0){
            onNavigationDrawerItemSelected(pilaFragmentos.lastElement().getTipoFragmento(), pilaFragmentos.lastElement().getParametros()); //abre fragmneto anterior
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        vp.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapterPerfil extends FragmentStatePagerAdapter {

        public SectionsPagerAdapterPerfil(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return perfil_completo.newInstance(new perfil_completo(), R.layout.fragment_perfil_completo, TipoFragmento.PERFIL_COMPLETO, new String[0]);
            }else{
                return direccion.newInstance(new direccion(), R.layout.fragment_direccion, TipoFragmento.CONFIG, new String[]{"ente"});
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
                    return "Datos Personales";
                case 1:
                    return "Direccion";
            }
            return null;
        }
    }

    public class SectionsPagerAdapterEmpresa extends FragmentStatePagerAdapter {

        public SectionsPagerAdapterEmpresa(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return empresa.newInstance(new empresa(), R.layout.fragment_empresa, TipoFragmento.PERFIL_COMPLETO, new String[0]);
            }else{
                return direccion.newInstance(new direccion(), R.layout.fragment_direccion, TipoFragmento.CONFIG, new String[]{"empresa"});
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
                    return "Datos";
                case 1:
                    return "Direccion";
            }
            return null;
        }

    }
}
