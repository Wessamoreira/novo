package negocio.comuns.academico;


import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

public class NotaConceitoIndicadorAvaliacaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5721573996972376946L;
	private Integer codigo;
	private String descricao;
	private String nomeArquivo;
	private String nomeArquivoApresentar;
	private PastaBaseArquivoEnum pastaBaseArquivo;
	private StatusAtivoInativoEnum situacao;
	
	/**
	 * Transiente  
	 * 
	 */
	private String urlImagem;
	private String nomeArquivoAnt;
	
	
	
	public String getNomeArquivoAnt() {
		if (nomeArquivoAnt == null) {
			nomeArquivoAnt = "";
		}
		return nomeArquivoAnt;
	}
	public void setNomeArquivoAnt(String nomeArquivoAnt) {
		this.nomeArquivoAnt = nomeArquivoAnt;
	}
	public String getUrlImagem() {
		if (urlImagem == null) {
			urlImagem = "";
		}
		return urlImagem;
	}
	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getNomeArquivo() {
		if (nomeArquivo == null) {
			nomeArquivo = "";
		}
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public String getNomeArquivoApresentar() {
		if (nomeArquivoApresentar == null) {
			nomeArquivoApresentar = "";
		}
		return nomeArquivoApresentar;
	}
	public void setNomeArquivoApresentar(String nomeArquivoApresentar) {
		this.nomeArquivoApresentar = nomeArquivoApresentar;
	}
	public PastaBaseArquivoEnum getPastaBaseArquivo() {
		if (pastaBaseArquivo == null) {
			pastaBaseArquivo = PastaBaseArquivoEnum.INDICADOR_AVALIACAO;
		}
		return pastaBaseArquivo;
	}
	public void setPastaBaseArquivo(PastaBaseArquivoEnum pastaBaseArquivo) {
		this.pastaBaseArquivo = pastaBaseArquivo;
	}
	public StatusAtivoInativoEnum getSituacao() {
		if (situacao == null) {
			situacao = StatusAtivoInativoEnum.ATIVO;
		}
		return situacao;
	}
	public void setSituacao(StatusAtivoInativoEnum situacao) {
		this.situacao = situacao;
	}
	

	
	
}
