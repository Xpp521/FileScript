package com.xpp.filescript;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xpp.dialog.ButtonDialogFragment;
import com.xpp.dialog.NeutralDialogFragment;
import com.xpp.settings.SettingsActivity;

import java.io.File;
import java.util.ArrayList;

//import android.content.res.Resources;

public class FSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs);

        TextView textView = findViewById(R.id.textView1);
        if (StorageManager.isExternalStorageAvailable()){
            textView.setText(String.format(getString(R.string.storage_space), StorageManager.getExternalMemorySize(FSActivity.this), StorageManager.getAvailableExternalMemorySize(FSActivity.this)));
        }
        else {
            textView.setText(String.format(getString(R.string.storage_space), StorageManager.getInternalMemorySize(FSActivity.this), StorageManager.getAvailableInternalMemorySize(FSActivity.this)));
        }

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                   //移动预告片方法
                String tencent_path = "/storage/emulated/0/tencent/QQfile_recv";
                String trailer_path = "/storage/emulated/0/Video/电影/预告片";
                File oldPath = new File(tencent_path);
                MyFileFilter filter = null;
                try {
                    filter = new MyFileFilter(new String[]{".flv", ".mp4"}, new String[]{"预告", "定档", "先导", "上映", "片段", "曝光"}, 0, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String[] li = oldPath.list(filter);
                int count = 0;
                for (String aLi : li) {
                    File f = new File(tencent_path + "/" + aLi);
                    if (f.renameTo(new File(trailer_path + "/" + aLi)))
                        count++;
                }
                if (0 == count)
                    Toast.makeText(FSActivity.this, getString(R.string.no_trailer), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(FSActivity.this, String.format(getString(R.string.trailer_move_result), count), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                         //重命名音乐方法
                final String music_path = "/storage/emulated/0/Music";
                File oldPath = new File(music_path);
                MyFileFilter filter = null;
                try {
                    filter = new MyFileFilter(new String[]{".flac", ".mp3", ".ape"}, null, 0, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final String[] li = oldPath.list(filter);
                final ArrayList<String> new_names = new ArrayList<>();
                int count = 0;
                for (String aLi : li) {
                    File f = new File(music_path + "/" + aLi);
                    int point_index = aLi.lastIndexOf(".");
                    int bar_index = aLi.indexOf(" - ");
                    String song = aLi.substring(0, bar_index);                    //音乐名
                    String singer = aLi.substring(bar_index + 3, point_index);    //歌手名
                    String extension = aLi.substring(point_index, aLi.length());  //扩展名
                    String new_name = singer + " - " + song + extension;
                    new_names.add(new_name);
                    if (f.renameTo(new File(music_path + "/" + new_name)))
                        count++;
                }
                if (0 == count) {
                    Toast.makeText(FSActivity.this, getString(R.string.no_music), Toast.LENGTH_SHORT).show();
                } else {
                    ButtonDialogFragment buttonDialogFragment = new ButtonDialogFragment();
                    buttonDialogFragment.show(getString(R.string.share_to_pc), String.format(getString(R.string.music_rename_result), count), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {         //将音乐文件分享到电脑
                            ArrayList<Uri> files = new ArrayList<>();
                            for (String new_name : new_names) {
                                files.add(Uri.fromFile(new File(music_path + "/" + new_name)));
                            }
                            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                            intent.setType("*/*");
                            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                            startActivity(intent);
                            Toast.makeText(FSActivity.this, getString(R.string.music_sending_result), Toast.LENGTH_LONG).show();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, getFragmentManager());
                }
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FSActivity.this.finish();
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FSActivity.this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FSActivity.this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_About:                     //关于
                PackageInfo pkg;
                try {
                    pkg = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(FSActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    break;
                }
                String app_name = pkg.applicationInfo.loadLabel(getPackageManager()).toString();
                String app_version = pkg.versionName;
                NeutralDialogFragment neutralDialogFragment = new NeutralDialogFragment();
                neutralDialogFragment.show(app_name, String.format(getString(R.string.about_information), app_version, getString(R.string.app_author)), getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }, getFragmentManager());
                break;
            case R.id.navigation_Settings:                  //设置
                Intent intent = new Intent(FSActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
