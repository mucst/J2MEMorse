
package hu.tmx.j2me.morse.media;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.ToneControl;


public class ToneDeviceMorseSequencePlayer implements MorseSequencePlayer {
    
    private final ToneControl tc;
    private final Player p;
    private byte tempo = 60; // TODO make it possible to change this runtime
   
    public ToneDeviceMorseSequencePlayer() throws IOException, MediaException {
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
        String letterCode = (String) Constants.ALPHABET.get(String.valueOf(c).toUpperCase());
        
        if (letterCode == null) {
            if (c == ' ') {
                buffer.addElement(new Byte(ToneControl.SILENCE));
                buffer.addElement(new Byte((byte) Constants.TAH_DURATION));
            } else {
                // unknown character
                buffer.addElement(new Byte((byte) Constants.PITCH_UNKNOWN)); 
                buffer.addElement(new Byte((byte) Constants.TAH_DURATION)); 
            }
            
            
            
        } else {
            for (int i = 0; i < letterCode.length(); i++) {

                char beepLength = letterCode.charAt(i);
                buffer.addElement(new Byte((byte) Constants.PITCH)); 
                switch (beepLength) {
                    case Constants.MORSE_LONG_BEEP:
                        buffer.addElement(new Byte((byte) Constants.TAH_DURATION)); 
                        break;
                    case Constants.MORSE_SHORT_BEEP:
                        buffer.addElement(new Byte((byte) Constants.TIH_DURATION)); 
                        break;
                }

                // add short pause after each beep (tih / lenth of 1/8)
                buffer.addElement(new Byte(ToneControl.SILENCE));
                buffer.addElement(new Byte((byte) Constants.TIH_DURATION));
            }
        }

        // tah pause after letter
        buffer.addElement(new Byte(ToneControl.SILENCE));
        buffer.addElement(new Byte((byte) Constants.TAH_DURATION));
      
        return buffer;
    }
    
}
