package com.shatteredpixel.shatteredpixeldungeon.services.updates;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.utils.Callback;

import java.util.Date;

public class Updates {

	public static UpdateService service;

	public static boolean supportsUpdates(){
		return service != null;
	}

	private static Date lastCheck = null;
	private static final long CHECK_DELAY = 1000*60*60; //1 hour

	public static boolean supportsUpdatePrompts(){
		return supportsUpdates() && service.supportsUpdatePrompts();
	}

	public static boolean supportsBetaChannel(){
		return supportsUpdates() && service.supportsBetaChannel();
	}

	// --- MODIFIED: Stripped out the internet check ---
	public static void checkForUpdate(){
		// Instantly forces the game to stop looking for updates
		lastCheck = null;
		updateData = null;
	}

	public static void launchUpdate( AvailableUpdateData data ){
		// Disabled so the game cannot try to download anything
	}

	private static AvailableUpdateData updateData = null;

	// --- MODIFIED: Always tell the game there is NO update available ---
	public static boolean updateAvailable(){
		return false;
	}

	// --- MODIFIED: Always return null ---
	public static AvailableUpdateData updateData(){
		return null;
	}

	public static void clearUpdate(){
		updateData = null;
		lastCheck = null;
	}

	public static boolean supportsReviews() {
		return false; // Disabled the review prompt so it doesn't link to the official game
	}

	public static void launchReview(Callback callback){
		// Just silently close the review prompt if it somehow opens
		if (callback != null) {
			callback.call();
		}
	}

	public static void openReviewURI(){
		// Disabled so your mod doesn't open the official Google Play page
	}

}