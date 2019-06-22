package app.sounds;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class Sound {


    private Clip clip;
    public Sound(String file) throws IOException {
        try {
            File soundFile = new File(file);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new IOException(e.getMessage());
        }
    }
    public void play() {
        if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
        clip.setFramePosition(0); // rewind to the beginning
        clip.start();     // Start playing

    }


}