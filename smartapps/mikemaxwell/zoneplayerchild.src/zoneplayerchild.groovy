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
    description: "child application 1.0.0 for 'Zone Player Manager', do not install directly.",
    category: "My Apps",
    parent: "MikeMaxwell:Zone Player Manager",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
	page(name: "main")
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
                		,multiple	: false
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
                    ,title			: "Zone volume level (percent of requested) "
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
               		,title		: "Manual zone activation switches"
               		,multiple	: true
              		,required	: false
               		,type		: "capability.switch"
           		) 
				input(
           			name		: "muteSwitch"
               		,title		: "Switch to activate during message"
               		,multiple	: false
              		,required	: false
               		,type		: "capability.switch"
           		) 
				input(
           			name		: "muteAV"
               		,title		: "AV mute to activate during message"
               		,multiple	: false
              		,required	: false
               		,type		: "capability.musicPlayer"
           		) 
				input(
           			name			: "volBoostActivate"
               		,title			: "Volume boost/cut override switch"
               		,multiple		: false
              		,required		: false
               		,type			: "capability.switch"
                    ,submitOnChange	: true
           		)
                if (volBoostActivate){
                    input(
                        name			: "volBoostLevel" 
                        ,title			: "Volume boost or cut during message"
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
    log.info "cmd: ${cmd}, params: ${p[0]}" //, model: ${player?.currentState("model")}"
    def params = p[0].tokenize("~")
    def text //= p[0]
    def level = 0
    if (volBoostActivate && volBoostActivate.currentValue("switch").contains("on")) level = volBoostLevel.toInteger()
    def duration
    //log.info "level: ${level}"
    if (motionActive() || manActive()){
    	activateMute(true)
		switch (cmd) {
        	case ["playTextAndResume","playTextAndRestore"] :
            	text = params[0]
                //def sound = textToSpeech(text)
                level = level + (params[1].toInteger() * zoneVolume.toFloat()).toInteger()
                log.info "${cmd} text: ${text}, requestLevel: ${params[1]},  sentLevel: ${level}"
        		zonePlayers."${cmd}"(text,level)
                log.info "TTS sent"
				break
            case ["playTrackAndResume","playTrackAndRestore"] :
            	text = params[0]
                duration = params[1].toInteger()
                level = level + (params[2].toInteger() * zoneVolume.toFloat()).toInteger()
                log.info "${cmd} text: ${text}, duration: ${duration}, requestLevel: ${params[2]}, sentLevel: ${level}"
        		zonePlayers."${cmd}"(text,duration,level)    	
                log.info "MP3 played"
            	break
    	}
        activateMute(false)
    } else {
    	log.info "Nothing sent/played..."
    }
}

def activateMute(enable){
	log.info "activateMute: ${enable}, muteSwitch: ${muteSwitch}, muteAV: ${muteAV}"
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
	def enable
    def window = settings.zoneTimeout.toInteger() * 1000
    //log.info "window: ${window}"
    def evtStart = new Date(now() - window)
    if (window == 0){
    	enable = motionSensors.currentValue("motion").contains("active")
    } else {
		enable = motionSensors.any{ s -> s.statesSince("motion", evtStart).size > 0}
    }
    return enable
}
