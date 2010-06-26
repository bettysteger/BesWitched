package mmt08.beswitched;

import mmt08.beswitched.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class About extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.about);

      Button back = (Button) findViewById(R.id.button);
      
      back.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
              Intent i = new Intent();
              i.setClassName("mmt08.beswitched", "mmt08.beswitched.GameOver");
              startActivity(i);
          }
      });
   }
}

