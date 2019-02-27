package com.gameex.dw.mcapplication.interFace;

public interface MusicPlayInterface {
    void playMusic(int position);
    void pauseMusic();
    void continueMusic();
    void seekTo(int progress);
    boolean isPlaying();
}
