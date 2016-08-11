package maciejsikora.com.colorfader;

import android.animation.ArgbEvaluator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ColorFader colorFader;
    private Button toggleButton;
    private boolean hsv;//if hsv evaluator turned

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton=(Button)findViewById(R.id.toggle);

        ArrayList<Integer> colors=new ArrayList<Integer>();

        colors.add(getResources().getColor(R.color.blue));
        colors.add(getResources().getColor(R.color.green));
        colors.add(getResources().getColor(R.color.red));


        colorFader=new ColorFader(colors,findViewById(R.id.color_fader_container));

        colorFader.addColor(getResources().getColor(R.color.yellow))
        .addColor(getResources().getColor(R.color.grey))
        .addColor(getResources().getColor(R.color.orange))
        .addColor(getResources().getColor(R.color.pink));

        colorFader.setDuration(4000);//4 s


        colorFader.startLoop();
    }

    public void toggle(View view) {

        if (colorFader.isLoopOn()) {
            colorFader.stopLoop();
            toggleButton.setText("Start");
        }
        else {
            colorFader.startLoop();
            toggleButton.setText("Stop");
        }
    }

    public void toRed(View view) {

        colorFader.fadeToColor(2);//red has 2 index
        toggleButton.setText("Start");
    }

    public void changeEvaluator(View view) {

        colorFader.endLoop();

        if (!this.hsv)
        colorFader.setEvaluator(new ColorFader.HsvEvaluator());
        else
            colorFader.setEvaluator(new ArgbEvaluator());

        toggleButton.setText("Start");
    }
}
