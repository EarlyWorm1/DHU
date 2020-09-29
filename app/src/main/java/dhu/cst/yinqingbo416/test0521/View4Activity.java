package dhu.cst.yinqingbo416.test0521;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zph.glpanorama.GLPanorama;

import java.io.FileNotFoundException;

public class View4Activity extends AppCompatActivity implements View.OnClickListener {
    private GLPanorama glPanorama;
    private LinearLayout left;
    private LinearLayout straight;
    private LinearLayout retreat;
    private LinearLayout right;
    private ImageView viewBtn;//视图按钮
    private LinearLayout viewsLayout;//视图显示区域
    private RecyclerView recyclerView;//滚动视图
    private ImageView imgCut;//图片识别按钮
    private LinearLayout sideBarShow;//侧边栏显示按钮
    private LinearLayout sideBar;//侧边栏
    private ImageView introduce;//简介
    private ImageView report;//语音播报
    private ImageView iat;//语音识别
    private ImageView sidebarHide;//侧边栏隐藏按钮
    private TextView introduceText;//简介文本
    private Boolean introduceShow = false;
    private Boolean reportShow = false;
    private TTSUtils ttsUtils;//语音合成类
    private IatUtils iatUtils;//语音识别类
    private IntentFilter filter;//接收本地广播
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceive localReceive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view4);
        setStatusBarTransparent();
        glPanorama = findViewById(R.id.view4_GLPanorama);
        left = findViewById(R.id.view4_left);
        straight = findViewById(R.id.view4_straight);
        retreat = findViewById(R.id.view4_retreat);
        right = findViewById(R.id.view4_right);
        initViewsLayout();
        glPanorama.setGLPanorama(R.drawable.view4);
        left.setOnClickListener(this);
        straight.setOnClickListener(this);
        retreat.setOnClickListener(this);
        right.setOnClickListener(this);
        initSidebar();
        initImgCut();
    }
    //设置状态栏透明
    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view4_left:
                startActivity(new Intent(View4Activity.this,View7Activity.class));
                break;
            case R.id.view4_straight:
                startActivity(new Intent(View4Activity.this,View2Activity.class));
                break;
            case R.id.view4_retreat:
                startActivity(new Intent(View4Activity.this,View5Activity.class));
                break;
            case R.id.view4_right:
                startActivity(new Intent(View4Activity.this,View6Activity.class));
                break;
        }
        finish();
    }

    //视图列表初始化
    public void initViewsLayout(){
        Tools.setViewIndex(3);
        viewBtn = findViewById(R.id.view4_views);
        viewsLayout = findViewById(R.id.view4_switch_view);
        viewsLayout.getBackground().setAlpha(76);
        recyclerView = findViewById(R.id.view4_recyclerView);
        recyclerView.smoothScrollToPosition(3);
        if(Tools.isViewsVisibility()){
            viewsLayout.setVisibility(View.VISIBLE);
        }else {
            viewsLayout.setVisibility(View.GONE);
        }
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Tools.isViewsVisibility()){
                    viewsLayout.setVisibility(View.GONE);
                    Tools.setViewsVisibility(false);
                }else {
                    viewsLayout.setVisibility(View.VISIBLE);
                    Tools.setViewsVisibility(true);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(3);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        SceneAdapter adapter = new SceneAdapter(Tools.getSceneList(),View4Activity.this);
        recyclerView.setAdapter(adapter);
    }

    //侧边栏功能初始化
    private void initSidebar(){
        sideBarShow = findViewById(R.id.view4_show);
        sideBar = findViewById(R.id.view4_sidebar);
        introduce = findViewById(R.id.view4_introduce);
        report = findViewById(R.id.view4_tts);
        iat = findViewById(R.id.view4_iat);
        sidebarHide = findViewById(R.id.view4_hide);
        introduceText = findViewById(R.id.view4_introduce_text);
        ttsUtils = new TTSUtils(this);
        iatUtils = new IatUtils(this);
        if(Tools.isSidebarVisibility()){
            sideBarShow.setVisibility(View.GONE);
            sideBar.setVisibility(View.VISIBLE);
        }else {
            sideBarShow.setVisibility(View.VISIBLE);
            sideBar.setVisibility(View.GONE);
        }
        sideBarShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.setSidebarVisibility(true);
                sideBarShow.setVisibility(View.GONE);
                sideBar.setVisibility(View.VISIBLE);
            }
        });
        sidebarHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.setSidebarVisibility(false);
                sideBar.setVisibility(View.GONE);
                sideBarShow.setVisibility(View.VISIBLE);
            }
        });
        introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(introduceShow){
                    introduceText.setVisibility(View.GONE);
                    introduceShow = false;
                }else {
                    introduceText.setVisibility(View.VISIBLE);
                    introduceShow = true;
                }
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportShow){
                    reportShow = false;
                    report.setImageResource(R.mipmap.silence);
                    ttsUtils.stopSpeaking();
                }else {
                    reportShow = true;
                    report.setImageResource(R.mipmap.report);
                    ttsUtils.startSpeaking(introduceText.getText().toString());
                }
            }
        });
        iat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iatUtils.SpeechToText();
            }
        });
        receiveLocalBroadcast();
    }
    //图片识别
    private void initImgCut(){
        imgCut = findViewById(R.id.view4_img);
        imgCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
    }
    //获取图片资源
    private void getImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    11101);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 11102);
        }
    }
    //接收本地广播
    private void receiveLocalBroadcast(){
        filter = new IntentFilter();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        filter.addAction("dhu.cst.yqb.BroadcastTest.LOCAL_BROADCAST");
        localReceive = new LocalReceive();
        localBroadcastManager.registerReceiver(localReceive,filter);//注册本地广播监听器
    }
    //重写返回函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 11102:
                    if (data != null) {
                        Uri uri = data.getData();
                        ContentResolver cr = this.getContentResolver();
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                            int ret = ImageCompare.getImgIndex(this,bitmap);
                            Tools.switchActivity(View4Activity.this,ret);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
    //回收资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ttsUtils != null){
            ttsUtils.destroy();
        }
        if(iatUtils != null){
            iatUtils.destroy();
        }
        localBroadcastManager.unregisterReceiver(localReceive);
    }
    @Override
    public void finish() {
        if(ttsUtils != null){
            ttsUtils.destroy();
        }
        super.finish();
    }

    private class LocalReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(View1Activity.this,"语音合成完成！",Toast.LENGTH_SHORT).show();
            reportShow = false;
            report.setImageResource(R.mipmap.silence);
        }
    }
}
