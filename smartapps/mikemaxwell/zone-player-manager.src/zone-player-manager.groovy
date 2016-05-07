/*
	Zone Player Manager
    
 	Author: Mike Maxwell 2016
    
    1.0.0a 2016-05-07 	Update child device create to use correct hub id
    					add version info to parent
	    
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
    description: "Creates a virtual music player device, then directs TTS messages only to the occupied areas of your home.",
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

def getHubID(){
	def hubID
	if (myHub){
    	hubID = myHub.id
    } else {
    	def hubs = location.hubs.findAll{ it.type == physicalgraph.device.HubType.PHYSICAL }
        //log.debug "hub count: ${hubs.size()}"
        if (hubs.size() == 1) hubID = hubs[0].id 
    }
    //log.debug "hubID: ${hubID}"
    return hubID
}

def initialize() {
	state.vParent = "1.0.0a"
    def deviceID = "${app.id}"
    def zName = "virtualMusicPlayer"
    def simPlayer = getChildDevice(deviceID)
    if (!simPlayer) {
    	log.info "create virtual music player ${zName}"
        simPlayer = addChildDevice("MikeMaxwell", "simulatedMusicPlayer", deviceID, getHubID(), [name: zName, label: zName, completedSetup: true])
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

def getVersionInfo(){
	return "Versions:\n\tZone Player Manager: ${state.vParent ?: "No data available yet."}\n\tzonePlayerChild: ${state.vChild ?: "No data available yet."}"
}

def updateVer(vChild){
    state.vChild = vChild
}


/* page methods	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
def main(){
	def installed = app.installationState == "COMPLETE"
    getHubID()
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
                section (getVersionInfo()) { }
            } else {
            	section(){
                	if (getHubID() == null){
                		input(
                        	name			: "myHub"
                        	,type			: "hub"
                        	,title			: "Select your hub"
                        	,multiple		: false
                        	,required		: true
                            ,submitOnChange	: true
                    	)
                    } else {
                		paragraph("Tap done to finish the initial installation.\nRe-open the app from the smartApps flyout to create your sound zones.")
                    }
                }
            }
	}
}