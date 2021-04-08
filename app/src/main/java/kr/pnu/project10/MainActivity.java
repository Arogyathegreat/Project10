package kr.pnu.project10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout top_left = (LinearLayout) findViewById(R.id.top_left);

        TextView text1 = (TextView) top_left.getChildAt(0);

        top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), text1.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}