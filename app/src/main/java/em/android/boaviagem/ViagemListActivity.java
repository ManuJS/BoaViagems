package em.android.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import em.android.boaviagem.DataBase.DatabaseHelper;
import em.android.boaviagem.DataBase.DatabaseHelper.Viagem;

/**
 * Created by Emanuelle Menali on 06/05/2016.
 */
public class ViagemListActivity extends ListActivity implements OnItemClickListener, OnClickListener, ViewBinder {

    private AlertDialog alertDialog;
    private int viagemSelecionada;
    private AlertDialog dialogConfirmacao;
    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private Double valorLimite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        SharedPreferences preferencias =
                PreferenceManager.getDefaultSharedPreferences(this);
        String valor = preferencias.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);

        getListView().setOnItemClickListener(this);
        alertDialog = criarAlertDialog();
        dialogConfirmacao = criarDialogConfirmacao();

//        if (getIntent().hasExtra(Constantes.MODO_SELECIONAR_VIAGEM)) {
//            modoSelecionarViagem =
//                    getIntent().getExtras()
//                            .getBoolean(Constantes.MODO_SELECIONAR_VIAGEM);
//        }

        //TODO mostrar textview informando que os registros estão sendo carregados

        new Task().execute((Void[]) null);

    }

    private AlertDialog criarDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);

        return builder.create();
    }

    private AlertDialog criarAlertDialog() {
        final CharSequence[] items = { getString(R.string.editar),
                getString(R.string.novo_gasto),
                getString(R.string.gastos_realizados),
                getString(R.string.remover) };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);

        return builder.create();
    }


    private class Task extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            return listarViagens();
        }
        @Override
        protected void onPostExecute(List<Map<String, Object>> result) {
            String[] de = { "imagem", "destino", "data",
                    "total" };

            int[] para = { R.id.tipoViagem, R.id.destino,
                    R.id.data, R.id.valor };

            SimpleAdapter adapter = new SimpleAdapter(ViagemListActivity.this, result, R.layout.lista_viagem, de, para);

            adapter.setViewBinder(ViagemListActivity.this);

            setListAdapter(adapter);
        }
    }


    private List<Map<String, Object>> viagens;


   private List<Map<String, Object>> listarViagens() {


        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.rawQuery("SELECT _id, tipo_viagem, destino, data_chegada, data_saida, orcamento FROM viagem", null);

        cursor.moveToFirst();

        viagens = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < cursor.getCount(); i++) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "não existe viagem salva", Toast.LENGTH_SHORT).show();
            } else {

                Map<String, Object> item = new HashMap<String, Object>();

                String id = cursor.getString(0);
                int tipoViagem = cursor.getInt(1);
                String destino = cursor.getString(2);
                long dataChegada = cursor.getLong(3);
                long dataSaida = cursor.getLong(4);
                double orcamento = cursor.getDouble(5);

                item.put("id", id);

                if (tipoViagem == Constantes.VIAGEM_LAZER) {
                    item.put("imagem", R.drawable.icone_lazer);
                } else {
                    item.put("imagem", R.drawable.icone_negocios);
                }

                item.put("destino", destino);

                Date dataChegadaDate = new Date(dataChegada);
                Date dataSaidaDate = new Date(dataSaida);

                String periodo = dateFormat.format(dataChegadaDate) + " a " + dateFormat.format(dataSaidaDate);

                item.put("data", periodo);
                double totalGasto = calcularTotalGasto(db, id);

                item.put("total", "Gasto total R$ " + orcamento);

//        double alerta = orcamento*valorLimite/100;
//        Double[] valores =
//                new Double[]{
//                        orcamento, alerta, totalGasto};
//        item.put("barraPrograsso", valores);

                viagens.add(item);
                cursor.moveToNext();
            }
        }
    cursor.close();
    db.close();

    return viagens;
    }

    private double calcularTotalGasto(SQLiteDatabase db, String id) {

        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM gasto WHERE viagem_id = ?", new String[]{id});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();
        return total;
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

        Intent intent;
        String id = (String) viagens.get(viagemSelecionada).get("id");

        switch (item) {
            case 0:
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastoListActivity.class));
                break;

            case 3: //confirmacao de exclusao
                dialogConfirmacao.show();
                break;

            case DialogInterface.BUTTON_POSITIVE: //exclusao
                viagens.remove(viagemSelecionada);
                removerViagem(id);
                getListView().invalidateViews();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;


        }
    }

    private void removerViagem(String id) {

        SQLiteDatabase db = helper.getWritableDatabase();
        String where [] = new String[]{id};
        db.delete("gasto", "viagem_id = ?", where);
        db.delete("viagem", "_id = ?", where);

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

