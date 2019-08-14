package iks.market.marketwarehouse;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    ImageView imageView;
    String licence;
    StringBuilder stringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.logo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }

            licence = encode(Build.getSerial());
            System.out.println("Лицензия:" + licence);
        }
        readLicenceFile();
        if(licence.equals(stringBuilder.toString())){
            System.out.println("LICENCE ACCEPTED");
            handler = new Handler();
            long timer = 2500;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, timer);

        }else
        {
            System.out.println("LICENCE GOVNO");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Внимание").setMessage("Лицензия не найдена").setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


      public String encode (String serial){
        int temp = Integer.valueOf(serial.substring(5, 9));
        return receivedLicence( ((temp +131) * 2781)*3);
    }

    public String receivedLicence(int a){
        String shit = String.valueOf(a);
        shit = Base64.encodeToString(shit.getBytes(), 0);
        System.out.println(shit);
        return shit;
    }

    public boolean readLicenceFile(){
        File licencefile = Environment.getExternalStorageDirectory();
        File file = new File(licencefile, "licence.txt");
        stringBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
            br.close();
        }
        catch (IOException e) {

        }
return true;
    }


}
