package net.skyebook.jme2loader;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

/**
 * @author Skye Book
 *
 */
public class TextureConverter {

	private static final Logger logger = Logger.getLogger(TextureConverter.class.getName());

	private static ArrayList<com.jme.image.Image.Format> unsupportedImageFormat;

	static{
		unsupportedImageFormat = new ArrayList<com.jme.image.Image.Format>();
		for(com.jme.image.Image.Format format : com.jme.image.Image.Format.values()){
			try{
				if(Image.Format.valueOf(format.name())==null){
					unsupportedImageFormat.add(format);
				}
			}catch (IllegalArgumentException e) {
				unsupportedImageFormat.add(format);
			}
		}
	}
	
	public static boolean isImageFormatSupported(com.jme.image.Image.Format format){
		return !unsupportedImageFormat.contains(format);
	}

	public static Texture twoToThree(com.jme.image.Texture jme2Texture, AssetManager assetManager){
		
		Texture newTexture = null;

		// If the texture is stored in the file, we need to extract it manually
		if(jme2Texture.isStoreTexture()){
			try{
				if(jme2Texture.getImage()==null){
					logger.warning("WTF, null texture coming from jME2?");
					return null;
				}
			}catch(NullPointerException e){
				logger.warning("WTF, null texture coming from jME2?");
				return null;
			}
			Image jme3Image = twoToThree(jme2Texture.getImage());
			if(jme3Image==null){
				return null;
			}
			if(jme2Texture instanceof com.jme.image.Texture1D){
				System.out.println("1D texture");
				return new Texture2D(jme3Image);
			}
			else if(jme2Texture instanceof com.jme.image.Texture2D){
				return new Texture2D(jme3Image);
			}
			else if(jme2Texture instanceof com.jme.image.Texture3D){
				System.out.println("1D texture");
				newTexture = new Texture2D(jme3Image);
			}
			else{
				System.out.println("Conversion failed");
				return null;
			}
		}
		else{
			File imageLocation = new File(jme2Texture.getImageLocation());
			
			assetManager.registerLocator("binary/", ClasspathLocator.class);
			
			newTexture = assetManager.loadTexture(imageLocation.getName());
		}
		
		
		transferTextureCoordinates(jme2Texture, newTexture);
		return newTexture;
	}

	private static void transferTextureCoordinates(com.jme.image.Texture jme2Texture, Texture jme3Texture){
//		Vector3f jme2TextureCoordinates = jme2Texture.getTranslation();
		
		// convert to a jME3 Vector3f, now what?
//		com.jme3.math.Vector3f jme3TextureCoordintes = JME2Loader.twoToThree(jme2TextureCoordinates);
	}

	public static Image twoToThree(com.jme.image.Image image){
		//Image.Format.
		if(isImageFormatSupported(image.getFormat())){
			return new Image(Image.Format.valueOf(image.getFormat().name()), image.getWidth(), image.getHeight(), image.getDepth(), image.getData());
		}
		else{
			logger.warning(image.getFormat().name() + " is not supported in jME3!");
			return null;
		}
	}

}
