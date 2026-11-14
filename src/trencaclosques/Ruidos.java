package trencaclosques;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
public class Ruidos {
    private static Clip moveClip;
    private static Clip victoryClip;
    static {
        try {
            // Carga los WAV desde resources (por ejemplo en /src/main/resources/sounds/)
            moveClip    = loadClip("/ruidos/move.aiff");
            victoryClip = loadClip("/ruidos/victory.wav");
        } catch (Exception e) {
            System.err.println("Error carregant renous: " + e.getMessage());
        }
    }
    private static Clip loadClip(String path) throws UnsupportedAudioFileException,
                                                    IOException,
                                                    LineUnavailableException {
        URL url = Ruidos.class.getResource(path);
        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        return clip;
    }
    public static void playMove() {
        if (moveClip == null) return;
        if (moveClip.isRunning()) moveClip.stop();
        moveClip.setFramePosition(0);
        moveClip.start();
    }
    public static void playVictory() {
        if (victoryClip == null) return;
        if (victoryClip.isRunning()) victoryClip.stop();
        victoryClip.setFramePosition(0);
        victoryClip.start();
    }
}
