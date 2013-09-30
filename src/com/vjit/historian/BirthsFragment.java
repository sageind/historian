package com.vjit.historian;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.util.ByteArrayBuffer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("SdCardPath")
public class BirthsFragment extends Fragment implements TabListener {
	private Fragment biFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ListView birthsList = new ListView(getActivity());
		byte[] buffer = new byte[51200];

		File file = new File("/data/data/com.vjit.historian/BIRTHS.html");
		InputStream streamIN = null;
		try {
			streamIN = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			streamIN.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			streamIN.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String sectionData = new String(buffer);
		Document doc = Jsoup.parse(sectionData);
		List<String> parsedText = new ArrayList<String>();
		Elements liElements = doc.select("ul>li");

		for (Element liElm : liElements) {
			parsedText.add(liElm.text() + "\n");
		}

		ArrayAdapter<String> parsedBirths = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1, parsedText);
		birthsList.setAdapter(parsedBirths);
		return birthsList;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		birthsPull();

	}
	
	private void birthsPull() {
		String BIRTHS_BASE_URL = "http://en.wikipedia.org//w/api.php?action=parse&format=txt&prop=text&uselang=en&section=2&contentformat=text%2Fplain&contentmodel=text&mobileformat=html&noimages=&mainpage=";
		Calendar cal = Calendar.getInstance();
		String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
				Locale.UK);
		int dateVal = cal.get(Calendar.DAY_OF_MONTH);

		final String finalVal = monthName + "_" + dateVal;
		String BIRTHS_URL = BIRTHS_BASE_URL + "&page=" + finalVal;

		 new AsyncBirthsPull().execute(BIRTHS_URL);
	}

	public class AsyncBirthsPull extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... BIRTHS_URL) {
			try {
				URL url = new URL(BIRTHS_URL[0]);
				URLConnection con = url.openConnection();
				InputStream streamIN = con.getInputStream();
				BufferedInputStream buf = new BufferedInputStream(streamIN);

				ByteArrayBuffer buffer = new ByteArrayBuffer(51200);
				int current = 0;
				while ((current = buf.read()) != -1) {
					buffer.append((byte) current);
				}

				File BIRTHS_FILE = new File(
						"/data/data/com.vjit.historian/BIRTHS.html");
				FileOutputStream streamOUT = new FileOutputStream(BIRTHS_FILE);
				streamOUT.write(buffer.toByteArray());
				streamOUT.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Let onTabSelected manage the state
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		biFragment = new BirthsFragment();
		ft.add(android.R.id.content, biFragment);
		ft.attach(biFragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(biFragment);
	}

}