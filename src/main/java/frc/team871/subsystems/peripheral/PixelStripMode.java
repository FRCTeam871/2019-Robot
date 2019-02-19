package frc.team871.subsystems.peripheral;

public enum PixelStripMode {

    DISABLED(0),
    FIRE_RED(1),
    FIRE_BLUE(2),
    BALLS(3),
    RAINBOW(4),
    BLINK_R(5),
    BLINK_G(6),
    BLINK_B(7),
    BLINK_R_FAST(8),
    BLINK_G_FAST(9),
    BLINK_B_FAST(10),
    CHASE_R(11),
    CHASE_G(12),
    CHASE_B(13),
    CHASE_R_FAST(14),
    CHASE_G_FAST(15),
    CHASE_B_FAST(16),
    PULSE_R(17),
    PULSE_G(18),
    PULSE_B(19),
    PULSE_R_FAST(20),
    PULSE_G_FAST(21),
    PULSE_B_FAST(22),
    PULSE_CHASE_R(23),
    PULSE_CHASE_G(24),
    PULSE_CHASE_B(25),
    PULSE_CHASE_R_FAST(26),
    PULSE_CHASE_G_FAST(27),
    PULSE_CHASE_B_FAST(28);

    private int index = 0;
    private PixelStripMode(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
