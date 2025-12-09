(function($) {	 
	$(document).dblclick( function (event) {    	
        var d = event.srcElement || event.target;        
        if (d.tagName.toUpperCase() === 'INPUT' && (d.type.toUpperCase() === 'IMAGE' || d.type.toUpperCase() === 'SUBMIT')) {        
        	event.stopPropagation()
        	event.preventDefault();        	
        	return false;
        }
    });		
}(jQuery));