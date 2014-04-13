package org.teacake.monolith.apk;

public interface Sound {
	
	public void addSound(int resid, boolean isLooping);
	public void startSound();
	public void stopSound();
	public void stopSound(int resid);
	public void playSound(int resid);
	public void startMusic(int resid);
	public void stopMusic(int resid);
	public void pauseMusic(int resid);
	public void resumeMusic(int resid);

}
