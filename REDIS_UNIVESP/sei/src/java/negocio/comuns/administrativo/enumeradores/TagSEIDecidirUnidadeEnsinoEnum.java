package negocio.comuns.administrativo.enumeradores;
/**
 * @author Leonardo Riciolle
 * 			14/11/2014 
 */
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirUnidadeEnsinoEnum implements PerfilTagSEIDecidirEnum {
	
	UNIDADE_ENSINO_CODIGO("unidaeensino", "Código Unidade Ensino", "unidadeensino.codigo",TipoCampoEnum.INTEIRO, 20),
	UNIDADE_ENSINO_NOME("unidadeensino", "Nome Unidade Ensino", "unidadeensino.nome", TipoCampoEnum.TEXTO, 40),
	UNIDADE_ENSINO_NOMEEXPEDICAODIPLOMA("unidaeensino", "Nome Expedição Diploma", "unidadeensino.nomeExpedicaoDiploma",TipoCampoEnum.INTEIRO, 40),
	UNIDADE_ENSINO_CIDADE("unidaeensinocidade", "Cidade", "unidadeensinocidade.nome",TipoCampoEnum.TEXTO, 30),
	UNIDADE_ENSINO_ESTADO_SIGLA("unidaeensinoestado","Sigla Estado","unidadeensinoestado.sigla",TipoCampoEnum.TEXTO, 15),
	UNIDADE_ENSINO_ESTADO_NOME("unidaeensinoestado", "Nome Estado", "unidadeensinoestado.nome",TipoCampoEnum.TEXTO, 20),
	UNIDADE_ENSINO_CNPJ("unidadeensino", "CNPJ", "unidadeensino.CNPJ", TipoCampoEnum.TEXTO, 20),
	UNIDADE_ENSINO_INSCESTADUAL("unidadeensino", "Inscrição Estadual", "unidadeensino.inscEstadual", TipoCampoEnum.TEXTO, 20),
	UNIDADE_ENSINO_INSCMUNICIPAL("unidadeensino", "Inscrição Municipal", "unidadeensino.inscMunicipal", TipoCampoEnum.TEXTO, 20),
	UNIDADE_ENSINO_CODIGOIES("unidadeensino", "Código IES", "unidadeensino.codigoies", TipoCampoEnum.INTEIRO, 10),
	UNIDADE_ENSINO_CREDENCIAMENTOPORTARIA("unidadeensino", "Credenciamento", "unidadeensino.credenciamentoportaria", TipoCampoEnum.TEXTO, 30),
	UNIDADE_ENSINO_DATAPUBLICACAO("unidadeensino", "Data Publicação", "unidadeensino.datapublicacaodo", TipoCampoEnum.DATA, 20),
	UNIDADE_ENSINO_MANTENEDORA("unidadeensino", "Mantenedora", "unidadeensino.mantenedora", TipoCampoEnum.TEXTO, 40),
	UNIDADE_ENSINO_RAZAOSOCIAL("unidadeensino", "Razão Social", "unidadeensino.razaoSocial", TipoCampoEnum.TEXTO, 40),
	UNIDADE_ENSINO_ENDERECO("unidadeensino", "Endereço", "unidadeensino.endereco", TipoCampoEnum.TEXTO, 40),
	UNIDADE_ENSINO_SETOR("unidadeensino", "Bairro", "unidadeensino.setor", TipoCampoEnum.TEXTO, 30),
	UNIDADE_ENSINO_NUMERO("unidadeensino", "Número End.", "unidadeensino.numero", TipoCampoEnum.INTEIRO, 20),
	UNIDADE_ENSINO_COMPLEMENTO("unidadeensino", "Complemento End.",  "unidadeensino.complemento", TipoCampoEnum.TEXTO, 30),
	UNIDADE_ENSINO_CEP("unidadeensino", "CEP", "unidadeensino.CEP", TipoCampoEnum.TEXTO, 20),
	UNIDADE_ENSINO_TELCOMERCIAL1("unidadeensino", "Telefone 1", "unidadeensino.telComercial1", TipoCampoEnum.TEXTO, 20),
	UNIDADE_ENSINO_TELCOMERCIAL2("unidadeensino", "Telefone 2", "unidadeensino.telComercial2", TipoCampoEnum.TEXTO, 20),
	UNIDADE_ENSINO_TELCOMERCIAL3("unidadeensino", "Telefone 3", "unidadeensino.telComercial3", TipoCampoEnum.TEXTO, 20),
	;

	private TagSEIDecidirUnidadeEnsinoEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;
		this.entidade = entidade;
		this.campo = campo;
		this.atributo = atributo;
		this.tipoCampo = tipoCampo;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TagSEIDecidirUnidadeEnsinoEnum_" + this.name());
	}

	private String entidade;
	private String campo;
	private String atributo;
	private TipoCampoEnum tipoCampo;
	private Integer tamanhoCampo;
	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getCampo() {	
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public TipoCampoEnum getTipoCampo() {		
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampoEnum tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	@Override
	public String getTag() {	
		return this.name();
	}

	public String getAtributo() {
		if (atributo == null) {
			atributo = "";
		}
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
		
	/* (non-Javadoc)
	 * @see negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#getTamanhoCampo()
	 */
	@Override
	public Integer getTamanhoCampo() {

		return tamanhoCampo;
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#setTamanhoCampo(java.lang.Integer)
	 */
	@Override
	public void setTamanhoCampo(Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;		
	}
	
}
