package org.teacake.monolith.apk;
import android.media.SoundPool;
import android.media.JetPlayer;
class SoundPoolEvent
{
	public SoundPoolEvent(int eventType,int eventSound)
	{
		this.eventType = eventType;
		this.eventSound = eventSound;
	}
	public int eventType;
	public int eventSound;
	
	public static final int SOUND_PLAY=0;
	public static final int SOUND_STOP=1;
	public static final int SOUND_MUSIC_PLAY=2;
	public static final int SOUND_MUSIC_PAUSE=3;
	public static final int SOUND_MUSIC_STOP=4;
	public static final int SOUND_MUSIC_RESUME=5;
}
class SoundStatus
{
	public SoundStatus()
	{
		
	}
	public static final int STATUS_LOOPING_NOT_STARTED=0;
	public static final int STATUS_LOOPING_PAUSED=1;
	public static final int STATUS_LOOPING_PLAYING=2;
	
	
}
public class SoundPoolManager implements Sound
{
	SoundPoolManager(android.content.Context context)
	{
		this.context = context;
		soundEvents = new java.util.LinkedList<SoundPoolEvent>();
		sounds = new java.util.HashMap<Integer, Boolean>();
		handles = new java.util.HashMap<Integer, Integer>();
		streamIds =  new java.util.HashMap<Integer, Integer>();
		isRunning = false;
		finished = false;
		this.musicPlayer =JetPlayer.getJetPlayer();
		this.musicPlayer.loadJetFile(context.getResources().openRawResourceFd(R.raw.monolith3));
		byte segmentId = 0;

		this.musicPlayer.queueJetSegment(0, -1, -1, 0, 0, segmentId++);
	}
	public void addSound(int resid, boolean isLooping)
	{
		
		sounds.put(resid, new Boolean(isLooping));
		
	}
	


	public void startSound()
	{
		this.soundPool = new android.media.SoundPool(this.sounds.size(),android.media.AudioManager.STREAM_MUSIC,100);
		java.util.Iterator <Integer> iterator = sounds.keySet().iterator();
		
		while(iterator.hasNext())
		{
			int soundid = iterator.next().intValue();
			int soundhandle = this.soundPool.load(this.context, soundid, 1);
			handles.put(new Integer(soundid), new Integer(soundhandle));
		}
				

	}
	public void stopSound()
	{
		try
		{
			java.util.Iterator <Integer> iterator = sounds.keySet().iterator();
		
			while(iterator.hasNext())
			{
				
				int soundid = iterator.next().intValue();

				this.soundPool.pause( this.handles.get(soundid).intValue());
				this.soundPool.stop(this.handles.get(soundid).intValue());
					
				
				
			}		
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			try
			{
				this.musicPlayer.pause();
			}
			catch(Exception e1)
			{
				
			}
			try
			{
				this.musicPlayer.release();
			}
			catch(Exception e2)
			{
				
			}
			try
			{
				this.soundPool.release();
			}
			catch(Exception e3)
			{
				
			}
						
		}


	}

	public int currentPlayer;
	private boolean isRunning;
	private java.util.HashMap<Integer, Boolean> sounds;
	private java.util.HashMap<Integer, Integer> handles;
	private java.util.HashMap<Integer, Integer> streamIds;
	private android.content.Context context;
	private java.util.LinkedList<SoundPoolEvent > soundEvents;
	private java.util.HashMap<Integer, android.media.MediaPlayer> mediaPlayers;
	public void stopSound(int resid)
	{

	}
	public void playSound(int resid)
	{
		if(soundEvents!=null)
		{
			try
			{
				android.media.AudioManager mgr = (android.media.AudioManager) context.getSystemService(android.content.Context.AUDIO_SERVICE); 
				int streamVolume = mgr.getStreamVolume(android.media.AudioManager.STREAM_MUSIC); 
				int streamID = soundPool.play(handles.get( resid).intValue(), streamVolume, streamVolume, 1, 0, 1.0f);
				int maxvolume = mgr.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC);
				mgr.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, maxvolume, 0);
				this.streamIds.put(resid, streamID);

			}
			catch(Exception e)
			{
				
			}
		}
	}
	public void startMusic(int resid)
	{
		
		this.musicPlayer.play();	
		
	}
	public void stopMusic(int resid)
	{
		this.musicPlayer.pause();	
	}
	public void pauseMusic(int resid)
	{
		this.musicPlayer.pause();
	}
	public void resumeMusic(int resid)
	{
		this.musicPlayer.play();
	}
	SoundPool soundPool;
	JetPlayer musicPlayer;
	
	boolean finished = false;
	
}
