package com.pghrecycles.pghrecycles.data.providers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.format.Time;
import android.util.Log;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;
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
//	public static final String KEY_PROJECT = "project";
//    public static final String KEY_NAME = "name";
//    private static final String TAG = "";
//    private static final String DATABASE_PATH = "data/data/com.pghrecycles.pghrecycles/databases/pickup.db";
    private static final String DATABASE_PATH = "data/data/com.pghrecycles.pghrecycles/pickup.db";
    private static final String DATABASE_NAME = "pickup.db";
//	private static final String DATABASE_NAME = "pickup";
//	private static final String DATABASE_NAME = "/sdcard/pickup.db";
    private static final String DATABASE_TABLE = "pickup";
    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_CREATE = "";
    // cols
    private static final String DBASE_COL_STREET = "street";
    private static final String DBASE_COL_LEFT_LOW = "left_low";
    private static final String DBASE_COL_LEFT_HIGH = "left_high";
    private static final String DBASE_COL_RIGHT_LOW = "right_low";
    private static final String DBASE_COL_RIGHT_HIGH = "right_high";
    private static final String DBASE_COL_HOOD = "hood";
    private static final String DBASE_COL_ZIP = "zip";
    private static final String DBASE_COL_DIVISION = "division";
    private static final String DBASE_COL_STREET_BASE = "street_name_base";
    private static final String DBASE_COL_YEAR = "year";
    private static final String DBASE_COL_DAY = "day";
//        "create table project (codProject integer primary key autoincrement, "
//        + "name text);";
	
	
	// information about the global application environment
    private static Context context; 
//    private DatabaseOpener dbOpener;
    private DataBaseHelper dbOpener;
    private SQLiteDatabase db;
    
	public DBPickupInfoProvider(Context context) {
		DBPickupInfoProvider.context = context;
//		dbOpener = new DatabaseOpener(context);
		dbOpener = new DataBaseHelper(context);
//		dbOpener.initializeDatabase();
		db = dbOpener.getReadableDatabase();
//		db = dbOpener.getWritableDatabase();
		
	}
	
//	public DBPickupInfoProvider open() throws SQLException {
//        db = dbOpener.getWritableDatabase();
//        return this;
//    }
//
    public void close() {
        dbOpener.close();
    }
	
	@Override
	public PickupInfo getPickupInfo(LocationInfo locationInfo, Time yearQuery) {
		
		int addressNumQuery = locationInfo.getAddressNum();
		int zipQuery = locationInfo.getZip();
		String streetQuery = locationInfo.getStreet();
//		String streetBase = locationInfo.getStreetBase();
		
//		String selectStr = "SELECT * FROM "+DATABASE_TABLE+" WHERE '"+DBASE_COL_LEFT_LOW+"' <= "+addressNumQuery+" AND '"+DBASE_COL_LEFT_HIGH+"' >= "+addressNumQuery+" OR '"+DBASE_COL_RIGHT_LOW+"' <= "+addressNumQuery+" AND '"+DBASE_COL_RIGHT_HIGH+"' >= "+addressNumQuery+" AND '"+DBASE_COL_YEAR+"' = "+yearQuery.year;
		String selectStr = "SELECT * FROM "+DATABASE_TABLE+" WHERE (("+DBASE_COL_LEFT_LOW+" <= "+addressNumQuery+" AND "+DBASE_COL_LEFT_HIGH+" >= "+addressNumQuery+") OR ("+DBASE_COL_RIGHT_LOW+" <= "+addressNumQuery+" AND "+DBASE_COL_RIGHT_HIGH+" >= "+addressNumQuery+")) AND "+DBASE_COL_ZIP+" = "+zipQuery+" AND "+DBASE_COL_STREET+" = "+streetQuery;
//		String selectStr = "SELECT * FROM "+DATABASE_TABLE+" WHERE (('"+DBASE_COL_LEFT_LOW+"' <= "+addressNumQuery+" AND '"+DBASE_COL_LEFT_HIGH+"' >= "+addressNumQuery+") OR ('"+DBASE_COL_RIGHT_LOW+"' <= "+addressNumQuery+" AND '"+DBASE_COL_RIGHT_HIGH+"' >= "+addressNumQuery+"))";
//		String selectStr = "SELECT * FROM "+DATABASE_TABLE;
		Cursor results = db.rawQuery(selectStr, null);
		
		// TODO if we have multiple results, choose the row for which oddness or evenness matches the left_low or right_low
		// TODO proper try / catch handling (for missing results)
		
		// for now, use 1st result, get relevant fields
		try {
			results.moveToFirst();
			int numResults = results.getCount();
			if (numResults > 1) {
				// more than 1 res. 
			}
			int leftLow = results.getInt(results
					.getColumnIndex(DBASE_COL_LEFT_LOW));
			int leftHigh = results.getInt(results
					.getColumnIndex(DBASE_COL_LEFT_HIGH));
			int rightLow = results.getInt(results
					.getColumnIndex(DBASE_COL_RIGHT_LOW));
			int rightHigh = results.getInt(results
					.getColumnIndex(DBASE_COL_RIGHT_HIGH));
			int zip = results.getInt(results.getColumnIndex(DBASE_COL_ZIP));
			String divisionStr = results.getString(results
					.getColumnIndex(DBASE_COL_DIVISION));
			Division division = null;
			if (divisionStr.equalsIgnoreCase(Division.CENTRAL.toString())) {
				division = Division.CENTRAL;
			} else if (divisionStr.equalsIgnoreCase(Division.SOUTHERN
					.toString())) {
				division = Division.SOUTHERN;
			} else if (divisionStr
					.equalsIgnoreCase(Division.EASTERN.toString())) {
				division = Division.EASTERN;
			} else if (divisionStr.equalsIgnoreCase(Division.NORTHERN
					.toString())) {
				division = Division.NORTHERN;
			} else {
				// shouldn't happen, invalid division name
				assert false;
			}
			String streetBase = results.getString(results
					.getColumnIndex(DBASE_COL_STREET_BASE));
			String street = results.getString(results
					.getColumnIndex(DBASE_COL_STREET));
			String hood = results.getString(results
					.getColumnIndex(DBASE_COL_HOOD));
			int year = results.getInt(results.getColumnIndex(DBASE_COL_YEAR));
			int day = results.getInt(results.getColumnIndex(DBASE_COL_DAY));

			PickupInfo pickupInfo = new PickupInfo(leftLow, leftHigh, rightLow,
					rightHigh, zip, hood, division, streetBase, street, year,
					day);
			return pickupInfo;
		} catch (CursorIndexOutOfBoundsException e) {
			// no results returned
			PickupInfo pickupInfo = new PickupInfo();
			return pickupInfo;
		}
	}
	
	
	
	static class DataBaseHelper extends SQLiteOpenHelper{
		 
	    //The Android's default system path of your application database.
	    private static String DB_PATH = DATABASE_PATH;
	 
	    private static String DB_NAME = DATABASE_NAME;
	 
	    private SQLiteDatabase myDataBase; 
	 
	    private final Context myContext;
	 
	    /**
	     * Constructor
	     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	     * @param context
	     */
	    public DataBaseHelper(Context context) {
	 
	    	super(context, DB_NAME, null, 1);
	        this.myContext = context;
	    }	
	 
	  /**
	     * Creates a empty database on the system and rewrites it with your own database.
	     * */
	    public void createDataBase() throws IOException{
	 
	    	boolean dbExist = checkDataBase();
	 
	    	if(dbExist){
	    		//do nothing - database already exist
	    	}else{
	 
	    		//By calling this method and empty database will be created into the default system path
	               //of your application so we are gonna be able to overwrite that database with our database.
	        	this.getReadableDatabase();
	 
	        	try {
	 
	    			copyDataBase();
	 
	    		} catch (IOException e) {
	 
	        		throw new Error("Error copying database");
	 
	        	}
	    	}
	 
	    }
	 
	    /**
	     * Check if the database already exist to avoid re-copying the file each time you open the application.
	     * @return true if it exists, false if it doesn't
	     */
	    private boolean checkDataBase(){
	 
	    	SQLiteDatabase checkDB = null;
	 
	    	try{
	    		String myPath = DB_PATH + DB_NAME;
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	    	}catch(SQLiteException e){
	 
	    		//database does't exist yet.
	 
	    	}
	 
	    	if(checkDB != null){
	 
	    		checkDB.close();
	 
	    	}
	 
	    	return checkDB != null ? true : false;
	    }
	 
	    /**
	     * Copies your database from your local assets-folder to the just created empty database in the
	     * system folder, from where it can be accessed and handled.
	     * This is done by transfering bytestream.
	     * */
	    private void copyDataBase() throws IOException{
	 
	    	//Open your local db as the input stream
	    	InputStream myInput = myContext.getAssets().open(DB_NAME);
	 
	    	// Path to the just created empty db
	    	String outFileName = DB_PATH + DB_NAME;
	 
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	 
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	 
	    }
	 
	    public void openDataBase() throws SQLException{
	 
	    	//Open the database
	        String myPath = DB_PATH + DB_NAME;
	    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	    }
	 
	    @Override
		public synchronized void close() {
	 
	    	    if(myDataBase != null)
	    		    myDataBase.close();
	 
	    	    super.close();
	 
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) {
	 
		}
	 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
		}
	 
	        // Add your public helper methods to access and get content from the database.
	       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
	       // to you to create adapters for your views.
	 
	}
	
	
}
