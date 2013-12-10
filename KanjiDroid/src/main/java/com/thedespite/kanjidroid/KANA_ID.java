package com.thedespite.kanjidroid;

/**
 * Created by Despite on 12/10/13.
 */
public enum KANA_ID {
    a (0x3042, "a"),
    i (0x3044, "i"),
    u (0x3046, "u"),
    e (0x3048, "e"),
    o (0x304A, "o"),

    ka(0x304B, "ka"),
    ki(0x304D, "ki"),
    ku(0x304F, "ku"),
    ke(0x3051, "ke"),
    ko(0x3053, "ko"),

    sa(0x3055, "sa"),
    si(0x3057, "si"),
    su(0x3059, "su"),
    se(0x305B, "se"),
    so(0x305D, "so"),

    ta(0x305F, "ta"),
    chi(0x3061, "chi"),
    tsu(0x3064, "tsu"),
    te(0x3066, "te"),
    to(0x3068, "to"),

    na(0x306A, "na"),
    ni(0x306B, "ni"),
    nu(0x306C, "nu"),
    ne(0x306D, "ne"),
    no(0x306E, "no");


    private final int cp; // Code Point (if you must know)
    private final String romaji;
    KANA_ID(int cp, String romaji)
    {
        this.cp = cp;
        this.romaji = romaji;
    }

    public String GetUCharInString()
    {
        return new String(Character.toChars(cp));
    }

    public String GetRomajiString()
    {
        return romaji;
    }

    enum BASIC
    {
        b_a(a),
        b_i(i),
        b_u(u),
        b_e(e),
        b_o(o);

        public final KANA_ID kana_id;

        BASIC(KANA_ID kana_id)
        {
            this.kana_id = kana_id;
        }
    }

    enum K_KANA
    {
        k_ka(ka),
        k_ki(ki),
        k_ku(ku),
        k_ke(ke),
        k_ko(ko);

        public final KANA_ID kana_id;

        K_KANA(KANA_ID kana_id)
        {
            this.kana_id = kana_id;
        }
    }

    enum S_KANA
    {
        s_sa(sa),
        s_si(si),
        s_su(su),
        s_se(se),
        s_so(so);

        public final KANA_ID kana_id;

        S_KANA(KANA_ID kana_id)
        {
            this.kana_id = kana_id;
        }
    }

    enum T_KANA
    {
        t_ta(ta),
        t_chi(chi),
        t_tsu(tsu),
        t_te(te),
        t_to(to);

        public final KANA_ID kana_id;

        T_KANA(KANA_ID kana_id)
        {
            this.kana_id = kana_id;
        }
    }

    enum N_KANA
    {
        n_na(na),
        n_ni(ni),
        n_nu(nu),
        n_ne(ne),
        n_no(no);

        public final KANA_ID kana_id;

        N_KANA(KANA_ID kana_id)
        {
            this.kana_id = kana_id;
        }
    }
}
