package em.android.boaviagem.antigas;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
/**
 * Created by Emanuelle Menali on 06/05/2016.
 */
public class GastoListActivity_Antiga extends ListActivity implements AdapterView.OnItemClickListener, View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listarGastos()));
        ListView listView = getListView();
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView textView= (TextView) view;
        Toast.makeText(this, "Gasto Selecionado: "+ textView.getText(),
                Toast.LENGTH_SHORT).show();

    }

    private List<String> listarGastos() {
        return Arrays.asList("Sanduiche R$ 19,90",
                "Taxi aeroporto - Hotel R$ 80,00",
                "Revista R$ 12,00");
    }
}
