
package hu.tmx.j2me.morse;

import hu.tmx.j2me.morse.media.MorseCharacterPlayer;

import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.media.MediaException;
import javax.microedition.midlet.*;


public class MorseMidlet extends MIDlet implements CommandListener {

    private TextField input;
    private MorseCharacterPlayer player ;
    private final Random randomizer;
    
    public MorseMidlet() {
        this.randomizer = new Random();
    }
    
    public void startApp() {
        try {
            this.player = new MorseCharacterPlayer();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        
        Form form = new Form("Input text to be played");
  
        this.input = new TextField("input", "", 200, TextField.ANY);
        form.append(input);

        form.addCommand(new Command("play", Command.OK, 1));      
        form.addCommand(new Command("rnd", Command.HELP, 1));
        form.setCommandListener(this);
        
        Display.getDisplay(this).setCurrent(form);
        
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        switch(c.getCommandType()) {
            case Command.HELP:
                generateRandomText();
                break;
            case Command.OK:
                try {
                    player.playSequence(input.getString());
                } catch (MediaException ex) {
                	// TODO show alert instead
                    throw new RuntimeException(ex.getMessage());
                }
                break;
        }
    }
    
    private void generateRandomText() {

        Vector choices = new Vector();
        for (Enumeration e=MorseCharacterPlayer.ALPHABET.keys(); e.hasMoreElements();) {
        	choices.addElement(e.nextElement());
        }

        StringBuffer buffer = new StringBuffer();
        
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                Object letter = choices.elementAt(randomNumber(0, MorseCharacterPlayer.ALPHABET.size() - 1));
                buffer.append(letter);
            }
            buffer.append(" ");
        }
        buffer.delete(buffer.length() -1, buffer.length()); // delete trailing space
        input.setString(buffer.toString());
    }
    
    private int randomNumber(int min, int max) { 
        return randomizer.nextInt((max - min) + 1) + min;
    }
}
