package com.boybook.BorderAPI;

/**
 * Created by funcraft on 2016/2/11.
 */
public enum MotionDirection {
    xX, Xx,  //X变大 变小
    zZ, Zz;   //Z变大 变小

    public double getYaw() {
        switch (this) {
            case Xx:
                return 270;
            case xX:
                return 90;
            case Zz:
                return 180;
            case zZ:
                return 0;
            default:
                return 0;
        }
    }
}
