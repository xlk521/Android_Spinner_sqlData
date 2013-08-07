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

	// ������Դ��ͼƬ(������������) id������
	int[] drawableIds = { R.drawable.football, R.drawable.basketball,R.drawable.volleyball ,R.drawable.volleyball};
	// ������Դ�ַ��� (������������) id������
	int[] msgIds = { R.string.zp, R.string.lq, R.string.pq };
	//ģ�����ָı�
	private ArrayList<String> text = null;
	private Button button = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(this);
		//��������Ƴ�ȡ���ݿ��е���������д������б�
		new WelcomeHttp().execute();
		
		text = new ArrayList<String>();
		text.add(0, "��ѡ��");
		Spinner sp = (Spinner) findViewById(R.id.Spinner01);
		//����������
		BaseAdapter ba = new BaseAdapter() {
		
			public int getCount() {
				// һ������ѡ��
				return text.size();
			}
		
			public Object getItem(int position) {
				return null;
			}
		
			public long getItemId(int position) {
				return 0;
			}
			//�˺�����������ʾitem�е������Դ��
			@SuppressLint("ResourceAsColor")
			public View getView(int position, View convertView, ViewGroup parent) {
				// ��̬����ÿ���������Ӧ��View��ÿ��������View��LinearLayout
				// �а���һ��ImageView��һ��TextView����
				// ��ʼ��LinearLayout
				LinearLayout ll = new LinearLayout(MainActivity.this);
				//�����м��
				ll.setOrientation(LinearLayout.HORIZONTAL);
				// ��ʼ��ImageView
				ImageView ii = new ImageView(MainActivity.this);
				//��Ҫ��ImageView����κε���Դ�������Ϳ�����ʾ������
				//��ͼƬ����ʾ������ӵ����ֵ���
				ll.addView(ii);
				// ��ʼ��TextView
				TextView tv = new TextView(MainActivity.this);
				//�˴���ʹ��string�е�id�滻Ϊʹ��string�б�ķ�ʽ����ʾ����
				tv.setText(text.get(position));
				tv.setTextColor(R.color.black);
				tv.setTextSize(24);
				ll.addView(tv);
				return ll;
			}
		};
		// ΪSpinner�������������
		sp.setAdapter(ba);
		//ΪSpinner����¼���Ӧ
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			//�˴��ǵ�ѡ��itemʱ����Ӧ�ĸı�ָ���������Ϣ
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// ��ȡ������TextView
				TextView tv = (TextView) findViewById(R.id.TextView01);
				// ��ȡ��ǰѡ��ѡ���Ӧ��LinearLayout
				LinearLayout ll = (LinearLayout) view;
				// ��ȡ���е�TextView
				TextView tvn = (TextView) ll.getChildAt(1);
				// ��StringBuilder��̬������Ϣ
				StringBuilder sb = new StringBuilder();
				sb.append(getResources().getText(R.string.ys));
				sb.append(":");
				sb.append(tvn.getText());
				// ��Ϣ���ý�ס����
				tv.setText(sb.toString());
			}
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
	
	private ArrayList<String> getDataBySql() {
		JSONObject json = null;
		//����list������ʢ�����ݿ���ȡ����������
		ArrayList<String> textList = new ArrayList<String>();
		//���⴫�����ݣ�ֻ��Ϊ��������غ���
		String[] s = {};
		UploadFileTask uploadFileTask=new UploadFileTask(this);
		AsyncTask<String, Void, String> str = uploadFileTask.execute(s);
		//��ȡ���ص����ݣ��˴���ȡ�ķ��ص�JSONģʽ��String���ݣ���Ҫͨ��JSONObject�Ƚ���ת����jsn��ʽ���ڽ������ݵĽ���
		try {
			String ss = str.get();
			//���ַ�����ʽ��ת����Ϊjson��ʽ��
			try {
				text = new ArrayList<String>();
				text.add(0, "��ѡ��");
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
	//�����ϴ�������Ϣ�Ϳ�ʼ����������
	public class UploadFileTask extends AsyncTask<String, Void, String>{
		
		private ProgressDialog progressDialog = null;
		private Activity context = null;
		private String title = "�����ϴ�������";
		private String message = "�������ڴ����С�����";
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
			//�ϴ�������Ϣ
			JSONObject json = new JSONObject();
			try {
				//������Ϣ���
				json.put(JSONKey.FLAG, JSONKey.FLAG_SPINNER);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				//�ϴ�����
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
				// ����������URL��ַ��json��ʽ�ַ���
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
				if (result.equals("�����ɹ�") ) {
					// �������ܲ˵�
					Toast.makeText(MainActivity.this, result,Toast.LENGTH_LONG).show();
				} else {
					// ��½ʧ�ܵĴ�����ʾ
					Toast.makeText(MainActivity.this, result,Toast.LENGTH_LONG).show();
				}
				
			}
		}
}
