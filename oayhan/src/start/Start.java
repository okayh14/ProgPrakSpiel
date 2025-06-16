package start;

import gui.GameWindow;
import javax.sound.sampled.*;
import java.io.InputStream;

public class Start {
    public static void main(String[] args) {
        try {
            
            InputStream audioSrc = Start.class.getResourceAsStream("HxH.wav");
            

            // AudioInputStream vorbereiten
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);

            // Clip erzeugen
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Starten
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); 

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Danach dein Spiel-Fenster öffnen
        new GameWindow();
    }
}

