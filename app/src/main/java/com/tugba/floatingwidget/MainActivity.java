package com.tugba.floatingwidget;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ACTION_MANAGE_OVERLAY_PERMISSION ve CODE_DRAW_OVER_OTHER_APP_PERMISSION izni
        // bir uygulamanın kullanmakta oldugunuz diğer uygulamaların üzerinde görüntülenmesine ve diğer uygulamalardaki
        //arayüz kullanımınıza müdahale etmek istediginizde kullanılır.Oyuzden asağıda bu izinleri belirttik
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();
        }
    }

    /**
     * FloatingViewService adlı sınıftaki servisi start ettik...
     */
    private void initializeView() {
        findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, FloatingViewService.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //İzin verilme kontrolu
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //İzin verilmediği durum
                Toast.makeText(this,
                        "Uygulamayı kullanabilmek için lütfen izin veriniz",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
