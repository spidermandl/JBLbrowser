package com.zixun.piratesfantasy.model;

public class ShareModel {

	/**新浪微博分享*/
	public final static int SHARE_WAY_SINA = 1;
	/**邮箱分享*/
	public final static int SHARE_WAY_MAIL = 2;
	/**短信分享*/
	public final static int SHARE_WAY_SMS = 3;
	
	/**邀请好友*/
	public final static int SHARE_TYPE_REWARD = 1;
	/**获得卡片*/
	public final static int SHARE_TYPE_GETCARD = 2;
	/**战斗胜利*/
	public final static int SHARE_TYPE_WINBATTLE = 3;
	/**BOSS战斗胜利*/
	public final static int SHARE_TYPE_WINBOSS = 4;
	/**升级*/
	public final static int SHARE_TYPE_UP = 5;
	
	/**奖励类型：不奖励*/
	public final static int REWARD_TYPE_NULL = 0;
	/**奖励类型：友情pt*/
	public final static int REWARD_TYPE_PT = 1;
	/**奖励类型：卡片*/
	public final static int REWARD_TYPE_CARD = 2;
	/**奖励类型：道具*/
	public final static int REWARD_TYPE_ITEM = 3;
	
	private int shareWay = 0; //分享方式
	private int shareType = 0; //分享类型（邀请、战斗胜利、获得卡片等）
	private String shareContent; //分享内容
	private String sharePicPath; //分享图片
	private String sharePicName;
	private String shareRewardCode; //邀请码
	private int shareLevel = 0;  //等级提升分享时的等级
	private int shareRewardType = 0;  //奖励类型
	private int shareRewardPt = 0;  //奖励的友情PT数
	private int shareShowEnd = 0; //是否显示关键信息
	public int getShareWay() {
		return shareWay;
	}
	public void setShareWay(int shareWay) {
		this.shareWay = shareWay;
	}
	public int getShareType() {
		return shareType;
	}
	public void setShareType(int shareType) {
		this.shareType = shareType;
	}
	public String getShareContent() {
		return shareContent;
	}
	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}
	public String getSharePicPath() {
		return sharePicPath;
	}
	public void setSharePicPath(String sharePicPath) {
		this.sharePicPath = sharePicPath;
	}
	public String getSharePicName() {
		return sharePicName;
	}
	public void setSharePicName(String sharePicName) {
		this.sharePicName = sharePicName;
	}
	public String getShareRewardCode() {
		return shareRewardCode;
	}
	public void setShareRewardCode(String shareRewardCode) {
		this.shareRewardCode = shareRewardCode;
	}
	public int getShareLevel() {
		return shareLevel;
	}
	public void setShareLevel(int shareLevel) {
		this.shareLevel = shareLevel;
	}
	public int getShareRewardType() {
		return shareRewardType;
	}
	public void setShareRewardType(int shareRewardType) {
		this.shareRewardType = shareRewardType;
	}
	public int getShareRewardPt() {
		return shareRewardPt;
	}
	public void setShareRewardPt(int shareRewardPt) {
		this.shareRewardPt = shareRewardPt;
	}
	public int getShareShowEnd() {
		return shareShowEnd;
	}
	public void setShareShowEnd(int shareShowEnd) {
		this.shareShowEnd = shareShowEnd;
	}
	
	
}
