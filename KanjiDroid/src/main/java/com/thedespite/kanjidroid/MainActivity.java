package com.thedespite.kanjidroid;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private KanaMgr m_kanaMgr;

    private TextView debugLine;
    private TextView kana;
    private EditText userEntry;
    private TextView messageUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new KanaPracticeFragment())
                    .commit();
        }

        m_kanaMgr = new KanaMgr();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class KanaPracticeFragment extends Fragment{

        private View rootView;
        final Handler delayHandler = new Handler();

        public KanaPracticeFragment() {
        }

        private void AssignReferences()
        {
            debugLine = (TextView)findViewById(R.id.tvDebug);
            kana = (TextView)findViewById(R.id.kana);
            userEntry = (EditText)findViewById(R.id.userEntry);
            messageUser = (TextView)findViewById(R.id.tvMessageUser);
        }

        private void GenerateNewKana()
        {
            m_kanaMgr.NewKana();

            if (kana != null)
                kana.setText(m_kanaMgr.GetKanaString());
        }

        private void ValidateInput(Editable e)
        {
            if (e.toString().toLowerCase().equals(m_kanaMgr.GetRomajiString()))
            {
                rootView.setBackgroundColor(Color.argb(15, 0, 255, 0));
                userEntry.setTextColor(Color.argb(255, 0, 255, 0));

                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try {
                            Thread.sleep(500);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                rootView.setBackgroundColor(Color.TRANSPARENT);
                                userEntry.setTextColor(Color.BLACK);

                                GenerateNewKana();
                                userEntry.setText("");
                                messageUser.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).start();

                return;
            }

            if(e.length() >= m_kanaMgr.GetRomajiString().length())
            {
                rootView.setBackgroundColor(Color.argb(15, 255, 0, 0));
                userEntry.setTextColor(Color.RED);
            }

            else
            {
                rootView.setBackgroundColor(Color.TRANSPARENT);
                userEntry.setTextColor(Color.BLACK);
            }

            // Handle special cases
            if (e.toString().toLowerCase().equals("ti") && m_kanaMgr.m_kana == KANA_ID.chi)
            {
                messageUser.setText("This romaji differs from the table!");
                messageUser.setVisibility(View.VISIBLE);

                // Hide message after delay
                delayHandler.postDelayed(new Runnable(){

                    @Override
                    public void run()
                    {
                        messageUser.setVisibility(View.INVISIBLE);
                    }
                }, 3000);

            }

            if (e.toString().toLowerCase().equals("tu") && m_kanaMgr.m_kana == KANA_ID.tsu)
            {
                messageUser.setText("This romaji differs from the table!");
                messageUser.setVisibility(View.VISIBLE);

                // Hide message after delay
                delayHandler.postDelayed(new Runnable(){

                    @Override
                    public void run()
                    {
                        messageUser.setVisibility(View.INVISIBLE);
                    }
                }, 3000);

            }
        }

        private void DisplayAnswer()
        {
            messageUser.setText(m_kanaMgr.GetRomajiString());
            messageUser.setVisibility(View.VISIBLE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            AssignReferences();


            userEntry.setInputType(InputType.TYPE_NULL);
            userEntry.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ValidateInput(s);
                }
            });

            kana.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DisplayAnswer();
                }
            });

            GenerateNewKana();
        }
    }
}
