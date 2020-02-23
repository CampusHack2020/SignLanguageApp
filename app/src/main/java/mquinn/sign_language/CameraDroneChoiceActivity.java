package mquinn.sign_language;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class CameraDroneChoiceActivity extends Activity {

    protected enum Difficulty { EASY, MEDIUM, HARD}
    static protected Difficulty difficulty = Difficulty.MEDIUM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_drone_choice_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void openCamera (View view){
        CameraActivity.droneMode = false;
        startActivity(new Intent(CameraDroneChoiceActivity.this, CameraActivity.class));
    }

    public void openDrone (View view){
        CameraActivity.droneMode = true;
        startActivity(new Intent(CameraDroneChoiceActivity.this, CameraActivity.class));
    }

    public void openDifficulty (View view){
        startActivity(new Intent(CameraDroneChoiceActivity.this, DifficultyActivity.class));
    }

}