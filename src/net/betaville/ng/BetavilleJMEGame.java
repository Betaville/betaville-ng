package net.betaville.ng;

import java.util.List;

import net.skyebook.jme2loader.JME2Loader;

import org.apache.log4j.Logger;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.scene.Spatial;

import edu.poly.bxmc.betaville.jme.map.MapManager;
import edu.poly.bxmc.betaville.jme.map.Rotator;
import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.EmptyDesign;
import edu.poly.bxmc.betaville.model.ModeledDesign;
import edu.poly.bxmc.betaville.model.Wormhole;

/**
 * A jme3 version of the Betaville application
 *  
 * @author jannes
 */
public class BetavilleJMEGame extends SimpleApplication {

	private static final Logger logger = Logger.getLogger(BetavilleJMEGame.class.getName());
	
	private Wormhole wormhole;
	private List<Design> designs;
	
//	ColorRGBA backgroundColor = ColorRGBA.White;
	ColorRGBA backgroundColor = new ColorRGBA(0.62745f, 0.76078f, 0.98824f, 1);
	private FilterPostProcessor fpp;
    private FogFilter fog;
	private boolean fogEnabled = false;
	
	public BetavilleJMEGame(Wormhole startLocation, List<Design> designs) {
		this.wormhole = startLocation;
		this.designs = designs;
	}
	
	@Override
	public void simpleInitApp() {
		// Don't show debugging information
		if (!Betaville.DEBUG) {
			setDisplayStatView(false);
			setDisplayFps(false);
		}

		// Set distance
		cam.setFrustumPerspective(45, (float)cam.getWidth() / cam.getHeight(), 0.01f, 1000);
		
		// Lighting
		initLighting();
		viewPort.setBackgroundColor(backgroundColor);
		initFog();
		
		// Events
		initInput();
		
		// Set camera position
		UTMCoordinate startPosition = wormhole.getLocation();
		MapManager.setUTMZone(startPosition.getLonZone(), startPosition.getLatZone());
		//Vector3f cameraPosition = MapManager.locationToBetaville(startPosition);
		//cam.setLocation(cameraPosition);
		MapManager.adjustOffsets(startPosition);
		
		// Legacy model support
		assetManager.registerLoader(JME2Loader.class, "jme");
		
		String dataFolder = SettingsPreferences.getDataFolder().getPath();
		assetManager.registerLocator(dataFolder, FileLocator.class.getName());
		
		logger.info(dataFolder);
		logger.info("Designs: " + designs.size());
		
		// Enable wireframe
		Material wire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		wire.setColor("Color", ColorRGBA.Gray);
		wire.getAdditionalRenderState().setWireframe(true);
		
		// Add all designs to the rootNode
		int max = designs.size();
		for (int i = 0; i < max; ++i) {
			Design design = designs.get(i);
			
			if (design instanceof EmptyDesign) {
				continue;
			}
			// Blacklist $4603 NYC_Water_1
			if (design.getID() == 4603) {
				continue;
			}
			
			Spatial model = assetManager.loadModel(design.getFilepath());
			if(design instanceof ModeledDesign) {
				ModeledDesign modeledDesign = (ModeledDesign) design;
				Vector3f pos = MapManager.locationToBetaville(design.getCoordinate());
				model.setLocalTranslation(pos);
				model.setLocalRotation(Rotator.fromThreeAngles(modeledDesign.getRotationX(), modeledDesign.getRotationY(), modeledDesign.getRotationZ()));
				
				// Set wireframe
				//model.setMaterial(wire);
				
				// Attach to root node
				rootNode.attachChild(model);
			} else {
				logger.warn("Not a ModeledDesign");
			}
		}
		
		//logger.debug("Camera: " + cameraPosition);
	}
	
	private void initLighting() {
		ColorRGBA diffuseLightColor = new ColorRGBA(1f, 1f, 1f, 1f);
		ColorRGBA diffuseLightColor2 = new ColorRGBA(.3f,.4f,.45f,.3f);
		
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setDirection(new Vector3f(.25f, -.85f, .75f));
		directionalLight.setColor(diffuseLightColor);

		DirectionalLight directionalLight2 = new DirectionalLight();
		directionalLight2.setDirection(new Vector3f(-.25f,.85f,-.75f));
		directionalLight2.setColor(diffuseLightColor2);

		rootNode.addLight(directionalLight);
		rootNode.addLight(directionalLight2);
	}
	
	private void initFog() {
		fpp = new FilterPostProcessor(assetManager);
		fog = new FogFilter();
		fog.setFogColor(backgroundColor);
		fog.setFogDistance(3000);
		fog.setFogDensity(1.5f);
		fpp.addFilter(fog);
		if (fogEnabled) {
			viewPort.addProcessor(fpp);
		}
	}

	private void initInput() {
		ActionListener actionListener = new ActionListener() {
			public void onAction(String name, boolean keyPressed, float tpf) {
				if (name.equals("ToggleFog") && keyPressed) {
					if (fogEnabled){
						fogEnabled = false;
						viewPort.removeProcessor(fpp);
					} else{
						fogEnabled = true;
						viewPort.addProcessor(fpp);
					}
				}
			}
		};

		AnalogListener analogListener = new AnalogListener() {
			public void onAnalog(String name, float isPressed, float tpf) {
				if(name.equals("DensityUp")){
					fog.setFogDensity(fog.getFogDensity() + 0.1f);
					System.out.println("Fog density : " + fog.getFogDensity());
				}
				if(name.equals("DensityDown")){
					fog.setFogDensity(fog.getFogDensity() - 0.1f);
					System.out.println("Fog density : " + fog.getFogDensity());
				}
				if(name.equals("DistanceUp")){
					fog.setFogDistance(fog.getFogDistance() + 100);
					System.out.println("Fog Distance : " + fog.getFogDistance());
				}
				if(name.equals("DistanceDown")){
					fog.setFogDistance(fog.getFogDistance() - 100);
					System.out.println("Fog Distance : " + fog.getFogDistance());
				}
			}
		};

		inputManager.addMapping("ToggleFog", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("DensityUp", new KeyTrigger(KeyInput.KEY_Y));
		inputManager.addMapping("DensityDown", new KeyTrigger(KeyInput.KEY_H));
		inputManager.addMapping("DistanceUp", new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping("DistanceDown", new KeyTrigger(KeyInput.KEY_J));
		
		inputManager.addListener(actionListener, "ToggleFog");
		inputManager.addListener(analogListener, "DensityUp","DensityDown","DistanceUp","DistanceDown");
	}

}