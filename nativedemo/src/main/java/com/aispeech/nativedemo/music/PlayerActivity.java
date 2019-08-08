package com.aispeech.nativedemo.music;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aispeech.nativedemo.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";

    @BindView(R.id.playerType)
    protected AppCompatImageView playerType;
    @BindView(R.id.playerPause)
    protected AppCompatImageView playerPause;
    @BindView(R.id.playerPre)
    protected AppCompatImageView playerPre;
    @BindView(R.id.playerNext)
    protected AppCompatImageView playerNext;
    @BindView(R.id.playerFavorite)
    protected AppCompatImageView playerCollect;

    @BindView(R.id.playerList)
    protected AppCompatImageView playerList;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;

    @BindView(R.id.playerName)
    protected TextView playerName;

    @BindView(R.id.playerArtist)
    protected TextView playerArtist;

    @BindView(R.id.playerAlbum)
    protected ImageView playerAlbum;

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @BindView(R.id.playerProgress)
    TextView playerProgress;

    @BindView(R.id.playerDuration)
    TextView playerDuration;

    @BindView(R.id.playerSeekBar)
    AppCompatSeekBar seekBar;

    private List<Music> musicList;

    private Gson mGson;

    private Player player;

    private DetailAdapter adapter;

    private Disposable timer;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String data = intent.getStringExtra("data");
        musicList = loadMusic(data);
        if (player != null) {
            player.stop();
            player = null;
        }
        init();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.player_main);
        ButterKnife.bind(this);
        mGson = new Gson();
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        musicList = loadMusic(data);
        init();
    }

    private void init() {
        if (musicList != null && musicList.size() > 0) {
            player = Player.getInstance();
            player.init(musicList);
            player.setOnPreparedListener(mp -> {
                if (mp != null) {
                    mp.start();
                }
                if (!TextUtils.isEmpty(player.getCurrentMusic().getImageUrl())) {
                    Observable.just("")
                            .observeOn(Schedulers.io())
                            .subscribe(o -> {
                                Bitmap bitmap = Picasso.get().load(player.getCurrentMusic().getImageUrl()).get();
                                runOnUiThread(() -> playerAlbum.setImageBitmap(bitmap));
                            }, throwable -> Log.e(TAG, throwable.getLocalizedMessage(), throwable));
                } else {
                    playerAlbum.setImageResource(R.drawable.default_record_album);
                }
                playerName.setText(player.getCurrentMusic().getTitle());
                if (!TextUtils.isEmpty(player.getCurrentMusic().getSubTitle())) {
                    playerArtist.setText(player.getCurrentMusic().getSubTitle());
                } else {
                    playerArtist.setText("");
                }
                playerPause.setImageResource(R.drawable.ic_pause);
                timer = Observable.interval(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(__ -> {
                            if (player != null) {
                                int d = player.getProgress();
                                String duration = Player.formatDuration(player.getCurrentDuration());
                                playerProgress.setText(duration);
                                seekBar.setProgress(d);
                            }
                        });
                playerDuration.setText(Player.formatDuration(player.getDuration()));
                seekBar.setMax(player.getDuration() / 1000);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            player.seekTo(progress * 1000);
                            playerProgress.setText(Player.formatDuration(player.getCurrentDuration()));
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                if (LocalMusicSp.get().isFavorite(player.getCurrentMusic().getTitle())) {
                    playerCollect.setImageResource(R.drawable.ic_favorite_yes);
                } else {
                    playerCollect.setImageResource(R.drawable.ic_favorite_no);
                }
                switch (LocalMusicSp.get().getInt("playMode")) {
                    case Player.PlayMode.PLAY_MODE_LIST:
                        playerType.setImageResource(R.drawable.ic_play_mode_list);
                        break;
                    case Player.PlayMode.PLAY_MODE_REPEAT_LIST:
                        playerType.setImageResource(R.drawable.ic_play_mode_loop);
                        break;
                    case Player.PlayMode.PLAY_MODE_REPEAT_ONE:
                        playerType.setImageResource(R.drawable.ic_play_mode_single);
                        break;
                    case Player.PlayMode.PLAY_MODE_SHUFFLE:
                        playerType.setImageResource(R.drawable.ic_play_mode_shuffle);
                        break;
                        default:
                }
            });
            adapter = new DetailAdapter(musicList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
            Log.d(TAG, "initComplete");
        }
    }

    private List<Music> loadMusic(String data) {
        try {
            Log.d(TAG, "loadMusic: " + data);
            JSONObject object = new JSONObject(data);
            Music[] musics = mGson.fromJson(object.optJSONArray("content").toString(), Music[].class);
            return Arrays.asList(musics);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @OnClick(R.id.playerType)
    public void playerTypeClick() {
        int mode = player.getPlayMode();
        switch (mode) {
            case Player.PlayMode.PLAY_MODE_LIST:
                playerType.setImageResource(R.drawable.ic_play_mode_loop);
                player.setPlayMode(Player.PlayMode.PLAY_MODE_REPEAT_LIST);
                LocalMusicSp.get().put("playMode", 1);
                break;
            case Player.PlayMode.PLAY_MODE_REPEAT_LIST:
                playerType.setImageResource(R.drawable.ic_play_mode_single);
                player.setPlayMode(Player.PlayMode.PLAY_MODE_REPEAT_ONE);
                LocalMusicSp.get().put("playMode", 2);
                break;
            case Player.PlayMode.PLAY_MODE_REPEAT_ONE:
                playerType.setImageResource(R.drawable.ic_play_mode_shuffle);
                player.setPlayMode(Player.PlayMode.PLAY_MODE_SHUFFLE);
                LocalMusicSp.get().put("playMode", 3);
                break;
            case Player.PlayMode.PLAY_MODE_SHUFFLE:
                playerType.setImageResource(R.drawable.ic_play_mode_list);
                player.setPlayMode(Player.PlayMode.PLAY_MODE_LIST);
                LocalMusicSp.get().put("playMode", 0);
                break;
            default:
        }
    }

    @OnClick(R.id.playerPause)
    public void playerPauseClick() {
        if (player.isPlaying()) {
            player.pause();
            playerPause.setImageResource(R.drawable.ic_play);
        } else {
            player.resume();
            playerPause.setImageResource(R.drawable.ic_pause);
        }
    }

    @OnClick(R.id.playerPre)
    public void playerPreClick() {
        player.prev();
    }

    @OnClick(R.id.playerNext)
    public void playerNextClick() {
        player.next();
    }

    @OnClick(R.id.playerFavorite)
    public void playerCollectClick() {
        if (player.isFavorite()) {
            playerCollect.setImageResource(R.drawable.ic_favorite_no);
            player.unFavorite();
        } else {
            playerCollect.setImageResource(R.drawable.ic_favorite_yes);
            player.favorite();
        }
    }

    @OnClick(R.id.playerList)
    public void playerListClick() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player = null;
        mGson = null;
        musicList = null;
        if (timer != null) {
            timer.dispose();
        }
    }

    class DetailAdapter extends RecyclerView.Adapter {

        List<Music> list;

        public DetailAdapter(List<Music> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(PlayerActivity.this)
                    .inflate(R.layout.detail_item, viewGroup, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            final VH vh = (VH) viewHolder;
            vh.textView.setText(list.get(i).getTitle());
            if (!TextUtils.isEmpty(list.get(i).getSubTitle())) {
                vh.textSinger.setText(list.get(i).getSubTitle());
            } else {
                vh.textSinger.setText("");
            }
            vh.layout.setOnClickListener(v -> player.playByIndex(i));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class VH extends RecyclerView.ViewHolder {

            public TextView textView;
            public TextView textSinger;
            public LinearLayout layout;

            public VH(View view) {
                super(view);
                textView = view.findViewById(R.id.songName);
                textSinger = view.findViewById(R.id.singer);
                layout = view.findViewById(R.id.layout);
            }
        }
    }

}
