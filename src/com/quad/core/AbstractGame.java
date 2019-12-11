package com.quad.core;


import com.quad.core.components.State;
import com.quad.states.MenuState;
import com.quad.states.TestState;


public class AbstractGame{
	
	private State[] states;
	private int currentState;
	
	//Pause
	private boolean paused;
	
	//states
	public static final int NUMSTATES = 10;
	public static final int MENU = 0;
	public static final int LEVELONE = 1;
	public static final int TEST = 2;
	
	public AbstractGame(){
		
		states = new State[NUMSTATES];
		
		paused = false;
		
		
		currentState = MENU;
		loadState(currentState);
	}
	
	private void loadState(int state) {
		if(state == MENU) states[currentState] = new MenuState();
		if(state == LEVELONE) states[currentState] = new TestState();
	}
	
	private void unloadState(int state) {
		states[state] = null;
	}
	
	public void setState(GameContainer gc,int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		init(gc);
	}
	
	public void setPaused(GameContainer gc, boolean i){
		paused = i;
		
		if(paused) init(gc);
	}
	
	public void init(GameContainer gc){
		
		if(states[currentState] != null) states[currentState].init(gc);
	}
	
	public void update(GameContainer gc, float dt){
		
		if(states[currentState] != null) states[currentState].update(gc, dt);
	}
	
	public void render(GameContainer gc, Renderer r){
		
		if(states[currentState] != null) states[currentState].render(gc, r);
		
	}
	
	public State getCurrentState() {
		return states[currentState];
	}
}
