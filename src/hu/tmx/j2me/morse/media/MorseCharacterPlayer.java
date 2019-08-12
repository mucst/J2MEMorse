/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.tmx.j2me.morse.media;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.ToneControl;

/**
 *
 * @author tmucs
 */
public class MorseCharacterPlayer {
    
    
	public static final Hashtable ALPHABET;
    
    private static final byte TAH_DURATION = 16;
    private static final byte TIH_DURATION = 8;
    private static final byte PITCH = 81;
    private static final byte PITCH_UNKNOWN = 50;
    private static final char MORSE_LONG_BEEP = '3';
    private static final char MORSE_SHORT_BEEP = '1';
    
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
    
    private final ToneControl tc;
    private final Player p;
    private byte tempo = 60; // TODO make it possible to change this runtime
   
    public MorseCharacterPlayer() throws IOException, MediaException {
        this.p = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
        this.p.realize();
        p.addPlayerListener(new PlayerListener() {

            public void playerUpdate(Player player, String event, Object eventData) {
                if (event.equals(PlayerListener.END_OF_MEDIA)) {
                    player.deallocate();
                }
            }
        });
        
        
        this.tc = (ToneControl) p.getControl("ToneControl");
    }

    
    
    public void playSequence(String input) throws MediaException {
        if (p.getState() == Player.STARTED) {
            p.stop();
            p.deallocate();
        }
        p.realize();
        
        this.tc.setSequence(stringToToneSequence(input));
        this.p.start();
    }
 
    private byte[] stringToToneSequence(String input) {
        Vector buffer = new Vector();

        buffer.addElement(new Byte(ToneControl.VERSION));
        buffer.addElement(new Byte((byte) 1));

        buffer.addElement(new Byte(ToneControl.TEMPO));
        buffer.addElement(new Byte(tempo));

        buffer.addElement(new Byte(ToneControl.BLOCK_START));
        buffer.addElement(new Byte((byte) 0));
        
        for (int i=0; i<input.length(); i++) {
            char letter = input.charAt(i);
            Vector bytes = characterToBytes(letter);
            for(Enumeration e = bytes.elements(); e.hasMoreElements();) {
                buffer.addElement(e.nextElement());
            } 
        }
        
        buffer.addElement(new Byte(ToneControl.BLOCK_END));
        buffer.addElement(new Byte((byte) 0));

        buffer.addElement(new Byte(ToneControl.PLAY_BLOCK));
        buffer.addElement(new Byte((byte) 0));
        
        // copy from Vector of Byte's into array of byte's (unbox)
        byte[] toneSequence = new byte[buffer.size()];
        for (int i = 0; i < buffer.size(); i++) {
            Byte b = (Byte) buffer.elementAt(i);
            toneSequence[i] = b.byteValue();
        }

        return toneSequence;
        
        
    }
    
    private Vector characterToBytes(char c) {
        
        Vector buffer = new Vector();
        
        // actual beep-boops
        String letterCode = (String) ALPHABET.get(String.valueOf(c).toUpperCase());
        
        if (letterCode == null) {
            if (c == ' ') {
                buffer.addElement(new Byte(ToneControl.SILENCE));
                buffer.addElement(new Byte((byte) TAH_DURATION));
            } else {
                // unknown character
                buffer.addElement(new Byte((byte) PITCH_UNKNOWN)); 
                buffer.addElement(new Byte((byte) TAH_DURATION)); 
            }
            
            
            
        } else {
            for (int i = 0; i < letterCode.length(); i++) {

                char beepLength = letterCode.charAt(i);
                buffer.addElement(new Byte((byte) PITCH)); // tone is constant
                switch (beepLength) {
                    case MORSE_LONG_BEEP:
                        buffer.addElement(new Byte((byte) TAH_DURATION)); // 1/4 "tah"
                        break;
                    case MORSE_SHORT_BEEP:
                        buffer.addElement(new Byte((byte) TIH_DURATION)); // 1/8 "tih"
                        break;
                }

                // add short pause after each beep (tih / lenth of 1/8)
                buffer.addElement(new Byte(ToneControl.SILENCE));
                buffer.addElement(new Byte((byte) TIH_DURATION));
            }
        }

        // tah pause after letter
        buffer.addElement(new Byte(ToneControl.SILENCE));
        buffer.addElement(new Byte((byte) TAH_DURATION));
      
        return buffer;
    }
    
}
