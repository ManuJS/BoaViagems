package em.android.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Emanuelle Menali on 04/05/2016.
 */
public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.dashboard);


    }

    public void selecionarOpcao(View view) {

        TextView textView = (TextView) view;
        String opcao = "Opção" + textView.getText().toString();
        Toast.makeText(this, opcao, Toast.LENGTH_LONG).show();
        //startActivity(new Intent(this, ViagemListActivity.class));

        switch (view.getId()) {
            case R.id.nova_viagem:
                startActivity(new Intent(this, ViagemActivity.class));
                break;
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case R.id.minhas_viagens:
                startActivity(new Intent(this, ViagemListActivity.class));
                break;
            case R.id.configuracoes:
               startActivity(new Intent(this, ConfiguracoesActivity .class));


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashbord_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        finish();
        return true;
    }
}
