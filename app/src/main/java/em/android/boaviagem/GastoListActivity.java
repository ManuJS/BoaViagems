package em.android.boaviagem;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Emanuelle Menali on 06/05/2016.
 */
public class GastoListActivity extends ListActivity implements OnItemClickListener {

    private String dataAnterior = "";
    private List<Map<String, Object>> gastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] de = {"data", "descricao", "valor", "categoria"};
        int[] para = {R.id.data, R.id.descricao, R.id.valor, R.id.categoria};

        SimpleAdapter adapter = new SimpleAdapter(this,
                listarGastos(), R.layout.lista_gasto, de, para);

        adapter.setViewBinder(new GastoViewBinder());

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);

        registerForContextMenu(getListView());

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.remover){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            getListView().invalidateViews();
            dataAnterior = "";
            return true;
        }
        return super.onContextItemSelected(item);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Map<String, Object> map = gastos.get(position);
        String descricao = (String) map.get("descrição");
        String mensagem = "Gasto selecionado: " + descricao;
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();

    }


    private List<Map<String, Object>> listarGastos() {
        gastos = new ArrayList<Map<String, Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("data", "04/04/2016");
        item.put("descricao", "Diária hotel");
        item.put("valor", "R$ 1300,00");
        item.put("categoria", R.color.categoria_hospedagem);
        gastos.add(item);

        item = new HashMap<String, Object>();
        item.put("data", "04/04/2016");
        item.put("descricao", "Diária hotel");
        item.put("valor", "R$ 1300,00");
        item.put("categoria", R.color.categoria_hospedagem);
        gastos.add(item);

        item = new HashMap<String, Object>();
        item.put("data", "02/04/2016");
        item.put("descricao", "Restaurante");
        item.put("valor", "R$ 130,00");
        item.put("categoria", R.color.categoria_alimentacao);
        gastos.add(item);


        return gastos;
    }

    private class GastoViewBinder implements SimpleAdapter.ViewBinder {


        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            if (view.getId() == R.id.data) {
                if (!dataAnterior.equals(data)) {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }
            if (view.getId() == R.id.categoria) {
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }

}
