package edu.cnm.deepdive.craps.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.cnm.deepdive.craps.R;
import edu.cnm.deepdive.craps.models.Craps;
import java.util.List;

public class DiceImageAdapter extends ArrayAdapter<int[]> {

  private Drawable[] faces;//load 6 dice images
  private int resourceId;
  private List<int[]> rolls;
  private Craps.State state;
  private int winColor;
  private int loseColor;


  public DiceImageAdapter(Context context, int resource,
      List<int[]> objects, Craps.State state, Drawable[] faces) {
    super(context, resource, objects);
    this.resourceId = resource;
    this.rolls = objects;
    this.state = state;
    this.faces = faces;
    winColor = context.getResources().getColor(R.color.winning_roll);
    loseColor = context.getResources().getColor(R.color.losing_roll);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    int[] roll = rolls.get(position);
    LinearLayout view = (LinearLayout) LayoutInflater.from(getContext()).inflate(resourceId, null, false);
    ImageView dice0 = view.findViewById(R.id.dice_0);
    ImageView dice1 = view.findViewById(R.id.dice_1);
    TextView diceTotal = view.findViewById(R.id.dice_total);
    dice0.setImageDrawable(faces[roll[0] - 1]);
    dice1.setImageDrawable(faces[roll[1] - 1]);
    diceTotal.setText(getContext().getString(R.string.roll_total_format,  roll[0] + roll[1]));
    if (position == rolls.size() - 1) {
      if (state == Craps.State.WIN) {
        view.setBackgroundColor(winColor);
      } else {
        view.setBackgroundColor(loseColor);
      }
    }
    return view;
  }
}
