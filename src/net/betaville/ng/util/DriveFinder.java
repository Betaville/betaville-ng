package net.betaville.ng.util;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

import net.betaville.ng.SettingsPreferences;


/**
 * @author Skye Book
 *
 */
public class DriveFinder {
	public static ArrayList<File> getPartitions(){
		ArrayList<File> roots = new ArrayList<File>();
		if(System.getProperty("os.name").toLowerCase().startsWith("win")){

			String[] names = {"A:\\","B:\\","C:\\","D:\\","E:\\","F:\\","G:\\","H:\\",
					"I:\\","J:\\","K:\\","L:\\","M:\\","N:\\","O:\\","P:\\","Q:\\",
					"R:\\","S:\\","T:\\","U:\\","V:\\","W:\\","X:\\","Y:\\","Z:\\"};

			for(int i=0; i<names.length; i++){
				File f = new File(names[i]);
				if(f.exists()){
					roots.add(f);
				}
			}
		}
		else if(System.getProperty("os.name").toLowerCase().startsWith("mac")){
			File volFolder = new File("/Volumes/");
			for(String volume : volFolder.list()){
				File f = new File("/Volumes/"+volume+"/");
				roots.add(f);
			}
		}
		else if(System.getProperty("os.name").toLowerCase().startsWith("lin")){}
		return roots;
	}

	public static File getHomeDir(){
		File homeDir = FileSystemView.getFileSystemView().getHomeDirectory();
		
		// Java thinks the Windows home directory is the desktop folder.
		if(System.getProperty("os.name").startsWith("Windows")){
			homeDir = new File(homeDir.getParent());
		}
		
		return homeDir;
	}
	
	/**
	 * Gets the Betaville folder located at ~/.betaville.
	 * @return The file representing this folder, including
	 * a slash at the end
	 */
	public static File getBetavilleFolder(){
		return new File(getHomeDir().toString()+"/.betaville/");
	}
	
	public static File getServerFolder(){
		File serverFolder = new File(getBetavilleFolder().toString()+"/"+SettingsPreferences.getServerIP()+"/");
		if(!serverFolder.exists()) serverFolder.mkdir();
		return serverFolder;
	}
	
	public static File getBookmarksFolder(){
		File bookmarksFolder = new File(getServerFolder().toString()+"/bookmarks/");
		if(!bookmarksFolder.exists()) bookmarksFolder.mkdir();
		return bookmarksFolder;
	}
}
