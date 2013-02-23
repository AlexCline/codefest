package com.pghrecycles.pghrecycles.data.providers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

import com.pghrecycles.pghrecycles.data.LocationInfo;
import com.pghrecycles.pghrecycles.data.PickupInfo;

/**
 * Puls PickupInfo from a local DB.
 * (note: code adopted partialy in part from http://stackoverflow.com/questions/5901536/read-database-android-sqlite)
 * 
 * @author alexander.p.conrad@gmail.com
 *
 */
public class DBPickupInfoProvider implements PickupInfoProvider {
	
	// constants for interacting with the dbase
	public static final String KEY_PROJECT = "project";
    public static final String KEY_NAME = "name";
    private static final String TAG = "DBCreation";
    private static final String DATABASE_NAME = ".db";
    private static final String DATABASE_TABLE = "project";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "";
//        "create table project (codProject integer primary key autoincrement, "
//        + "name text);";
	
	
	// information about the global application environment
    private final Context context; 
    private DatabaseOpener dbOpener;
    private SQLiteDatabase db;
    
	public DBPickupInfoProvider(Context context) {
		this.context = context;
		dbOpener = new DatabaseOpener(context);
		db = dbOpener.getReadableDatabase();
		
	}
	
//	public DBPickupInfoProvider open() throws SQLException {
//        db = dbOpener.getWritableDatabase();
//        return this;
//    }
//
//    public void close() {
//        dbOpener.close();
//    }
	
	@Override
	public PickupInfo getPickupInfo(LocationInfo locationInfo, Time year) {
		
		// 
		
		// select * from db where street = locationInfo.getStreet(), (leftLow <= locationInfo.getStreet#?() && leftHigh >= locationInfo.getStreet#?()) || ( or for right )
		
		// WHERE streetAddr
		// BETWEEN 'leftLow' AND 'leftHigh'
		// OR
		// BETWEEN 'rightLow' AND 'rightHigh'
		
		
		
		
		
	}
	
	/**
	 * subclass of SQLiteOpenHelper, purpose is to provide access to back-end dbase
	 * 
	 * @author alexander.p.conrad@gmail.com
	 *
	 */
	private static class DatabaseOpener extends SQLiteOpenHelper {
		
        DatabaseOpener(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	// we shouldn't be calling this method; assert: database already exists!
//            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	// we shouldn't be calling this method; database should not be modifiable by application?
//            Log.w(TAG, "Upgrading database from version " + oldVersion 
//                    + " to "
//                    + newVersion + ", which will destroy all old data");
//            db.execSQL("DROP TABLE IF EXISTS projecto");
//            onCreate(db);
        }
        
//        @Override
//        public void onOpen(SQLiteDatabase db) {
//        	
//        }
        
    } 
	
}
