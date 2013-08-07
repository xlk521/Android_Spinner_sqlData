package com.example.spinnertext2;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.spinnertext2.MainActivity;
import com.example.spinnertext2.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity implements OnClickListener{

	// 所有资源的图片(足球、篮球、排球) id的数组
	int[] drawableIds = { R.drawable.football, R.drawable.basketball,R.drawable.volleyball ,R.drawable.volleyball};
	// 所有资源字符串 (足球、篮球、排球) id的数组
	int[] msgIds = { R.string.zp, R.string.lq, R.string.pq };
	//模拟文字改变
	private ArrayList<String> text = null;
	private Button button = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(this);
		//这用来设计抽取数据库中的数据来填写下面的列表
		new WelcomeHttp().execute();
		
		text = new ArrayList<String>();
		text.add(0, "请选择");
		Spinner sp = (Spinner) findViewById(R.id.Spinner01);
		//创建适配器
		BaseAdapter ba = new BaseAdapter() {
		
			public int getCount() {
				// 一共三个选项
				return text.size();
			}
		
			public Object getItem(int position) {
				return null;
			}
		
			public long getItemId(int position) {
				return 0;
			}
			//此函数是用来显示item中的添加资源的
			@SuppressLint("ResourceAsColor")
			public View getView(int position, View convertView, ViewGroup parent) {
				// 动态生成每个下拉项对应的View，每个下拉项View由LinearLayout
				// 中包含一个ImageView及一个TextView构成
				// 初始化LinearLayout
				LinearLayout ll = new LinearLayout(MainActivity.this);
				//设置列间距
				ll.setOrientation(LinearLayout.HORIZONTAL);
				// 初始化ImageView
				ImageView ii = new ImageView(MainActivity.this);
				//不要给ImageView添加任何的资源，这样就可以显示文字了
				//将图片的显示区域添加到布局当中
				ll.addView(ii);
				// 初始化TextView
				TextView tv = new TextView(MainActivity.this);
				//此处将使用string中的id替换为使用string列表的方式来显示数据
				tv.setText(text.get(position));
				tv.setTextColor(R.color.black);
				tv.setTextSize(24);
				ll.addView(tv);
				return ll;
			}
		};
		// 为Spinner添加内容适配器
		sp.setAdapter(ba);
		//为Spinner添加事件响应
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			//此处是当选择item时，对应的改变指定区域的信息
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// 获取主界面TextView
				TextView tv = (TextView) findViewById(R.id.TextView01);
				// 获取当前选中选项对应的LinearLayout
				LinearLayout ll = (LinearLayout) view;
				// 获取其中的TextView
				TextView tvn = (TextView) ll.getChildAt(1);
				// 用StringBuilder动态生成信息
				StringBuilder sb = new StringBuilder();
				sb.append(getResources().getText(R.string.ys));
				sb.append(":");
				sb.append(tvn.getText());
				// 信息设置进住界面
				tv.setText(sb.toString());
			}
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
	
	private ArrayList<String> getDataBySql() {
		JSONObject json = null;
		//建立list，用来盛放数据库中取出来的数据
		ArrayList<String> textList = new ArrayList<String>();
		//随意传入数据，只是为了启动相关函数
		String[] s = {};
		UploadFileTask uploadFileTask=new UploadFileTask(this);
		AsyncTask<String, Void, String> str = uploadFileTask.execute(s);
		//获取返回的数据，此处获取的返回的JSON模式的String数据，需要通过JSONObject等将其转换成jsn格式，在进行数据的解析
		try {
			String ss = str.get();
			//将字符串形式的转换成为json形式的
			try {
				text = new ArrayList<String>();
				text.add(0, "请选择");
				json = new JSONObject(ss);
				int num = json.getInt("num");
				for (int i = 1; i <= num; i++) {
					text.add(i, json.getString(""+i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return textList;
	}
	//用于上传请求信息和开始的联网测试
	public class UploadFileTask extends AsyncTask<String, Void, String>{
		
		private ProgressDialog progressDialog = null;
		private Activity context = null;
		private String title = "正在上传···";
		private String message = "请求正在处理中···";
		public UploadFileTask(Activity con){
			this.context = con;
			progressDialog = ProgressDialog.show(context,title , message);
		}
		@Override
	    protected void onPostExecute(String result) {}
		@Override
		protected void onPreExecute() {}
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
		@Override
		protected String doInBackground(String... params) {
			System.out.println("params003");
			String result = null;
			String jsonData = null;
			//上传文字信息
			JSONObject json = new JSONObject();
			try {
				//文字信息组合
				json.put(JSONKey.FLAG, JSONKey.FLAG_SPINNER);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				//上传文字
				result = TKYHttpClient.connect(Constants.BASE_HTTP_URL,json.toString());
				Log.i("200result", result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("params005");
			progressDialog.cancel();
			return result;
		}
		@Override
		protected void onProgressUpdate(Void... values) {}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			ArrayList<String> json = getDataBySql();
			break;

		default:
			break;
		}
	}
	
	class WelcomeHttp extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {
			String s = null;
			JSONObject   json=new JSONObject();
			try {
				json.put(JSONKey.FLAG,"Welcom");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			try {
				// 联网，传入URL地址，json格式字符串
				String result = TKYHttpClient.connect(Constants.BASE_HTTP_URL,json.toString());
					
				if (result == null) {
					s = getResources().getString(R.string.toast_http_error);
				} else {
					//System.out.println(result);
					JSONObject jsonObject = new JSONObject(result);
					s=jsonObject.getString("welcom");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				s = getResources().getString(R.string.toast_http_error);
			}
			return s;
		}

		protected void onCancelled() {
			
		}

		@Override
		protected void onPostExecute(String result) {
				if (result.equals("联网成功") ) {
					// 启动功能菜单
					Toast.makeText(MainActivity.this, result,Toast.LENGTH_LONG).show();
				} else {
					// 登陆失败的错误提示
					Toast.makeText(MainActivity.this, result,Toast.LENGTH_LONG).show();
				}
				
			}
		}
}
