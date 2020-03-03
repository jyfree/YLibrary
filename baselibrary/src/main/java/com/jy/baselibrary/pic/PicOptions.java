package com.jy.baselibrary.pic;

import java.io.File;

/**
 * @Author Administrator
 * @Date 2019/10/18-9:43
 * @TODO
 */
public class PicOptions {

    private boolean isCrop;
    private int cropWidth;
    private int cropHeight;
    private String providerAuthority;
    private File cacheFile;

    private PicOptions(Builder builder) {
        isCrop = builder.isCrop;
        cropWidth = builder.cropWidth;
        cropHeight = builder.cropHeight;
        providerAuthority = builder.providerAuthority;
        cacheFile = builder.cacheFile;
    }

    public boolean isCrop() {
        return isCrop;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public String getProviderAuthority() {
        return providerAuthority;
    }

    public File getCacheFile() {
        return cacheFile;
    }

    public static Builder beginBuilder() {
        return new Builder();
    }

    public static class Builder {

        private boolean isCrop = false;
        private int cropWidth = 0;
        private int cropHeight = 0;
        private String providerAuthority;
        private File cacheFile;

        public Builder setCrop(boolean crop) {
            isCrop = crop;
            return this;
        }

        public Builder setCropWidth(int cropWidth) {
            this.cropWidth = cropWidth;
            return this;
        }

        public Builder setCropHeight(int cropHeight) {
            this.cropHeight = cropHeight;
            return this;
        }

        public Builder setProviderAuthority(String providerAuthority) {
            this.providerAuthority = providerAuthority;
            return this;
        }

        public Builder setCacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public PicOptions build() {
            if (this.cacheFile == null) {
                throw new IllegalArgumentException("cacheFile no found...");
            }
            return new PicOptions(this);
        }
    }
}
