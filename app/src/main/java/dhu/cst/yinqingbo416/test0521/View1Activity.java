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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zph.glpanorama.GLPanorama;

import java.io.FileNotFoundException;

public class View1Activity extends AppCompatActivity implements View.OnClickListener {
    private GLPanorama glPanorama;//全景图
    private LinearLayout straight;//直行
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
    private TextView introduceText;
    private Boolean introduceShow = false;
    private Boolean reportShow = false;
    private TTSUtils ttsUtils;
    private IatUtils iatUtils;
    private IntentFilter filter;//接收本地广播
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceive localReceive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view1);
        setStatusBarTransparent();//设置状态栏透明
        glPanorama = findViewById(R.id.view1_GLPanorama);
        straight = findViewById(R.id.view1_straight);
        initViewsLayout();
        initSidebar();
        initImgCut();
        glPanorama.setGLPanorama(R.drawable.view1);
        straight.setOnClickListener(this);
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
    //点击事件
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view1_straight:
                startActivity(new Intent(View1Activity.this,View2Activity.class));
                finish();
                break;
            default:
                break;
        }
    }
    //视图列表初始化
    public void initViewsLayout(){
        Tools.setViewIndex(0);
        viewBtn = findViewById(R.id.view1_views);
        viewsLayout = findViewById(R.id.view1_switch_view);
        viewsLayout.getBackground().setAlpha(76);
        recyclerView = findViewById(R.id.view1_recyclerView);
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
        layoutManager.scrollToPosition(0);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        SceneAdapter adapter = new SceneAdapter(Tools.getSceneList(),View1Activity.this);
        recyclerView.setAdapter(adapter);
    }
    //侧边栏功能初始化
    private void initSidebar(){
        sideBarShow = findViewById(R.id.view1_show);
        sideBar = findViewById(R.id.view1_sidebar);
        introduce = findViewById(R.id.view1_introduce);
        report = findViewById(R.id.view1_tts);
        iat = findViewById(R.id.view1_iat);
        sidebarHide = findViewById(R.id.view1_hide);
        introduceText = findViewById(R.id.view1_introduce_text);
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
        receiveLocalBroadcast();//接收本地广播，判断语音合成是否完成
    }
    //图片识别
    private void initImgCut(){
        imgCut = findViewById(R.id.view1_img);
        imgCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
    }
    //打开相册
    private void getImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    11101);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 11101);
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
                case 11101:
                    if (data != null) {
                        Uri uri = data.getData();
                        ContentResolver cr = this.getContentResolver();
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                            int ret = ImageCompare.getImgIndex(this,bitmap);
                            Tools.switchActivity(View1Activity.this,ret);
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
        //ttsUtils.stopSpeaking();
        //Log.d("ViewActivity", "Activity已经被销毁！");
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
