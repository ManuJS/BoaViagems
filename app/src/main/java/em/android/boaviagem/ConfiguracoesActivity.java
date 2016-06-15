package em.android.boaviagem;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Emanuelle Menali on 17/05/2016.
 */
public class ConfiguracoesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
