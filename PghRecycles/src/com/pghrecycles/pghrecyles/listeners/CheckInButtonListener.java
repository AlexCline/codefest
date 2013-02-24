package com.pghrecycles.pghrecyles.listeners;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.pghrecycles.pghrecycles.R;
import com.pghrecycles.pghrecycles.data.providers.PointsProvider;

public class CheckInButtonListener extends Activity implements OnClickListener {
	private PointsProvider pointsProvider = null;
	
	public CheckInButtonListener(PointsProvider pointsProvider) {
		this.pointsProvider = pointsProvider;
	}

	@Override
	public void onClick(View v) {
		View parent = (View)v.getParent();
		
		int newPoints = pointsProvider.addPoints(100);

	    ((TextView)parent.findViewById(R.id.pointsHolder)).setText(Integer.toString(newPoints));
	}

}
