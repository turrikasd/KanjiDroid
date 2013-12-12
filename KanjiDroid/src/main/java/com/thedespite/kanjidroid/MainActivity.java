package com.thedespite.kanjidroid;

import android.content.res.Configuration;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends ActionBarActivity {

    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new KanaPracticeFragment())
                    .commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
            setContentView(R.layout.activity_main);
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
    public static class KanaPracticeFragment extends Fragment{

        private View rootView;
        final Handler delayHandler = new Handler();

        private KanaMgr m_kanaMgr;

        private TextView debugLine;
        private TextView kana;
        private EditText userEntry;
        private TextView messageUser;
        private AdView topAdd;
        private InterstitialAd interstitial;
        private int hintCount;

        public KanaPracticeFragment() {
        }

        private void AssignReferences()
        {
            debugLine = (TextView)mainActivity.findViewById(R.id.tvDebug);
            kana = (TextView)mainActivity.findViewById(R.id.kana);
            userEntry = (EditText)mainActivity.findViewById(R.id.userEntry);
            messageUser = (TextView)mainActivity.findViewById(R.id.tvMessageUser);
            topAdd = (AdView)mainActivity.findViewById(R.id.adTopAdd);
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

                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
            hintCount++;

            if (hintCount >= 5 && interstitial.isLoaded())
            {
                interstitial.show();
                RequestNewInterstitial();
                hintCount = 0;
            }

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
            hintCount = 0;

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

            m_kanaMgr = new KanaMgr();
            GenerateNewKana();

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("0A446759ABA7D31D664B90B362EF800C") // S4
                    .build();
            topAdd.loadAd(adRequest);

            // Handle Interstitial ad loading
            RequestNewInterstitial();
        }

        private void RequestNewInterstitial()
        {
            interstitial = new InterstitialAd(mainActivity);
            interstitial.setAdUnitId("ca-app-pub-5939868031369014/1874921489");

            AdRequest iAdReq = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("0A446759ABA7D31D664B90B362EF800C") // S4
                    .build();

            interstitial.loadAd(iAdReq);
        }
    }
}
