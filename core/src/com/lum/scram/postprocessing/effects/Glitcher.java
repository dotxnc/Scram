package com.lum.scram.postprocessing.effects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.lum.scram.postprocessing.PostProcessorEffect;
import com.lum.scram.postprocessing.filters.GlitchEffect;

public class Glitcher extends PostProcessorEffect{
	
	public Glitcher(){
		glitchEffect = new GlitchEffect();
	}
	
	GlitchEffect glitchEffect;
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rebind() {
		// TODO Auto-generated method stub
		glitchEffect.rebind();
	}

	@Override
	public void render(FrameBuffer src, FrameBuffer dest) {
		// TODO Auto-generated method stub
		restoreViewport(dest);
		glitchEffect.setInput(src).setOutput(dest).render();
	}
	
	public void setTime(float time)
	{
		glitchEffect.setTime(time);
	}
	
	public void setAmplitude(float amp)
	{
		glitchEffect.setAmplitude(amp);
	}
	
	public void setSpeed(float speed)
	{
		glitchEffect.setSpeed(speed);
	}

}
