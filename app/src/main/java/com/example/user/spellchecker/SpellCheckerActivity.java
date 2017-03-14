package com.example.user.spellchecker;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener;
import android.widget.TextView;
import java.lang.StringBuilder;
import java.util.Locale;


public class SpellCheckerActivity extends AppCompatActivity implements SpellCheckerSessionListener {
    private static final String TAG = SpellCheckerActivity.class.getSimpleName();
    private static final int NOT_A_LENGTH = -1;
    private TextView mMainView;
    private SpellCheckerSession mScs;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mMainView = (TextView)findViewById(R.id.main);
    }
    private boolean isSentenceSpellCheckSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
    @Override
    public void onResume() {
        super.onResume();
        final TextServicesManager tsm = (TextServicesManager) getSystemService(
                Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = tsm.newSpellCheckerSession(null, Locale.ENGLISH, this, true);
        if (mScs != null) {
            // Instantiate TextInfo for each query
            // TextInfo can be passed a sequence number and a cookie number to identify the result
            if (isSentenceSpellCheckSupported()) {
                // Note that getSentenceSuggestions works on JB or later.
                Log.d(TAG, "Sentence spellchecking supported.");
                mScs.getSentenceSuggestions(new TextInfo[] {new TextInfo("cuanve")}, 3);
            } else {
                // Note that getSuggestions() is a deprecated API.
                // It is recommended for an application running on Jelly Bean or later
                // to call getSentenceSuggestions() only.
                mScs.getSuggestions(new TextInfo("tgis"), 3);
                mScs.getSuggestions(new TextInfo("hllo"), 3);
                mScs.getSuggestions(new TextInfo("helloworld"), 3);
            }
        } else {
            Log.e(TAG, "Couldn't obtain the spell checker service.");
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mScs != null) {
            mScs.close();
        }
    }
    private void dumpSuggestionsInfoInternal(
            final StringBuilder sb, final SuggestionsInfo si, final int length, final int offset) {
        // Returned suggestions are contained in SuggestionsInfo
        final int len = si.getSuggestionsCount();
        sb.append('\n');
        for (int j = 0; j < len; ++j) {
            if (j != 0) {
                sb.append(", ");
            }
            sb.append(si.getSuggestionAt(j));
        }
        sb.append(" (" + len + ")");
        if (length != NOT_A_LENGTH) {
            sb.append(" length = " + length + ", offset = " + offset);
        }
    }
    /**
     * Callback for {@link SpellCheckerSession#getSuggestions(TextInfo, int)}
     * and {@link SpellCheckerSession#getSuggestions(TextInfo[], int, boolean)}
     * These results are suggestions for {@link TextInfo}s queried by
     * {@link SpellCheckerSession#getSuggestions(TextInfo, int)} or
     * {@link SpellCheckerSession#getSuggestions(TextInfo[], int, boolean)}
     */
    @Override
    public void onGetSuggestions(final SuggestionsInfo[] arg0) {
        Log.d(TAG, "onGetSuggestions");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arg0.length; ++i) {
            dumpSuggestionsInfoInternal(sb, arg0[i], 0, NOT_A_LENGTH);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainView.append(sb.toString());
            }
        });
    }
    /**
     * Callback for {@link SpellCheckerSession#getSentenceSuggestions(TextInfo[], int)}
     * These results are suggestions for {@link TextInfo}s
     * queried by {@link SpellCheckerSession#getSentenceSuggestions(TextInfo[], int)}.
     */
    @Override
    public void onGetSentenceSuggestions(final SentenceSuggestionsInfo[] arg0) {
        if (!isSentenceSpellCheckSupported()) {
            Log.e(TAG, "Sentence spell check is not supported on this platform, "
                    + "but accidentially called.");
            return;
        }
        Log.d(TAG, "onGetSentenceSuggestions");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arg0.length; ++i) {
            final SentenceSuggestionsInfo ssi = arg0[i];
            for (int j = 0; j < ssi.getSuggestionsCount(); ++j) {
                dumpSuggestionsInfoInternal(
                        sb, ssi.getSuggestionsInfoAt(j), ssi.getOffsetAt(j), ssi.getLengthAt(j));
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainView.append(sb.toString());
            }
        });
    }
}