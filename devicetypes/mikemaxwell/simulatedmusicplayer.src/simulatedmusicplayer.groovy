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
        capability "Polling"   
		command "playTrackAndResume"	, ["string","number","number"]
		command "playTextAndResume"		, ["string","number"]
		command "playTrackAndRestore"	, ["string","number","number"]
		command "playTextAndRestore"	, ["string","number"]
        attribute "cmd"					, "string"
	}

	simulator {
	}
	tiles(scale: 2) {
  		multiAttributeTile(name:"switch", type:"generic", width:6, height:4, canChangeicon: true) {
    		tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
    			attributeState "default", label: "Online", action: "", backgroundColor: "#53a7c0", icon: "st.Electronics.electronics13"
    		}
		}
		main "switch"
		details "switch"
	}
}
def updated(){
	poll()
}

def parse(String description) {
	log.debug "Parsing '${description}'"
}
def poll(){
	sendEvent(
    	name 			: "status"
        ,value			: "playing"
        ,isDisplayed 	: false
    )
}
//custom comands
def playTrackAndResume(text,duration,level){
	log.info "device playTrackAndResume- url:${text}, ${duration}, level:${level}"
    sendEvent(
    	name			: "cmd"
    	,value			: "playTrackAndResume"
        ,displayed		: false
        ,isStateChange	: true
        ,data			: "${text}~${duration}~${level}"
    )    
}

def playTrackAndRestore(text,duration,level){
	log.info "device playTrackAndRestore- url:${text}, duration:${duration}, level:${level}"
    sendEvent(
    	name			: "cmd"
    	,value			: "playTrackAndRestore"
        ,displayed		: false
        ,isStateChange	: true
        ,data			: "${text}~${duration}~${level}"
    )    
}
def playTextAndResume(text,level){
	log.info "device playTextAndResume- text:${text}, level:${level}"
    sendEvent(
    	name			: "cmd"
    	,value			: "playTextAndResume"
        ,displayed		: false
        ,isStateChange	: true
        ,data			: "${text}~${level}"
    )    
}

def playTextAndRestore(text,level){
	log.info "device playTextAndRestore- text: ${text}, level:${level}"
    sendEvent(
    	name			: "cmd"
    	,value			: "playTextAndRestore"
        ,displayed		: false
        ,isStateChange	: true
        ,data			: "${text}~${level}"
    )
}

// handle commands
def play() {
	log.debug "'play' command is not implemented"
	// TODO: handle 'play' command
}

def pause() {
	log.debug "'pause' command is not implemented"
	// TODO: handle 'pause' command
}

def stop() {
	log.debug "'stop' command is not implemented"
	// TODO: handle 'stop' command
}

def nextTrack() {
	log.debug "'nextTrack' command is not implemented"
	// TODO: handle 'nextTrack' command
}

def playTrack(Map trackData) {
	log.debug "'playTrack' command is not implemented, trackData: ${trackData}"
	// TODO: handle 'playTrack' command
}

def playTrack(String url, metaData="") {
	log.debug "'playTrack' command is not implemented, uri: ${url}, metaData: ${metaData}"
	// TODO: handle 'playTrack' command
}


def setLevel(level) {
	log.debug "'setLevel' command is not implemented, level: ${level}"
	// TODO: handle 'setLevel' command
}

def playText(text) {
	log.debug "'playText' command is not implemented, text: ${text}"
	// TODO: handle 'playText' command
}

def mute() {
	log.debug "'mute' command is not implemented"
	// TODO: handle 'mute' command
}

def previousTrack() {
	log.debug "'previousTrack' command is not implemented"
	// TODO: handle 'previousTrack' command
}

def unmute() {
	log.debug "'unmute' command is not implemented"
	// TODO: handle 'unmute' command
}

def setTrack(Map trackData) {
	log.debug "'setTrack' command is not implemented, trackData: ${trackData}"
	// TODO: handle 'setTrack' command
}

def setTrack(String url, metaData="") {
	log.debug "'setTrack' command is not implemented, url: ${url}, metaData: ${metaData}"
	// TODO: handle 'setTrack' command
}

def resumeTrack(Map trackData = null) {
	log.debug "'resumeTrack' command is not implemented, trackData: ${trackData}"
	// TODO: handle 'resumeTrack' command
}

def restoreTrack(Map trackData = null) {
	log.debug "'restoreTrack' command is not implemented, trackData: ${trackData}"
	// TODO: handle 'restoreTrack' command
}
