package edu.cnm.deepdive.craps.controllers;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import edu.cnm.deepdive.craps.R;
import edu.cnm.deepdive.craps.helpers.DiceImageAdapter;
import edu.cnm.deepdive.craps.helpers.DiceTextAdapter;
import edu.cnm.deepdive.craps.models.Craps;
import edu.cnm.deepdive.craps.models.Game;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String FACE_PREFIX = "face_"; //match all image name

  private TextView playsValue;
  private TextView winsValue;
  private TextView winsPercentage;
  private Button play;
  private ToggleButton run;
  private Button reset;
  private Game game;
  private ListView rollsList;
  private boolean running = false;
  private Runner runner = null;
  private Drawable[] faces;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    playsValue = findViewById(R.id.plays_value);
    winsValue = findViewById(R.id.wins_value);
    winsPercentage = findViewById(R.id.percentage_value);
    play = findViewById(R.id.play);
    run = findViewById(R.id.play_on);
    reset = findViewById(R.id.reset);
    rollsList = findViewById(R.id.rolls_list);
    game = new Game();
    faces = loadDiceFaces();
    setupEvents();
  }

  private void updateDisplay(){
    Craps.State state;
    long wins;
    long losses;
    List<int[]> rolls;
    synchronized (game) {
      state = game.getCraps().getState();
      wins = game.getWins();
      losses = game.getLosses();
      rolls = game.getCraps().getRolls();
    }
    long plays = wins + losses;
    double percentage = (plays > 0 ) ? (100.0 * wins / plays) : 0; // 0/0 is Not A number
    playsValue.setText(String.format("%,d", plays));
    winsValue.setText(String.format("%,d", wins));
    winsPercentage.setText(String.format("%.2f%%", percentage));
    rollsList.setAdapter(new DiceImageAdapter(
        MainActivity.this, R.layout.item_roll_dice, rolls, state, faces));
  }

  private Drawable[] loadDiceFaces(){
    Drawable[] faces = new Drawable[6];
    Resources res = getResources(); //reference to any in res pack
    for (int i = 0 ; i < faces.length; i++){
      int id = res.getIdentifier(FACE_PREFIX + (i + 1), "drawable", getPackageName());  //get id first then
      faces[i] = res.getDrawable(id, null);
    }
    return faces;
  }

  private void setupEvents(){
    play.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        game.play();
        updateDisplay();
      }
    });
    reset.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        game.reset();
        updateDisplay();
      }
    });
    run.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (run.isChecked()) {
          running = true;
          play.setEnabled(false); //disable ui button
          reset.setEnabled(false);
          runner = new Runner();
          runner.start();
        } else {
          running = false;
          runner = null;
          play.setEnabled(true); //enable ui button
          reset.setEnabled(true);
        }
      }
    });


  }

  private class Runner extends Thread {

    public static final int UPDATE_INTERVAL = 10_000;

    @Override
    public void run() {
      long counter = 0;
      while (running) {
        synchronized (game) {
          game.play();
        }
        if (++counter % UPDATE_INTERVAL == 0) { //only do at 1000th
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              updateDisplay();
            }
          }); //end if runOnUiThread
        } //end if
      } //end while
    } //end run
  }
}
