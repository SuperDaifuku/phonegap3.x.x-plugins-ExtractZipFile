/**
 * modified
 * Unzip files package
 * 
 * Updated to work with android and phonegap v2.7.0: 29.07.2013 punger
 * Updated to work in own thread so it doesn't block the ui-thread punger
 * 
 * @author		punger
 * @see			http://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java
 */

package com.phonegap.plugin.ExtractZipFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONArray;
import org.json.JSONException;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import android.os.Environment;
import android.util.Log;

public class ExtractZipFilePlugin extends CordovaPlugin {

	private static final int BUFFER_SIZE = 4096;
	private boolean success = true;
	
	@Override
	public boolean execute(String arg0, final JSONArray args, final CallbackContext callbackContext) throws JSONException
	{
		cordova.getThreadPool().execute(new Runnable()
		{
			public void run(){

		    	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		    	
		    	String filename = "";
		    	try {
		    		filename = baseDir+args.getString(0);
		    	}
		    	catch(JSONException e){
		    		success = false;
		    		callbackContext.error("ArgumentError");
		    	}

				String[] dirToSplit=filename.split(File.separator);
				String destDirectory="";
				for(int i=0;i<dirToSplit.length-1;i++)
				{
					destDirectory+=dirToSplit[i]+File.separator;
				}
				
				destDirectory = destDirectory.substring(0, destDirectory.length() -1);
				
				File destDir = new File(destDirectory);
		        if (!destDir.exists()) {
		            destDir.mkdir();
		        }
		        
		        try {
			        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(filename));
			        ZipEntry entry = zipIn.getNextEntry();

			        while (entry != null) {
			            String filePath = destDirectory + File.separator + entry.getName();
			            
			            if (!entry.isDirectory()) {
			                extractFile(zipIn, filePath);
			            } else {
			                File dir = new File(filePath);
			                dir.mkdir();
			            }
			            zipIn.closeEntry();
			            entry = zipIn.getNextEntry();
			        }
			        zipIn.close();	
		        }
		        catch(IOException e1){
		        	success = false;
		        	callbackContext.error("IOException");
		        }
		        
		        // delete zip-file
		        File zipFile = new File(filename);
		        boolean deleted = zipFile.delete();

		        callbackContext.success();
			}
		});

		return success;
	}
	
	/**
     * Extracts a zip entry (file entry)
     * 
     * 
     * @param 		zipIn
     * @param 		filePath
     * @throws 		IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
    	
    	String[] path = filePath.split("/");
    	String thePath = "";
    	
    	for(int i = 1; i < path.length-1; i++){
    		thePath+= File.separator+path[i];
    	}
    	
    	File f = new File(thePath);
    	if(!f.exists())
    		f.mkdirs();

    	
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
