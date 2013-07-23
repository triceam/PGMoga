
window.PGMoga = {
	verbose: false,
	UPDATE_EVENT:"PGMoga_Update",
	init:function() {
		console.log("PGMoga init");
		cordova.exec(function(){ console.log("PGMoga init success") }, function(){ console.log("PGMoga init failure") }, "PGMoga", "init", []);
	},
	update: function( state ) {
		this.state = state;
		if (this.verbose) {
			console.log( "state>> a:" + state.a + " b:" + state.b + " axisX:"+state.axisX + " axisY:"+state.axisY + " axisZ:"+state.axisZ + " axisRZ:"+state.axisRZ );
		}
		var event = document.createEvent( 'CustomEvent' );
    	event.initCustomEvent( this.UPDATE_EVENT, false, true, {"state":this.state} );
        document.dispatchEvent(event);
	}
}