package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.academico.Forum;


public enum TipoRecursoEducacionalEnum {

    
    TEXTO_HTML("../../resources/imagens/ead/textoHtml.png", "", "../../resources/imagens/ead/textoHtmlReduzido.png", "" ,"AMBOS",true, true, false, "far fa-file-code"),
    PAGINA_HTML("../../resources/imagens/ead/paginaHtml.png", "html, htm, HTML, HTM", "../../resources/imagens/ead/paginaHtmlReduzido.png", "" ,"AMBOS",false, false, false, "fas fa-file-code"),
    ARQUIVO_DOWNLOAD("../../resources/imagens/ead/arquivoDownload.png", "", "../../resources/imagens/ead/arquivoDonwloadReduzido.png", "" ,"AMBOS",true, false, true, "fas fa-file-download"),
    FLASH("../../resources/imagens/ead/flash.png", "swf, SWF", "../../resources/imagens/ead/flashReduzido.png", "" ,"AMBOS",true, true, true, "fab fa-adobe"),
    VIDEO_URL("../../resources/imagens/ead/videoUrl.png", "", "../../resources/imagens/ead/videoUrlReduzido.png", "" ,"AMBOS",true, true, false, "fas fa-video"),
//    VIDEO("../../resources/imagens/ead/video.png", "mp4, ogv, ogg, webm, MP4, OGV, OGG, WEBM", "../../resources/imagens/ead/videoReduzido.png", "" ,"AMBOS",true, true, true),
    IMAGEM("../../resources/imagens/ead/imagem.png", "gif, png, jpg, jpeg, bmp, GIF, PNG, JPG, JPEG, BMP", "../../resources/imagens/ead/imagemReduzido.png", "" ,"AMBOS",true, true, true, "fas fa-image"),
    EXERCICIO("../../resources/imagens/ead/exercicio.png", "", "../../resources/imagens/ead/exercicioReduzido.png", "" ,"ALUNO",true, false, false, "fas fa-tasks"),
    FORUM("../../resources/imagens/ead/forum.png", "", "../../resources/imagens/ead/forumReduzido.png", "" ,"ALUNO",true, false, false, "fas fa-users"),
    GRAFICO("../../resources/imagens/ead/grafico.png", "", "../../resources/imagens/ead/graficoReduzido.png", "" ,"AMBOS",true, true, false, "fas fa-chart-pie"),
    SLIDE_IMAGEM("../../resources/imagens/ead/slideImagem.png", "gif, png, jpg, jpeg, bmp, GIF, PNG, JPG, JPEG, BMP", "../../resources/imagens/ead/slideImagemReduzido.png", "" ,"AMBOS",true, true, true, "fas fa-images"),
    AUDIO("../../resources/imagens/ead/audio.png", "mp3, ogg, webm, MP3, OGG, WEBM", "../../resources/imagens/ead/audioReduzido.png", "" ,"AMBOS",true, true, true, "fas fa-headphones"),
	AVALIACAO_PBL("../../resources/imagens/ead/avaliacao_pbl.png", "", "../../resources/imagens/ead/avaliacao_pblReduzido.png", "../../resources/imagens/ead/avaliacao_finalizada_pblReduzido.png" ,"ALUNO",true, false, false, "fas fa-atlas"),
	AVALIACAO_ONLINE("../../resources/imagens/ead/avaliacaoOnlineRea.png", "", "../../resources/imagens/ead/avaliacaoOnlineRea.png", "" ,"ALUNO",true, false, false, "fas fa-globe"),
	ATA_PBL("../../resources/imagens/ead/ata_pbl.png", "", "../../resources/imagens/ead/ata_pblReduzido.png","../../resources/imagens/ead/ata_pblReduzido.png" ,"AMBOS",true, false, false, "far fa-address-book");
	
    private String urlIcone;
    private String icone;
    private String extensao;
    private String iconeReduzido;   
    private String iconeReduzidoSituacaoFinalizada;
    /**
     * Valores aceito para o campo tipoApresentacao: AMBOS, ALUNO, PROFESSOR;
     */
    private String tipoApresentacao;
    private boolean utilizarComoGatilho;
    private boolean utilizarComoConteudo;
    private boolean necessitaUploadArquivo;
    
        
    private TipoRecursoEducacionalEnum(String urlIcone, String extensao, String iconeReduzido, String iconeReduzidoSituacaoFinalizada,  String tipoApresentacao, boolean utilizarComoGatilho, boolean utilizarComoConteudo, boolean necessitaUploadArquivo, String icone) {
        this.urlIcone = urlIcone;
        this.iconeReduzido = iconeReduzido;
        this.iconeReduzidoSituacaoFinalizada = iconeReduzidoSituacaoFinalizada;
        this.utilizarComoGatilho = utilizarComoGatilho;
        this.utilizarComoConteudo = utilizarComoConteudo;
        this.necessitaUploadArquivo = necessitaUploadArquivo;
        this.extensao = extensao;
        this.tipoApresentacao = tipoApresentacao;
        this.icone = icone;
    }
    
    
    
    
    public String getExtensao() {
        return extensao;
    }



    
    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }



    public boolean getNecessitaUploadArquivo() {
        return necessitaUploadArquivo;
    }
    
    public void setNecessitaUploadArquivo(boolean necessitaUploadArquivo) {
        this.necessitaUploadArquivo = necessitaUploadArquivo;
    }

    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoRecursoEducacionalEnum_"+this.name());
    }


    public String getUrlIcone() {
        return urlIcone;
    }

    
    public void setUrlIcone(String urlIcone) {
        this.urlIcone = urlIcone;
    }


    
    public boolean getUtilizarComoGatilho() {
        return utilizarComoGatilho;
    }


    
    public void setUtilizarComoGatilho(boolean utilizarComoGatilho) {
        this.utilizarComoGatilho = utilizarComoGatilho;
    }


    
    public boolean getUtilizarComoConteudo() {
        return utilizarComoConteudo;
    }


    
    public void setUtilizarComoConteudo(boolean utilizarComoConteudo) {
        this.utilizarComoConteudo = utilizarComoConteudo;
    }
    
    private static List<TipoRecursoEducacionalEnum> tipoRecursoEducacionalConteudo;
    private static List<TipoRecursoEducacionalEnum> tipoRecursoEducacionalGatilho;
    private static List<TipoRecursoEducacionalEnum> tipoRecursoEducacionalGatilhoApoioProfessor;
    
  
    public static List<TipoRecursoEducacionalEnum> getTipoRecursoEducacionalConteudo() {
        if(tipoRecursoEducacionalConteudo == null){
            tipoRecursoEducacionalConteudo = new ArrayList<TipoRecursoEducacionalEnum>();
            for(TipoRecursoEducacionalEnum tipo:TipoRecursoEducacionalEnum.values()){
                if(tipo.getUtilizarComoConteudo() && !tipo.equals(TipoRecursoEducacionalEnum.FLASH)){
                    tipoRecursoEducacionalConteudo.add(tipo);
                }
            }                       
        }
        return tipoRecursoEducacionalConteudo;
    }
    
    public static List<TipoRecursoEducacionalEnum> getTipoRecursoEducacionalGatilho() {
       if(tipoRecursoEducacionalGatilho == null){
           tipoRecursoEducacionalGatilho = new ArrayList<TipoRecursoEducacionalEnum>();
           for(TipoRecursoEducacionalEnum tipo:TipoRecursoEducacionalEnum.values()){
               if(tipo.getUtilizarComoGatilho() && !tipo.equals(TipoRecursoEducacionalEnum.FLASH)){
                   tipoRecursoEducacionalGatilho.add(tipo);
               }
           }                       
       }
       return tipoRecursoEducacionalGatilho;
   }
    
    public static List<TipoRecursoEducacionalEnum> getTipoRecursoEducacionalGatilhoApoioProfessor() {
    	if(tipoRecursoEducacionalGatilhoApoioProfessor == null){
    		tipoRecursoEducacionalGatilhoApoioProfessor = new ArrayList<TipoRecursoEducacionalEnum>();
    		for(TipoRecursoEducacionalEnum tipo:TipoRecursoEducacionalEnum.values()){
    			if(tipo.getUtilizarComoGatilho() && !tipo.getTipoApresentacao().equals("ALUNO") && !tipo.equals(TipoRecursoEducacionalEnum.FLASH)){
    				tipoRecursoEducacionalGatilhoApoioProfessor.add(tipo);
    			}
    		}                       
    	}
    	return tipoRecursoEducacionalGatilhoApoioProfessor;
    }
    
    public String getIconeReduzido() {
        return iconeReduzido;
    }

    
    public void setIconeReduzido(String iconeReduzido) {
        this.iconeReduzido = iconeReduzido;
    }
    
    public String getIconeReduzidoSituacaoFinalizada() {
		return iconeReduzidoSituacaoFinalizada;
	}


	public void setIconeReduzidoSituacaoFinalizada(String iconeReduzidoSituacaoFinalizada) {
		this.iconeReduzidoSituacaoFinalizada = iconeReduzidoSituacaoFinalizada;
	}

   
    public String getIconeMenu(){
        switch (this) {
            case ARQUIVO_DOWNLOAD:
                return "../resources/imagens/ead/iconeArquivoDownload.png";
            case AUDIO:
                return "../resources/imagens/ead/iconeAudio.png";
            case EXERCICIO:
                return "../resources/imagens/ead/iconeExercicio.png";
            case FLASH:
                return "../resources//imagens/ead/iconeFlash.png";
            case FORUM:
                return "../resources/imagens/ead/iconeForum.png";
            case GRAFICO:
                return "../resources/imagens/ead/iconeGrafico.png";
            case IMAGEM:
                return "../resources/imagens/ead/iconeImagem.png";
            case PAGINA_HTML:
                return "../resources/imagens/ead/iconeHtml.png";
            case SLIDE_IMAGEM:
                return "../resources/imagens/ead/iconeSlideImagem.png";
            case TEXTO_HTML:
                return "../resources/imagens/ead/iconeHtml.png";
//            case VIDEO:
//                return "../resources/imagens/ead/iconeVideo.png";
            case VIDEO_URL:
                return "../resources/imagens/ead/iconeVideoUrl.png";                
            case AVALIACAO_PBL:
            	return "../resources/imagens/ead/avaliacao_pbl.png";
            case AVALIACAO_ONLINE:
            	return "../resources/imagens/ead/avaliacaoOnlineRea.png";       
            case ATA_PBL:
            	return "../resources/imagens/ead/ata_pbl.png";                
            default:
                return "../resources/imagens/ead/iconeHtml.png";
        }
    }
    
    public String getIconeMenuVisaoAluno(){
    	switch (this) {
    	case ARQUIVO_DOWNLOAD:
    		return "../resources/imagens/ead/iconeArquivoDownload.png";
    	case AUDIO:
    		return "../resources/imagens/ead/iconeAudio.png";
    	case EXERCICIO:
    		return "../resources/imagens/ead/iconeExercicio.png";
    	case FLASH:
    		return "../resources//imagens/ead/iconeFlash.png";
    	case FORUM:
    		return "../resources/imagens/ead/iconeForum.png";
    	case GRAFICO:
    		return "../resources/imagens/ead/iconeGrafico.png";
    	case IMAGEM:
    		return "../resources/imagens/ead/iconeImagem.png";
    	case PAGINA_HTML:
    		return "../resources/imagens/ead/iconeHtml.png";
    	case SLIDE_IMAGEM:
    		return "../resources/imagens/ead/iconeSlideImagem.png";
    	case TEXTO_HTML:
    		return "../resources/imagens/ead/iconeHtml.png";
//    	case VIDEO:
//    		return "../resources/imagens/ead/iconeVideo.png";
    	case VIDEO_URL:
    		return "../resources/imagens/ead/iconeVideoUrl.png";                
    	case AVALIACAO_PBL:
    		return "../resources/imagens/ead/avaliacao_pblReduzido.png";
    	case AVALIACAO_ONLINE:
        	return "../resources/imagens/ead/avaliacaoOnlineRea.png";
    	case ATA_PBL:
    		return "../resources/imagens/ead/ata_pbl.png";                
    	default:
    		return "../resources/imagens/ead/iconeHtml.png";
    	}
    }
    public String getIconeReduzidoSituacaoFinalizadaVisaoAluno(){
    	switch (this) {
    	case ARQUIVO_DOWNLOAD:
    		return "../resources/imagens/ead/iconeArquivoDownload.png";
    	case AUDIO:
    		return "../resources/imagens/ead/iconeAudio.png";
    	case EXERCICIO:
    		return "../resources/imagens/ead/iconeExercicio.png";
    	case FLASH:
    		return "../resources//imagens/ead/iconeFlash.png";
    	case FORUM:
    		return "../resources/imagens/ead/iconeForum.png";
    	case GRAFICO:
    		return "../resources/imagens/ead/iconeGrafico.png";
    	case IMAGEM:
    		return "../resources/imagens/ead/iconeImagem.png";
    	case PAGINA_HTML:
    		return "../resources/imagens/ead/iconeHtml.png";
    	case SLIDE_IMAGEM:
    		return "../resources/imagens/ead/iconeSlideImagem.png";
    	case TEXTO_HTML:
    		return "../resources/imagens/ead/iconeHtml.png";
//    	case VIDEO:
//    		return "../resources/imagens/ead/iconeVideo.png";
    	case VIDEO_URL:
    		return "../resources/imagens/ead/iconeVideoUrl.png";                
    	case AVALIACAO_PBL:
    		return "../resources/imagens/ead/avaliacao_finalizada_pblReduzido.png";
    	case AVALIACAO_ONLINE:
    		return "../resources/imagens/ead/avaliacaoOnlineRea.png";
    	case ATA_PBL:
    		return "../resources/imagens/ead/ata_pbl.png";                
    	default:
    		return "../resources/imagens/ead/iconeHtml.png";
    	}
    }
    
	public String getIconeMenuApoioProfessor(){
		switch (this) {
		case ARQUIVO_DOWNLOAD:
			return "../resources/imagens/ead/iconeArquivoDownload.png";
		case AUDIO:
			return "../resources/imagens/ead/iconeAudio.png";
		case EXERCICIO:
			return "../resources/imagens/ead/iconeExercicio.png";
		case FLASH:
			return "../resources/imagens/ead/iconeFlash.png";
		case FORUM:
			return "../resources/imagens/ead/iconeForum.png";
		case GRAFICO:
			return "../resources/imagens/ead/iconeGrafico.png";
		case IMAGEM:
			return "../resources/imagens/ead/iconeImagem.png";
		case PAGINA_HTML:
			return "../resources/imagens/ead/iconeHtml.png";
		case SLIDE_IMAGEM:
			return "../resources/imagens/ead/iconeSlideImagem.png";
		case TEXTO_HTML:
			return "../resources/imagens/requerimento.png";
//		case VIDEO:
//			return "../resources/imagens/ead/iconeVideo.png";
		case VIDEO_URL:
			return "../resources/imagens/ead/iconeVideoUrl.png";    
		case AVALIACAO_PBL:
			return "../resources/imagens/ead/avaliacao_pbl.png";                
		case AVALIACAO_ONLINE:
			return "../resources/imagens/ead/avaliacaoOnlineRea.png";                
		case ATA_PBL:
			return "../resources/imagens/ead/ata_pbl.png";
		default:
			return "../resources/imagens/ead/iconeHtml.png";
		}
	}
    
    public boolean isTipoRecursoApresentarURL() {
    	return !isTipoRecursoAtaPbl()  && !isTipoRecursoAvaliacaoPbl() && !isTipoAvaliacaoOnline();
 	}
     
     public boolean isTipoRecursoAtaPbl() {
 		return name().equals(TipoRecursoEducacionalEnum.ATA_PBL.name());
 	}
     
     public boolean isTipoRecursoAvaliacaoPbl() {
     	return name().equals(TipoRecursoEducacionalEnum.AVALIACAO_PBL.name());
     }
     
     public boolean isTipoRecursoExercicio() {
     	return name().equals(TipoRecursoEducacionalEnum.EXERCICIO.name());
     }
     
     public boolean isTipoRecursoForum() {
     	return name().equals(TipoRecursoEducacionalEnum.FORUM.name());
     }
     
     public boolean isTipoRecursoTextoHtml() {
     	return name().equals(TipoRecursoEducacionalEnum.TEXTO_HTML.name());
     }
     
     public boolean isTipoAvaliacaoOnline() {
     	return name().equals(TipoRecursoEducacionalEnum.AVALIACAO_ONLINE.name());
     }
    
    public String getTipoApresentacao() {
		return tipoApresentacao;
	}




	public void setTipoApresentacao(String tipoApresentacao) {
		this.tipoApresentacao = tipoApresentacao;
	}




	public String getIcone() {
		return icone;
	}




	public void setIcone(String icone) {
		this.icone = icone;
	}
  
    
    
}
