package negocio.comuns.secretaria.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoUploadArquivoEnum {
	IMAGEM("jpg, png, JPEG, jpeg, gif, bmp"), 
	DOCUMENTO("doc, docx, ppt, pptx, pdf, xls, xlsx, rar, zip, 7z, txt"), 
	AUDIO("mp3, wma, aac, ogg, ac3, wav"), 
	VIDEO("avi, mpeg, mov, rmvb, mkv"),
	TODOS("jpg, png, JPEG, jpeg, gif, bmp, doc, docx, ppt, pptx, pdf, xls, xlsx, rar, zip, 7z, txt, mp3, wma, aac, ogg, ac3, wav, avi, mpeg, mov, rmvb, mkv");
	private String extensao;
	
	private TipoUploadArquivoEnum(String extensao) {
		this.extensao = extensao;
	}

	public String getExtensao() {		
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoUploadArquivoEnum_" + this.name());
	}
	
}
