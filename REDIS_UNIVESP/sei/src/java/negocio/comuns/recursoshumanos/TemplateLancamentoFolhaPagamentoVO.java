package negocio.comuns.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoTemplateFolhaPagamentoEnum;

/**
 * Reponsavel por manter os dados da entidade TemplateLancamentoFolhaPagamento.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TemplateLancamentoFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private Boolean lancarEventosFuncionario;
	private Boolean lancarEmprestimos;
	private Boolean lancarValeTransporte;
	private Boolean lancarSalarioMaternidade;
	private String formaContratacaoFuncionario;
	private String tipoRecebimento;
	private String situacaoFuncionario;
	private TipoTemplateFolhaPagamentoEnum tipoTemplateFolhaPagamento;
	
	private FuncionarioCargoVO funcionarioCargoVO;
	
	private SecaoFolhaPagamentoVO secaoFolhaPagamento;
	
	private List<TemplateEventoFolhaPagamentoVO> listaEventosDoTemplate;

	private Boolean lancarEventosGrupo;
	private Boolean lancarSalarioComposto;
	private Boolean lancarPensao;
	
	//Transiente
	private LancamentoFolhaPagamentoVO lancamentoFolhaPagamento;
	
	//Sprint 5
	//Lancamento de Ferias
	private Boolean lancarFerias;
	private Boolean lancarAdiantamentoFerias;
	
	//Sprint 6
	private Boolean lancarEventosFolhaNormal;
	private Date dataHora;
	
	//Sprint 7
	private Boolean lancar13Parcela1;
	private Boolean lancar13Parcela2;
	
	private Boolean lancarRescisao;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public boolean getLancarEventosFuncionario() {
		if (lancarEventosFuncionario == null) {
			lancarEventosFuncionario = false;
		}
		return lancarEventosFuncionario;
	}

	public void setLancarEventosFuncionario(boolean lancarEventosFuncionario) {
		this.lancarEventosFuncionario = lancarEventosFuncionario;
	}

	public boolean getLancarEmprestimos() {
		if (lancarEmprestimos == null) {
			lancarEmprestimos = false;
		}
		return lancarEmprestimos;
	}

	public void setLancarEmprestimos(boolean lancarEmprestimos) {
		this.lancarEmprestimos = lancarEmprestimos;
	}

	public boolean getLancarValeTransporte() {
		if (lancarValeTransporte == null) {
			lancarValeTransporte = false;
		}
		return lancarValeTransporte;
	}

	public void setLancarValeTransporte(boolean lancarValeTransporte) {
		this.lancarValeTransporte = lancarValeTransporte;
	}

	public boolean getLancarSalarioMaternidade() {
		if (lancarSalarioMaternidade == null) {
			lancarSalarioMaternidade = false;
		}
		return lancarSalarioMaternidade;
	}

	public void setLancarSalarioMaternidade(boolean lancarSalarioMaternidade) {
		this.lancarSalarioMaternidade = lancarSalarioMaternidade;
	}

	public String getFormaContratacaoFuncionario() {
		if (formaContratacaoFuncionario == null) {
			formaContratacaoFuncionario = "";
		}
		return formaContratacaoFuncionario;
	}

	public void setFormaContratacaoFuncionario(String formaContratacaoFuncionario) {
		this.formaContratacaoFuncionario = formaContratacaoFuncionario;
	}

	public String getTipoRecebimento() {
		if (tipoRecebimento == null) {
			tipoRecebimento = "";
		}
		return tipoRecebimento;
	}

	public void setTipoRecebimento(String tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}

	public String getSituacaoFuncionario() {
		if (situacaoFuncionario == null) {
			situacaoFuncionario = "";
		}
		return situacaoFuncionario;
	}

	public void setSituacaoFuncionario(String situacaoFuncionario) {
		this.situacaoFuncionario = situacaoFuncionario;
	}

	public TipoTemplateFolhaPagamentoEnum getTipoTemplateFolhaPagamento() {
		return tipoTemplateFolhaPagamento;
	}

	public void setTipoTemplateFolhaPagamento(TipoTemplateFolhaPagamentoEnum tipoTemplateFolhaPagamento) {
		this.tipoTemplateFolhaPagamento = tipoTemplateFolhaPagamento;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null)
			funcionarioCargoVO = new FuncionarioCargoVO();
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public SecaoFolhaPagamentoVO getSecaoFolhaPagamento() {
		if (secaoFolhaPagamento == null)
			secaoFolhaPagamento = new SecaoFolhaPagamentoVO();
		return secaoFolhaPagamento;
	}

	public void setSecaoFolhaPagamento(SecaoFolhaPagamentoVO secaoFolhaPagamento) {
		this.secaoFolhaPagamento = secaoFolhaPagamento;
	}

	public List<TemplateEventoFolhaPagamentoVO> getListaEventosDoTemplate() {
		if (listaEventosDoTemplate == null)
			listaEventosDoTemplate = new ArrayList<>();
		return listaEventosDoTemplate;
	}

	public void setListaEventosDoTemplate(List<TemplateEventoFolhaPagamentoVO> listaEventosDoTemplate) {
		this.listaEventosDoTemplate = listaEventosDoTemplate;
	}

	public Boolean getLancarEventosGrupo() {
		if(lancarEventosGrupo == null)
			lancarEventosGrupo = false;
		return lancarEventosGrupo;
	}

	public void setLancarEventosGrupo(Boolean lancarEventosGrupo) {
		this.lancarEventosGrupo = lancarEventosGrupo;
	}

	public void setLancarEventosFuncionario(Boolean lancarEventosFuncionario) {
		this.lancarEventosFuncionario = lancarEventosFuncionario;
	}

	public void setLancarEmprestimos(Boolean lancarEmprestimos) {
		this.lancarEmprestimos = lancarEmprestimos;
	}

	public void setLancarValeTransporte(Boolean lancarValeTransporte) {
		this.lancarValeTransporte = lancarValeTransporte;
	}

	public void setLancarSalarioMaternidade(Boolean lancarSalarioMaternidade) {
		this.lancarSalarioMaternidade = lancarSalarioMaternidade;
	}

	public Boolean getLancarSalarioComposto() {
		if(lancarSalarioComposto == null)
			lancarSalarioComposto = false;
		return lancarSalarioComposto;
	}

	public void setLancarSalarioComposto(Boolean lancarSalarioComposto) {
		this.lancarSalarioComposto = lancarSalarioComposto;
	}

	public LancamentoFolhaPagamentoVO getLancamentoFolhaPagamento() {
		if(lancamentoFolhaPagamento == null)
			lancamentoFolhaPagamento = new LancamentoFolhaPagamentoVO();
		return lancamentoFolhaPagamento;
	}

	public void setLancamentoFolhaPagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) {
		this.lancamentoFolhaPagamento = lancamentoFolhaPagamento;
	}

	public Boolean getLancarFerias() {
		if (lancarFerias == null)
			lancarFerias = false;
		return lancarFerias;
	}

	public void setLancarFerias(Boolean lancarFerias) {
		this.lancarFerias = lancarFerias;
	}

	public Boolean getLancarAdiantamentoFerias() {
		if (lancarAdiantamentoFerias == null)
			lancarAdiantamentoFerias = false;
		return lancarAdiantamentoFerias;
	}

	public void setLancarAdiantamentoFerias(Boolean lancarAdiantamentoFerias) {
		this.lancarAdiantamentoFerias = lancarAdiantamentoFerias;
	}

	public Date getDataHora() {
		if (dataHora == null)
			dataHora = new Date();
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public Boolean getLancarEventosFolhaNormal() {
		if (lancarEventosFolhaNormal == null)
			lancarEventosFolhaNormal = false;
		return lancarEventosFolhaNormal;
	}

	public void setLancarEventosFolhaNormal(Boolean lancarEventosFolhaNormal) {
		this.lancarEventosFolhaNormal = lancarEventosFolhaNormal;
	}

	public Boolean getLancar13Parcela1() {
		if(lancar13Parcela1 == null)
			lancar13Parcela1 = false;
		return lancar13Parcela1;
	}

	public void setLancar13Parcela1(Boolean lancar13Parcela1) {
		this.lancar13Parcela1 = lancar13Parcela1;
	}

	public Boolean getLancar13Parcela2() {
		if(lancar13Parcela2 == null)
			lancar13Parcela2 = false;
		return lancar13Parcela2;
	}

	public void setLancar13Parcela2(Boolean lancar13Parcela2) {
		this.lancar13Parcela2 = lancar13Parcela2;
	}

	public Boolean getLancarRescisao() {
		if (lancarRescisao == null)
			lancarRescisao = false;
		return lancarRescisao;
	}

	public void setLancarRescisao(Boolean lancarRescisao) {
		this.lancarRescisao = lancarRescisao;
	}

	public Boolean getLancarPensao() {
		if (lancarPensao == null)
			lancarPensao = false;
		return lancarPensao;
	}

	public void setLancarPensao(Boolean lancarPensao) {
		this.lancarPensao = lancarPensao;
	}

}