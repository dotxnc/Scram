package com.lum.scram.postprocessing.filters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.lum.scram.utils.ShaderLoader;

public class GlitchEffect extends Filter<GlitchEffect>{

	private float speed, amplitude;
	public enum Param implements Parameter {
		// @formatter:off
		Texture0("u_texture0", 0),  Time("time",0), Speed("SPEED",0), Amplitude("AMPLITUDE",0);
		// @formatter:on

		private final String mnemonic;
		private int elementSize;

		private Param (String m, int elementSize) {
			this.mnemonic = m;
			this.elementSize = elementSize;
		}

		@Override
		public String mnemonic () {
			return this.mnemonic;
		}

		@Override
		public int arrayElementSize () {
			return this.elementSize;
		}
	}

	
	Texture noiseTexture;
	public GlitchEffect() {
		super(ShaderLoader.fromFile("screenspace", "glitch"));
		noiseTexture = new Texture(Gdx.files.internal("noise.png"));
		setAmplitude(1f);
		setSpeed(1f);
		rebind();
	}

	public void setTime(float time)
	{
		
		setParam(Param.Time, time);
	}
	
	public void setSpeed(float speed)
	{
		this.speed=speed;
		setParam(Param.Speed, this.speed);
	}
	

	public void setAmplitude(float amp)
	{
		this.amplitude=amp;
		setParam(Param.Amplitude, this.amplitude);
	}

	@Override
	public void rebind() {
		// TODO Auto-generated method stub
		setParams(Param.Texture0, u_texture0);
		setParam(Param.Amplitude, this.amplitude);
		setParam(Param.Speed, this.speed);
		
		endParams();
		
	}
	
	@Override
	protected void onBeforeRender() {
		inputTexture.bind(u_texture0);
		noiseTexture.bind(1);
		
	}

}
