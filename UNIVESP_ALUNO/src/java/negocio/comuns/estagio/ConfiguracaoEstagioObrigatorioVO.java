package negocio.comuns.estagio;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;

public class ConfiguracaoEstagioObrigatorioVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8560359865435353478L;
	private Integer codigo;
	private String fonteDeDadosBlackboardEstagio;
	private String fonteDeDadosBlackboardComponenteEstagio;
	private String textoOrientacaoAberturaTermo;
	private String textoOrientacaoEntregaRelatorioFinal;
	private FuncionarioVO funcionarioTestemunhaAssinatura1;
	private FuncionarioVO funcionarioTestemunhaAssinatura2;
	private Integer qtdMinimaMantidoPorFacilitador;
	private Integer qtdVagasPorSalaEstagio;
	private Integer qtdDiasMaximoParaAssinaturaEstagio;
	private Integer qtdDiasNotificacaoAssinaturaEstagio;
	private Integer qtdDiasMaximoParaAnaliseRelatoriofinal;
	private Integer periodicidadeParaNotificacaoEntregaRelatorioFinal;
	private String textoOrientacaoAnaliseRelatorioFinal;
	private Integer qtdDiasMaximoParaCorrecaoRelatorioFinal;
	private Integer periodicidadeParaNotificacaoEntregaNovoRelatorioFinal;
	private String textoOrientacaoSolicitacaoAproveitamento;
	private Integer qtdDiasMaximoParaRespostaAnaliseAproveitamento;
	private Integer periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento;
	private Integer qtdDiasMaximoRespostaRetornoAnaliseAproveitamento;
	private Integer periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento;
	private String textoOrientacaoSolicitacaoEquivalencia;
	private Integer qtdDiasMaximoParaRespostaAnaliseEquivalencia;
	private Integer periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia;
	private Integer qtdDiasMaximoRespostaRetornoAnaliseEquivalencia;
	private Integer periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia;
	private List<ConfiguracaoEstagioObrigatorioFuncionarioVO> listaConfiguracaoEstagioObrigatorioFuncionarioVO;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getTextoOrientacaoAberturaTermo() {
		if (textoOrientacaoAberturaTermo == null) {
			textoOrientacaoAberturaTermo = "";
		}
		return textoOrientacaoAberturaTermo;
	}

	public void setTextoOrientacaoAberturaTermo(String textoOrientacaoAberturaTermo) {
		this.textoOrientacaoAberturaTermo = textoOrientacaoAberturaTermo;
	}

	public String getTextoOrientacaoEntregaRelatorioFinal() {
		if (textoOrientacaoEntregaRelatorioFinal == null) {
			textoOrientacaoEntregaRelatorioFinal = "";
		}
		return textoOrientacaoEntregaRelatorioFinal;
	}

	public void setTextoOrientacaoEntregaRelatorioFinal(String textoOrientacaoEntregaRelatorioFinal) {
		this.textoOrientacaoEntregaRelatorioFinal = textoOrientacaoEntregaRelatorioFinal;
	}

	public FuncionarioVO getFuncionarioTestemunhaAssinatura1() {
		if (funcionarioTestemunhaAssinatura1 == null) {
			funcionarioTestemunhaAssinatura1 = new FuncionarioVO();
		}
		return funcionarioTestemunhaAssinatura1;
	}

	public void setFuncionarioTestemunhaAssinatura1(FuncionarioVO funcionarioTestemunhaAssinatura1) {
		this.funcionarioTestemunhaAssinatura1 = funcionarioTestemunhaAssinatura1;
	}

	public FuncionarioVO getFuncionarioTestemunhaAssinatura2() {
		if (funcionarioTestemunhaAssinatura2 == null) {
			funcionarioTestemunhaAssinatura2 = new FuncionarioVO();
		}
		return funcionarioTestemunhaAssinatura2;
	}

	public void setFuncionarioTestemunhaAssinatura2(FuncionarioVO funcionarioTestemunhaAssinatura2) {
		this.funcionarioTestemunhaAssinatura2 = funcionarioTestemunhaAssinatura2;
	}

	public Integer getQtdMinimaMantidoPorFacilitador() {
		if (qtdMinimaMantidoPorFacilitador == null) {
			qtdMinimaMantidoPorFacilitador = 0;
		}
		return qtdMinimaMantidoPorFacilitador;
	}

	public void setQtdMinimaMantidoPorFacilitador(Integer qtdMinimaMantidoPorFacilitador) {
		this.qtdMinimaMantidoPorFacilitador = qtdMinimaMantidoPorFacilitador;
	}

	public Integer getQtdVagasPorSalaEstagio() {
		if (qtdVagasPorSalaEstagio == null) {
			qtdVagasPorSalaEstagio = 0;
		}
		return qtdVagasPorSalaEstagio;
	}

	public void setQtdVagasPorSalaEstagio(Integer qtdVagasPorSalaEstagio) {
		this.qtdVagasPorSalaEstagio = qtdVagasPorSalaEstagio;
	}

	public Integer getQtdDiasMaximoParaAnaliseRelatoriofinal() {
		if (qtdDiasMaximoParaAnaliseRelatoriofinal == null) {
			qtdDiasMaximoParaAnaliseRelatoriofinal = 0;
		}
		return qtdDiasMaximoParaAnaliseRelatoriofinal;
	}

	public void setQtdDiasMaximoParaAnaliseRelatoriofinal(Integer qtdDiasMaximoParaAnaliseRelatoriofinal) {
		this.qtdDiasMaximoParaAnaliseRelatoriofinal = qtdDiasMaximoParaAnaliseRelatoriofinal;
	}

	public Integer getPeriodicidadeParaNotificacaoEntregaRelatorioFinal() {
		if (periodicidadeParaNotificacaoEntregaRelatorioFinal == null) {
			periodicidadeParaNotificacaoEntregaRelatorioFinal = 0;
		}
		return periodicidadeParaNotificacaoEntregaRelatorioFinal;
	}

	public void setPeriodicidadeParaNotificacaoEntregaRelatorioFinal(Integer periodicidadeParaNotificacaoEntregaRelatorioFinal) {
		this.periodicidadeParaNotificacaoEntregaRelatorioFinal = periodicidadeParaNotificacaoEntregaRelatorioFinal;
	}

	public String getTextoOrientacaoAnaliseRelatorioFinal() {
		if (textoOrientacaoAnaliseRelatorioFinal == null) {
			textoOrientacaoAnaliseRelatorioFinal = "";
		}
		return textoOrientacaoAnaliseRelatorioFinal;
	}

	public void setTextoOrientacaoAnaliseRelatorioFinal(String textoOrientacaoAnaliseRelatorioFinal) {
		this.textoOrientacaoAnaliseRelatorioFinal = textoOrientacaoAnaliseRelatorioFinal;
	}

	public Integer getQtdDiasMaximoParaCorrecaoRelatorioFinal() {
		if (qtdDiasMaximoParaCorrecaoRelatorioFinal == null) {
			qtdDiasMaximoParaCorrecaoRelatorioFinal = 0;
		}
		return qtdDiasMaximoParaCorrecaoRelatorioFinal;
	}

	public void setQtdDiasMaximoParaCorrecaoRelatorioFinal(Integer qtdDiasMaximoParaCorrecaoRelatorioFinal) {
		this.qtdDiasMaximoParaCorrecaoRelatorioFinal = qtdDiasMaximoParaCorrecaoRelatorioFinal;
	}

	public Integer getPeriodicidadeParaNotificacaoEntregaNovoRelatorioFinal() {
		if (periodicidadeParaNotificacaoEntregaNovoRelatorioFinal == null) {
			periodicidadeParaNotificacaoEntregaNovoRelatorioFinal = 0;
		}
		return periodicidadeParaNotificacaoEntregaNovoRelatorioFinal;
	}

	public void setPeriodicidadeParaNotificacaoEntregaNovoRelatorioFinal(Integer periodicidadeParaNotificacaoEntregaNovoRelatorioFinal) {
		this.periodicidadeParaNotificacaoEntregaNovoRelatorioFinal = periodicidadeParaNotificacaoEntregaNovoRelatorioFinal;
	}

	public String getTextoOrientacaoSolicitacaoAproveitamento() {
		if (textoOrientacaoSolicitacaoAproveitamento == null) {
			textoOrientacaoSolicitacaoAproveitamento = "";
		}
		return textoOrientacaoSolicitacaoAproveitamento;
	}

	public void setTextoOrientacaoSolicitacaoAproveitamento(String textoOrientacaoSolicitacaoAproveitamento) {
		this.textoOrientacaoSolicitacaoAproveitamento = textoOrientacaoSolicitacaoAproveitamento;
	}

	public Integer getQtdDiasMaximoParaRespostaAnaliseAproveitamento() {
		if (qtdDiasMaximoParaRespostaAnaliseAproveitamento == null) {
			qtdDiasMaximoParaRespostaAnaliseAproveitamento = 0;
		}
		return qtdDiasMaximoParaRespostaAnaliseAproveitamento;
	}

	public void setQtdDiasMaximoParaRespostaAnaliseAproveitamento(Integer qtdDiasMaximoParaRespostaAnaliseAproveitamento) {
		this.qtdDiasMaximoParaRespostaAnaliseAproveitamento = qtdDiasMaximoParaRespostaAnaliseAproveitamento;
	}

	public Integer getPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento() {
		if (periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento == null) {
			periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento = 0;
		}
		return periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento;
	}

	public void setPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento(Integer periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento) {
		this.periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento = periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento;
	}

	public Integer getQtdDiasMaximoRespostaRetornoAnaliseAproveitamento() {
		if (qtdDiasMaximoRespostaRetornoAnaliseAproveitamento == null) {
			qtdDiasMaximoRespostaRetornoAnaliseAproveitamento = 0;
		}
		return qtdDiasMaximoRespostaRetornoAnaliseAproveitamento;
	}

	public void setQtdDiasMaximoRespostaRetornoAnaliseAproveitamento(Integer qtdDiasMaximoRespostaRetornoAnaliseAproveitamento) {
		this.qtdDiasMaximoRespostaRetornoAnaliseAproveitamento = qtdDiasMaximoRespostaRetornoAnaliseAproveitamento;
	}

	public Integer getPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento() {
		if (periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento == null) {
			periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento = 0;
		}
		return periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento;
	}

	public void setPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento(Integer periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento) {
		this.periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento = periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento;
	}

	public String getTextoOrientacaoSolicitacaoEquivalencia() {
		if (textoOrientacaoSolicitacaoEquivalencia == null) {
			textoOrientacaoSolicitacaoEquivalencia = "";
		}
		return textoOrientacaoSolicitacaoEquivalencia;
	}

	public void setTextoOrientacaoSolicitacaoEquivalencia(String textoOrientacaoSolicitacaoEquivalencia) {
		this.textoOrientacaoSolicitacaoEquivalencia = textoOrientacaoSolicitacaoEquivalencia;
	}

	public Integer getQtdDiasMaximoParaRespostaAnaliseEquivalencia() {
		if (qtdDiasMaximoParaRespostaAnaliseEquivalencia == null) {
			qtdDiasMaximoParaRespostaAnaliseEquivalencia = 0;
		}
		return qtdDiasMaximoParaRespostaAnaliseEquivalencia;
	}

	public void setQtdDiasMaximoParaRespostaAnaliseEquivalencia(Integer qtdDiasMaximoParaRespostaAnaliseEquivalencia) {
		this.qtdDiasMaximoParaRespostaAnaliseEquivalencia = qtdDiasMaximoParaRespostaAnaliseEquivalencia;
	}

	public Integer getPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia() {
		if (periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia == null) {
			periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia = 0;
		}
		return periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia;
	}

	public void setPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia(Integer periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia) {
		this.periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia = periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia;
	}

	public Integer getQtdDiasMaximoRespostaRetornoAnaliseEquivalencia() {
		if (qtdDiasMaximoRespostaRetornoAnaliseEquivalencia == null) {
			qtdDiasMaximoRespostaRetornoAnaliseEquivalencia = 0;
		}
		return qtdDiasMaximoRespostaRetornoAnaliseEquivalencia;
	}

	public void setQtdDiasMaximoRespostaRetornoAnaliseEquivalencia(Integer qtdDiasMaximoRespostaRetornoAnaliseEquivalencia) {
		this.qtdDiasMaximoRespostaRetornoAnaliseEquivalencia = qtdDiasMaximoRespostaRetornoAnaliseEquivalencia;
	}

	public Integer getPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia() {
		if (periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia == null) {
			periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia = 0;
		}
		return periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia;
	}

	public void setPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia(Integer periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia) {
		this.periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia = periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia;
	}

	public List<ConfiguracaoEstagioObrigatorioFuncionarioVO> getListaConfiguracaoEstagioObrigatorioFuncionarioVO() {
		if (listaConfiguracaoEstagioObrigatorioFuncionarioVO == null) {
			listaConfiguracaoEstagioObrigatorioFuncionarioVO = new ArrayList<>();
		}
		return listaConfiguracaoEstagioObrigatorioFuncionarioVO;
	}

	public void setListaConfiguracaoEstagioObrigatorioFuncionarioVO(List<ConfiguracaoEstagioObrigatorioFuncionarioVO> listaConfiguracaoEstagioObrigatorioFuncionarioVO) {
		this.listaConfiguracaoEstagioObrigatorioFuncionarioVO = listaConfiguracaoEstagioObrigatorioFuncionarioVO;
	}	

	public Integer getQtdDiasNotificacaoAssinaturaEstagio() {
		if (qtdDiasNotificacaoAssinaturaEstagio == null) {
			qtdDiasNotificacaoAssinaturaEstagio = 0;
		}
		return qtdDiasNotificacaoAssinaturaEstagio;
	}

	public void setQtdDiasNotificacaoAssinaturaEstagio(Integer qtdDiasNotificacaoAssinaturaEstagio) {
		this.qtdDiasNotificacaoAssinaturaEstagio = qtdDiasNotificacaoAssinaturaEstagio;
	}

	public Integer getQtdDiasMaximoParaAssinaturaEstagio() {
		if (qtdDiasMaximoParaAssinaturaEstagio == null) {
			qtdDiasMaximoParaAssinaturaEstagio = 0;
		}
		return qtdDiasMaximoParaAssinaturaEstagio;
	}

	public void setQtdDiasMaximoParaAssinaturaEstagio(Integer qtdDiasMaximoParaAssinaturaEstagio) {
		this.qtdDiasMaximoParaAssinaturaEstagio = qtdDiasMaximoParaAssinaturaEstagio;
	}

	public String getFonteDeDadosBlackboardEstagio() {
		if (fonteDeDadosBlackboardEstagio == null) {
			fonteDeDadosBlackboardEstagio = "";
		}
		return fonteDeDadosBlackboardEstagio;
	}

	public void setFonteDeDadosBlackboardEstagio(String fonteDeDadosBlackboardEstagio) {
		this.fonteDeDadosBlackboardEstagio = fonteDeDadosBlackboardEstagio;
	}

	public String getFonteDeDadosBlackboardComponenteEstagio() {
		if (fonteDeDadosBlackboardComponenteEstagio == null) {
			fonteDeDadosBlackboardComponenteEstagio = "";
		}
		return fonteDeDadosBlackboardComponenteEstagio;
	}

	public void setFonteDeDadosBlackboardComponenteEstagio(String fonteDeDadosBlackboardComponenteEstagio) {
		this.fonteDeDadosBlackboardComponenteEstagio = fonteDeDadosBlackboardComponenteEstagio;
	}
	
	
	
	

}
