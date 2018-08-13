package subreddit.android.appstore.backend.scrapers;

public class ImgSize {
    public static final int DONT_CARE = -1;
    private final int sizePixel;

    private ImgSize(int sizePixel) {
        this.sizePixel = sizePixel;
    }

    public static ImgSize dontCare() {
        return new ImgSize(DONT_CARE);
    }

    public static ImgSize px(int pixel) {
        return new ImgSize(pixel);
    }

    public int getSizePixel() {
        return sizePixel;
    }
}
