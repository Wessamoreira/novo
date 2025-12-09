(function($) {
	$(".meter > span").each(function() {
		$(this)
			.data("origWidth", $(this).width())
			.width(0)
			.animate({
				width: $(this).data("origWidth")
			}, 1200);
	});
});


function verifica(campo){
	senha = document.getElementById(campo).value;
	forca = 0;
	mostra = document.getElementById("page-wrap");
	if((senha.length >= 4) && (senha.length <= 7)){
		forca += 10;
	}else if(senha.length>7){
	
		forca += 25;
	}
	if(senha.match(/[a-z]+/)){
	
		forca += 10;
	}
	if(senha.match(/[A-Z]+/)){
	
		forca += 20;
	}
	if(senha.match(/[0-9]/)){
	
		forca += 20;
	}
	if(senha.match(/[@#$%^&+=]/)){
	
		forca += 25;
	}
	return mostra_res();
};
function mostra_res(){
	if(forca < 30){
		mostra.innerHTML = '<div class="meter red nostripes"> <span style="width: '+forca+'%"></span> </div>';
	}else if((forca >= 30) && (forca < 60)){
		mostra.innerHTML = '<div class="meter orange nostripes"> <span style="width: '+forca+'%"></span> </div>';
	}else if((forca >= 60) && (forca < 85)){
	mostra.innerHTML = '<div class="meter blue nostripes"> <span style="width: '+forca+'%"></span> </div>';
	}else{
	mostra.innerHTML = '<div class="meter nostripes"> <span style="width: '+forca+'%"></span> </div>';

	}
};