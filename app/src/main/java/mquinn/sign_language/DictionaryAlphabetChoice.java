package mquinn.sign_language;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import mquinn.sign_language.dictionary.ItemListActivity;

public class DictionaryAlphabetChoice extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_alphabet_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void openDictionary (View view){
        startActivity(new Intent(DictionaryAlphabetChoice.this, ItemListActivity.class));
    }

    public void openHomepage(View view){
        startActivity(new Intent(DictionaryAlphabetChoice.this, HomeActivity.class));
    }

    public void openAlphabetChart (View view){
        startActivity(new Intent(DictionaryAlphabetChoice.this, AlphabetChartActivity.class));
    }
}
