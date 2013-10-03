phonegap3.x.x-plugins-ExtractZipFile
====================================

ExtractZipFile modified plugin for phonegap 3.x.x

All credits goes to:
--------------------
https://github.com/phonegap/phonegap-plugins


What I have changed:

-> extract method declaration

-> run in own thread


be sure to check out the description of the original plugin:

https://github.com/phonegap/phonegap-plugins/tree/master/iOS/ExtractZipFile

https://github.com/phonegap/phonegap-plugins/tree/master/Android/ExtractZipFile



quick javascript example:
-----------------------------------------
		cordova.exec(function(winParam) {
			
			// success
			
		}, function(error) {
		
      // error
			
		}, "ZipPlugin", "extract", [ZipFileWithPath, destination]);
