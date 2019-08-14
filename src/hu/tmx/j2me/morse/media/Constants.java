
package hu.tmx.j2me.morse.media;

import java.util.Hashtable;

public final class Constants {
    
    public static final Hashtable ALPHABET;
    
    static {
        ALPHABET = new Hashtable(36);
        ALPHABET.put("A", "13");
        ALPHABET.put("B", "3111");
        ALPHABET.put("C", "3131");
        ALPHABET.put("D", "311");
        ALPHABET.put("E", "1");
        ALPHABET.put("F", "1131");
        ALPHABET.put("G", "331");
        ALPHABET.put("H", "1111");
        ALPHABET.put("I", "11");
        ALPHABET.put("J", "1333");
        ALPHABET.put("K", "313");
        ALPHABET.put("L", "1311");
        ALPHABET.put("M", "33");
        ALPHABET.put("N", "31");
        ALPHABET.put("O", "333");
        ALPHABET.put("P", "1331");
        ALPHABET.put("Q", "3313");
        ALPHABET.put("R", "131");
        ALPHABET.put("S", "111");
        ALPHABET.put("T", "3");
        ALPHABET.put("U", "113");
        ALPHABET.put("V", "1113");
        ALPHABET.put("W", "133");
        ALPHABET.put("X", "3113");
        ALPHABET.put("Y", "3133");
        ALPHABET.put("Z", "3311");
        ALPHABET.put("0", "33333");
        ALPHABET.put("1", "13333");
        ALPHABET.put("2", "11333");
        ALPHABET.put("3", "11133");
        ALPHABET.put("4", "11113");
        ALPHABET.put("5", "11111");
        ALPHABET.put("6", "31111");
        ALPHABET.put("7", "33111");
        ALPHABET.put("8", "33311");
        ALPHABET.put("9", "33331");
    }

    static final byte TAH_DURATION = 16;
    static final byte TIH_DURATION = 8;
    static final byte PITCH = 81;
    static final byte PITCH_UNKNOWN = 50;
    static final char MORSE_LONG_BEEP = '3';
    static final char MORSE_SHORT_BEEP = '1';
    
    private Constants() {
        throw new RuntimeException();
    }

}
