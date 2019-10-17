package gr.advantage.adam.themoviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String title = (String) intent.getSerializableExtra("title");
        Toast.makeText(this,title,Toast.LENGTH_SHORT).show();
    }
}
