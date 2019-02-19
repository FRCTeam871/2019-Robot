package frc.team871.subsystems.peripheral;

public enum Sound {
    TAKE_ON_ME("music/aha.wav"),
    TETRIS_THEME("music/tetris.wav"),
    CRAZY_TRAIN("music/crazy.wav"),
    LUFTBALLONS("music/luft.wav"),
    VENUS("music/venus.wav"),
    ITS_STILL_ROCK_AND_ROLL_TO_ME("music/rnr.wav"),
    SWEET_DREAMS("music/dreams.wav"),
    XEVIOUS_THEME("music/xevious.wav"),
    NEVER_GONNA_GIVE_YOU_UP("music/rick.wav"),
    DK_UP("sfx/dk1.wav"),
    GALAGA_SPIDER("sfx/galaga1.wav"),
    GALAGA_CELEBRATE("sfx/galaga2.wav"),
    MARIO_JINGLE("sfx/mario1.wav"),
    XEVIOUS_BOMB("sfx/xev1.wav"),
    LEEROY_JENKINS("memes/leeroy.wav"),
    STARTUP("memes/start.wav"),
    IM_A_COMPUTER("memes/comp.wav"),
    REEE("memes/ree.wav");

    private String path;

    Sound(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
