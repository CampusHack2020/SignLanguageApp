package mquinn.sign_language;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import mquinn.sign_language.imaging.IFrame;
import mquinn.sign_language.processing.DetectionMethod;
import mquinn.sign_language.processing.DownSamplingFrameProcessor;
import mquinn.sign_language.processing.IFrameProcessor;
import mquinn.sign_language.processing.MainFrameProcessor;
import mquinn.sign_language.processing.ResizingFrameProcessor;
import mquinn.sign_language.processing.SizeOperation;
import mquinn.sign_language.processing.postprocessing.IFramePostProcessor;
import mquinn.sign_language.processing.postprocessing.OutputFramePostProcessor;
import mquinn.sign_language.processing.postprocessing.UpScalingFramePostProcessor;
import mquinn.sign_language.processing.preprocessing.CameraFrameAdapter;
import mquinn.sign_language.processing.preprocessing.IFramePreProcessor;
import mquinn.sign_language.processing.preprocessing.InputFramePreProcessor;
import mquinn.sign_language.rendering.IRenderer;
import mquinn.sign_language.rendering.MainRenderer;
import mquinn.sign_language.svm.FrameClassifier;

public class CameraActivity extends AppCompatActivity implements CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;

    private IFramePreProcessor preProcessor;
    private IFramePostProcessor postProcessor;
    private IFrame preProcessedFrame, processedFrame, postProcessedFrame, classifiedFrame;

    private IFrameProcessor mainFrameProcessor, frameClassifier;

    private IRenderer mainRenderer;

    private TextView signedString, word, stringSoFar;

    private int wordPosIndex = 0;

    private List<String> words = Arrays.asList("you", "you", "you", "you", "you", "you");
    private List<Character> letters = new ArrayList<>();

    private String currentLetter, previousLetter, modLetter, currentWord;

    private DetectionMethod detectionMethod;

    private Random rand = new Random();

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
//                    mOpenCvCameraView.enableFpsMeter();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newRandWord("");

        // Set view parameters
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set content view
        setContentView(R.layout.sign_language_activity_surface_view);

        // Camera config
        mOpenCvCameraView = findViewById(R.id.sign_language_activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
        }

        word = findViewById(R.id.stringToSign);
        word.setText("Sign: " + currentWord);
        signedString = findViewById(R.id.signedString);
        stringSoFar = findViewById(R.id.stringSoFar);


    }

    public void newRandWord(String current) {

        String newWord = current;

        while (newWord.equals(current)) {
            newWord = words.get(rand.nextInt(words.size()));
        }

        currentWord = newWord;
    }

    public void setCameraDroneChoice (View view){
        startActivity(new Intent(CameraActivity.this, CameraDroneChoiceActivity.class));
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        // Generate Frame from input frame and downsample
        preProcessedFrame = preProcessor.preProcess(inputFrame);

        // Generate useful information from frame
        processedFrame = mainFrameProcessor.process(preProcessedFrame);

        // Post processing of processed frame and upsampling
        postProcessedFrame = postProcessor.postProcess(processedFrame);

        // Actual frame classification
        classifiedFrame = frameClassifier.process(postProcessedFrame);

        previousLetter = currentLetter;
        currentLetter = getDisplayableLetter(classifiedFrame.getLetterClass().toString());

        setLetterIfChanged();

        // Display anything required
//        mainRenderer.display(postProcessedFrame);

        // Return processed Mat
        return classifiedFrame.getRGBA();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }



    public void onCameraViewStarted(int width, int height) {

        setProcessors(DetectionMethod.CANNY_EDGES);

        File xmlFile = initialiseXMLTrainingData();

        frameClassifier = new FrameClassifier(xmlFile);

    }

    public void onCameraViewStopped() {

    }

    private void setProcessors(DetectionMethod method){
        // Set detection method
        detectionMethod = method;

        // Create renderer's
        mainRenderer = new MainRenderer(detectionMethod);

        // Pre processors
        preProcessor = new InputFramePreProcessor(
                new CameraFrameAdapter(
                        new DownSamplingFrameProcessor(),
                        new ResizingFrameProcessor(SizeOperation.UP)
                )
        );

        // Frame Processors
        mainFrameProcessor = new MainFrameProcessor(detectionMethod);

        // Post processors
        postProcessor = new OutputFramePostProcessor(
                new UpScalingFramePostProcessor(),
                new ResizingFrameProcessor(SizeOperation.UP)
        );
    }

    private void setLetterIfChanged(){
        if (!currentLetter.equals(previousLetter)){
            modLetter = currentLetter;
            if (modLetter.equals("NONE"))
                modLetter = "?";
            if (modLetter.equals("SPACE"))
                modLetter = " ";
            setPossibleLetter(modLetter);
        }
    }

    private void setPossibleLetter(final String currentLetterForMod){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                String soFar = letters.stream().map(String::valueOf).collect(Collectors.joining());

                if (soFar.equals(currentWord)) {

//                    Toast.makeText(CameraActivity.this, "Great job!", Toast.LENGTH_SHORT).show();

                    newRandWord(currentWord);
                    word.setText("Sign: " + currentWord);
                    letters.clear();
                    signedString.setText("Great job!");
                    stringSoFar.setText("");

                    wordPosIndex = 0;

                    return;

                }

                char currentChar = currentLetterForMod.trim().toLowerCase().charAt(0);

                if (!currentLetterForMod.contains("?") && (currentWord.charAt(wordPosIndex) == currentChar)) {
                    letters.add(currentChar);
                    signedString.setText(currentLetterForMod);

                    stringSoFar.setText(letters.stream().map(String::valueOf).collect(Collectors.joining()));
                    wordPosIndex++;

                } else if (!currentLetterForMod.contains("?")) {
                    signedString.setText("Oops! That was a " + currentLetterForMod);
                }

            }
        });
    }

    private String getDisplayableLetter(String letter){

        switch (letter){
            case "NONE":
                return "?";
            case "SPACE":
                return " ";
            default:
                return letter;
        }

    }

    private File initialiseXMLTrainingData(){

        try {
            InputStream is = getResources().openRawResource(R.raw.trained);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir,"training.xml");

            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            is.close();
            os.close();

            return mCascadeFile;

        } catch (Exception e) {
            e.printStackTrace();
            return new File("");
        }

    }

}