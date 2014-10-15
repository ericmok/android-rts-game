package game.androidgame2;

import java.util.ArrayList;

public class AnimationPack {
	private ArrayList<String> textureNames;
	private ArrayList<Integer> durations;
	
	private String currentTexture = null;
	private long startTime = 0;
	
	public AnimationPack() {
		textureNames = new ArrayList<String>();
		durations = new ArrayList<Integer>();
	}
	
	public void addFrame(String textureName, int duration) {
		textureNames.add(textureName);
		int accumulation = 0;
		if ( durations.size() > 0) {
			duration = durations.get( durations.size() - 1 ) + duration;
		}
		durations.add(duration);
	}
	
	public void startPlaying(long startTime) {
		if (currentTexture == null) {
			currentTexture = textureNames.get(0);
		}
		this.startTime = startTime;
	}
	
	
	public void update(long currentTime) {
		// Find max duration 
		for (int i = 0; i < durations.size(); i++) {
			if (durations.get(i) > currentTime - startTime) {
				currentTexture = textureNames.get(i);
			}
		}
	}
	
	public String getCurrentFrame() {
		return currentTexture;
	}
}
