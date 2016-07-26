package em.android.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.app.DatePickerDialog.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import em.android.boaviagem.DataBase.DatabaseHelper;

/**
 * Created by Emanuelle Menali on 05/05/2016.
 */
public class ViagemActivity extends Activity {

    private DatabaseHelper helper;
    private int ano, mes, dia;
    private EditText  destino, quantidadeDePessoas, orcamento;
    private RadioGroup radioGroup;
    private Button dataChegadaButton;
    private Button dataSaidaButton;
    private Date dataChegada;
    private Date dataSaida;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_viagem3);

        //pegar data
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);


        dataChegadaButton = (Button) findViewById(R.id.dataChegada);
        //dataChegadaButton.setText(dia + "/" + (mes + 1) + "/" + ano);

        dataSaidaButton = (Button) findViewById(R.id.dataSaida);


        destino = (EditText) findViewById(R.id.destino);
        quantidadeDePessoas = (EditText) findViewById(R.id.quantidadeDePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        //acesso ao banco - TEM QUE ALTERAR O METODO CONSTRUTOR DE DATABASEHELPERcmd
        helper = new DatabaseHelper(this);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        if (id != null){
            prepararEdicao();
        }

    }

    private void prepararEdicao() {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor =
                db.rawQuery("SELECT tipo_viagem, destino, data_chegada, " +
                        "data_saida, quantidadeDePessoas, orcamento " +
                        "FROM VIAGEM WHERE _ID = ?", new String[]{id});
        cursor.moveToFirst();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (cursor.getInt(0) ==  Constantes.VIAGEM_LAZER){
            radioGroup.check(R.id.lazer);
        }else{
            radioGroup.check(R.id.negocios);
        }
        destino.setText(cursor.getString(1));
        dataChegada = new Date(cursor.getLong(2));
        dataSaida = new Date(cursor.getLong(3));
        dataChegadaButton.setText(dateFormat.format(dataChegada));
       //arrumar esse botao.
        //
        dataSaidaButton.setText(dateFormat.format(dataSaida));
        quantidadeDePessoas.setText(cursor.getString(4));
        orcamento.setText(cursor.getString(5));
        cursor.close();

    }

    private OnDateSetListener dataChegadaListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
            dataChegada = criarData(anoSelecionado, mesSelecionado, diaSelecionado);

            dataChegadaButton.setText(diaSelecionado + "/" + (mesSelecionado+1) + "/" + anoSelecionado);

        }
    };

    private OnDateSetListener dataSaidaListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,int anoSelecionado, int mesSelecionado, int diaSelecionado) {
            dataSaida = criarData(anoSelecionado, mesSelecionado, diaSelecionado);
            //elemento declarado dentro do metodo
            //dataSaidaButton = (Button) findViewById(R.id.dataSaida);
            dataSaidaButton.setText(diaSelecionado + "/" + (mesSelecionado+1) + "/" + anoSelecionado);

        }
    };


    //caixa de dialogo para seleção da data
    @Override
    protected Dialog onCreateDialog(int id){

        switch(id){
            case R.id.dataChegada:
                return new DatePickerDialog(this, dataChegadaListener, ano, mes, dia);

            case R.id.dataSaida:
                return new DatePickerDialog(this, dataSaidaListener, ano, mes, dia);
        }

        return null;
    }


    private Date criarData(int anoSelecionado, int mesSelecionado, int diaSelecionado){
       Calendar calendar = Calendar.getInstance();
        calendar.set(anoSelecionado, mesSelecionado, diaSelecionado);
        return calendar.getTime();
    }



    public void salvarViagem(View v){

//aqui eu pego os dados da view pra por no banco
        String destinos = destino.getText().toString();
        long dataC = dataChegada.getTime();
        long dataS = dataSaida.getTime();
        String orcamentos = orcamento.getText().toString();
        String qtdp = quantidadeDePessoas.getText().toString();
        int vl =  Constantes.VIAGEM_LAZER;
        int vn = Constantes.VIAGEM_NEGOCIOS;

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("destino", destinos);
        //tenho algum problema nas datas.
        values.put("data_chegada", dataC);
        values.put("data_saida",dataS);
        values.put("orcamento", orcamentos);
        values.put("quantidadeDePessoas",qtdp);

        int tipo = radioGroup.getCheckedRadioButtonId();

        if (tipo == R.id.lazer){
            values.put("tipo_viagem", vl);
        }
        if (tipo == R.id.negocios){
            values.put("tipo_viagem", vn);
        }

        long resultado;

        if (id == null){
           resultado = db.insert("viagem", null, values);
        }else{
            resultado = db.update("viagem", values, "_id = ?", new String[]{id});
        }


        if (resultado != -1){
            Toast.makeText(this, getString(R.string.registro_realizado), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getString(R.string.erro_salvar), Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(this, DashboardActivity.class));
    }

    private void removerViagem (String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String where [] = new String[]{id};
        //db.delete("GASTO", "VIAGEM_ID = ?", where);
        db.delete("VIAGEM", "ID = ?", where);
    }


    public void selecionarOpcao(View v) {

    }

    public void selecionarData(View v){
        showDialog(v.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viagem_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {

            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }
}


