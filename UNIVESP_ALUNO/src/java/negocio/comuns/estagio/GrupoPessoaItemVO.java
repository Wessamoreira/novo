package negocio.comuns.estagio;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.Constantes;

public class GrupoPessoaItemVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3027241749211290744L;
	private Integer codigo;
	private GrupoPessoaVO grupoPessoaVO;
	private PessoaVO pessoaVO;
	private StatusAtivoInativoEnum statusAtivoInativoEnum;
	/***
	 * transient
	 */
	private String operacao;
	private Integer qtdeEstagioEmCorrecaoAnalise;
	private Integer qtdeEstagioObrigatorio;
	private Integer qtdeEstagioNaoObrigatorio;
	private String  codigoEstagios;
	private List<Integer> listarCodigoEstagioRedistribuir;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public GrupoPessoaVO getGrupoPessoaVO() {
		if (grupoPessoaVO == null) {
			grupoPessoaVO = new GrupoPessoaVO();
		}
		return grupoPessoaVO;
	}

	public void setGrupoPessoaVO(GrupoPessoaVO grupoEstagioVO) {
		this.grupoPessoaVO = grupoEstagioVO;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public StatusAtivoInativoEnum getStatusAtivoInativoEnum() {
		if (statusAtivoInativoEnum == null) {
			statusAtivoInativoEnum = StatusAtivoInativoEnum.ATIVO;
		}
		return statusAtivoInativoEnum;
	}

	public void setStatusAtivoInativoEnum(StatusAtivoInativoEnum statusAtivoInativoEnum) {
		this.statusAtivoInativoEnum = statusAtivoInativoEnum;
	}

	public String getOperacao() {
		if (operacao == null) {
			operacao = "";
		}
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	
	public Integer getQtdeEstagioObrigatorio() {
		if (qtdeEstagioObrigatorio == null) {
			qtdeEstagioObrigatorio = 0;
		}
		return qtdeEstagioObrigatorio;
	}

	public void setQtdeEstagioObrigatorio(Integer qtdeEstagioObrigatorio) {
		this.qtdeEstagioObrigatorio = qtdeEstagioObrigatorio;
	}

	public Integer getQtdeEstagioNaoObrigatorio() {
		if (qtdeEstagioNaoObrigatorio == null) {
			qtdeEstagioNaoObrigatorio = 0;
		}
		return qtdeEstagioNaoObrigatorio;
	}

	public void setQtdeEstagioNaoObrigatorio(Integer qtdeEstagioNaoObrigatorio) {
		this.qtdeEstagioNaoObrigatorio = qtdeEstagioNaoObrigatorio;
	}

	public boolean isOperacaoAdicionar() {
		return getOperacao().equals("INCLUIR");
	}
	public boolean isOperacaoInativar() {
		return getOperacao().equals("INATIVAR");
	}
	
	public boolean isOperacaoCpfInvalido() {
		return getPessoaVO().getNome().equals("Pessoa Não Cadastrada!");
	}
	
	public String corTexto;
	public String getCorTexto() {
		if(corTexto == null) {
			if(isNovoObj()) {
				corTexto = "text-success";
				setOperacao("INCLUIR");
			}else if ((!isNovoObj() && getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.INATIVO) && getPessoaVO().getSelecionado())){
				corTexto = "text-success";
				setOperacao("REATIVAR");
			}else if ((!isNovoObj() && getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.INATIVO)) 
					|| (!isNovoObj() && getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO) && !getPessoaVO().getSelecionado())){
				corTexto = "text-danger";
				setOperacao("INATIVAR");
			}else if(isOperacaoCpfInvalido()) {
				corTexto = "text-warning";
				setOperacao("ERRO");			
			}else {
				corTexto = "";
				setOperacao("MANTER");
			}
		}
		return corTexto;
	} 
	
	

	public String getOrdenacaoImportacao() {
		getCorTexto();
		if(getOperacao().equals("INCLUIR") || getOperacao().equals("REATIVAR")) {
			return "0"+getPessoaVO().getNome();
		}else if(getOperacao().equals("INATIVAR")) {
			return "1"+getPessoaVO().getNome();
		}else if(getOperacao().equals("ERRO")) {
			return "2"+getPessoaVO().getNome();
		}
		return "3"+getPessoaVO().getNome();
	}

	public Integer getQtdeEstagioEmCorrecaoAnalise() {
		if(qtdeEstagioEmCorrecaoAnalise == null ) {
			qtdeEstagioEmCorrecaoAnalise = 0;
		}
		return qtdeEstagioEmCorrecaoAnalise;
	}

	public void setQtdeEstagioEmCorrecaoAnalise(Integer qtdeEstagioEmCorrecaoAnalise) {
		this.qtdeEstagioEmCorrecaoAnalise = qtdeEstagioEmCorrecaoAnalise;
	}

	public String getCodigoEstagios() {
		if(codigoEstagios == null ) {
			codigoEstagios =Constantes.EMPTY;
		}
		return codigoEstagios;
	}

	public void setCodigoEstagios(String codigoEstagios) {
		this.codigoEstagios = codigoEstagios;
	}

	public List<Integer> getListarCodigoEstagioRedistribuir() {
		if(listarCodigoEstagioRedistribuir == null ) {
			listarCodigoEstagioRedistribuir = new ArrayList<Integer>(0);
		}
		return listarCodigoEstagioRedistribuir;
	}

	public void setListarCodigoEstagioRedistribuir(List<Integer> listarCodigoEstagioRedistribuir) {
		this.listarCodigoEstagioRedistribuir = listarCodigoEstagioRedistribuir;
	}

}
