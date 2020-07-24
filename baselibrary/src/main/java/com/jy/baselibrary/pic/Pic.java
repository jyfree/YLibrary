package com.jy.baselibrary.pic;

/**
 * @Author Administrator
 * @Date 2019/10/17-17:26
 * @TODO
 */
public class Pic {
    private PicManager picManager;

    private Pic() {
        picManager = new PicManager();
    }

    public static Pic getInstance() {
        return PicHolder.instance;
    }

    private static class PicHolder {
        private static final Pic instance = new Pic();
    }

    public PicManager getPicManager() {
        return picManager;
    }
}
