package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * Reponsavel por manter os dados da entidade ControleLancamentoFolhapagamento.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ControleLancamentoFolhapagamentoVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private ContraChequeVO contraCheque;
	private FuncionarioCargoVO funcionarioCargo;
	private Boolean primeiraParcela13;
	private Boolean segundaParcela13;
	private Boolean rescisao;
	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO;
	private CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO;
	private Integer anoCompetencia;
	private Integer mesCompetencia;
	private ContaPagarVO contaPagarVO;

	// Campos transientes
	private ControleMarcacaoFeriasVO controleMarcacaoFeriasVO;
	private TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ContraChequeVO getContraCheque() {
		if (contraCheque == null) {
			contraCheque = new ContraChequeVO();
		}
		return contraCheque;
	}

	public void setContraCheque(ContraChequeVO contraCheque) {
		this.contraCheque = contraCheque;
	}

	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamentoVO() {
		if (competenciaFolhaPagamentoVO == null)
			competenciaFolhaPagamentoVO = new CompetenciaFolhaPagamentoVO();
		return competenciaFolhaPagamentoVO;
	}

	public void setCompetenciaFolhaPagamentoVO(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		this.competenciaFolhaPagamentoVO = competenciaFolhaPagamentoVO;
	}

	public ControleMarcacaoFeriasVO getControleMarcacaoFeriasVO() {
		if (controleMarcacaoFeriasVO == null)
			controleMarcacaoFeriasVO = new ControleMarcacaoFeriasVO();
		return controleMarcacaoFeriasVO;
	}

	public void setControleMarcacaoFeriasVO(ControleMarcacaoFeriasVO controleMarcacaoFeriasVO) {
		this.controleMarcacaoFeriasVO = controleMarcacaoFeriasVO;
	}

	public TemplateLancamentoFolhaPagamentoVO getTemplateLancamentoFolhaPagamentoVO() {
		if (templateLancamentoFolhaPagamentoVO == null)
			templateLancamentoFolhaPagamentoVO = new TemplateLancamentoFolhaPagamentoVO();
		return templateLancamentoFolhaPagamentoVO;
	}

	public void setTemplateLancamentoFolhaPagamentoVO(
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO) {
		this.templateLancamentoFolhaPagamentoVO = templateLancamentoFolhaPagamentoVO;
	}

	public Boolean getPrimeiraParcela13() {
		if (primeiraParcela13 == null)
			primeiraParcela13 = false;
		return primeiraParcela13;
	}

	public void setPrimeiraParcela13(Boolean primeiraParcela13) {
		this.primeiraParcela13 = primeiraParcela13;
	}

	public Boolean getSegundaParcela13() {
		if (segundaParcela13 == null)
			segundaParcela13 = false;
		return segundaParcela13;
	}

	public void setSegundaParcela13(Boolean segundaParcela13) {
		this.segundaParcela13 = segundaParcela13;
	}

	public Integer getAnoCompetencia() {
		if (anoCompetencia == null)
			anoCompetencia = UteisData.getAnoData(new Date());
		return anoCompetencia;
	}

	public void setAnoCompetencia(Integer anoCompetencia) {
		this.anoCompetencia = anoCompetencia;
	}

	public Integer getMesCompetencia() {
		if (mesCompetencia == null)
			mesCompetencia = UteisData.getMesData(new Date());
		return mesCompetencia;
	}

	public void setMesCompetencia(Integer mesCompetencia) {
		this.mesCompetencia = mesCompetencia;
	}

	public CompetenciaPeriodoFolhaPagamentoVO getCompetenciaPeriodoFolhaPagamentoVO() {
		if (competenciaPeriodoFolhaPagamentoVO == null)
			competenciaPeriodoFolhaPagamentoVO = new CompetenciaPeriodoFolhaPagamentoVO();
		return competenciaPeriodoFolhaPagamentoVO;
	}

	public void setCompetenciaPeriodoFolhaPagamentoVO(
			CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO) {
		this.competenciaPeriodoFolhaPagamentoVO = competenciaPeriodoFolhaPagamentoVO;
	}

	public Boolean getRescisao() {
		if (rescisao == null)
			rescisao = Boolean.FALSE;
		return rescisao;
	}

	public void setRescisao(Boolean rescisao) {
		this.rescisao = rescisao;
	}

	public ContaPagarVO getContaPagarVO() {
		if (contaPagarVO == null) {
			contaPagarVO = new ContaPagarVO();
		}
		return contaPagarVO;
	}

	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

}