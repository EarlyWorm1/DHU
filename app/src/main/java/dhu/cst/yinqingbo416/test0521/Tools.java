package dhu.cst.yinqingbo416.test0521;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Tools {
    private static boolean viewsVisibility = false;//标记视图选择列表是否可见
    private static boolean sidebarVisibility = false;//标记侧边栏是否可见
    private static int viewIndex = 0;//标记目前视图位置
    private static List<Scene> SceneList = new ArrayList<>();

    public static List<Scene> getSceneList() {
        return SceneList;
    }

    public static void setSceneList(List<Scene> sceneList) {
        SceneList = sceneList;
    }

    public static boolean isViewsVisibility() {
        return viewsVisibility;
    }

    public static void setViewsVisibility(boolean viewsVisibility) {
        Tools.viewsVisibility = viewsVisibility;
    }

    public static int getViewIndex() {
        return viewIndex;
    }

    public static void setViewIndex(int viewIndex) {
        Tools.viewIndex = viewIndex;
    }

    public static boolean isSidebarVisibility() {
        return sidebarVisibility;
    }

    public static void setSidebarVisibility(boolean sidebarVisibility) {
        Tools.sidebarVisibility = sidebarVisibility;
    }

    //切换视图
    public static void switchActivity(Activity activity,int index){
        if(index == viewIndex+1){
            Toast.makeText(activity,"识别场景就为当前场景",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (index){
            case 1:
                activity.startActivity(new Intent(activity,View1Activity.class));
                activity.finish();
                break;
            case 2:
                activity.startActivity(new Intent(activity,View2Activity.class));
                activity.finish();
                break;
            case 3:
                activity.startActivity(new Intent(activity,View3Activity.class));
                activity.finish();
                break;
            case 4:
                activity.startActivity(new Intent(activity,View4Activity.class));
                activity.finish();
                break;
            case 5:
                activity.startActivity(new Intent(activity,View5Activity.class));
                activity.finish();
                break;
            case 6:
                activity.startActivity(new Intent(activity,View6Activity.class));
                activity.finish();
                break;
            case 7:
                activity.startActivity(new Intent(activity,View7Activity.class));
                activity.finish();
                break;
            default:
                Toast.makeText(activity,"未能识别该场景",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
