package com.lum.scram;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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

public class Player {
	private BodyDef bdef;
	private FixtureDef fdef;
	
	public Body body;
	public Fixture fixture;
	public PolygonShape shape;
	
	private Texture texture;
	private Sprite sprite;
	
	private final float x;
	private final float y;
	
	public int uid_local;

	private ParticleEffect shipEffect;
	
	public final float zapMax = 1;
	public float zapTimer = 0;
	
	public PositionalSound zap;
	
	private boolean findSpawn = true;
	
	public Player(float x, float y, int uid) {
		this.x = x;
		this.y = y;
		this.uid_local = uid;
	}
	
	public void Create() {
		bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.linearDamping = 5;
		bdef.angularDamping = 5;
		bdef.position.set(new Vector2(x, y));
		bdef.angle = MathUtils.random(360);
		
		body = Core.world.createBody(bdef);
		body.setUserData(this);
		
		shape = new PolygonShape();
		shape.setAsBox(0.5f, 0.5f);
		
		fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0.5f;
		fdef.friction = 0.5f;
		fdef.restitution = 0.f;
		
		fixture = body.createFixture(fdef);
		
		shape.dispose();
		
		texture = new Texture(Gdx.files.internal("player.png"));
		sprite = new Sprite(texture);
		sprite.setScale(Core.PIM);
		sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
		
		shipEffect = new ParticleEffect();
		shipEffect.load(Gdx.files.internal("ship.particle"), Gdx.files.internal(""));
		shipEffect.scaleEffect(Core.PIM/2f);
		shipEffect.start();
		
		zap = new PositionalSound("shoot.wav");
		
		body.setTransform(Core.map.getRandomSpawn(), 0);
		
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
	
	public void Render(SpriteBatch batch, float delta) {
		zapTimer -= delta;
		
		batch.setProjectionMatrix(Core.mainCam.combined);
		batch.begin();
		batch.setColor(Color.WHITE);
		
		if (body != null) {
			Vector2 vel = body.getLinearVelocity();
			Vector2 pos = body.getPosition();
			float rot = body.getAngle();
			
			float normal = vel.len();
			
			if (normal < 4)
				shipEffect.allowCompletion();
			else
				shipEffect.start();
			
			shipEffect.setPosition(pos.x, pos.y);
			shipEffect.update(delta);
			shipEffect.draw(batch);
			
			sprite.setPosition(body.getTransform().getPosition().x - sprite.getOriginX(), body.getTransform().getPosition().y - sprite.getOriginY());
			sprite.setRotation(body.getAngle()*MathUtils.radiansToDegrees);
			sprite.setColor(MathUtils.random(1f), MathUtils.random(1f), MathUtils.random(1f), 1);
			sprite.draw(batch);
			
			// floating point fix
			if (Math.abs(normal) < 0.1f)
				body.setLinearVelocity(0, 0);
		}
		
		batch.end();
	}
	
	public void HandleInput() {
		
	}
	
	public Vector2 GetVelocity() {
		return body.getLinearVelocity();
	}
	
}
