package dhu.cst.yinqingbo416.test0521;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class IatUtils {
    private Activity activity;
    //语音听写对象
    private SpeechRecognizer mIat;
    //语音听写UI
    private RecognizerDialog mIatDialog;
    //显示语音听写内容
    //private EditText editText;
    //使用HashMap储存听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();
    private Toast mToast;
    private int ret = 0;//函数调用返回值
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    public IatUtils(Activity activity, EditText editText) {
        this.activity = activity;
        //this.editText = editText;
        mIat = com.iflytek.cloud.SpeechRecognizer.createRecognizer(activity, mInitListener);
        if(mIat == null){
            Log.d("TAG", "IatUtils: mIat为null");
        }
        mIatDialog = new RecognizerDialog(activity, mInitListener);
        mToast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
        upload_userwords();//上传用户词表
    }
    public IatUtils(Activity activity){
        this.activity = activity;
        mIat = com.iflytek.cloud.SpeechRecognizer.createRecognizer(activity, mInitListener);
        mIatDialog = new RecognizerDialog(activity, mInitListener);
        mToast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
        upload_userwords();//上传用户词表
    }
    //语音听写
    public void SpeechToText(){
        //editText.setText(null);
        mIatResults.clear();//清空结果集
        //设置参数
        setParam();
        // 显示听写对话框
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
        showTip("请开始讲话");
    }
    //设置语音听写参数
    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        String lag = "mandarin";
        // 设置引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 在线听写支持多种小语种，若想了解请下载在线听写能力，参看其speechDemo
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }
    //上传用户词表
    private void upload_userwords(){
        String userWords = activity.getString(R.string.shool_words);
        // 指定引擎类型
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 置编码类型
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        ret = mIat.updateLexicon("userword", userWords, mLexiconListener);
        if (ret != ErrorCode.SUCCESS)
            showTip("上传热词失败,错误码：" + ret+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
    }

    //上传词表监听器
    private LexiconListener mLexiconListener = new LexiconListener() {

        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if (error != null) {
                showTip(error.toString());
            } else {
                //showTip("用户词表上传成功！");
            }
        }
    };
    //在主线程中调用toast
    private void showTip(final String str) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
    //初始化监听器
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };
    //听写UI监听器
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            //Log.d(TAG, "recognizer result：" + results.getResultString());
            String text = JsonParser.parseIatResult(results.getResultString());
            if(text.contains("北门") && Tools.getViewIndex() != 0){
                activity.startActivity(new Intent(activity,View1Activity.class));
                activity.finish();
            }else if(text.contains("图文") && Tools.getViewIndex() != 1){
                activity.startActivity(new Intent(activity,View2Activity.class));
                activity.finish();
            }else if(text.contains("镜月湖") && Tools.getViewIndex() != 2){
                activity.startActivity(new Intent(activity,View3Activity.class));
                activity.finish();
            }else if((text.contains("大活") || text.contains("大学生活动中心"))&& Tools.getViewIndex() != 3){
                activity.startActivity(new Intent(activity,View4Activity.class));
                activity.finish();
            }else if(text.contains("体育场") && Tools.getViewIndex() != 4){
                activity.startActivity(new Intent(activity,View5Activity.class));
                activity.finish();
            }else if((text.contains("一教") || text.contains("1教"))&&Tools.getViewIndex() != 5){
                activity.startActivity(new Intent(activity,View6Activity.class));
                activity.finish();
            }else if(text.contains("攀岩")&& Tools.getViewIndex() != 6){
                activity.startActivity(new Intent(activity,View6Activity.class));
                activity.finish();
            }
            //editText.append(text);
            //editText.setSelection(editText.length());
            //mResultText.setSelection(mResultText.length());
        }

        @Override
        public void onError(SpeechError speechError) {

        }

    };
    //回收资源
    public void destroy(){
        if(mIat != null){
            mIat.destroy();
        }
    }
}
