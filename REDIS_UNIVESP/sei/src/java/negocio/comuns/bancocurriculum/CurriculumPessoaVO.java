package negocio.comuns.bancocurriculum;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

public class CurriculumPessoaVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2964072523489556182L;
	private Integer codigo;
	private PessoaVO pessoa;
	private String nomeRealArquivo;
	private String nomeApresentacaoArquivo;
	private String descricao;
	private Date dataCadastro;
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public PessoaVO getPessoa() {
		if(pessoa == null){
			pessoa = new PessoaVO();
		}
		return pessoa;
	}
	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}
	public String getNomeRealArquivo() {
		if(nomeRealArquivo == null){
			nomeRealArquivo = "";
		}
		return nomeRealArquivo;
	}
	public void setNomeRealArquivo(String nomeRealArquivo) {
		this.nomeRealArquivo = nomeRealArquivo;
	}
	public String getNomeApresentacaoArquivo() {
		if(nomeApresentacaoArquivo == null){
			nomeApresentacaoArquivo = "";
		}
		return nomeApresentacaoArquivo;
	}
	public void setNomeApresentacaoArquivo(String nomeApresentacaoArquivo) {
		this.nomeApresentacaoArquivo = nomeApresentacaoArquivo;
	}
	public String getDescricao() {
		if(descricao == null){
			descricao = "";
		}
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getDataCadastro() {
		if(dataCadastro == null){
			dataCadastro= new Date();
		}
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	
	

}
