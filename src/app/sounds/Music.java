package app.sounds;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;




public enum Music {
    SONG("res\\music\\mainMenuMusic.wav"),
    SONG2("res\\music\\breakingBad.wav"),
    SONG3("res\\music\\action1.wav"),
    SONG4("res\\music\\medeval.wav");

    private static ArrayList<Music> pickable = new ArrayList<Music>();
    private static Music current = null;

    // Each sound effect has its own clip, loaded with its own sound file.
    private Clip clip;

    // Constructor to construct each element of the enum with its own sound file.
    Music(String soundFileName) {

        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void updateLoop(){
        //refill pickables if they have all been played.
        if(pickable.isEmpty()){
            System.out.println("chacha");
            pickable.add(Music.SONG2);
            pickable.add(Music.SONG3);
            pickable.add(Music.SONG4);
        }
        //pick a new song if need be and remove it from pickables.
        if(current==null || current.hasEnded()){
            Random rand = new Random();
            int index = rand.nextInt(pickable.size());
            current = pickable.get(index);
            pickable.remove(current);
            current.play();
        }

    }
    public static void updateMenuLoop(){
        //refill pickables if they have all been played.
        if(pickable.isEmpty()){
            pickable.add(Music.SONG);
        }
        //pick a new song if need be and remove it from pickables.
        if(current==null || current.hasEnded()){
            Random rand = new Random();
            int index = rand.nextInt(pickable.size());
            current = pickable.get(index);
            pickable.remove(current);
            current.play();
        }

    }
    public static void stopMusic(){
        current.stop();
        current = null;
    }

    private void play() {

        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();

    }
    private boolean hasEnded() {
        return clip.getFramePosition()>=clip.getFrameLength();
    }
    private void playIfFinished() {

        if (clip.getFramePosition()>=clip.getFrameLength()) {
            clip.setFramePosition(0);
            clip.start(); // Start playing

        }

    }
    private void stop() {
        if (clip.isRunning())
            clip.stop();
    }

    public static void init() {
        values();
    }
}

