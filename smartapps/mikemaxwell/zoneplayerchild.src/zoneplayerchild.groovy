/*
	zonePlayerChild
    
 	Author: Mike Maxwell 2016
	    
	This software if free for Private Use. You may use and modify the software without distributing it.
 
	This software and derivatives may not be used for commercial purposes.
	You may not distribute or sublicense this software.
	You may not grant a sublicense to modify and distribute this software to third parties not included in the license.

	Software is provided without warranty and the software author/license owner cannot be held liable for damages.        
        
*/
 
definition(
    name: "zonePlayerChild",
    namespace: "MikeMaxwell",
    author: "Mike Maxwell",
    description: "child application for 'Zone Player Manager', do not install directly.",
    category: "My Apps",
    parent: "MikeMaxwell:Zone Player Manager",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
	page(name: "main")
    //page(name: "triggers", nextPage	: "main")
}
def installed() {
}

def updated() {
	unsubscribe()
	initialize()
}

def initialize() {
    app.updateLabel("${settings.zoneName} Sound Zone") 
    state.clear()
}


/* page methods	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
def main(){
	def installed = app.installationState == "COMPLETE"
	return dynamicPage(
    	name		: "main"
        ,title		: "Zone Configuration"
        ,install	: true
        ,uninstall	: installed
        ){
		     section(){
                    input(
                        name		: "zoneName"
                        ,type		: "text"
                        ,title		: "Name of this Sound Zone:"
                        ,multiple	: false
                        ,required	: true
                    )
					input(
            			name		: "zonePlayers"
                		,title		: "Music Players"
                		,multiple	: true
                		,required	: true
                		,type		: "capability.musicPlayer"
            		)                    
					input(
            			name		: "motionSensors"
                		,title		: "Motion Sensors"
                		,multiple	: true
                		,required	: true
                		,type		: "capability.motionSensor"
            		)                    
            }
            section("Optional settings"){
                input(
                    name			: "zoneVolume" 
                    ,title			: "zone level percent"
                    ,multiple		: false
                    ,required		: true
                    ,type			: "enum"
                    ,options		: [[".1":"10%"],[".2":"20%"],[".3":"30%"],[".4":"40%"],[".5":"50%"],[".6":"60%"],[".7":"70%"],[".8":"80%"],[".9":"90%"],["1":"100%"]]
                    ,defaultValue	: "1"
                )
                input(
                    name			: "zoneTimeout" 
                    ,title			: "Motion Timeout"
                    ,multiple		: false
                    ,required		: true
                    ,type			: "enum"
                    ,options		: [["0":"Disabled"],["60":"1 Minute"],["120":"2 Minutes"],["180":"3 Minutes"],["240":"4 Minutes"],["300":"5 Minutes"],["600":"10 Minutes"],["900":"15 Minutes"],["1800":"30 Minutes"],["3600":"1 Hour"]]
                    ,defaultValue	: "300"
                )
				input(
           			name		: "manActivate"
               		,title		: "Switches for manual activation"
               		,multiple	: true
              		,required	: false
               		,type		: "capability.switch"
           		) 
				input(
           			name		: "muteSwitch"
               		,title		: "Switch to activate while zone is playing"
               		,multiple	: false
              		,required	: false
               		,type		: "capability.switch"
           		) 
				input(
           			name		: "muteAV"
               		,title		: "Main AV mute"
               		,multiple	: false
              		,required	: false
               		,type		: "capability.musicPlayer"
           		) 
				input(
           			name			: "volBoostActivate"
               		,title			: "Switch for volume boost/cut"
               		,multiple		: false
              		,required		: false
               		,type			: "capability.switch"
                    ,submitOnChange	: true
           		)
                if (volBoostActivate){
                    input(
                        name			: "volBoostLevel" 
                        ,title			: "volume boost or cut"
                        ,multiple		: false
                        ,required		: false
                        ,type			: "enum"
                        ,options		: [["30":"+30%"],["20":"+20%"],["10":"+10%"],["-10":"-10%"],["-20":"-20%"],["-30":"-30%"]]
                        ,defaultValue	: "20"
                    )
                }
			} //end section optional settings
	}
}

def exec(cmd,p){
    log.info "cmd: ${cmd}, params: ${p}" //, model: ${player?.currentState("model")}"
    def text //= p[0]
    def level = 0
    if (volBoostActivate && volBoostActivate.currentValue("switch").contains("on")) level = volBoostLevel.toInteger()
    def duration
    //log.info "level: ${level}"
    if (motionActive() || manActive()){
    	activateMute(true)
		switch (cmd) {
        	case ["playTextAndResume","playTextAndRestore"] :
            	text = p[0]
                def sound = textToSpeech(text)
                level = level + (p[1].toInteger() * zoneVolume.toFloat()).toInteger()
                log.info "${cmd} text: ${text}, requestLevel: ${p[1]},  sentLevel: ${level}"
        		zonePlayers."${cmd}"(text,level)    	
				break
            case ["playTrackAndResume","playTrackAndRestore"] :
            	text = p[0]
                duration = p[1]
                level = level + (p[2].toInteger() * zoneVolume.toFloat()).toInteger()
                log.info "${cmd} text: ${text}, duration: ${duration}, requestLevel: ${p[2]}, sentLevel: ${level}"
        		zonePlayers."${cmd}"(text,duration,level)    	
            	break
    	}
    	log.info "TTS sent"
        activateMute(false)
    } else {
    	log.info "TTS not sent"
    }
}

def activateMute(enable){
	log.info "activateMute: ${enable} muteSwitch: ${muteSwitch}"
	if (muteSwitch || muteAV){
    	if (enable){
        	muteSwitch?.on()
            muteAV?.mute()
            log.info "mute enabled"
        } else {
        	muteSwitch?.off()
            muteAV?.unmute()
            log.info "mute disabled"
        }
    } //else log.info "nope, mute not switch not selected"
}

def manActive(){
	def enable = false
    if (manActivate && manActivate.currentValue("switch").contains("on")) enable = true
    return enable
}

def motionActive(){
	def now = now()
	def enable
    def window = settings.zoneTimeout.toInteger() * 1000
    if (window == 0){
    	enable = motionSensors.currentValue("motion").contains("active")
    } else {
		enable = motionSensors.currentState("motion").any{ s -> s.value == "active" && (now - s.date.getTime()) < window}
    }
    return enable
}
