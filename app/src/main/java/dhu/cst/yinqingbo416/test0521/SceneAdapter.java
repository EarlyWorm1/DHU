package dhu.cst.yinqingbo416.test0521;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SceneAdapter extends RecyclerView.Adapter<SceneAdapter.ViewHolder> {
    private List<Scene> sceneList;
    private View selectView;
    private Activity activity;
    public SceneAdapter(List<Scene>sceneList,Activity activity){
        this.sceneList = sceneList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.views_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Scene scene = sceneList.get(position);
        holder.imageView.setImageResource(scene.getImgId());
        holder.textView.setText(scene.getName());
        if(position == Tools.getViewIndex()){
            holder.view.setBackgroundColor(Color.parseColor("#52A4ED"));
            selectView = holder.view;
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectView.setBackgroundColor(0);
                holder.view.setBackgroundColor(Color.parseColor("#52A4ED"));
                selectView = holder.view;
                if(position != Tools.getViewIndex()){
                    Intent intent;
                    switch (position){
                        case 0:
                            intent = new Intent(v.getContext(),View1Activity.class);
                            break;
                        case 1:
                            intent = new Intent(v.getContext(),View2Activity.class);
                            break;
                        case 2:
                            intent = new Intent(v.getContext(),View3Activity.class);
                            break;
                        case 3:
                            intent = new Intent(v.getContext(),View4Activity.class);
                            break;
                        case 4:
                            intent = new Intent(v.getContext(),View5Activity.class);
                            break;
                        case 5:
                            intent = new Intent(v.getContext(),View6Activity.class);
                            break;
                        case 6:
                            intent = new Intent(v.getContext(),View7Activity.class);
                            break;
                        default:
                            intent = new Intent(v.getContext(),MainActivity.class);
                            break;
                    }
                    v.getContext().startActivity(intent);
                    activity.finish();
                    Tools.setViewIndex(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sceneList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        View view;
        public ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.views_list_img);
            textView = view.findViewById(R.id.views_list_text);
            this.view = view;
        }
    }

}
