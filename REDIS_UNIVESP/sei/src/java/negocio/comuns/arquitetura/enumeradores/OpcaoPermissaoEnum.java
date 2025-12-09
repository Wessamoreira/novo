package negocio.comuns.arquitetura.enumeradores;

/**
 * @author Otimize
 *
 */
public enum OpcaoPermissaoEnum {
    NENHUM(-1, "", "Nenhum"), 
    TOTAL(100, "(0)(1)(2)(3)(9)(12)", "Total"), 
    TOTAL_SEM_EXCLUIR(99, "(0)(1)(2)(9)(12)", "Total Sem Excluir"),
    NOVO(9, "(9)", "Novo"),
    INCLUIR(1, "(1)", "Incluir"), 
    ALTERAR(2, "(2)", "Alterar"), 
    EXCLUIR(3, "(3)", "Excluir"),
    CONSULTAR(2, "(0)", "Consultar"),
	RELATORIO(13, "(12)", "Relatório");
		
	/**
	 * @param key
	 * @param permissao
	 * @param nome
	 */
	private OpcaoPermissaoEnum(Integer key, String permissao, String nome) {
		this.key = key;
		this.permissao = permissao;
		this.nome = nome;
	}
	private Integer key;
	private String permissao;
	private String nome;
	public Integer getKey() {
		if (key == null) {
			key = 0;
		}
		return key;
	}
	public void setKey(Integer key) {
		this.key = key;
	}
	public String getPermissao() {
		if (permissao == null) {
			permissao = "";
		}
		return permissao;
	}
	public void setPermissao(String permissao) {
		this.permissao = permissao;
	}
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
}
