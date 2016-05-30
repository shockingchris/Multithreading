package multi.multithread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.lang.Thread;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> adapter;
    File file;
    ProgressBar progressBar;
    private int progressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        listView = (ListView) findViewById(R.id.my_list_view);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_selectable_list_item,
                list);

        listView.setAdapter(adapter);

        file = new File(getFilesDir(), "numbers.txt");

        Button createButton = (Button) findViewById(R.id.create);
        Button loadButton = (Button) findViewById(R.id.load);
        Button clearButton = (Button) findViewById(R.id.clear);

        if (createButton != null) {
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new WriteFile().execute();
                }
            });
        }

        if (loadButton != null) {
            loadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ReadFile().execute();
                }
            });
        }

        if (clearButton != null) {
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressStatus = 0;
                    progressBar.setProgress(progressStatus);
                    list.clear();
                    adapter.clear();
                }
            });
        }
}

    private class ReadFile extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... Params) {
            try {
                InputStream is = new FileInputStream(file.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = br.readLine()) != null) {
                    list.add("Read Line: " + line);
                    progressStatus += 10;
                    publishProgress();
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        System.out.print("This won't ever happen");
                    }
                }
                br.close();
                progressStatus = 0;
                publishProgress();
            } catch (Exception ex) {
                   ex.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... Params) {
            adapter.notifyDataSetChanged();
            progressBar.setProgress(progressStatus);
        }
    }

        private class WriteFile extends AsyncTask<Void, Void, Void> {
            protected Void doInBackground(Void... Params) {
                try {
                    FileOutputStream output =
                            new FileOutputStream(file.toString());
                    String temp;
                    for (int i = 0; i < 10; i++) {
                        progressStatus += 10;
                        temp = (Integer.toString(i+1) + "\n");
                        output.write(temp.getBytes());
                        publishProgress();
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            System.out.print("This won't really happen");
                        }
                    }
                    output.close();
                    progressStatus = 0;
                    publishProgress();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            protected void onProgressUpdate(Void... Params) {
                progressBar.setProgress(progressStatus);
            }
        }
}
