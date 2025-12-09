(function($) {
						
			$(document).ready(function(){
				$('#filtroMenu').focus();
				if($('#accordionSidebar').width() <= 0){
					fecharMenuTop();
				}
				apresentarSubMenu();
			
			});	
									
		
			apresentarSubMenu = function(){
				$('.submenuitem').each(function(){							
					if($(this).find('.li2:visible').length == 0){
						$(this).hide();
					}else{
						$(this).show();
					}
				});
				$('.submenu').each(function(){									
					if($(this).find('.li2:visible').length == 0){
						$(this).hide();
					}else{
						$(this).show();
					}
				});
			}
			
			
			
			filtrarMenu = function(){
				$('.submenu').each(function(){	
					$(this).show();
				});
				$('.submenuitem').each(function(){							
					$(this).show();					
				});
				filtro = $('#filtroMenu').val().toLowerCase().normalize("NFD").replace(/[^a-zA-Zs]/g, "");							
				$(".li2").each(function(){	
					$(this).show();						
					if(filtro != '' && $($(this).find("a.corfontMenu")).text().toLowerCase().normalize("NFD").replace(/[^a-zA-Zs]/g, "").indexOf(filtro) < 0){						
						$(this).hide();
					}
				});				
				apresentarSubMenu();
			};
			
		})(jQuery);