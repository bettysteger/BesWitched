package mmt08.beswitched;

import android.app.Activity;
import mmt08.beswitched.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.menu);

      Button start = (Button) findViewById(R.id.button);
      
      start.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
              Intent i = new Intent();
              i.setClassName("mmt08.beswitched", "mmt08.beswitched.BeSwitched");
              startActivity(i);
          }
      });
   }
}

