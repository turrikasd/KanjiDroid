package com.thedespite.kanjidroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Despite on 12/10/13.
 */
public class KanaMgr {

    enum SET
    {
        BASIC(true),
        K_KANA(true),
        S_KANA(true),
        T_KANA(true),
        N_KANA(true);

        private boolean enabled;
        SET(boolean enabled)
        {
            this.enabled = enabled;
        }

        public boolean isEnabled()
        {
            return enabled;
        }
    }

    public KANA_ID m_kana;

    public boolean NewKana()
    {
        // List all enabled sets
        List<SET> enabledSets = new ArrayList<SET>();
        for (int i = 0; i < SET.values().length; i++)
        {
            if (SET.values()[i].isEnabled())
                enabledSets.add(SET.values()[i]);
        }


        Random rnd = new Random();
        int offset;

        // Select random set from the enabled list
        offset = rnd.nextInt(enabledSets.size());
        SET selectedSet = enabledSets.get(offset);

        // Select a random item from the selected set
        switch (selectedSet)
        {
            case BASIC:
                offset = rnd.nextInt(KANA_ID.BASIC.values().length);
                m_kana = KANA_ID.BASIC.values()[offset].kana_id;
                break;

            case K_KANA:
                offset = rnd.nextInt(KANA_ID.K_KANA.values().length);
                m_kana = KANA_ID.K_KANA.values()[offset].kana_id;
                break;

            case S_KANA:
                offset = rnd.nextInt(KANA_ID.S_KANA.values().length);
                m_kana = KANA_ID.S_KANA.values()[offset].kana_id;
                break;

            case T_KANA:
                offset = rnd.nextInt(KANA_ID.T_KANA.values().length);
                m_kana = KANA_ID.T_KANA.values()[offset].kana_id;
                break;

            case N_KANA:
                offset = rnd.nextInt(KANA_ID.N_KANA.values().length);
                m_kana = KANA_ID.N_KANA.values()[offset].kana_id;
                break;
        }

        if (m_kana == null)
            return false;

        return true;
    }

    public String GetKanaString()
    {
        return m_kana.GetUCharInString();
    }

    public String GetRomajiString()
    {
        return m_kana.GetRomajiString();
    }

}
