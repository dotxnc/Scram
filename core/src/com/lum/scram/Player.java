package com.lum.scram;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import java.util.Map;

public class Player {
	private BodyDef bdef;
	private FixtureDef fdef;
	
	public Body body;
	public Fixture fixture;
	public PolygonShape shape;
	
	private Texture texture;
	private Sprite sprite;
	
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
		bdef.angularDamping = 5;
		bdef.position.set(new Vector2(x, y));
		bdef.angle = MathUtils.random(360);
		
		body = Core.world.createBody(bdef);
		shape = new PolygonShape();
		shape.setAsBox(0.5f, 0.5f);
		
		fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0.5f;
		fdef.friction = 0.5f;
		fdef.restitution = 1.f;
		
		fixture = body.createFixture(fdef);
		
		shape.dispose();
		
		texture = new Texture(Gdx.files.internal("player.png"));
		sprite = new Sprite(texture);
		sprite.setScale(Core.PIM);
		sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
		
	}
	
	public void dispose() {
		Core.world.destroyBody(body);
	}
	
	public Vector3 GetPosition() {
		
		float x = body.getPosition().x;
		float y = body.getPosition().y;
		float rot = body.getAngle();
		
		return new Vector3(x, y, rot);
	}
	
	public void Render(SpriteBatch batch) {
		batch.setProjectionMatrix(Core.mainCam.combined);
		batch.begin();
		
		for (Map.Entry<Integer, Player> playerEntry : Core.players.entrySet()) {
			if (body != null) {
				Vector2 pos = body.getPosition();
				float rot = body.getAngle();
				sprite.setPosition(body.getTransform().getPosition().x - sprite.getOriginX(), body.getTransform().getPosition().y - sprite.getOriginY());
				sprite.setRotation(body.getAngle()*MathUtils.radiansToDegrees);
				sprite.draw(batch);
				
				// floating point fix
				Vector2 vel = body.getLinearVelocity();
				float normal = (vel.x+vel.y)/2;
				if (Math.abs(normal) < 0.01f)
					body.setLinearVelocity(0, 0);
			}
		}
		
		batch.end();
	}
	
	public void HandleInput() {
		
	}
	
	public Vector2 GetVelocity() {
		return body.getLinearVelocity();
	}
	
}
