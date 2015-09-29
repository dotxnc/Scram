package com.lum.scram;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player {
	private BodyDef bdef;
	private FixtureDef fdef;
	
	public Body body;
	public Fixture fixture;
	public PolygonShape shape;
	
	private float x;
	private float y;
	
	public Player(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void Create() {
		bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.linearDamping = 5;
		bdef.position.set(new Vector2(x, y));
		
		body = Core.world.createBody(bdef);
		shape = new PolygonShape();
		shape.setAsBox(1.f, 1.f);
		
		fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0.5f;
		fdef.friction = 0.5f;
		fdef.restitution = 1.f;
		
		fixture = body.createFixture(fdef);
		
		shape.dispose();
	}
	
	public void dispose() {
		Core.world.destroyBody(body);
	}
		
	
	public void Render(ShapeRenderer srend) {
	}
	
	public void HandleInput() {
		
	}
	
}
