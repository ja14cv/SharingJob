package com.example.jcaal.sharingjob_v01.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    public void otrosParametros(Bundle args, String[] parms) {

    }

    @Override
    public void hacerOnCreate() {
        mCallback.seleccion(TipoFragmento.INICIO);
        final SearchView sv = (SearchView) getView().findViewById(R.id.inicio_sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v("inicio", "buscar: " + query);
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
        }
    }

    @Override
    public void Wsdl2CodeStartedRequest() {

    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        procesar_categorias((String)Data);
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {

    }

    @Override
    public void Wsdl2CodeEndedRequest() {

    }

    private void procesar_categorias(String _json){
        TreeNode root = TreeNode.root();
        try {
            //String json = "[{\"id_categoria\":\"1\",\"nombre\":\"Profesional\",\"descripcion\":null,\"id_categoria_padre\":null,\"segundo_nivel\":[{\"id_categoria\":\"2\",\"nombre\":\"Ingenieria\",\"descripcion\":null,\"id_categoria_padre\":\"1\",\"tercer_nivel\":[{\"id_categoria\":\"4\",\"nombre\":\"Sistemas\",\"descripcion\":null,\"id_categoria_padre\":\"2\"},{\"id_categoria\":\"5\",\"nombre\":\"Mecanica\",\"descripcion\":null,\"id_categoria_padre\":\"2\"}]},{\"id_categoria\":\"6\",\"nombre\":\"Medicina\",\"descripcion\":null,\"id_categoria_padre\":\"1\",\"tercer_nivel\":null},{\"id_categoria\":\"7\",\"nombre\":\"Politicas\",\"descripcion\":null,\"id_categoria_padre\":\"1\",\"tercer_nivel\":null}]},{\"id_categoria\":\"8\",\"nombre\":\"Tecnica\",\"descripcion\":null,\"id_categoria_padre\":null,\"segundo_nivel\":[{\"id_categoria\":\"9\",\"nombre\":\"Mecanico\",\"descripcion\":null,\"id_categoria_padre\":\"8\",\"tercer_nivel\":null},{\"id_categoria\":\"10\",\"nombre\":\"Panadero\",\"descripcion\":null,\"id_categoria_padre\":\"8\",\"tercer_nivel\":null}]},{\"id_categoria\":\"11\",\"nombre\":\"Artistica\",\"descripcion\":null,\"id_categoria_padre\":null,\"segundo_nivel\":[{\"id_categoria\":\"12\",\"nombre\":\"Danza\",\"descripcion\":null,\"id_categoria_padre\":\"11\",\"tercer_nivel\":null},{\"id_categoria\":\"13\",\"nombre\":\"Canto\",\"descripcion\":null,\"id_categoria_padre\":\"11\",\"tercer_nivel\":null},{\"id_categoria\":\"14\",\"nombre\":\"Teatro\",\"descripcion\":null,\"id_categoria_padre\":\"11\",\"tercer_nivel\":null}]},{\"id_categoria\":\"15\",\"nombre\":\"Otros\",\"descripcion\":null,\"id_categoria_padre\":null,\"segundo_nivel\":null}]";

            JSONArray temp = new JSONObject(_json).getJSONArray("array");

            for (int i=0 ; i<temp.length() ; i++){
                JSONObject temporal = temp.getJSONObject(i);
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
                            250,
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
                                250,
                                Integer.parseInt(temporal2.getString("id_categoria")),
                                1,
                                padre_n1);
                        TreeNode hijo_n1 = new TreeNode(nodo_n1).setViewHolder(new MyHolder(getView().getContext()));
                        if(padre_n1) {
                            MyHolder.IconTreeItem nodo1_n1 = new MyHolder.IconTreeItem(
                                    0,
                                    "Todo " + temporal2.getString("nombre"),
                                    350,
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
                                        350,
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
            LinearLayout ll = (LinearLayout)getView().findViewById(R.id.cuenta_sv_contenedor);
            ll.addView(tView.getView());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}