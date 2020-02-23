package mquinn.sign_language;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class AlphabetChartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphabet_chart);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void openDictionaryAlphabetChoice (View view){
        startActivity(new Intent(AlphabetChartActivity.this, DictionaryAlphabetChoice.class));
    }
}
