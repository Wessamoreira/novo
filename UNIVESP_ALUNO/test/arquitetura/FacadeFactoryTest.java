package arquitetura;

import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.facade.jdbc.academico.Arquivo;
import negocio.facade.jdbc.academico.DisciplinasInteresse;
import negocio.facade.jdbc.academico.Filiacao;
import negocio.facade.jdbc.academico.HorarioProfessor;
import negocio.facade.jdbc.academico.Turno;
import negocio.facade.jdbc.academico.TurnoHorario;
import negocio.facade.jdbc.administrativo.Cargo;
import negocio.facade.jdbc.administrativo.ConfiguracaoGeralSistema;
import negocio.facade.jdbc.administrativo.Departamento;
import negocio.facade.jdbc.administrativo.FormacaoExtraCurricular;
import negocio.facade.jdbc.administrativo.Funcionario;
import negocio.facade.jdbc.administrativo.FuncionarioCargo;
import negocio.facade.jdbc.administrativo.FuncionarioDependente;
import negocio.facade.jdbc.administrativo.LayoutRelatorioSEIDecidir;
import negocio.facade.jdbc.administrativo.LayoutRelatorioSeiDecidirCampo;
import negocio.facade.jdbc.administrativo.UnidadeEnsino;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.facade.jdbc.basico.AreaProfissionalInteresseContratacao;
import negocio.facade.jdbc.basico.Cidade;
import negocio.facade.jdbc.basico.DocumetacaoPessoa;
import negocio.facade.jdbc.basico.Estado;
import negocio.facade.jdbc.basico.Paiz;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.facade.jdbc.basico.ScriptExecutado;
import negocio.facade.jdbc.compras.FormaPagamento;
import negocio.facade.jdbc.compras.RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentario;
import negocio.facade.jdbc.contabil.ConfiguracaoContabil;
import negocio.facade.jdbc.faturamento.nfe.ConfiguracaoNotaFiscal;
import negocio.facade.jdbc.financeiro.Agencia;
import negocio.facade.jdbc.financeiro.Banco;
import negocio.facade.jdbc.financeiro.CategoriaDespesa;
import negocio.facade.jdbc.financeiro.CategoriaDespesaRateio;
import negocio.facade.jdbc.financeiro.CentroResultadoOrigem;
import negocio.facade.jdbc.financeiro.ContaCorrente;
import negocio.facade.jdbc.financeiro.ContaPagar;
import negocio.facade.jdbc.financeiro.ContaReceber;
import negocio.facade.jdbc.financeiro.ControleRemessaMX;
import negocio.facade.jdbc.financeiro.Parceiro;
import negocio.facade.jdbc.financeiro.PerfilEconomico;
import negocio.facade.jdbc.pesquisa.AreaConhecimento;
import negocio.facade.jdbc.recursoshumanos.CompetenciaFolhaPagamento;
import negocio.facade.jdbc.recursoshumanos.ContraCheque;
import negocio.facade.jdbc.recursoshumanos.ContraChequeEvento;
import negocio.facade.jdbc.recursoshumanos.ControleMarcacaoFerias;
import negocio.facade.jdbc.recursoshumanos.EventoFolhaPagamento;
import negocio.facade.jdbc.recursoshumanos.FaixaSalarial;
import negocio.facade.jdbc.recursoshumanos.HistoricoDependentes;
import negocio.facade.jdbc.recursoshumanos.HistoricoFuncao;
import negocio.facade.jdbc.recursoshumanos.HistoricoSalarial;
import negocio.facade.jdbc.recursoshumanos.HistoricoSecao;
import negocio.facade.jdbc.recursoshumanos.HistoricoSituacao;
import negocio.facade.jdbc.recursoshumanos.LancamentoFolhaPagamento;
import negocio.facade.jdbc.recursoshumanos.MarcacaoFerias;
import negocio.facade.jdbc.recursoshumanos.MarcacaoFeriasColetivas;
import negocio.facade.jdbc.recursoshumanos.NivelSalarial;
import negocio.facade.jdbc.recursoshumanos.PeriodoAquisitivoFerias;
import negocio.facade.jdbc.recursoshumanos.ProgressaoSalarial;
import negocio.facade.jdbc.recursoshumanos.SecaoFolhaPagamento;
import negocio.facade.jdbc.recursoshumanos.Sindicato;
import negocio.facade.jdbc.recursoshumanos.SindicatoMediaFerias;
import negocio.facade.jdbc.recursoshumanos.TemplateLancamentoFolhaPagamento;
import negocio.facade.jdbc.recursoshumanos.ValorReferenciaFolhaPagamento;
import negocio.facade.jdbc.utilitarias.Conexao;
import relatorio.negocio.jdbc.administrativo.RelatorioSeiDecidir;

public class FacadeFactoryTest {

	private Conexao conexao;
	private FacadeFactory facadeFactory;

	public FacadeFactoryTest(Conexao conexao) {
		this.conexao = conexao;
		setFacadeFactory(new FacadeFactory());
		instanciarFacades();
	}

	private void instanciarFacades() {
		try {
			getFacadeFactory().setUsuarioFacade(new Usuario());
			getFacadeFactory().setCompetenciaFolhaPagamentoInterfaceFacade(new CompetenciaFolhaPagamento());
			getFacadeFactory().setPessoaFacade(new Pessoa());
			getFacadeFactory().setValorReferenciaFolhaPagamentoInterfaceFacade(new ValorReferenciaFolhaPagamento());
			getFacadeFactory().setMarcacaoFeriasInterfaceFacade(new MarcacaoFerias());
			getFacadeFactory().setMarcacaoFeriasColetivasInterfaceFacade(new MarcacaoFeriasColetivas());
			getFacadeFactory().setPeriodoAquisitivoFeriasInterfaceFacade(new PeriodoAquisitivoFerias());
			getFacadeFactory().setControleMarcacaoFeriasInterfaceFacade(new ControleMarcacaoFerias());
			getFacadeFactory().setHistoricoDependentesInterfaceFacade(new HistoricoDependentes());
			getFacadeFactory().setHistoricoFuncaoInterfaceFacade(new HistoricoFuncao());
			getFacadeFactory().setHistoricoSalarialInterfaceFacade(new HistoricoSalarial());
			getFacadeFactory().setHistoricoSecaoInterfaceFacade(new HistoricoSecao());
			getFacadeFactory().setHistoricoSituacaoInterfaceFacade(new HistoricoSituacao());
			getFacadeFactory().setContraChequeInterfaceFacade(new ContraCheque());
			getFacadeFactory().setContraChequeEventoInterfaceFacade(new ContraChequeEvento());
			getFacadeFactory().setTemplateLancamentoFolhaPagamentoInterfaceFacade(new TemplateLancamentoFolhaPagamento());
			getFacadeFactory().setLancamentoFolhaPagamentoInterfaceFacade(new LancamentoFolhaPagamento());
			//getFacadeFactory().setHistoricoAfastamentoInterfaceFacade(new HistoricoAfastamento());

			getFacadeFactory().setFuncionarioFacade(new Funcionario());
			getFacadeFactory().setFuncionarioCargoFacade(new FuncionarioCargo());
			getFacadeFactory().setFuncionarioDependenteInterfaceFacade(new FuncionarioDependente());
			getFacadeFactory().setFuncionarioFacade(new Funcionario());
			getFacadeFactory().setSindicatoInterfaceFacade(new Sindicato());
			getFacadeFactory().setParceiroFacade(new Parceiro());
			getFacadeFactory().setCargoFacade(new Cargo());
			getFacadeFactory().setDepartamentoFacade(new Departamento());
			getFacadeFactory().setSecaoFolhaPagamentoInterfaceFacade(new SecaoFolhaPagamento());
			getFacadeFactory().setEventoFolhaPagamentoInterfaceFacade(new EventoFolhaPagamento());
			getFacadeFactory().setSindicatoMediaFeriasInterfaceFacade(new SindicatoMediaFerias());
			getFacadeFactory().setNivelSalarialInterfaceFacade(new NivelSalarial());
			getFacadeFactory().setFaixaSalarialInterfaceFacade(new FaixaSalarial());
			getFacadeFactory().setProgressaoSalarialInterfaceFacade(new ProgressaoSalarial());

			getFacadeFactory().setUnidadeEnsinoFacade(new UnidadeEnsino());
			getFacadeFactory().setConfiguracaoGeralSistemaFacade(new ConfiguracaoGeralSistema());
			getFacadeFactory().setArquivoFacade(new Arquivo());
			getFacadeFactory().setCidadeFacade(new Cidade());
			getFacadeFactory().setEstadoFacade(new Estado());
			getFacadeFactory().setAreaProfissionalInteresseContratacaoFacade(new AreaProfissionalInteresseContratacao());
			getFacadeFactory().setAreaConhecimentoFacade(new AreaConhecimento());
			getFacadeFactory().setFormacaoExtraCurricularFacade(new FormacaoExtraCurricular());
			getFacadeFactory().setDisciplinasInteresseFacade(new DisciplinasInteresse());
			getFacadeFactory().setDocumetacaoPessoaFacade(new DocumetacaoPessoa());
			getFacadeFactory().setHorarioProfessorFacade(new HorarioProfessor());
			getFacadeFactory().setTurnoFacade(new Turno());
			getFacadeFactory().setTurnoHorarioFacade(new TurnoHorario());
			getFacadeFactory().setPaizFacade(new Paiz());
			getFacadeFactory().setPerfilEconomicoFacade(new PerfilEconomico());
			getFacadeFactory().setFiliacaoFacade(new Filiacao());
			getFacadeFactory().setConfiguracaoNotaFiscalFacade(new ConfiguracaoNotaFiscal());
			
			getFacadeFactory().setBancoFacade(new Banco());
			getFacadeFactory().setAgenciaFacade(new Agencia());
			getFacadeFactory().setContaCorrenteFacade(new ContaCorrente());
			getFacadeFactory().setControleRemessaMXFacade(new ControleRemessaMX());
			getFacadeFactory().setContaPagarFacade(new ContaPagar());
			getFacadeFactory().setContaReceberFacade(new ContaReceber());
			getFacadeFactory().setCategoriaDespesaFacade(new CategoriaDespesa());
			getFacadeFactory().setCentroResultadoOrigemInterfaceFacade(new CentroResultadoOrigem());
			getFacadeFactory().setCategoriaDespesaRateioFacade(new CategoriaDespesaRateio());
			getFacadeFactory().setConfiguracaoContabilFacade(new ConfiguracaoContabil());
			getFacadeFactory().setFormaPagamentoFacade(new FormaPagamento());
			
			//Relatorios
			getFacadeFactory().setRelatorioSeiDecidirFacade(new RelatorioSeiDecidir());
			getFacadeFactory().setLayoutRelatorioSEIDecidirInterfaceFacade(new LayoutRelatorioSEIDecidir());
			getFacadeFactory().setLayoutRelatorioSEIDecidirCampoInterfaceFacade(new LayoutRelatorioSeiDecidirCampo());
			
			getFacadeFactory().setScriptExecutadoInterfaceFacade(new ScriptExecutado());
			getFacadeFactory().setArquivoHelper(new ArquivoHelper());

			//PLANO ORCAMENTARIO
			getFacadeFactory().setRequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade(new RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentario());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Conexao getConexao() {
		return conexao;
	}

	public void setConexao(Conexao conexao) {
		this.conexao = conexao;
	}
	
	public FacadeFactory getFacadeFactory() {
		return facadeFactory;
	}

	public void setFacadeFactory(FacadeFactory facadeFactory) {
		this.facadeFactory = facadeFactory;
	}

}