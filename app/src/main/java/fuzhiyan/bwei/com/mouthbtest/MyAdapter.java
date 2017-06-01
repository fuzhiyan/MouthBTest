package fuzhiyan.bwei.com.mouthbtest;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/28.
 * time:
 * author:付智焱
 */

public class MyAdapter extends BaseAdapter{
    public Map<Integer,Boolean> isCheck = null;
    private Context context;
    private List<MyBean.AppBean> list;

    public MyAdapter(Context context, List<MyBean.AppBean> list) {
        this.context = context;
        this.list = list;
        isCheck = new HashMap<Integer, Boolean>();
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < list.size(); i++) {
            isCheck.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
class ViewHolder{
    private TextView textView;
    private CheckBox checkBox;
    private LinearLayout layout;
}
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=View.inflate(context,R.layout.item,null);
            vh.textView= (TextView) convertView.findViewById(R.id.item_text);
            vh.checkBox= (CheckBox) convertView.findViewById(R.id.checkbox);
            vh.layout=(LinearLayout)convertView.findViewById(R.id.layout);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vh.checkBox.isChecked()){
                    vh.checkBox.setChecked(false);
                }else{
                    vh.checkBox.setChecked(true);
                }
            }
        });
        vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCheck.put(position, true);
                } else {
                    isCheck.put(position, false);
                }
            }
        });
        vh.checkBox.setChecked(isCheck.get(position));
        vh.textView.setText(list.get(position).getName().toString());

        return convertView;
    }
}
