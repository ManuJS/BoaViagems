package em.android.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import em.android.boaviagem.DataBase.DatabaseHelper;

/**
 * Created by Emanuelle Menali on 06/05/2016.
 */
public class GastoActivity extends Activity {


    private DatabaseHelper helper;
    private int ano, mes, dia;
    private Button dataGasto;
    private Spinner categoria;
    private Spinner viagem;
    EditText valor;

    private List<String> listaViagens = new ArrayList<String>();
    private String ListaViagem;



    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo_gasto_viagem);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataGasto = (Button) findViewById(R.id.data);
        dataGasto.setText(dia + "/" + (mes + 1) + "/" + ano);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categoria_gasto,
                android.R.layout.simple_spinner_item);
        categoria = (Spinner) findViewById(R.id.categoria);
        categoria.setAdapter(adapter);

        valor = (EditText) findViewById(R.id.valor);
      //  String valor = (vlr_gasto.getText().toString());


        //Adicionando Nomes no ArrayList
        listaViagens.add("João");
        listaViagens.add("Maria");
        listaViagens.add("José");
        listaViagens.add("Paulo");
        listaViagens.add("Adriana");

        //Identifica o Spinner no layout
        viagem = (Spinner) findViewById(R.id.viagem);
        //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaViagens);
        ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        viagem.setAdapter(spinnerArrayAdapter);

    }


    //começo do código para pegar data
    public void selecionarData(View view){
        showDialog(view.getId());

    }
    @Override
    protected Dialog onCreateDialog(int id){

        if (R.id.data == id){
            return new DatePickerDialog(this, listener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataGasto.setText(dia + "/" +( mes +1)+ "/" + ano);
        }
    };//fim do código para pegar data - é meio obvio mas eu posso esquecer como fiz isso. :(


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    public void registroGasto(View view){

        String valors = valor.getText().toString();
        Toast.makeText(this, "apertei o botão salvar " + valors, Toast.LENGTH_SHORT).show();

    }
}
