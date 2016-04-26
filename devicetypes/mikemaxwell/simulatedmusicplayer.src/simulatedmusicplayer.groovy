/**
 *  Virtual Speaker
 *
 *  Copyright 2016 Mike Maxwell
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "simulatedMusicPlayer", namespace: "MikeMaxwell", author: "Mike Maxwell") {
		capability 	"Music Player"
		command "tileSetLevel"			, ["number"]
		//command "playTrackAtVolume"		, ["string","number"]
		command "playTrackAndResume"	, ["string","number","number"]
		command "playTextAndResume"		, ["string","number"]
		command "playTrackAndRestore"	, ["string","number","number"]
		command "playTextAndRestore"	, ["string","number"]
        attribute "cmd"					, "string"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		// TODO: define your main and details tiles here
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'status' attribute
	// TODO: handle 'level' attribute
	// TODO: handle 'trackDescription' attribute
	// TODO: handle 'trackData' attribute
	// TODO: handle 'mute' attribute

}
//custom comands
def playTrackAndResume(text,duration,level){
	log.info "device playTrackAndResume- text: ${text}, ${duration}, level:${level}"
    sendEvent(
    	name	: "cmd"
    	,value	: "playTrackAndResume"
        ,displayed	: false
        ,isStateChange	: true
        ,data	: "${text},${duration},${level}"
    )    
}

def playTrackAndRestore(text,duration,level){
	log.info "device playTrackAndRestore- text: ${text}, level:${level}"
    sendEvent(
    	name	: "cmd"
    	,value	: "playTrackAndRestore"
        ,displayed	: false
        ,isStateChange	: true
        ,data	: "${text},${duration},${level}"
    )    
}
def playTextAndResume(text,level){
	log.info "device playTextAndResume- text: ${text}, level:${level}"
    sendEvent(
    	name	: "cmd"
    	,value	: "playTextAndResume"
        ,displayed	: false
        ,isStateChange	: true
        ,data	: "${text},${level}"
    )    
}

def playTextAndRestore(text,level){
	log.info "device playTextAndRestore- text: ${text}, level:${level}"
    //parent.playTextAndRestore(text,level)
    sendEvent(
    	name	: "cmd"
    	,value	: "playTextAndRestore"
        ,displayed	: false
        ,isStateChange	: true
        ,data	: "${text},${level}"
    )
}

// handle commands
def play() {
	log.debug "Executing 'play'"
	// TODO: handle 'play' command
}

def pause() {
	log.debug "Executing 'pause'"
	// TODO: handle 'pause' command
}

def stop() {
	log.debug "Executing 'stop'"
	// TODO: handle 'stop' command
}

def nextTrack() {
	log.debug "Executing 'nextTrack'"
	// TODO: handle 'nextTrack' command
}

def playTrack() {
	log.debug "Executing 'playTrack'"
	// TODO: handle 'playTrack' command
}

def setLevel() {
	log.debug "Executing 'setLevel'"
	// TODO: handle 'setLevel' command
}

def playText(text) {
	log.debug "Executing 'playText'"
	// TODO: handle 'playText' command
}

def mute() {
	log.debug "Executing 'mute'"
	// TODO: handle 'mute' command
}

def previousTrack() {
	log.debug "Executing 'previousTrack'"
	// TODO: handle 'previousTrack' command
}

def unmute() {
	log.debug "Executing 'unmute'"
	// TODO: handle 'unmute' command
}

def setTrack() {
	log.debug "Executing 'setTrack'"
	// TODO: handle 'setTrack' command
}

def resumeTrack() {
	log.debug "Executing 'resumeTrack'"
	// TODO: handle 'resumeTrack' command
}

def restoreTrack() {
	log.debug "Executing 'restoreTrack'"
	// TODO: handle 'restoreTrack' command
}
