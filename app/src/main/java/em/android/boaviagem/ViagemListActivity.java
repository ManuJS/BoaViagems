package em.android.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Emanuelle Menali on 06/05/2016.
 */
public class ViagemListActivity extends ListActivity implements OnItemClickListener, OnClickListener, ViewBinder {

    private AlertDialog alertDialog;
    private int viagemSelecionada;
    private AlertDialog dialogConfirmacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //preencher valores na linha do listview.
        String[] de = {"imagem", "destino", "data", "total"};
        int[] para = {R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, /*R.id.barraProgresso*/};

        SimpleAdapter adapter =
                new SimpleAdapter(this, listarViagens(),
                        R.layout.lista_viagem, de, para);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        this.alertDialog = criaAlertDialog();
        this.dialogConfirmacao = criarDialogoConfirmacao();

        adapter.setViewBinder(this);

    }

    private List<Map<String, Object>> viagens;


    private List<Map<String, Object>> listarViagens() {

        viagens = new ArrayList<Map<String, Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.icone_negocios);
        item.put("destino", "São Paulo");
        item.put("data", "02/05/2016 a 04/06/2016");
        item.put("total", "Gasto total R$ 1000,00");
        item.put("barraProgresso",
                new Double[]{500.0, 450.0, 314.98});
        viagens.add(item);

        item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.icone_lazer);
        item.put("destino", "Maragogi");
        item.put("data", "02/07/2015 a 20/07/2015");
        item.put("total", "Gasto total R$ 3000,00");
        viagens.add(item);

        return viagens;
    }

    @Override
    public boolean setViewValue (View v, Object data, String textRepresentation){

//        if (v.getId() == R.id.barraProgresso){
//            Double valores[] = (Double[])data;
//            ProgressBar progressBar= (ProgressBar) v;
//            progressBar.setMax(valores[0].intValue());
//            progressBar.setSecondaryProgress(
//                    valores[1].intValue());
//            progressBar.setProgress(
//                    valores[2].intValue());
//            return true ;
//        }
//
       return false;
    }


    @Override
    public void onClick(DialogInterface dialog, int item) {

        switch (item) {
            case 0:
                startActivity(new Intent(this, ViagemActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastoListActivity.class));
                break;
            case 3:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;

//                viagens.remove(this.viagemSelecionada);
//                getListView().invalidateViews();
//                break;
            case 4:
                startActivity(new Intent(this, GastoListActivity.class));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Map<String, Object> map = viagens.get(position);
        String destino = (String) map.get("destino");
        String mensagem = "Viagem selecionada: " + destino;

        this.viagemSelecionada = position;
        alertDialog.show();


    }


    private AlertDialog criaAlertDialog() {

        final CharSequence[] items = {

                getString(R.string.editar),
                getString(R.string.novo_gasto),
                getString(R.string.gastos_realizados),
                getString(R.string.remover),
                getString(R.string.visualizar)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);

        return builder.create();

    }

    private AlertDialog criarDialogoConfirmacao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);

        return builder.create();

    }

//    private class ViagemViewBinder implements ViewBinder {
//        @Override
//        public boolean setViewValue(View view, Object data, String textRepresentation) {
//
//
//            if (view.getId() == R.id.barraProgresso) {
//                Double valores[] = (Double[]) data;
//                ProgressBar progressBar = (ProgressBar) view;
//                progressBar.setMax(valores[0].intValue());
//                progressBar.setSecondaryProgress(valores[1].intValue());
//                progressBar.setProgress(valores[2].intValue());
//                return true;
//            }
//            return false;
//        }
//    }
}

