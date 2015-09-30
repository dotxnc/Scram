package com.lum.scram;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import static com.lum.scram.Core.PIM;
import java.util.Map;

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
		bdef.angle = MathUtils.random(360);
		
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
	
	public Vector3 GetPosition() {
		
		float x = body.getPosition().x;
		float y = body.getPosition().y;
		float rot = body.getAngle();
		
		return new Vector3(x, y, rot);
	}
	
	public void Render(ShapeRenderer srend) {
		srend.setProjectionMatrix(Core.mainCam.combined);
		srend.begin(ShapeRenderer.ShapeType.Line);
		
		for (Map.Entry<Integer, Player> playerEntry : Core.players.entrySet()) {
			if (((Player)playerEntry.getValue()).body != null) {
				Vector2 pos = ((Player)playerEntry.getValue()).body.getPosition();
				float rot = ((Player)playerEntry.getValue()).body.getAngle();
				srend.rect(pos.x, pos.y, 0.25f, 0.25f, 0.5f, 0.5f, 1, 1, rot);
				srend.circle(pos.x+0.25f, pos.y+0.25f, 0.15f, 100);
			}
			
		}
		
		srend.end();
	}
	
	public void HandleInput() {
		if (Gdx.input.isKeyPressed(Keys.W))
			body.applyLinearImpulse(0, 0.5f, GetPosition().x, GetPosition().y, true);
		if (Gdx.input.isKeyPressed(Keys.S))
			body.applyLinearImpulse(0, -0.5f, GetPosition().x, GetPosition().y, true);
		if (Gdx.input.isKeyPressed(Keys.A))
			body.applyLinearImpulse(-0.5f, 0, GetPosition().x, GetPosition().y, true);
		if (Gdx.input.isKeyPressed(Keys.D))
			body.applyLinearImpulse(0.5f, 0, GetPosition().x, GetPosition().y, true);
	}
	
}
