/*
	Zone Player Manager
    
 	Author: Mike Maxwell 2016
	    
	This software if free for Private Use. You may use and modify the software without distributing it.
 
	This software and derivatives may not be used for commercial purposes.
	You may not distribute or sublicense this software.
	You may not grant a sublicense to modify and distribute this software to third parties not included in the license.

	Software is provided without warranty and the software author/license owner cannot be held liable for damages.        
        
*/
 
definition(
    name: "Zone Player Manager",
    namespace: "MikeMaxwell",
    author: "Mike Maxwell",
    description: "Zone Player Manager 1.0.0",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Solution/areas.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Solution/areas@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Solution/areas@2x.png")

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
    def hub = location.hubs.first().hub
    def deviceID = "${app.id}"
    def zName = "virtualMusicPlayer"
    def simPlayer = getChildDevice(deviceID)
    if (!simPlayer) {
    	log.info "create virtual music player ${zName}"
        simPlayer = addChildDevice("MikeMaxwell", "simulatedMusicPlayer", deviceID, null, [name: zName, label: zName, completedSetup: true])
    } else {
    	log.info "virtual music player ${zName} exists"
    }
    subscribe(simPlayer,"cmd",playHandler)
}
def playHandler(evt){
	//log.info "event: ${evt.value} ${evt.data}"
    def cmd = evt.value
    def p = collect{evt.data.split(",")}[0]
    childApps.each{ child ->
		child.exec(cmd,p)
	}
}

/* page methods	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
def main(){
	def installed = app.installationState == "COMPLETE"
	return dynamicPage(
    	name		: "main"
        ,title		: "Zone Player"
        ,install	: true
        ,uninstall	: installed
        ){
            if (installed){
        		section(){
        			app(name: "childZones", appName: "zonePlayerChild", namespace: "MikeMaxwell", description: "Create New Player Zone...", multiple: true)	
                }
            }
	}
}