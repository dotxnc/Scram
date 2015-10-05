package com.lum.scram;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
	
	public float dx,dy;
	
	public int uid_local;

	private ParticleEffect shipEffect;
	
	public final float zapMax = 1;
	public float zapTimer = 0;
	public float deathTimer = 5;
	public boolean dead = false;
	
	public PositionalSound zap;
	
	private boolean findSpawn = true;
	
	public float health = 100;
	public float display = 100;
	
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
		
		display = MathUtils.lerp(display, health, 0.05f);
		
		if (health <= 0)
			dead = true;
		
		if (dead) {
			deathTimer -= delta;
			if (deathTimer < 0) {
				dead = false;
				deathTimer = 5;
				body.setTransform(Core.map.getRandomSpawn(), 0);
				health = 100;
				display = 100;
			}
		}
		
		batch.setProjectionMatrix(Core.mainCam.combined);
		batch.begin();
		batch.setColor(Color.WHITE);
		
		if (body != null) {
			Vector2 vel = body.getLinearVelocity();
			Vector2 pos = body.getPosition();
			float rot = body.getAngle();
			
			dx = MathUtils.cos(rot);
			dy = MathUtils.sin(rot);
			
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
		
		
		if (Core.localPlayer != null && Core.localPlayer.uid_local != uid_local) return;
		
		ShapeRenderer srend = Core.srend;
		srend.setProjectionMatrix(Core.hudCam.combined);
		srend.setAutoShapeType(true);
		srend.begin(ShapeType.Filled);
		srend.setColor(Color.WHITE);
		srend.set(ShapeType.Filled);
		srend.rect(10, 10, display*2, 10);
		srend.set(ShapeType.Line);
		srend.rect(8, 8, 200+4, 14);
		srend.end();
		
	}
	
	public void HandleInput() {
		
	}
	
	public Vector2 GetVelocity() {
		return body.getLinearVelocity();
	}
	
	public float getRealVelocity() {
		return body.getLinearVelocity().len();
	}
	
}
