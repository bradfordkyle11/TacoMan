package com.kmbapps.tacoman;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kmbapps.tacoman.Helpers.AdController;

public class AndroidLauncher extends AndroidApplication implements AdController {
	protected AdView mAdView;
	protected RelativeLayout mLayout;
	protected boolean adLoaded = false;
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-1809615004520302/4845247071";
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		View mGameView = initializeForView(new TacoMan(this), config);

		mLayout = new RelativeLayout(this);
//		mAdView = new AdView(this);
//		mAdView.setVisibility(View.VISIBLE);
//		mAdView.setAdSize(AdSize.BANNER);
//		mAdView.setAdUnitId("ca-app-pub-1809615004520302/4845247071");
//		mAdView.setAdListener(new AdListener() {
//			@Override
//			public void onAdFailedToLoad(int i) {
//				super.onAdFailedToLoad(i);
//				mAdView.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onAdLoaded() {
//					super.onAdLoaded();
//				mAdView.setVisibility(View.VISIBLE);
//			}
//		});

		mLayout.addView(mGameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		RelativeLayout.LayoutParams mAdParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		mAdParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		showBannerAd();
		mLayout.addView(mAdView);
		setContentView(mLayout);
		//initialize(new TacoMan(), config);
	}

	@Override
	protected void onResume(){
		super.onResume();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		if (mAdView != null) {
			mAdView.resume();
		}
//		getWindow().getDecorView().setSystemUiVisibility(
//				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//						| View.SYSTEM_UI_FLAG_FULLSCREEN
//						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}

	public void setupAds(){
		mAdView = new AdView(this);
		mAdView.setVisibility(View.INVISIBLE);
		mAdView.setBackgroundColor(0x00000000);
		mAdView.setAdUnitId(BANNER_AD_UNIT_ID);
		mAdView.setAdSize(AdSize.SMART_BANNER);
		mAdView.setAdListener(new AdListener() {
			@Override
			public void onAdFailedToLoad(int i) {
				super.onAdFailedToLoad(i);
				hideBannerAd();
				adLoaded = false;
			}

			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
				adLoaded = true;
				System.out.println("Ad loaded\n");
			}
		});
	}

	@Override
	public void showBannerAd() {
		if (mAdView == null){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setupAds();
					mAdView.setVisibility((View.VISIBLE));
					AdRequest adRequest = new AdRequest.Builder().addTestDevice("A127310EFE19D504207566F24866F68C")
				/*.addTestDevice("D50677A6A2897969B402262D75D7FDA3")*/.build();
					mAdView.loadAd(adRequest);
				}
			});
		}
		else if (mAdView.getVisibility() == View.INVISIBLE) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (!adLoaded) {
						setupAds();
						mAdView.setVisibility((View.VISIBLE));
						AdRequest adRequest = new AdRequest.Builder().addTestDevice("A127310EFE19D504207566F24866F68C")
				/*.addTestDevice("D50677A6A2897969B402262D75D7FDA3")*/.build();
						mAdView.loadAd(adRequest);
					}
					else {
						mAdView.setVisibility((View.VISIBLE));
					}
				}
			});
		}
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mAdView.setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mAdView != null) {
			mAdView.pause();
		}
	}
}
