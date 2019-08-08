package com.aispeech.nativedemo.music;


public class Music {

    /**
     * subTitle : 张学友
     * extra : {"resType":"mp3","source":0,"origintitle":"吻别"}
     * imageUrl : http://47.98.45.59/c/6460.jpg
     * linkUrl : http://47.98.45.59/34040665.mp3
     * label :
     * title : 吻别
     */

    //for music
    private String subTitle;
    private Extra extra;
    private String imageUrl;
    private String linkUrl;
    private String label;
    private String title;
    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class Extra {
        /**
         * resType : mp3
         * source : 0
         * origintitle : 吻别
         */

        private String resType;
        private String source;
        private String origintitle;

        public String getResType() {
            return resType;
        }

        public void setResType(String resType) {
            this.resType = resType;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getOrigintitle() {
            return origintitle;
        }

        public void setOrigintitle(String origintitle) {
            this.origintitle = origintitle;
        }

        @Override
        public String toString() {
            return "Extra{" +
                    "resType='" + resType + '\'' +
                    ", source='" + source + '\'' +
                    ", origintitle='" + origintitle + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "Music{" +
                "subTitle='" + subTitle + '\'' +
                ", extra=" + extra +
                ", imageUrl='" + imageUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", label='" + label + '\'' +
                ", title='" + title + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
