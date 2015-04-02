package com.example.jcaal.sharingjob_v01.gui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.jcaal.sharingjob_v01.R;
import com.example.jcaal.sharingjob_v01.logica.TipoFragmento;
import com.example.jcaal.sharingjob_v01.ws.IWsdl2CodeEvents;
import com.example.jcaal.sharingjob_v01.ws.ws_sharingJob;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class inicio extends FragmentGenerico implements IWsdl2CodeEvents{

    @Override
    public void otrosParametros(String[] parms) {

    }

    private ProgressDialog dialog;

    @Override
    public void hacerOnCreate() {
        mCallback.seleccion(tipoFragmento);
        SearchView sv = (SearchView) getView().findViewById(R.id.inicio_sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v("inicio", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ws_sharingJob ws = new ws_sharingJob(this);
        try {
            ws.get_categoria_trabajoAsync();
        } catch (Exception e) {
            e.printStackTrace();
            this.onDestroy();
            Toast.makeText(getActivity(), "Ocurrio un problema en las categorias", Toast.LENGTH_LONG).show();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.PROBLEMA);
        }
    }

    @Override
    public void Wsdl2CodeStartedRequest() {
        dialog = ProgressDialog.show(getView().getContext(), "Cargando", "Por favor espere...", true);
    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        Log.v("inicio", (String)Data);
        procesar_categorias(Data.toString());
        dialog.dismiss();
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        Log.e("inicio", ex.getMessage());
        this.onDestroy();
        Toast.makeText(getActivity(), "Ocurrio un problema en las categorias", Toast.LENGTH_LONG).show();
        mCallback.onNavigationDrawerItemSelected(TipoFragmento.PROBLEMA);
    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }

    private void procesar_categorias(String data){
        TreeNode root = TreeNode.root();
        try {
            JSONObject jso = new JSONObject(data);
            JSONObject t1 = jso.getJSONArray("datos").getJSONObject(0);

            String tipo = t1.getString("Tipo");
            String desc = t1.getString("Descripcion");

            if(tipo.equals("1")){
                JSONArray t2 = jso.getJSONArray("array");

                for (int i=0 ; i < t2.length() ; i++){
                    JSONObject temporal = t2.getJSONObject(i);
                    boolean padre = temporal.getString("segundo_nivel") != "null";
                    MyHolder.IconTreeItem nodeItem = new MyHolder.IconTreeItem(
                            0,
                            temporal.getString("nombre"),
                            150,
                            Integer.parseInt(temporal.getString("id_categoria")),
                            0,
                            padre);
                    TreeNode parent = new TreeNode(nodeItem).setViewHolder(new MyHolder(getView().getContext()));
                    if (padre){
                        MyHolder.IconTreeItem nodeItem1 = new MyHolder.IconTreeItem(
                                0,
                                "Todo " + temporal.getString("nombre"),
                                200,
                                Integer.parseInt(temporal.getString("id_categoria")),
                                0,
                                false);
                        TreeNode parent1 = new TreeNode(nodeItem1).setViewHolder(new MyHolder(getView().getContext()));
                        parent1.setClickListener(new TreeNode.TreeNodeClickListener() {
                            @Override
                            public void onClick(TreeNode treeNode, Object o) {
                                Toast.makeText(getActivity(), "Padre -> id:" + ((MyHolder.IconTreeItem)treeNode.getValue()).id, Toast.LENGTH_SHORT).show();
                            }
                        });
                        parent.addChild(parent1);
                    }else{
                        parent.setClickListener(new TreeNode.TreeNodeClickListener() {
                            @Override
                            public void onClick(TreeNode treeNode, Object o) {
                                Toast.makeText(getActivity(), "Padre -> id:" + ((MyHolder.IconTreeItem)treeNode.getValue()).id, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    if (padre) {
                        JSONArray temp2 = temporal.getJSONArray("segundo_nivel");
                        for (int j=0 ; j<temp2.length() ; j++){
                            JSONObject temporal2 = temp2.getJSONObject(j);
                            boolean padre_n1 = temporal2.getString("tercer_nivel") != "null";
                            MyHolder.IconTreeItem nodo_n1 = new MyHolder.IconTreeItem(
                                    0,
                                    temporal2.getString("nombre"),
                                    200,
                                    Integer.parseInt(temporal2.getString("id_categoria")),
                                    1,
                                    padre_n1);
                            TreeNode hijo_n1 = new TreeNode(nodo_n1).setViewHolder(new MyHolder(getView().getContext()));
                            if(padre_n1) {
                                MyHolder.IconTreeItem nodo1_n1 = new MyHolder.IconTreeItem(
                                        0,
                                        "Todo " + temporal2.getString("nombre"),
                                        250,
                                        Integer.parseInt(temporal2.getString("id_categoria")),
                                        1,
                                        false);
                                TreeNode hijo1_n1 = new TreeNode(nodo1_n1).setViewHolder(new MyHolder(getView().getContext()));
                                hijo1_n1.setClickListener(new TreeNode.TreeNodeClickListener() {
                                    @Override
                                    public void onClick(TreeNode treeNode, Object o) {
                                        Toast.makeText(getActivity(), "Padre -> id:" + ((MyHolder.IconTreeItem) treeNode.getValue()).id, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                hijo_n1.addChild(hijo1_n1);
                            }else{
                                hijo_n1.setClickListener(new TreeNode.TreeNodeClickListener() {
                                    @Override
                                    public void onClick(TreeNode treeNode, Object o) {
                                        Toast.makeText(getActivity(), "Padre -> id:" + ((MyHolder.IconTreeItem) treeNode.getValue()).id, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            if(padre_n1) {
                                JSONArray temp3 = temporal2.getJSONArray("tercer_nivel");
                                for (int k = 0; k < temp3.length(); k++) {
                                    JSONObject temporal3 = temp3.getJSONObject(k);
                                    MyHolder.IconTreeItem nodo_n2 = new MyHolder.IconTreeItem(
                                            0,
                                            temporal3.getString("nombre"),
                                            250,
                                            Integer.parseInt(temporal3.getString("id_categoria")),
                                            2,
                                            false);
                                    TreeNode hijo_n2 = new TreeNode(nodo_n2).setViewHolder(new MyHolder(getView().getContext()));
                                    hijo_n2.setClickListener(new TreeNode.TreeNodeClickListener() {
                                        @Override
                                        public void onClick(TreeNode treeNode, Object o) {
                                            Toast.makeText(getActivity(), "Padre -> id:" + ((MyHolder.IconTreeItem)treeNode.getValue()).id, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    hijo_n1.addChild(hijo_n2);
                                }
                            }
                            parent.addChild(hijo_n1);
                        }
                    }
                    root.addChild(parent);
                }

                AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
                LinearLayout fl = (LinearLayout)getView().findViewById(R.id.cuenta_sv_contenedor);
                fl.addView(tView.getView());
            }else{
                this.onDestroy();
                Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
                mCallback.onNavigationDrawerItemSelected(TipoFragmento.PROBLEMA);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.onDestroy();
            Toast.makeText(getActivity(), "Ocurrio un problema en las categorias", Toast.LENGTH_LONG).show();
            mCallback.onNavigationDrawerItemSelected(TipoFragmento.PROBLEMA);
        }
    }
}