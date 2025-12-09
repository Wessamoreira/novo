(function($) {
				
				nextFocus =  function(form, index){
					
 					if(form.elements[index + 1].type == 'hidden'  && form.elements[index + 1].name != 'javax.faces.ViewState'){
 						nextFocus(form, index + 1);
 					}else{
 						form.elements[index + 1].focus();
 					}
					
				};
				
				$.fn.enterKey = function (fnc) {
				    return this.each(function () {
				        $(this).keypress(function (ev) {
				            var keycode = (ev.keyCode ? ev.keyCode : ev.which);
				            if (keycode == '13') {
				                fnc.call(this, ev);
				            }
				        })
				    })
				};
				
				validarEnconding = function(){
					$('input,textarea').on('blur', function(){
			    		if($(this).val() != 'indefined' && $(this).val() != ''){
			    			var length = $(this).val().length;
			    			var valorFinal = "";
			    			var texto = $(this).val();
			    			 for (var i = 0; i < length; i++) {
			    				   var code = texto.charCodeAt(i);			    				   
			    				   if(code <= 255){
			    					   valorFinal += texto.substring(i, i+1);
			    				   }else{
			    					   valorFinal += " ";
			    				   }
			    			}
			    			 $(this).val(valorFinal);			    			 
			    		}
			    	});
				};
							
			    $(document).ready(function() {			    	
			    	iniciarColorPiker();
			    	iniciarPickList();
			    	carregarMenu('#accordionSidebar');	
			    	validarEnconding();
			    	$(document).on('ajax', validarEnconding());
			    	$(document).keypress(function(e){				    		
			    		
			    		if ((e.keyCode ? e.keyCode : e.which) == 13  && (e.target.nodeName === 'INPUT' || e.target.nodeName === 'SELECT' || e.target.nodeName === 'textarea' || e.target.nodeName === 'text')) {			    			
			    			var form = e.target.form;
			    			var index = Array.prototype.indexOf.call(form, e.target);			    			
			    			nextFocus(form, index);			    			
			    			e.preventDefault();
			    			return false;
			    		}
			    		if(((e.keyCode ? e.keyCode : e.which) == 32 || (e.keyCode ? e.keyCode : e.which) == 13) && (e.target.nodeName === 'A')){
			    			e.target.click();
			    			e.preventDefault();
			    		}		    			
			    	});	
			    	
			    				    			    	
			    	$('#conteudo').on('click', function(){
			    		if($('#box-options-user').hasClass('open')){	
			    			$('#box-options-user').removeClass('open');
			    			$('#box-options-user').addClass('close');
			    		}
			    	});
			    	$('#box-user').on('click', function(){
			    		if($('#box-options-user').hasClass('close')){			    		
				    		$('#box-options-user').addClass('open');
				    		$('#box-options-user').removeClass('close');			    		
				    	}else{
				    		$('#box-options-user').addClass('close');
				    		$('#box-options-user').removeClass('open');	
				    	}
			    	});
			    	$('#box-user-login').on('click', function(){
			    		if($('#box-options-login').hasClass('close')){			    		
				    		$('#box-options-login').addClass('open');
				    		$('#box-options-login').removeClass('close');			    		
				    	}else{
				    		$('#box-options-login').addClass('close');
				    		$('#box-options-login').removeClass('open');	
				    	}
			    	});
			    	
			    	$('#content-wrapper').on('scroll resize', function() {
			    		fixedHeadTable();
					});
			    	$('#content-wrapper').on('resize', function() {
			    		fixedHeadTable();
					});
			    	
			    	$('#tudo').on('scroll', function() {
			    		fixedHeadTable();
					});
			    	$('#tudo').on('resize', function() {
			    		fixedHeadTable();
					});
			    	

			    });	
			    
			    fixedHeadTable = function(){
			    	if($('div').hasClass('table-control')){			    		
			    	if($('.table-control') === 'undefined' || $('.table-control').offset() === 'undefined' || $('.table-control').offset().top === 'undefined' ){
			    		$('.table-head').removeClass('fixedHeadTable');
			    	}else if($('.table-control').offset().top <= 50){
						$('.table-head').addClass('fixedHeadTable');
						$('.table-head').css('width', $('.table-control').width())
					}else{
						$('.table-head').removeClass('fixedHeadTable');
						
					}
			    	}
			    };
			    
			    carregarMenu =  function(id) {
			    $(id).dcAccordion({
					eventType : 'click',
					autoClose : true,
					saveState : false,
					disableLink : true,
					speed : 'fast',
					showCount : false,
					autoExpand : false,
					menuClose: true,
					cookie : 'dcjq-accordion-1',
					classExpand : 'dcjq-current-parent'
				});
			    };
			    
			    definirBackgroundColor = function(id){
			    	var valorCampo = document.getElementById(id).value;
			    	
			    	definirBackgroundColorPiker(id, valorCampo);
			    };
			    
			    definirBackgroundColorPiker = function(idCampo, background){		
					if(background != null && background.includes('gradient')){
						document.getElementById(idCampo).style.backgroundImage = background;
					}else if(background != null && background.includes('#')){						
						document.getElementById(idCampo).style.backgroundColor = background+' !important;';
					}else if(background != null){			    								
						document.getElementById(idCampo).style.backgroundColor = '#'+background+' !important;';
					}
				};
				
				iniciarColorPiker =  function(idCampo, id){
					if($(id).hasClass('pick-a-color-markup') == false){						
			    	$(idCampo).pickAColor({
			    		showSavedColors: false
			    	});
					}
			    };
			    
			    iniciarColorPikerGradiente =  function(idForm, idCampo){
			    	var id = idForm+':'+idCampo;
			    	var idPanelGradiente = idCampo+'_gradiente';
			    	var idType = idCampo+'_type';
			    	var idAngle = idCampo+'_angle';
			    	const gp = new Grapick({el: '#'+idPanelGradiente, min: 1, max:99});  									
					var valorCampo = document.getElementById(id).value;						
					definirBackgroundColorPiker(idPanelGradiente, valorCampo);
					definirBackgroundColorPiker(id, valorCampo);
					var swType = document.getElementById(idType);
					var swAngle = document.getElementById(idAngle);
					
					   
					    if(valorCampo.includes('repeating-radial')){
					    	swType.value = 'repeating-radial'
					    	gp.setType('repeating-radial');
					    }else if(valorCampo.includes('repeating-linear')){
					    	swType.value = 'repeating-linear'
				    		gp.setType('repeating-linear');
					    }else if(valorCampo.includes('radial')){
					    	swType.value = 'radial'
				    		gp.setType('radial');
					    }else if(valorCampo.includes('linear')){
					    	swType.value = 'linear'
				    		gp.setType('linear');
				   		}
					    
					  	if(valorCampo.includes('top')){
					  		swAngle.value = 'top';
					  		gp.setDirection('top');
					  	}else if(valorCampo.includes('right')){
					  		swAngle.value = 'right';
					  		gp.setDirection('right');
					  	}else if(valorCampo.includes('center')){
					  		swAngle.value = 'center';
					  		gp.setDirection('center');
					  	}else if(valorCampo.includes('bottom')){
					  		swAngle.value = 'bottom';
					  		gp.setDirection('bottom');					  		
					  	}else if(valorCampo.includes('left')){
					  		swAngle.value = 'left';
					  		gp.setDirection('left');
					  	}
					  	
					 
					  	
					  	gp.on('change', complete => {
							document.getElementById(id).value = gp.getSafeValue();
							definirBackgroundColorPiker(id, gp.getSafeValue());						
						});
					  	

					    swType.addEventListener('change', function(e) {
					       gp.setType(this.value);
					     })

					     swAngle.addEventListener('change', function(e) {
					       gp.setDirection(this.value);
					    })
					    
					 	if(valorCampo.includes('gradient')){
							var colors = valorCampo.substring(valorCampo.indexOf('rgb'), valorCampo.size);
							colors = colors.substring(0, colors.lastIndexOf(')'));
							colors = colors.trim();						
							colors = colors.split('rgb');
							var count = 0;
							for(var x = 0; x  < colors.length; x++){
								var cor = colors[x];
								if(cor.includes('%')){									
									gp.addHandler(cor.substring(cor.indexOf(')')+1, cor.indexOf('%')).trim(), 'rgb'+cor.substring(cor.indexOf('('), cor.indexOf(')')+1).trim(), 0);
									count++;
								};
							};
						}else if(valorCampo.includes('#')){	
							gp.addHandler(100, valorCampo , 0);
						}else{
							gp.addHandler(100, '#'+valorCampo , 0);
						}
					  	gp.updatePreview();					  
			    };

			    
			    
		
			    
			    $(window).resize(function() {				    	
			    	$(".rf-pp-cntr").css("left",(($(window).width() - ($('.rf-pp-cntr').width()))/2));			    				    	
			    });
			    
			    
				clickUpload = function(idPanel){
					var input = $(idPanel).find("input:file");
					$(input).click();					
				};
				
				clickBotao = function(id){
					document.getElementById(id).click();					
				};
				
				clickBotaoCss = function(id, validarFocus){
					$(id).each(function(){
						if(!validarFocus  && $(this).is(':visible') && !$(this).is(':disabled')){
							$(this).click();
						}else	if($(this).is(':visible') && !$(this).is(':disabled') && validarFocus)
							$(this).click();
						}
					);								
				};
				
				
				acionarBotaoPelaTeclaEnter =  function(event, id){
					code = (event.keyCode ? event.keyCode: event.which);					
					if (code == 13 || (event.target.type == '' && code == 32)) {
						event.preventDefault();					
						document.getElementById(id).click();
					}
				};
				
				collapseCaixaMensagem = function(){					
					if($('#box-options-email').hasClass('close')){			    		
			    		$('#box-options-email').addClass('open');
			    		$('#box-options-email').removeClass('close');			    		
			    	}else{
			    		$('#box-options-email').addClass('close');
			    		$('#box-options-email').removeClass('open');	
			    	}
				};
				
				collapseIconeMenuTopo = function(){							
					if($('#box-icone-menu-topo').is(':visible')){			    		
			    		$('#box-icone-menu-topo').hide();
			    		$('#box-icone-menu-topo').removeClass('rounded');			    		
			    		$('#box-icone-menu-topo').removeClass('backMenu');			    		
			    		$('.iconMsg').removeClass('fonteMenu');			    		
			    		$('.iconMsg2').removeClass('fonteMenu');			    		
			    		$('.iconMsg3').removeClass('fonteMenu');			    		
			    		$('.iconeNovidades').removeClass('fonteMenu');			    		
			    		$('.iconeAvalInst').removeClass('fonteMenu');			    		
			    		$('.iconDocSign').removeClass('fonteMenu');
			    		$('.iconTimeLine').removeClass('fonteMenu');
			    		$('.iconePreferencia').removeClass('fonteMenu');
			    		$('.iconQrCode').removeClass('fonteMenu');
			    		$('.iconMoodle').removeClass('fonteMenu');
			    		$('.iconForum').removeClass('fonteMenu');
			    		$('.iconMapaDoc').removeClass('fonteMenu');
			    		$('.iconAtivDiscur').removeClass('fonteMenu');
			    		$('.iconeEstatisticaUsoSei').removeClass('fonteMenu');
			    	}else{
			    		$('#box-icone-menu-topo').show();			    		
			    		$('#box-icone-menu-topo').addClass('rounded');			    		
			    		$('#box-icone-menu-topo').addClass('backMenu');
			    		$('.iconMsg').addClass('fonteMenu');
			    		$('.iconMsg2').addClass('fonteMenu');
			    		$('.iconMsg3').addClass('fonteMenu');
			    		$('.iconeNovidades').addClass('fonteMenu');
			    		$('.iconeAvalInst').addClass('fonteMenu');
			    		$('.iconDocSign').addClass('fonteMenu');
			    		$('.iconTimeLine').addClass('fonteMenu');
			    		$('.iconePreferencia').addClass('fonteMenu');
			    		$('.iconQrCode').addClass('fonteMenu');
			    		$('.iconMoodle').addClass('fonteMenu');
			    		$('.iconForum').addClass('fonteMenu');
			    		$('.iconMapaDoc').addClass('fonteMenu');
			    		$('.iconAtivDiscur').addClass('fonteMenu');
			    		$('.iconeEstatisticaUsoSei').addClass('fonteMenu');
			    	}
				};
				
				 $(window).resize(function() {	
					 if($(window).width() > 700){
				    		$('#box-icone-menu-topo').removeClass('rounded');			    		
				    		$('#box-icone-menu-topo').removeClass('backMenu');			    		
				    		$('.iconMsg').removeClass('fonteMenu');	
				    		$('.iconMsg2').removeClass('fonteMenu');	
				    		$('.iconMsg3').removeClass('fonteMenu');	
				    		$('.iconTimeLine').removeClass('fonteMenu');	
				    		$('.iconeNovidades').removeClass('fonteMenu');			    		
				    		$('.iconeAvalInst').removeClass('fonteMenu');			    		
				    		$('.iconDocSign').removeClass('fonteMenu');	
				    		$('.iconePreferencia').removeClass('fonteMenu');	
				    		$('.iconQrCode').removeClass('fonteMenu');	
				    		$('.iconMoodle').removeClass('fonteMenu');	
				    		$('.iconForum').removeClass('fonteMenu');	
				    		$('.iconAtivDiscur').removeClass('fonteMenu');	
				    		$('.iconMapaDoc').removeClass('fonteMenu');	
				    		$('.iconeEstatisticaUsoSei').removeClass('fonteMenu');
						 	$('#box-icone-menu-topo').show();						 	
					 }else{
							$('#box-icone-menu-topo').hide();							
					 }
				 });
				
				
				closeOtmPanel = function(idDivCloe) {
					 var idPanelClose = '#'+idDivCloe+'close';
					 var idPanelBody = 	'#'+idDivCloe+'body';
					 var idPanelOpen = 	'#'+idDivCloe+'open';
					 
					$(idPanelClose).hide();
					$(idPanelBody).hide();
					$(idPanelOpen).show();					
				};
				
				openOtmPanel = function(idDivShow) {
					 var idPanelClose = '#'+idDivShow+'close';
					 var idPanelBody = 	'#'+idDivShow+'body';
					 var idPanelOpen = 	'#'+idDivShow+'open';
					 
					$(idPanelClose).show();
					$(idPanelBody).show();
					$(idPanelOpen).hide();
				};
				
				openCloseOtmPanel = function(idDivShow, event) {
					 var idPanelBody = 	'#'+idDivShow+'body';
					 console.log(event.relatedTarget);
					 if($(idPanelBody).css('display') == 'none'){
						 openOtmPanel(idDivShow);
					 }else{
						 closeOtmPanel(idDivShow);
					 }					
				};
				
				maximizarOtmPanel = function(idDivShow) {
					var idPanel = '#'+idDivShow;
					var idPanelMinimize = '#'+idDivShow+'minimize';
					var idPanelMaximize = '#'+idDivShow+'maximize';					
					$(idPanel).addClass("maximizarOtmPanel");
					$(idPanelMaximize).hide();
					$(idPanelMinimize).show();
				};
				
				minimizarOtmPanel = function(idDivShow) {
					var idPanel = '#'+idDivShow;
					var idPanelMinimize = '#'+idDivShow+'minimize';
					var idPanelMaximize = '#'+idDivShow+'maximize';
					$(idPanel).removeClass("maximizarOtmPanel");
					$(idPanelMinimize).hide();
					$(idPanelMaximize).show();
				};
			
				removeClass =  function(id, classCss){					
					$(id).removeClass(classCss);
				};
				
				addClass =  function(id, classCss){
					$(id).addClass(classCss);
				};
				
				scrollTO = function(idElement) {
					
					$('html, body').animate({
						scrollTop: $(idElement).offset().top - 120			    
					},1000);
				};
						
				clickUpload = function(idPanel){
					var input = $(idPanel).find("input:file");
					$(input).click();					
				};
				
				
				iniciarPickList = function(){
					$('.rf-pick-rem').each(
							function(){
								
								if($(this).has("i").length == 0 ) {
									$(this).append('<i class="fas fa-angle-left text-danger fs16 mr5 ml5"/>');
								}
							}
							);
			    	$('.rf-pick-add').each(
							function(){
								if($(this).has("i").length == 0 ) {
								$(this).append('<i class="fas fa-angle-right text-info fs16 mr5 ml5"/>');
								}
							}
							);
			    	$('.rf-pick-add-all').each(
							function(){
								if($(this).has("i").length == 0 ) {
								$(this).append('<i class="fas fa-angle-double-right text-info fs16 mr5 ml5"/>');
								}
							}
							);
			    	$('.rf-pick-rem-all').each(
							function(){
								if($(this).has("i").length == 0 ) {
								$(this).append('<i class="fas fa-angle-double-left text-danger fs16 mr5 ml5"/>');
								}
							}
							);
			    	$('.rf-ord-up-tp').each(
							function(){
								if($(this).has("i").length == 0 ) {
								$(this).append('<i class="fas fa-angle-double-up text-primary fs16 mr5 ml5"/>');
								}
							}
							);
			    	$('.rf-ord-up').each(
							function(){
								if($(this).has("i").length == 0 ) {
								$(this).append('<i class="fas fa-angle-up text-primary  fs16 mr5 ml5"/>');
								}
							}
							);
			    	$('.rf-ord-dn-bt').each(
							function(){
								if($(this).has("i").length == 0 ) {
								$(this).append('<i class="fas fa-angle-double-down text-primary fs16 mr5 ml5"/>');
								}
							}
							);
			    	$('.rf-ord-dn').each(
							function(){
								if($(this).has("i").length == 0 ) {
								$(this).append('<i class="fas fa-angle-down fs16 text-primary  mr5 ml5"/>');
								}
							}
							);
				};	
				
				initPanel = function(id, styleClassHeader, styleHeader, styleClassBody, styleBody, styleClassFooter, styleFooter, styleClassControl){
					$('.ui-panel').each(function(){
						if(this.id.indexOf(id) >= 0 ){
					var idHeader= this.id+"_header";
					var idBody= this.id+"_content";
					var idFooter = this.id+"_footer";					
					var idControl = this.id+'_toggler';					
					if(styleClassHeader != '' || styleHeader != ''){						
						$('.ui-panel-titlebar').each(function(){
							if(this.id.indexOf(idHeader) >= 0){
						if(styleClassHeader != ''){	
							$(this).addClass(styleClassHeader);
						}
						if(styleHeader != ''){							
							$(this).attr('style', styleHeader);
						}
						}
						});
						
					}
					if(styleClassBody != '' || styleBody != ''){						
						$('.ui-panel-content').each(function(){
							if(this.id.indexOf(idBody) >= 0){
					if(styleClassBody != ''){							
						$(this).addClass(styleClassBody);
					}
					if(styleBody != ''){							
						$(this).attr('style', styleBody);
					}
					}
					});
					}
					if(styleClassFooter != '' || styleFooter != ''){						
						$('.ui-panel-footer').each(function(){
							if(this.id.indexOf(idFooter) >= 0){
					if(styleClassFooter != ''){							
						$(this).addClass(styleClassFooter);
					}
					if(styleFooter != ''){							
						$(this).attr('style', styleFooter);
					}
					}
					});
					}
					if(styleClassControl != ''){	
						$('.ui-panel-titlebar-icon').each(function(){
							if(this.id.indexOf(idControl) >= 0){						
							$(this).addClass(styleClassControl);
						}});
					}
					}
				});
				};
				
//												urlUpload, urlDownload, urlRepositorio, ul,
				initEditor = function(id, apresentarToolbar, readonlyText, height,  theme){					
					var minHeight = '300px';
					if(height == 'auto'){
						minHeight = '0px';
					}
					var editor = Jodit.make(id, {
					    zIndex: 4004,
					    readonly: readonlyText,
					    showCharsCounter: apresentarToolbar,
  						showWordsCounter: apresentarToolbar,
  						showXPathInStatusbar: apresentarToolbar,
					    activeButtonsInReadOnly: ['source', 'fullsize', 'print', 'about', 'dots'],
					    toolbarButtonSize: 'middle',
					    theme: theme,
					    saveModeInCookie: true,
					    spellcheck: true,					    
					    editorCssClass: true,
					    triggerChangeEvent: true,
					    width: 'auto',
					    height: height,
					    minHeight: minHeight,
					    direction: '',
					    language: 'auto',
					    showPlaceholder: false,
					    debugLanguage: false,
					    i18n: 'pt',
					    tabIndex: -1,
					    toolbar: apresentarToolbar,
					    enter: "P",
					    defaultMode: Jodit.MODE_WYSIWYG,
					    useSplitMode: true,
					    pageBreak: {
							separator: '<!-- pagebreak -->'
						  },
					    colors: {
					        greyscale:  ['#000000', '#434343', '#666666', '#999999', '#B7B7B7', '#CCCCCC', '#D9D9D9', '#EFEFEF', '#F3F3F3', '#FFFFFF'],
					        palette:    ['#980000', '#FF0000', '#FF9900', '#FFFF00', '#00F0F0', '#00FFFF', '#4A86E8', '#0000FF', '#9900FF', '#FF00FF'],
					        full: [
					            '#E6B8AF', '#F4CCCC', '#FCE5CD', '#FFF2CC', '#D9EAD3', '#D0E0E3', '#C9DAF8', '#CFE2F3', '#D9D2E9', '#EAD1DC',
					            '#DD7E6B', '#EA9999', '#F9CB9C', '#FFE599', '#B6D7A8', '#A2C4C9', '#A4C2F4', '#9FC5E8', '#B4A7D6', '#D5A6BD',
					            '#CC4125', '#E06666', '#F6B26B', '#FFD966', '#93C47D', '#76A5AF', '#6D9EEB', '#6FA8DC', '#8E7CC3', '#C27BA0',
					            '#A61C00', '#CC0000', '#E69138', '#F1C232', '#6AA84F', '#45818E', '#3C78D8', '#3D85C6', '#674EA7', '#A64D79',
					            '#85200C', '#990000', '#B45F06', '#BF9000', '#38761D', '#134F5C', '#1155CC', '#0B5394', '#351C75', '#733554',
					            '#5B0F00', '#660000', '#783F04', '#7F6000', '#274E13', '#0C343D', '#1C4587', '#073763', '#20124D', '#4C1130'
					        ]
					    },
					    colorPickerDefaultTab: 'background',
					    imageDefaultWidth: 300,
					    removeButtons: [],
					    disablePlugins: [],
					    extraButtons: [],
					    sizeLG: 900,
					    sizeMD: 700,
					    sizeSM: 400,
					    sizeSM: 400,
					    buttons: [
					        'source', '|',
					        'bold',
					        'strikethrough',
					        'underline',
					        'italic', '|',
					        'ul',
					        'ol', '|',
					        'outdent', 'indent',  '|',
					        'font',
					        'fontsize',
					        'brush',
					        'paragraph', '|',
					        'image',
					        'video',
					        'table',
					        'link', '|',
					        'align', 'undo', 'redo', '|',
					        'hr',
					        'eraser',
					        'copyformat', '|',
					        'symbol',
					        'fullsize',
					        'print', 
					        
					    ],
					    buttonsXS: [
					        'bold',
					        'image', '|',
					        'brush',
					        'paragraph', '|',
					        'align', '|',
					        'undo', 'redo', '|',
					        'eraser',
					        'dots'
					    ],
					    events: {},
					    textIcons: false,
					    enableDragAndDropFileToEditor: true,
					    
					    uploader: {
					        insertImageAsBase64URI: true,
							defaultHandlerSuccess: function (data, resp) {
								var i, field = 'files';
								if (data[field] && data[field].length) {
									for (i = 0; i < data[field].length; i += 1) {
										this.s.insertImage(data.baseurl + data[field][i]);
									}
								}
							},
							error: function (e) {
								alert('Erro ao realizar upload - '+ e);
							}
					    },
					  
					});
				
				};
				
				

			})(jQuery);
			
			
				(function(window) { 
				  'use strict';  
				var noback = { 
					 
					//globals 
					version: '0.0.1', 
					history_api : typeof history.pushState !== 'undefined', 
					 
					init:function(){ 
						window.location.hash = '#no-back'; 
						noback.configure(); 
					}, 
					 
					hasChanged:function(){ 
						if (window.location.hash == '#no-back' ){ 
							window.location.hash = '#BLOQUEIO';
							//mostra mensagem que não pode usar o btn volta do browser
							if($( "#msgAviso" ).css('display') =='none'){
								$( "#msgAviso" ).slideToggle("slow");
							}
						} 
					}, 
					 
					checkCompat: function(){ 
					
					 if (window.attachEvent) { 
							window.attachEvent("onhashchange", noback.hasChanged); 
						}else{ 
							window.onhashchange = noback.hasChanged; 
						} 
					}, 
					 
					configure: function(){ 
						if ( window.location.hash == '#no-back' ) { 
							if ( this.history_api ){ 
								history.pushState(null, '', '#BLOQUEIO'); 
							}else{  
								window.location.hash = '#BLOQUEIO';
								//mostra mensagem que não pode usar o btn volta do browser
								if($( "#msgAviso" ).css('display') =='none'){
									$( "#msgAviso" ).slideToggle("slow");
								}
							} 
						} 
						noback.checkCompat(); 
						noback.hasChanged(); 
					} 
					 
					}; 
					noback.init();					
			});
			
		