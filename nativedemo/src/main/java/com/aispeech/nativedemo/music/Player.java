package com.aispeech.nativedemo.music;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.aispeech.nativedemo.DuiApplication;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Player {

    private static final String TAG = "MusicPlayer";

    private List<Music> musics;

    private MediaPlayer mPlayer;

    private Music currentMusic;

    private int mSize;

    private int currentIndex = 0;

    private int PLAY_MODE = 0;

    private static Player sInstance;

    private int currentDuration = 0;
    private int currentPosition = 0;
    private OnFinishListener onFinishListener;

    public static synchronized Player getInstance() {
        if (sInstance == null) {
            sInstance = new Player();
        }
        return sInstance;
    }

    interface PlayMode {

        /**
         * 顺序播放
         */
        int PLAY_MODE_LIST = 0;

        /**
         * 列表循环
         */
        int PLAY_MODE_REPEAT_LIST = 1;

        /**
         * 单曲循环
         */
        int PLAY_MODE_REPEAT_ONE = 2;

        /**
         * 随机播放
         */
        int PLAY_MODE_SHUFFLE = 3;
    }

    private Player() {
        mPlayer = new MediaPlayer();
    }

    public void init(List<Music> musics) {
        this.musics = musics;
        for (Music m : this.musics) {
            m.setFavorite(LocalMusicSp.get().isFavorite(m.getTitle()));
        }
        PLAY_MODE = LocalMusicSp.get().getInt("playMode");
        mSize = musics.size();
        currentMusic = musics.get(0);
        currentIndex = 0;
        mPlayer.setOnCompletionListener(mp -> {
            if (currentPosition / 1000 - currentDuration / 1000 <= 1) {
                switch (PLAY_MODE) {
                    case PlayMode.PLAY_MODE_LIST:
                        if (currentIndex == mSize - 1) {
                            if (onFinishListener != null) {
                                onFinishListener.onFinish();
                            } else {
                                mPlayer.stop();
                                mPlayer.reset();
                            }
                            return;
                        } else {
                            currentIndex++;
                        }
                        break;
                    case PlayMode.PLAY_MODE_REPEAT_LIST:
                        if (currentIndex == mSize - 1) {
                            currentIndex = 0;
                        } else {
                            currentIndex++;
                        }
                        break;
                    case PlayMode.PLAY_MODE_REPEAT_ONE:
                        break;
                    case PlayMode.PLAY_MODE_SHUFFLE:
                        int tempIndex = currentIndex;
                        int index = new Random().nextInt(mSize);
                        while (index == tempIndex) {
                            index = new Random().nextInt(mSize);
                        }
                        currentIndex = index;
                        break;
                    default:
                }
                currentMusic = musics.get(currentIndex);
            }
            mPlayer.reset();
            setAndPrepared();
        });
        mPlayer.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(DuiApplication.getContext(), "抱歉，当前资源找不到，即将播放下一个", Toast.LENGTH_LONG).show();
            Log.e(TAG, "onError: " + what + "," + extra);
            next();
            return false;
        });
        setAndPrepared();
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        mPlayer.setOnPreparedListener(mp -> {
            currentDuration = mp.getDuration();
            onPreparedListener.onPrepared(mp);
        });
    }

    public int getDuration() {
        return currentDuration;
    }

    public int getCurrentDuration() {
        try {
            if (mPlayer != null) {
                currentPosition = mPlayer.getCurrentPosition();
            }
        } catch (Exception e) {
            return 0;
        }
        return currentPosition;
    }

    public int getProgress() {
        try {
            if (mPlayer != null) {
                return mPlayer.getCurrentPosition() / 1000;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }


    public Music getCurrentMusic() {
        return currentMusic;
    }

    public void seekTo(int progress) {
        mPlayer.seekTo(progress);
    }

    public int getSize() {
        return mSize;
    }

    public boolean start() {
        if (mPlayer != null) {
            mPlayer.start();
            return true;
        } else {
            return false;
        }
    }

    public void playByIndex(int index) {
        Log.d(TAG, "playByIndex: " + index);
        currentIndex = index;
        currentMusic = musics.get(currentIndex);
        if (mPlayer != null) {
            mPlayer.pause();
            mPlayer.reset();
            setAndPrepared();
        }
    }

    public boolean stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            sInstance = null;
            return true;
        } else {
            return false;
        }
    }

    private void save(Music title) {
        LocalMusicSp.get().favorite(title);
    }

    private void remove(Music title) {
        LocalMusicSp.get().unFavorite(title);
    }

    public boolean rePlay() {
        if (mPlayer != null) {
            mPlayer.seekTo(0);
            return true;
        } else {
            return false;
        }
    }

    public boolean pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            return true;
        } else {
            return false;
        }
    }

    public boolean resume() {
        if (mPlayer != null) {
            mPlayer.start();
            return true;
        } else {
            return false;
        }
    }

    public void setPlayMode(int mode) {
        PLAY_MODE = mode;
        LocalMusicSp.get().put("playMode", PLAY_MODE);
    }

    public int getPlayMode() {
        return PLAY_MODE;
    }

    public boolean prev() {
        if (mPlayer != null) {
            switch (PLAY_MODE) {
                case PlayMode.PLAY_MODE_LIST:
                    if (currentIndex == 0) {
                        return false;
                    } else {
                        currentIndex--;
                    }
                    break;
                case PlayMode.PLAY_MODE_REPEAT_ONE:
                case PlayMode.PLAY_MODE_REPEAT_LIST:
                    if (currentIndex == 0) {
                        currentIndex = mSize - 1;
                    } else {
                        currentIndex--;
                    }
                    break;
                case PlayMode.PLAY_MODE_SHUFFLE:
                    int tempIndex = currentIndex;
                    int index = new Random().nextInt(mSize);
                    while (index == tempIndex) {
                        index = new Random().nextInt(mSize);
                    }
                    currentIndex = index;
                    break;
                default:
            }
            mPlayer.pause();
            mPlayer.reset();
            currentMusic = musics.get(currentIndex);
            setAndPrepared();
            return true;
        } else {
            return false;
        }
    }

    public boolean next() {
        if (mPlayer != null) {
            switch (PLAY_MODE) {
                case PlayMode.PLAY_MODE_LIST:
                    if (currentIndex == mSize - 1) {
                        return false;
                    } else {
                        currentIndex++;
                    }
                    break;
                case PlayMode.PLAY_MODE_REPEAT_ONE:
                case PlayMode.PLAY_MODE_REPEAT_LIST:
                    if (currentIndex == mSize - 1) {
                        currentIndex = 0;
                    } else {
                        currentIndex++;
                    }
                    break;
                case PlayMode.PLAY_MODE_SHUFFLE:
                    int tempIndex = currentIndex;
                    int index = new Random().nextInt(mSize);
                    while (index == tempIndex) {
                        index = new Random().nextInt(mSize);
                    }
                    currentIndex = index;
                    break;
                default:
            }
            mPlayer.pause();
            mPlayer.reset();
            currentMusic = musics.get(currentIndex);
            setAndPrepared();
            return true;
        } else {
            return false;
        }
    }

    private void setAndPrepared() {
        try {
            mPlayer.setDataSource(currentMusic.getLinkUrl());
            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public List<Music> getMusicList() {
        return musics;
    }

    public boolean favorite() {
        if (currentMusic != null) {
            currentMusic.setFavorite(true);
            save(currentMusic);
            return true;
        } else {
            return false;
        }
    }

    public boolean fastForward(int relativeTime, int absoluteTime) {
        Log.d(TAG, "fastForward: " + relativeTime + "," + absoluteTime);
        relativeTime = relativeTime * 1000;
        absoluteTime = absoluteTime * 1000;
        if (mPlayer != null) {
            int duration = mPlayer.getDuration();
            int position = mPlayer.getCurrentPosition();
            if (absoluteTime != 0) {
                mPlayer.seekTo(absoluteTime);
                return true;
            } else if (relativeTime != 0) {
                if (relativeTime + position > duration) {
                    return false;
                } else {
                    mPlayer.seekTo(relativeTime + position);
                    return true;
                }
            } else {
                if (position + 10 > duration) {
                    return false;
                } else {
                    mPlayer.seekTo(position + 10000);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public boolean backForward(int relativeTime, int absoluteTime) {
        Log.d(TAG, "backForward: " + relativeTime + "," + absoluteTime);
        relativeTime = relativeTime * 1000;
        absoluteTime = absoluteTime * 1000;
        if (mPlayer != null) {
            int duration = mPlayer.getDuration();
            int position = mPlayer.getCurrentPosition();
            if (absoluteTime != 0) {
                mPlayer.seekTo(absoluteTime);
                return true;
            } else if (relativeTime != 0) {
                if (position - relativeTime < 0) {
                    mPlayer.seekTo(0);
                    return false;
                } else {
                    mPlayer.seekTo(position - relativeTime);
                    return true;
                }
            } else {
                if (position - 10 < 0) {
                    return false;
                } else {
                    mPlayer.seekTo(position - 10000);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public boolean unFavorite() {
        if (currentMusic != null) {
            currentMusic.setFavorite(false);
            remove(currentMusic);
            return true;
        } else {
            return false;
        }
    }

    public boolean isFavorite() {
        return currentMusic.isFavorite();
    }

    public static String formatDuration(int duration) {
        // milliseconds into seconds
        duration /= 1000;
        int minute = duration / 60;
        int hour = minute / 60;
        minute %= 60;
        int second = duration % 60;
        if (hour != 0) {
            return String.format(Locale.CHINA, "%2d:%02d:%02d", hour, minute, second);
        } else {
            return String.format(Locale.CHINA, "%02d:%02d", minute, second);
        }
    }

    public static int getIntTime(String time) {
        try {
            String[] my = time.split(":");
            if (my.length == 3) {
                int hour = Integer.parseInt(my[0]);
                int min = Integer.parseInt(my[1]);
                int sec = Integer.parseInt(my[2]);
                return hour * 3600 + min * 60 + sec;
            } else if (my.length == 2) {
                int min = Integer.parseInt(my[0]);
                int sec = Integer.parseInt(my[1]);
                return min * 60 + sec;
            } else {
                return Integer.parseInt(my[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.onFinishListener = listener;
    }

    public interface OnFinishListener {
        void onFinish();
    }
}
