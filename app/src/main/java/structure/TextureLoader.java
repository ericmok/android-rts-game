package structure;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Loads textures. Remembers their handles.
 * Reloads all previously loaded textures.
 * Textures are not expected to be invalidated individually...
 */
public class TextureLoader {
			
	public static class TexturePack {
		public String name;
		
		// Animation Name to Animation Map 
		public HashMap<String, TextureAnimation> textureAnimations;
		
		public TexturePack() {
			this.name = "";
			this.textureAnimations = new HashMap<String, TextureAnimation>();
		}
	}
	
	public static class TextureAnimation {
		public String name;
		public ArrayList<TextureFrame> textureFrames;
		
		public TextureAnimation() {
			textureFrames = new ArrayList<TextureFrame>();
		}
		
		public void addFrame(int frameNumber, int glHandle) {
			TextureFrame frame = new TextureFrame(frameNumber, glHandle);
			textureFrames.add(frame);
		}
		
		/**
		 * Return the frame in the animation with the highest frame number but below
		 * the a certain number
		 * 
		 * @param limit
		 * @return
		 */
		public TextureFrame maxFrameUnderCriteria(int limit) {
			//int winningFrameNumber = textureFrames.get(0).frameNumber;
			//int winningFrameGLHandle = textureFrames.get(0).glTexture;
			
			TextureFrame maxFrame = textureFrames.get(0);

			for (int frameIndex = 1; frameIndex < textureFrames.size(); frameIndex++) {

				TextureFrame currentFrame = textureFrames.get(frameIndex);

				if ((currentFrame.frameNumber > maxFrame.frameNumber) && (currentFrame.frameNumber <= limit)) {
					maxFrame = currentFrame;
				}
			}

			return maxFrame;
		}
	}
	
	/**
	 * Name GLTexture
	 */
	public static class TextureFrame {
		public int frameNumber;
		
		public int glTexture;
		//public boolean rotated;
		
		public TextureFrame(int frameNumber, int glTexture) {
			this.frameNumber = frameNumber;

			this.glTexture = glTexture;
			//this.rotated = rotated;
		}
		
	}
	
	public static class LetterTexture {
		public char character;
		public Bitmap bitmap;
		
		public int glTexture;
		public float widthRatio;
	}
	
	/** For storing OpenGL texture ids **/
	//private HashMap<String, Texture> textures;
	
	public HashMap<Character, LetterTexture> letterTextures = new HashMap<Character, LetterTexture>(64);
	
	/**
	 * Deprecate?
	 */
	public HashMap<String, TexturePack> texturePacks;
	
	/**
	 * Examples:<br/>
	 * <ul>
	 *  <li>"Troops/Moving"</li>
	 *  <li>"Troops/Idling"</li>
	 * </ul>
	 */
	public HashMap<String, TextureAnimation> animations;
	
	/** For getting resources **/
	private Context context;
	
	/** Replacement for this **/
	private TextureLoader m = this;
	
	
	public TextureLoader(Context context) {
		m.context = context;
		//m.textures = new HashMap<String, Texture>();
		m.texturePacks = new HashMap<String, TexturePack>();
		
		m.animations = new HashMap<String, TextureAnimation>();
	}
	
	public Bitmap getBitmapFromPath(String texturePath, boolean rotated) {
		return null;
	}
	
	/**
	 * Read a JSON config file. Format must be a mapping
	 * @param fileName
	 * @return
	 * @throws java.io.IOException
	 * @throws java.io.FileNotFoundException
	 * @throws org.json.JSONException
	 */
	public JSONObject readJSONFile(String fileName) throws IOException, FileNotFoundException, JSONException {

			Log.i("CONFIG CONFIG", "CONFIG CONFIG: " + fileName);
			
			InputStream inputStream = m.context.getAssets().open(fileName);
			
			InputStreamReader reader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(reader);
			StringBuilder out = new StringBuilder();
			
			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				out.append(line);
			}
			
			JSONTokener jsonTokener = new JSONTokener(out.toString());
			
			Log.i("JSON", "JSON: " + out.toString());
			
			return (JSONObject) jsonTokener.nextValue();
	}
	
	
	public void loadAllAssetsInFolder(String folderName, boolean rotated) {
		try {
			String[] animations = m.context.getAssets().list(folderName);
			Log.i("ANIMATIONS FOLDER", "ANIMATIONS FOLDER " + animations.length);
			
			for (int i = 0; i < animations.length; i++) {

				boolean rotationConfiguration = rotated;
				
				try {
					JSONObject obj = readJSONFile(folderName + "/" + animations[i] + "/" + "config.json");
					rotationConfiguration = obj.getBoolean("axisAligned");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FileNotFoundException fe) {
					// Do nothing
				}
				
				loadTextureFromAssets(folderName + "/" + animations[i], rotationConfiguration);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads all bitmaps in the double nested directory
	 * Example: Troop > Dying > Frame1
	 * TODO: Clean up this code
	 * 
	 * @param texturePackName
	 * @param rotated
	 * @return
	 */
	public int loadTextureFromAssets(String texturePackName, boolean rotated) {
		
		Log.i("LOADTEXTUREFROMASSETS", "Loading");
		try {
			TexturePack texturePack = new TexturePack();
			texturePack.name = texturePackName;
			
			Log.i("LOADTEXTUREFROMASSETS", "texturePackName: " + texturePackName);
			
			// type > (states) > 1,2,3
			String[] animationStates = m.context.getAssets().list(texturePackName);

			
			// animations > type > (loop through states) > 1,2,3
			for (int i = 0; i < animationStates.length; i++) {

				//Log.i("rotationConfig1", "rotationConfig1: " + rotated);
				
				TextureAnimation animation = new TextureAnimation();
				animation.name = animationStates[i];
			
				texturePack.textureAnimations.put(animationStates[i], animation);
				animations.put(texturePackName + "/" + animationStates[i], animation);
				
				Log.i("LOADTEXTUREFROMASSETS", "animations key: " + texturePackName + "/" + animationStates[i]);
				//Log.i("LOADTEXTUREFROMASSETS", "textureAnimationName: " + animation.name);
				
				// animations > type > states > (1,2,3)
				String[] bitmapFrameNames = m.context.getAssets().list(texturePackName + "/" + animationStates[i]);				

				//Log.i("LOADTEXTUREFROMASSETS", "bitmapFrameNames length: " + bitmapFrameNames.length);
				
				for (int f = 0; f < bitmapFrameNames.length; f++) {
					String bitmapFileLocation = texturePackName + "/" + animationStates[i] + "/" + bitmapFrameNames[f];					
					
					Bitmap bitmap = BitmapFactory.decodeStream(m.context.getAssets().open(bitmapFileLocation));
					//Log.i("generate from rotationConfig", "generate from rotationConfig: " + rotated);
					int glHandle = this.generateGLTextureFromBitmap(bitmap, rotated);
					
					int frameNumber = Integer.parseInt( bitmapFrameNames[f].substring(0, bitmapFrameNames[f].lastIndexOf('.')) ); 
					animation.addFrame(frameNumber, glHandle);

					//Log.i("LOADTEXTUREFROMASSETS", "frameNumber: " + frameNumber);
					//Log.i("LOADTEXTUREFROMASSETS", "animation.textureFrames length: " + animation.name + ", " + animation.textureFrames.size());
				}
				
				Collections.sort(animation.textureFrames, new Comparator<TextureFrame>() {
					@SuppressLint("NewApi")
					@Override
					public int compare(TextureFrame lhs, TextureFrame rhs) { return Integer.compare(lhs.frameNumber, rhs.frameNumber); }
				});
				
				texturePacks.put(texturePackName, texturePack);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
	}
		
	
	/**
	 * Loads all letters of alphabet
	 */
	public void loadLetterTextures() {
		StringBuilder sb = new StringBuilder(1);
		String alphabet = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-*/=[]{}\"\'<>!@#$%^&*()?,.";
		
		for (int i = 0; i < alphabet.length(); i++) {
			// reset builder
			sb.setLength(0);
			sb.append(alphabet.charAt(i));
			
			// generate bitmap from single letter
			Bitmap bitmap = generateTextBitmap(sb);
			int texture = generateGLTextureFromBitmap(bitmap, false);
			
			// create the letter texture
			LetterTexture letterTexture = new LetterTexture();
			letterTexture.bitmap = bitmap;
			letterTexture.character = alphabet.charAt(i);
			letterTexture.glTexture = texture;
			letterTexture.widthRatio = (float)bitmap.getWidth() / bitmap.getHeight();
			
			letterTextures.put(alphabet.charAt(i), letterTexture);
		}
	}
	
	/**
	 * TODO: Figure out how to save aspect ratio of text
	 * http://stackoverflow.com/questions/8799290/convert-string-text-to-bitmap
	 * @param text
	 * @return
	 */
	public Bitmap generateTextBitmap(StringBuilder text) {		
		Paint paint = new Paint();
		paint.setTextSize(144);
		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(240, 240, 240));
		paint.setAlpha(170);
		//center version
		//paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextAlign(Paint.Align.LEFT);
		 
		int width = (int) (paint.measureText((CharSequence)text, 0 , text.length()) + 0.5f);
		float baseline = (int) (-paint.ascent() + 0.5f);
		int height = (int) (baseline + paint.descent() + 0.5f );
		
		Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(image);
		//canvas.drawText((CharSequence)text, 0, text.length(), width/2, baseline, paint);
		
		// At height 0, the text is drawn with the baseline on the top border
		//center version
		//canvas.drawText((CharSequence)text, 0, text.length(), width/2, height - paint.descent(), paint);
		
		// explicit version
		//canvas.drawText((CharSequence)text, 0, text.length(), 0, height - paint.descent(), paint);
		canvas.drawText((CharSequence)text, 0, text.length(), 0, baseline, paint);

		return image;
	}
	

	public int generateGLTextureFromBitmap(Bitmap bitmap, boolean rotated) {
		
		if (rotated == true) {
			//Matrix matrix = new Matrix();
			//matrix.postRotate(90);
			
			// Since bitmap is readonly, we create a mutable one
			Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas tempCanvas = new Canvas(result); 
			
			// Draw bitmap onto mutable bitmap
			tempCanvas.rotate(90, bitmap.getWidth()/2, bitmap.getHeight()/2);
			tempCanvas.drawBitmap(bitmap, 0, 0, null);
			
			// Swap!
			bitmap = result;
		}			
		
		int[] textureHandle = new int[1];
	
		GLES20.glGenTextures(1, textureHandle, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		
		if (textureHandle[0] == 0)
	    {
	        throw new RuntimeException("Error loading texture.");
	    }
	
		//Log.d("TextureHandle", Integer.toString(textureHandle[0]) );
		
		return textureHandle[0];
	}
	
	/**
	 * Creates OpenGL textures out of the resource.<br/>
	 * Preconditions: Needs to have OpenGL context
	 * textureLoader.loadTexture(UNIT1_TEXTURE, R.drawable.sprite1, true);
	 * 
	 * @param textureName
	 * @param resource
	 */
//	public int loadTexture(String textureName, int resource, boolean rotated) {
//		Bitmap bitmap = BitmapFactory.decodeResource(m.context.getResources(), resource);
//		
//		if (rotated == true) {
//			//Matrix matrix = new Matrix();
//			//matrix.postRotate(90);
//			
//			// Since bitmap is readonly, we create a mutable one
//			Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//			Canvas tempCanvas = new Canvas(result); 
//			
//			// Draw bitmap onto mutable bitmap
//			tempCanvas.rotate(90, bitmap.getWidth()/2, bitmap.getHeight()/2);
//			tempCanvas.drawBitmap(bitmap, 0, 0, null);
//			
//			// Swap!
//			bitmap = result;
//		}			
//		
//		int[] textureHandle = new int[1];
//	
//		GLES20.glGenTextures(1, textureHandle, 0);
//		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
//		
//		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
//		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//		
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
//		bitmap.recycle();
//		
//		if (textureHandle[0] == 0)
//	    {
//	        throw new RuntimeException("Error loading texture.");
//	    }
//		
//		if (textureName.length() != 0 && textureName != null) {
//			Texture texture = new Texture(textureName, resource, textureHandle[0], rotated);
//			m.textures.put(textureName, texture);
//		}
//				
//		Log.d("TextureHandle", Integer.toString(textureHandle[0]) );
//		
//		return textureHandle[0];
//	}
//	
//	public Texture getTexture(String textureName) {
//		return m.textures.get(textureName);
//	}
//	
//	public int getTextureGL(String textureName) {
//		return m.textures.get(textureName).glTexture;
//	}
//	
//	public void reloadAllTextures() {
//		for (Texture texture : m.textures.values()) {
//			m.loadTexture(null, texture.resourceID, texture.rotated);
//		}
//	}
}
