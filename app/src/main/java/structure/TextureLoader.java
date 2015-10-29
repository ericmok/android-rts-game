package structure;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

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

	public static class TextureAnimation {
		public String name;
		public ArrayList<TextureFrame> textureFrames;

		public VertexBuffer textureCoordsBuffer;

		public TextureAnimation() {
			textureFrames = new ArrayList<TextureFrame>();
		}
		
		public TextureFrame addFrame(int frameNumber, int glHandle) {
			TextureFrame frame = new TextureFrame(frameNumber, glHandle);
			textureFrames.add(frame);
			return frame;
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

		public Texture texture = new Texture();
		
		public TextureFrame(int frameNumber, int glTexture) {
			this.frameNumber = frameNumber;

			texture.glHandle = glTexture;
		}
		
	}
	
	public static class LetterTexture {
		public char character;
		public Bitmap bitmap;

		public Texture texture = new Texture();

		public float widthRatio;
	}

	public HashMap<Character, LetterTexture> letterTextures = new HashMap<Character, LetterTexture>(64);

	/**
	 * Examples:
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
		m.animations = new HashMap<String, TextureAnimation>();
	}

	/**
	 * Read a JSON config file. Format must be a mapping
	 * @param fileName
	 * @return
	 * @throws java.io.IOException
	 * @throws java.io.FileNotFoundException
	 * @throws org.json.JSONException
	 */
	public JSONObject readJSONFile(String fileName) throws IOException, JSONException {

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


	public void loadAssetsInRoot(String rootFolderName)
			throws FileNotFoundException, JSONException {
		try {
			String[] animations = m.context.getAssets().list(rootFolderName);
			Log.i("ANIMATIONS FOLDER", "ANIMATIONS FOLDER " + animations.length);

			FolderContext folderContext = new FolderContext(rootFolderName);

			for (int i = 0; i < animations.length; i++) {
				String fileName = rootFolderName + "/" + animations[i];
				exploreFolder(fileName, folderContext);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean containsFrame(String[] filenames) {

		for (int i = 0; i < filenames.length; i++) {
			if (isFrame(filenames[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean isFrame(String filename) {
		try {
			String toParse = filename.substring(0, filename.lastIndexOf('.'));
			Integer.parseInt(toParse);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void loadConfigFilesIfAny(String directory, FolderContext folderContext) throws JSONException, IOException {

		String[] fileNames = m.context.getAssets().list(directory);

		for (int i = 0; i < fileNames.length; i++) {
			if (fileNames[i].equals("config.json")) {
				JSONObject obj = readJSONFile(directory + "/" + fileNames[i]);

				Iterator<String> itr = obj.keys();

				while (itr.hasNext()) {
					String key = itr.next();
					folderContext.scope.put(key, obj.get(key));
				}
			}
		}
	}

	public void exploreFolder(String directory, FolderContext folderContext) throws JSONException, IOException {
		String[] directories = m.context.getAssets().list(directory);

		FolderContext childContext = new FolderContext(directory);
		childContext.parent = folderContext;

		loadConfigFilesIfAny(directory, childContext);

		// TODO: Load .json config vars into childContext

		if (containsFrame(directories)) {
			// Load textures
			loadImages(directory, childContext);
		}
		else {
			for (int i = 0; i < directories.length; i++) {
				exploreFolder(directory + "/" + directories[i], childContext);
			}
		}
	}

	public void loadImages(String directory, FolderContext folderContext) throws IOException {

		TextureAnimation animation = new TextureAnimation();
		animation.name = directory;

		animations.put(directory, animation);

		String[] imageFileNames = m.context.getAssets().list(directory);

		for (int i = 0; i < imageFileNames.length; i++) {

			if (!isFrame(imageFileNames[i])) {
				// TODO: Check if json file
				continue;
			}

			String imagePath = directory + "/" + imageFileNames[i];

			Bitmap bitmap = BitmapFactory.decodeStream(m.context.getAssets().open(imagePath));

			//boolean rotated = (boolean)scope.get("pointingUpAtZeroDegrees");
			Object rotatedOption = folderContext.scopeSearch("pointingUpAtZeroDegrees");

			//boolean rotated = true; // TODO: Make FolderContext objects
			boolean pointingUpAtZeroDegrees = false;

			if (rotatedOption != null) {
				pointingUpAtZeroDegrees = (boolean) rotatedOption;
			}

			int glHandle = this.generateGLTextureFromBitmap(bitmap, pointingUpAtZeroDegrees);

			int frameNumber = Integer.parseInt(imageFileNames[i].substring(0, imageFileNames[i].lastIndexOf('.')));
			TextureFrame frame = animation.addFrame(frameNumber, glHandle);

			Object boundsX1 = folderContext.scopeSearch("x1");
			Object boundsY1 = folderContext.scopeSearch("y1");
			Object boundsX2 = folderContext.scopeSearch("x2");
			Object boundsY2 = folderContext.scopeSearch("y2");

			// TODO: Create gl vertex buffer

			if (boundsX1 != null) {
				frame.texture.offsetX1 = ((float)(int)boundsX1) / bitmap.getWidth();
			}
			if (boundsY1 != null) {
				frame.texture.offsetY1 = ((float)(int)boundsY1) / bitmap.getHeight();
			}
			if (boundsX2 != null) {
				frame.texture.offsetX2 = ((float)(int)boundsX2) / bitmap.getWidth();
			}
			if (boundsY2 != null) {
				frame.texture.offsetY2 = ((float)(int)boundsY2) / bitmap.getHeight();
			}


			if (boundsX1 != null && boundsX2 != null && boundsY1 != null && boundsY2 != null) {
				float[] coords = new float[] {
					frame.texture.offsetX1,
					frame.texture.offsetY2,
					frame.texture.offsetX2,
					frame.texture.offsetY2,
					frame.texture.offsetX1,
					frame.texture.offsetY1,
					frame.texture.offsetX2,
					frame.texture.offsetY1
				};

				animation.textureCoordsBuffer = new VertexBuffer(6);
				animation.textureCoordsBuffer.initialize();
				animation.textureCoordsBuffer.bufferData(coords);
			}
		}

		Collections.sort(animation.textureFrames, new Comparator<TextureFrame>() {
			@SuppressLint("NewApi")
			@Override
			public int compare(TextureFrame lhs, TextureFrame rhs) {
				return Integer.compare(lhs.frameNumber, rhs.frameNumber);
			}
		});
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
			//letterTexture.glTexture = texture;
			letterTexture.texture.glHandle = texture;
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
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setAlpha(255);
		//center version
		paint.setTextAlign(Paint.Align.CENTER);
		//paint.setTextAlign(Paint.Align.LEFT);

        // Some of the constants here are eye-balled
		int width = (int) (paint.measureText(text, 0 , text.length()) + 0.5f);
		float baseline = (int) (-paint.ascent() + 0.5f);
		int height = (int) (paint.descent() + (-paint.ascent()) + 10);

		Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(image);

        // Hacky...
        canvas.scale(0.6f, 0.5f);
        canvas.translate(width / 2, height / 2);
		//canvas.drawText((CharSequence)text, 0, text.length(), width/2, baseline, paint);
		
		// At height 0, the text is drawn with the baseline on the top border
		//center version
		//canvas.drawText((CharSequence)text, 0, text.length(), width/2, height - paint.descent(), paint);
		
		// explicit version
		//canvas.drawText((CharSequence)text, 0, text.length(), 0, height - paint.descent(), paint);
        canvas.drawText((CharSequence) text, 0, text.length(), 0, baseline, paint);

		return image;
	}
	

	public int generateGLTextureFromBitmap(Bitmap bitmap, boolean pointingUpAtZeroDegrees) {
		
		if (pointingUpAtZeroDegrees != true) {
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

	public TextureAnimation getTextureAnimation(String textureName) {
		return animations.get(textureName);
	}

	private static class FolderContext {
		public FolderContext parent = null;
		public String uri;
		public HashMap<String, Object> scope = new HashMap<String, Object>();

		public FolderContext() {}

		public FolderContext(String uri) { this.uri = uri; }

		public Object scopeSearch(String key) {
			FolderContext search = this;
			Object ret = scope.get(key);

			while (ret == null && search.parent != null) {
				search = search.parent;
				ret = search.scope.get(key);
			}

			return ret;
		}
	}
}
