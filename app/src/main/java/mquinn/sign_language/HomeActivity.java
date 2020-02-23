package mquinn.sign_language;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void openDifficulty (View view){
        startActivity(new Intent(HomeActivity.this, DifficultyActivity.class));
    }

    public void openDictionaryAlphabetChoice (View view){
        startActivity(new Intent(HomeActivity.this, DictionaryAlphabetChoice.class));
    }

}
