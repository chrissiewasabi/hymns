package com.hymno.yimbo;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;

public class HomeActivity extends SherlockActivity {

	Context ctx = this;
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_NO = "no";
	static final String KEY_TITLE = "title";
	static final String KEY_BODY = "body";
	TextView textView;
	TextView textView_about;
	TextView textView_book;
	TextView textView_song;
	XMLHandler xmlHandler;
	String xml; // getting XML
	Document doc;
	NodeList nl;
	EditText editText;
	ImageView image;
	String stat = "0";

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setTitle("");
		xmlHandler = new XMLHandler(ctx);
		xml = xmlHandler.GetXML();
		doc = xmlHandler.getDomElement(xml);
		doc.getDocumentElement().normalize();
		nl = doc.getElementsByTagName(KEY_SONG);
		Log.d("ww", "node" + nl);
		textView = (TextView) findViewById(R.id.tv_home);
		textView_about = (TextView) findViewById(R.id.tv_about);
		textView_book = (TextView) findViewById(R.id.tv_book);
		textView_song = (TextView) findViewById(R.id.tv_song);
		image = (ImageView) findViewById(R.id.img);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(
					R.drawable.bg_striped);
			bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			getSupportActionBar().setBackgroundDrawable(bg);

			BitmapDrawable bgSplit = (BitmapDrawable) getResources()
					.getDrawable(R.drawable.bg_striped_split_img);
			bgSplit.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			getSupportActionBar().setSplitBackgroundDrawable(bgSplit);
		}
		Typeface bold = Typeface.createFromAsset(getAssets(), "bold.otf");
		textView.setTypeface(bold);
		textView_about.setTypeface(bold);
		textView_book.setTypeface(bold);
		// Search("1");

	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

			String text = editText.getText().toString()
					.toLowerCase(Locale.getDefault());
			if (text.equals("")) {

			}
			Search(text);
			textView_song.requestFocus();
			// if(getStat().equals("1")){
			// Toast.makeText(getApplicationContext(), "No match Found",
			// Toast.LENGTH_LONG).show();
			// }

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Get the options menu view from menu.xml in menu folder
		getSupportMenuInflater().inflate(R.menu.activity_home, menu);

				editText = (EditText) menu.findItem(R.id.actionViewLayout)
				.getActionView();
		
		editText.addTextChangedListener(textWatcher);

		MenuItem menuSearch = menu.findItem(R.id.actionViewLayout);

		menuSearch.setOnActionExpandListener(new OnActionExpandListener() {

			// Menu Action Collapse
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				editText.setText("");
				editText.requestFocus();

				return true;
			}

			// Menu Action Expand
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// Focus on EditText
				editText.requestFocus();

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
			}
		});

		return true;
	}

	public void SetVisibiltity() {
		textView.setVisibility(View.GONE);
		textView_about.setVisibility(View.GONE);
		textView_book.setVisibility(View.GONE);
		textView_song.setVisibility(View.VISIBLE);
		image.setVisibility(View.GONE);
	}

	public void Search(String param) {

		String compareTo;
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			Node node_num = nl.item(i).getAttributes().getNamedItem("no");

			if (node_num != null && node_num.getNodeValue().equals(param)) {

				compareTo = nl.item(i).getAttributes().getNamedItem("body")
						.getNodeValue();
				compareTo = compareTo.replaceAll("(\\t|\\r?\\n)+", " ");
				textView_song.setText(compareTo);
				SetVisibiltity();

				System.out.println(compareTo + "ibm");
				setStat("0");
				break;
			} else {
				setStat("1");
			}

		}

	}

}
