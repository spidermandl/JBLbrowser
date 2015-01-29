package com.jbl.browser.model;

public class MusicModel {

	/**音乐类型：音效*/
	public static final int TYPE_EFFECT = 0;
	/**音乐类型：背景音乐*/
	public static final int TYPE_MUSIC = 1;
	
	/**循环标识：不循环*/
	public static final int LOOP_FALSE = 0;
	/**循环标识：循环*/
	public static final int LOOP_TRUE = 1;
	
	private String musicName0;
	private String musicName;  
	private int musicType;
	private int musicLoopState;
	private boolean pauseBg = false;
	private int delayTime = 0;
	
	public String getMusicName0() {
		return musicName0;
	}
	public void setMusicName0(String musicName0) {
		this.musicName0 = musicName0;
	}
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public int getMusicType() {
		return musicType;
	}
	public void setMusicType(int musicType) {
		this.musicType = musicType;
	}
	public int getMusicLoopState() {
		return musicLoopState;
	}
	public void setMusicLoopState(int musicLoopState) {
		this.musicLoopState = musicLoopState;
	}
	public boolean isPauseBg() {
		return pauseBg;
	}
	public void setPauseBg(boolean pauseBg) {
		this.pauseBg = pauseBg;
	}
	public int getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
}
