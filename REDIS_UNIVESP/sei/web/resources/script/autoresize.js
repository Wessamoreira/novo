											(function($) {
												$(window).resize(function(){
												 	
 													$('.autoresize').each(
 																function(){ 																	
 																	$(this).attr('style', 'overflow:auto;width:'+($(window).width()-330)+'px;padding:10px');
 																}
 															);
 													$('.autoresizeadm').each(
															function(){
																
																$(this).attr('style', 'overflow:auto;width:'+(($(window).width()*97/100))+"px;padding:10px");
															}
														);
												});
												$(window).ready(function(){
													
 													$('.autoresize').each(
 																function(){
 																	
 																	$(this).attr('style', 'overflow:auto;width:'+($(window).width()-330)+"px;padding:10px;");
 																}
 															);
 													$('.autoresizeadm').each(
																function(){
																	
																	$(this).attr('style', 'overflow:auto;width:'+(($(window).width()*97/100))+"px;padding:10px");
																}
															);
												});
												
												resize = function(){
													
 													$('.autoresize').each(
 																function(){
 																	
 																	$(this).attr('style', 'overflow:auto;width:'+($(window).width()-330)+"px;padding:10px;");
 																}
 															);
 													$('.autoresizeadm').each(
																function(){
																	
																	$(this).attr('style', 'overflow:auto;width:'+(($(window).width()*97/100))+"px;padding:10px");
																}
															);
												};
												resize2 = function(width, style){													
 													$(style).each(
 																function(){ 																	
 																	$(this).attr('style', 'overflow:auto;width:'+($(window).width()-width)+"px;");
 																}
 														);
												};
											}(jQuery));
