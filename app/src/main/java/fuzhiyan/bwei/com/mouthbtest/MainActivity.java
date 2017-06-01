package fuzhiyan.bwei.com.mouthbtest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.example.xlistview.XListView;
import com.google.gson.Gson;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private XListView listView;
    private List<MyBean.AppBean> list=new ArrayList<>();
    private ProgressDialog progressdialog;
    private String path= Environment.getExternalStorageDirectory()+ File.separator;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }
    private void initData() {
        RequestParams params=new RequestParams(Url.url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String s=result.substring(0,result.length()-1);
                Gson gson=new Gson();
                MyBean bean=gson.fromJson(s,MyBean.class);
                list=bean.getApp();
                adapter=new MyAdapter(MainActivity.this,list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("网络选择")
                                .setIcon(R.mipmap.ic_launcher)
                                .setSingleChoiceItems(new String[]{"wifi", "手机流量"}, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                new AlertDialog.Builder(MainActivity.this)
                                                        .setTitle("版本更新")
                                                        .setMessage("现在检查到新版本，是否更新?")
                                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {



                                                                progressdialog = new ProgressDialog(MainActivity.this);
                                                                RequestParams params = new RequestParams(list.get(position).getUrl());
                                                                Log.e("--->",params.toString());
                                                                params.setSaveFilePath(path + list.get(position).getName() + ".apk");
                                                                x.http().post(params, new ProgressCallback<File>() {
                                                                    @Override
                                                                    public void onSuccess(File result) {

                                                                        Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();

                                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                                                                        intent.addCategory("android.intent.category.DEFAULT");
                                                                        intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                                                                        startActivity(intent);
                                                                    }

                                                                    @Override
                                                                    public void onError(Throwable ex, boolean isOnCallback) {
                                                                        Toast.makeText(MainActivity.this, "下载失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        Log.i("fzy", ex.getMessage());
                                                                        if (ex instanceof HttpException) { //网络错误
                                                                            HttpException httpEx = (HttpException) ex;
                                                                            int responseCode = httpEx.getCode();
                                                                            String responseMsg = httpEx.getMessage();
                                                                            String errorResult = httpEx.getResult();
                                                                            //...
                                                                        } else { //其他错误
                                                                            //...
                                                                        }
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(CancelledException cex) {

                                                                    }

                                                                    @Override
                                                                    public void onFinished() {

                                                                    }

                                                                    @Override
                                                                    public void onWaiting() {

                                                                    }

                                                                    @Override
                                                                    public void onStarted() {

                                                                    }

                                                                    @Override
                                                                    public void onLoading(long total, long current, boolean isDownloading) {
                                                                        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                                        progressdialog.setMessage("下载中");
                                                                        progressdialog.show();
                                                                        progressdialog.setMax((int) total);
                                                                        progressdialog.setProgress((int) current);
                                                                    }
                                                                });


                                                            }
                                                        }).show();

                                                break;
                                            case 1:
                                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                                startActivity(intent);
                                                break;

                                        }
                                    }
                                }).show();
                    }
                });

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void initView() {
        listView= (XListView) findViewById(R.id.xlv);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                list.clear();
                initData();
                adapter.notifyDataSetChanged();
                listView.stopRefresh();
            }
            @Override
            public void onLoadMore() {
                initData();
//                list.addAll(list);
                adapter.notifyDataSetChanged();
                listView.stopLoadMore();
            }
        });
    }
}
