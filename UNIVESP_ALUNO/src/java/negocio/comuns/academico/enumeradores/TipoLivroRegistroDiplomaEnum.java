package negocio.comuns.academico.enumeradores;

public enum TipoLivroRegistroDiplomaEnum {

	DIPLOMA, 
	CERTIFICADO;
	
	
	public boolean isTipoRegistroDiploma(){
		return this.name() != null && this.name().equals(TipoLivroRegistroDiplomaEnum.DIPLOMA.name());
	}
	
	public boolean isTipoRegistroCertificado(){
		return this.name() != null && this.name().equals(TipoLivroRegistroDiplomaEnum.CERTIFICADO.name());
	}
}
