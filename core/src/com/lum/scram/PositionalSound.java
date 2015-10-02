package com.lum.scram;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;

public class PositionalSound
{
	long id;
	
	private float pan;
	private float volume;
	private float timeSincePlay=100;
	private Vector3 location;
	
	public Boolean limitPlaying=false;
	public static float GLOBAL_PITCH=1f;
	public Sound sound;
	public int maxPan = 32;
	
	public PositionalSound(String path) {
		sound = Gdx.audio.newSound(Gdx.files.internal(path));
	}
	
	public void setLocation(Vector3 newLocation) {
		timeSincePlay+=Gdx.graphics.getDeltaTime();
		
		location = newLocation;
		//Calculate location of sound vs camera position
		Vector3 cameraPosition = Core.mainCam.position;
		
		float calcDistance = cameraPosition.dst(location);
		
		float calPan = 0;
	
		if(cameraPosition.x > location.x) //sound is on left
		{
			//Calculate pan left side
			if(cameraPosition.x - location.x > maxPan)//for max sound LEFT SIDE
			{
				calPan = -1;
			}
			else if(cameraPosition.x - location.x < maxPan)
			{
				calPan = -(float)(cameraPosition.x - location.x)/maxPan;
			}
			else//right side
			{
				//Calculate pan right side
				if(cameraPosition.x + location.x > maxPan)//for max sound LEFT SIDE
				{
					calPan = 1;
				}
				else if(cameraPosition.x + location.x < maxPan)
				{
					calPan = (float)(cameraPosition.x - location.x)/maxPan;
				}		
			}

		}
		pan = calPan;

		//volume calulcation
		calcDistance = 1 - calcDistance / maxPan;
		if(calcDistance < 0 ) calcDistance=0;
	
		volume = calcDistance;
		sound.setPan(id, pan, volume);
		sound.setPitch(id, GLOBAL_PITCH);
	
	}
	public void play() {
		
		if(limitPlaying&&timeSincePlay>0.25f) {
			id = sound.play(volume, GLOBAL_PITCH, pan);
			timeSincePlay=0;
		}else if(!limitPlaying){
			id = sound.play(volume, GLOBAL_PITCH, pan);
		}
	}
}