package mquinn.sign_language;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class DifficultyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void openCameraDroneChoice (View view){
        startActivity(new Intent(DifficultyActivity.this, CameraDroneChoiceActivity.class));
    }

    public void openMain (View view){

        switch (view.getId()) {
            case (R.id.buttonHard):
                CameraDroneChoiceActivity.difficulty = CameraDroneChoiceActivity.Difficulty.HARD;
                break;
            case (R.id.buttonMedium):
                CameraDroneChoiceActivity.difficulty = CameraDroneChoiceActivity.Difficulty.MEDIUM;
                break;
            case (R.id.buttonEasy):
                CameraDroneChoiceActivity.difficulty = CameraDroneChoiceActivity.Difficulty.EASY;
                break;
        }

        startActivity(new Intent(DifficultyActivity.this, MainActivity.class));
    }
}