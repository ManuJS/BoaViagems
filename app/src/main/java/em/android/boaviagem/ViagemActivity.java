package em.android.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
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
    private Button dataChegadaButton, dataSaidaButton;
    private Date dataChegada, dataSaida;


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

        //dataSaidaButton = (Button) findViewById(R.id.dataSaida);


        destino = (EditText) findViewById(R.id.destino);
        quantidadeDePessoas = (EditText) findViewById(R.id.quantidadeDePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        //acesso ao banco - TEM QUE ALTERAR O METODO CONSTRUTOR DE DATABASEHELPERcmd
        helper = new DatabaseHelper(this);

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
            dataSaidaButton = (Button) findViewById(R.id.dataSaida);
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

        long resultado = db.insert("viagem", null, values);

        if (resultado != -1){
            Toast.makeText(this, getString(R.string.registro_realizado), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getString(R.string.erro_salvar), Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(this, DashboardActivity.class));
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


