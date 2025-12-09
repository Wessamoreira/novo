(function($) {
	
	document.onkeyup = function(e){
		var evt = window.event || e;
		if(evt.ctrlKey && evt.keyCode == 67){         	    	
			return false;
		}
	};
	
	document.addEventListener('contextmenu', event => event.preventDefault());
	document.oncontextmenu = document.body.oncontextmenu = function() {return false;};	
	
}(jQuery));
