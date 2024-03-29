package net.skyebook.jme2loader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.jme.light.DirectionalLight;
import com.jme.light.Light;
import com.jme.light.PointLight;
import com.jme.light.SpotLight;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.dummy.DummySystemProvider;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.export.xml.XMLImporter;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

/**
 * 
 */

/**
 * Only supports TriMesh
 * jME3 geometry is what gets added to a scene (it contains a mesh object)
 * @author Skye Book
 *
 */
public class JME2Loader implements AssetLoader{
	
	private static final Logger logger = Logger.getLogger(JME2Loader.class);

	public static com.jme3.scene.Spatial twoToThree(Spatial spatial, AssetManager assetManager){
		
		com.jme3.scene.Spatial returnable;
		ArrayList<com.jme3.light.Light> newLights = new ArrayList<com.jme3.light.Light>();
		
		// process lighting
		if(spatial.getRenderState(StateType.Light) != null){
			LightState lightState = (LightState)spatial.getRenderState(StateType.Light);
			for(Light light : lightState.getLightList()){
				if(light instanceof DirectionalLight){
					newLights.add(twoToThree((DirectionalLight)light));
					logger.warn("Creating a DirectionalLight");
				}
				else if(light instanceof PointLight){
					newLights.add(twoToThree((PointLight)light));
					logger.warn("Creating a PointLight");
				}
				else if(light instanceof SpotLight){
					newLights.add(twoToThree((SpotLight)light));
					logger.warn("Creating a SpotLight");
				}
			}
		}
		
		if(spatial instanceof Node){
			com.jme3.scene.Node node = new com.jme3.scene.Node(spatial.getName());
			logger.info("Node name: " + spatial.getName() + ", children: " + ((Node)spatial).getQuantity());
			if(((Node)spatial).getQuantity()>0){
				for(Spatial child : ((Node)spatial).getChildren()){
					node.attachChild(twoToThree(child, assetManager));
				}
			}
			// do translation/rotation/scale
			transferTranslationScaleRotation(node, spatial);
			returnable=node;
		}
		else if(spatial instanceof Geometry){
			Mesh mesh = new Mesh();
			logger.info("Geometry name: " + spatial.getName());
			
			
			// Handle Geometry

			// transfer vertex buffer
			FloatBuffer vertexBuffer = ((Geometry)spatial).getVertexBuffer().duplicate();
			vertexBuffer.rewind();
			mesh.setBuffer(Type.Position, 3, vertexBuffer);

			// transfer normal buffer if it exists
			FloatBuffer normalBuffer = ((Geometry)spatial).getNormalBuffer();
			if(normalBuffer != null){
				normalBuffer = normalBuffer.duplicate();
				normalBuffer.rewind();
				mesh.setBuffer(Type.Normal, 3, normalBuffer);
			}

			// transfer color buffer if it exists
			FloatBuffer colorBuffer = ((Geometry)spatial).getColorBuffer();
			if(colorBuffer != null){
				colorBuffer = colorBuffer.duplicate();
				colorBuffer.rewind();
				mesh.setBuffer(Type.Color, 4, colorBuffer);
			}

			// transfer index buffer
			if(spatial instanceof TriMesh){
				IntBuffer indexBuffer = ((TriMesh)spatial).getIndexBuffer().duplicate();
				indexBuffer.rewind();
				mesh.setBuffer(Type.Index, 3, indexBuffer);
			}

			// replicate the model bounds
			if(((Geometry)spatial).getModelBound()!=null){
				if(((Geometry)spatial).getModelBound() instanceof com.jme.bounding.BoundingBox){
					mesh.setBound(new com.jme3.bounding.BoundingBox());
				}
				else if(((Geometry)spatial).getModelBound() instanceof com.jme.bounding.BoundingSphere){
					mesh.setBound(new com.jme3.bounding.BoundingSphere());
				}
				mesh.updateBound();
			}
			else{
				logger.info("No BoundingVolume found, defaulting to a BoundingBox");
				mesh.setBound(new com.jme3.bounding.BoundingBox());
				mesh.updateBound();
			}


			com.jme3.scene.Geometry geometry = new com.jme3.scene.Geometry(spatial.getName());
			geometry.setMesh(mesh);

			Material mat;

			// Handle Materials
			if(spatial.getRenderState(StateType.Material)!=null){
				mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
				MaterialState ms = (MaterialState)spatial.getRenderState(StateType.Material);
				mat.setColor("Diffuse", twoToThree(ms.getDiffuse()));
				mat.setColor("Ambient", twoToThree(ms.getAmbient()));
				mat.setColor("Specular", twoToThree(ms.getSpecular()));
				mat.setFloat("Shininess", ms.getShininess());
				mat.setTransparent(true);
				mat.getAdditionalRenderState().setAlphaTest(true);
				mat.setBoolean("UseMaterialColors" , true);
				mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

				if(spatial.getRenderState(StateType.Texture)!=null){
//					TextureState textureState = (TextureState)spatial.getRenderState(StateType.Texture);
//					logger.log(Level.WARNING, "There are {0} textures set.", textureState.getNumberOfSetTextures());
//					for(int i=0; i<TextureState.getNumberOfTotalUnits(); i++){
//						com.jme.image.Texture jme2Texture = textureState.getTexture(i);
//						if(jme2Texture!=null){
//							//Texture jme3Texture = TextureConverter.twoToThree(jme2Texture, assetManager);
//							
//							//if(jme2Texture!=null) mat.setTexture("DiffuseMap", jme3Texture);
//						}
//						
//					}
				}
				/*
				if(spatial.getRenderState(StateType.Texture)!=null){
					if(((TextureState)spatial.getRenderState(StateType.Texture)).getTexture()!=null){
						Texture texture = twoToThree(((TextureState)spatial.getRenderState(StateType.Texture)).getTexture());
						if(texture!=null) mat.setTexture("DiffuseMap", texture);
					}
					else{
						System.out.println("Hey, what's going on here?!  The texture on some texture state is null");
					}
				}
				 */
			}
			else{
				mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
				mat.setColor("Color", ColorRGBA.Blue);
				logger.warn("Unshaded material");
			}

			// Blend modes
//			if(spatial.getRenderState(StateType.Blend)!=null) {
//				BlendState blendState = ((BlendState)spatial.getRenderState(StateType.Blend));
//			}


			if(spatial.getRenderState(StateType.Wireframe)!=null){
				mat.getAdditionalRenderState().setWireframe(true);
			}

			geometry.setMaterial(mat);

			// do translation/rotation/scale
			transferTranslationScaleRotation(geometry, spatial);

			returnable = geometry;
		}
		else{
			logger.warn("Unrecognized type");
			return null;
		}
		
		for(com.jme3.light.Light newLight : newLights){
			returnable.addLight(newLight);
		}
		
		return returnable;
	}



	private static void transferTranslationScaleRotation(com.jme3.scene.Spatial newSpatial, com.jme.scene.Spatial oldSpatial){
		newSpatial.setLocalTranslation(twoToThree(oldSpatial.getLocalTranslation()));
		newSpatial.setLocalRotation(twoToThree(oldSpatial.getLocalRotation()));
		newSpatial.setLocalScale(twoToThree(oldSpatial.getLocalScale()));
	}

	public static com.jme3.math.ColorRGBA twoToThree(com.jme.renderer.ColorRGBA color){
		return new com.jme3.math.ColorRGBA(color.r, color.g, color.b, color.a);
	}
	
	public static com.jme3.light.DirectionalLight twoToThree(DirectionalLight light){
		com.jme3.light.DirectionalLight newLight = new com.jme3.light.DirectionalLight();
		newLight.setColor(twoToThree(light.getDiffuse()));
		return newLight;
	}
	
	public static com.jme3.light.PointLight twoToThree(PointLight light){
		com.jme3.light.PointLight newLight = new com.jme3.light.PointLight();
		newLight.setColor(twoToThree(light.getDiffuse()));
		newLight.setPosition(twoToThree(light.getLocation()));
		return newLight;
	}
	
	public static com.jme3.light.SpotLight twoToThree(SpotLight light){
		com.jme3.light.SpotLight newLight = new com.jme3.light.SpotLight();
		newLight.setColor(twoToThree(light.getDiffuse()));
		newLight.setPosition(twoToThree(light.getLocation()));
		newLight.setDirection(twoToThree(light.getDirection()));
		return newLight;
	}

	public static com.jme3.math.Vector3f twoToThree(com.jme.math.Vector3f vector3f){
		return new Vector3f(vector3f.x, vector3f.y, vector3f.z);
	}

	public static Matrix3f twoToThree(com.jme.math.Matrix3f m){
		return new Matrix3f(m.m00, m.m01, m.m02, m.m10, m.m11, m.m12, m.m20, m.m21, m.m22);
	}

	public static Quaternion twoToThree(com.jme.math.Quaternion q){
		return new Quaternion(q.x, q.y, q.z, q.w);
	}

	@Override
	public Object load(AssetInfo assetInfo) throws IOException{

		try {
			DummySystemProvider dsp = new DummySystemProvider();
			com.jme.system.DisplaySystem.setSystemProvider(dsp);
			if(com.jme.system.DisplaySystem.getDisplaySystem().getRenderer()==null){
				logger.warn("There should be a Renderer");
			}
		} catch (IllegalStateException e){
			logger.info("System provider already set");
		}

		Object loadedObject = null;	

		// load the jME2 Spatial based on whether its an xml or binary file
		if(assetInfo.getKey().getExtension().equals("xml")){
			loadedObject = XMLImporter.getInstance().load(assetInfo.openStream());
		}
		else if(assetInfo.getKey().getExtension().equals("jme")){
			loadedObject = BinaryImporter.getInstance().load(assetInfo.openStream());
		}

		if(loadedObject instanceof com.jme.scene.Spatial){
			// run the converter
			long start = System.currentTimeMillis();
			com.jme3.scene.Spatial spatial = twoToThree(((com.jme.scene.Spatial)loadedObject), assetInfo.getManager());
			logger.info("Conversion took " + (System.currentTimeMillis() - start) + "ms");
			return spatial;
		}
		else {
			return null;
		}

	}

}
