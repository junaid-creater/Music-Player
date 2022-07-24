package com.example.mcplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class AudioPlayer extends AppCompatActivity {

    ListView theList;
    String items[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        theList = findViewById(R.id.theList);
        runtimepermission();
    }
    public ArrayList<File> findSong(File file)
    {
            ArrayList<File> arrayList = new ArrayList<>();
            File[] files= file.listFiles();
                  for(File singleFile : files)
                  {
                      if (singleFile.isDirectory() && !singleFile.isHidden()) {
                          arrayList.addAll(findSong(singleFile));
                      }
                      else {
                          if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                              arrayList.add(singleFile);
                          }
                      }
                  }
              return arrayList;
    }
    public void runtimepermission(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    void display(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for(int i=0 ;i<mySongs.size() ; i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.song_layout,items);
        theList.setAdapter(adapter);

        theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName = theList.getItemAtPosition(position).toString();
                Intent i = new Intent(AudioPlayer.this,Player.class);
                i.putExtra("songs",mySongs);
                i.putExtra("pos",position);
                i.putExtra("songName",songName);
                startActivity(i);
            }
        });
    }
}
