/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import jakarta.faces. model.SelectItem;
import negocio.comuns.academico.CursoVO.enumCampoConsultaCurso;
import negocio.comuns.academico.TurmaVO.enumCampoConsultaTurma;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.GradeCurricularEstagioAreaEnum;
import negocio.comuns.academico.enumeradores.GradeCurricularEstagioQuestionarioEnum;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.OperacaoDeVinculoEstagioEnum;
import negocio.comuns.academico.enumeradores.OperacaoImportacaoSalaBlackboardEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.PublicoAlvoForumEnum;
import negocio.comuns.academico.enumeradores.RegraContagemPeriodoLetivoEnum;
import negocio.comuns.academico.enumeradores.RestricaoPublicoAlvoForumEnum;
import negocio.comuns.academico.enumeradores.SalaBlackboardSituacaoNotaEnum;
import negocio.comuns.academico.enumeradores.SemestreEnum;
import negocio.comuns.academico.enumeradores.SituacaoForumEnum;
import negocio.comuns.academico.enumeradores.SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.academico.enumeradores.TipoConsultaComboCursoEnum;
import negocio.comuns.academico.enumeradores.TipoConsultaComboTitulacaoCursoEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TipoGeracaoMaterialDidaticoEnum;
import negocio.comuns.academico.enumeradores.TipoHorarioEnum;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO.enumCampoConsultaConfiguracaoSeiBlackboard;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO.enumCampoConsultaConfiguracaoSeiGsuite;
import negocio.comuns.administrativo.DepartamentoVO.EnumCampoConsultaDepartamento;
import negocio.comuns.administrativo.enumeradores.EstadoCivilEnum;
import negocio.comuns.administrativo.enumeradores.FormaGeracaoEventoAulaOnLineGoogleMeetEnum;
import negocio.comuns.administrativo.enumeradores.GrauParentescoEnum;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoConsultaComboConfiguracaoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.TipoConsultaComboOuvidoriaEnum;
import negocio.comuns.administrativo.enumeradores.TipoConsultaComboTipagemOuvidoriaEnum;
import negocio.comuns.administrativo.enumeradores.TipoConsultaLocalArmazenamentoEnum;
import negocio.comuns.administrativo.enumeradores.TipoGeracaoEmailIntegracaoEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemOuvidoriaEnum;
import negocio.comuns.administrativo.enumeradores.TipoOuvidoriaEnum;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.arquitetura.enumeradores.FiltroBooleanEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.basico.enumeradores.PoliticaNotaSubstitutivaEnum;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.basico.enumeradores.TipoArtefatoAjudaEnum;
import negocio.comuns.basico.enumeradores.TipoConsultaComboArtefatoAjudaEnum;
import negocio.comuns.basico.enumeradores.TipoConsultaComboLayoutEtiquetaEnum;
import negocio.comuns.basico.enumeradores.TipoProvedorAssinaturaEnum;
import negocio.comuns.biblioteca.enumeradores.FrequenciaNotificacaoGestoresEnum;
import negocio.comuns.biblioteca.enumeradores.TipoClassificacaoEnum;
import negocio.comuns.biblioteca.enumeradores.TipoRelatorioEtiquetaAcademicoEnum;
import negocio.comuns.biblioteca.enumeradores.TipoRelatorioEtiquetaEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.ead.enumeradores.FuncaoResponsavelAtaEnum;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.estagio.enumeradores.MotivosPadroesEstagioCasoUsoEnum;
import negocio.comuns.estagio.enumeradores.RegrasSubstituicaoGrupoPessoaItemEnum;
import negocio.comuns.estagio.enumeradores.TipoConsultaComboSituacaoAproveitamentoEnum;
import negocio.comuns.financeiro.enumerador.AmbienteCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.AmbienteContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.EmpresaOperadoraCartaoEnum;
import negocio.comuns.financeiro.enumerador.FinalidadeDocEnum;
import negocio.comuns.financeiro.enumerador.FinalidadePixEnum;
import negocio.comuns.financeiro.enumerador.FinalidadeTedEnum;
import negocio.comuns.financeiro.enumerador.GestaoContasPagarOperacaoEnum;
import negocio.comuns.financeiro.enumerador.IntegracaoNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.ModalidadeTransferenciaBancariaEnum;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.ParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;
import negocio.comuns.financeiro.enumerador.RestricaoUsoCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.SituacaoPixEnum;
import negocio.comuns.financeiro.enumerador.StatusPixEnum;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoAutenticacaoRegistroRemessaOnlineEnum;
import negocio.comuns.financeiro.enumerador.TipoCobrancaPixEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoContratoAgenteNegativacaoCobrancaEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoFormaArredondamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoContribuinte;
import negocio.comuns.financeiro.enumerador.TipoIntervaloParcelaEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoParcelaNegociarEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.VisaoParcelarEnum;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.CorRaca;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoConsultaComboOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import relatorio.negocio.comuns.arquitetura.enumeradores.CrossTabEnum;

/**
 *
 * @author Otimize-Not
 */

@Controller(value = "EnumControle")
@Scope("singleton")
@Lazy
public class EnumControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 189377481963272439L;

	private List<SelectItem> listaSelectFiltroBooleanEnum;
	private List<SelectItem> comboboxTipoHorario;
	private List<SelectItem> comboboxTipoArtefatoAjuda;
	private List<SelectItem> tipoConsultaComboOperadoraCartaoEnum;
	private List<SelectItem> politicaNotaSubstitutivaEnum;
	private List<SelectItem> comboboxProvedorDeAssinaturaEnum;
	private List<SelectItem> comboboxTipoProvedorAssinaturaEnum;
	private List<SelectItem> comboboxTipoAssinaturaDocumentoEnum;
	private List<SelectItem> tipoCartaoOperadoraCartaoEnum;
	private List<SelectItem> tipoConsultaComboArtefatoAjudaEnum;
	private List<SelectItem> tipoConsultaComboCampanhaEnumPt;
	private List<SelectItem> tipoConsultaComboMetaEnumPt;
	private List<SelectItem> comboboxTipoMetaEnumPt;
	private List<SelectItem> comboboxControleSituacaoProspectPipelineEnum;
	private List<SelectItem> comboboxNivelExperienciaCargoEnumPt;
	private List<SelectItem> tipoConsultaComboWorkflowEnumPt;
	private List<SelectItem> tipoConsultaComboConfiguracaoRankingEnumPt;
	private List<SelectItem> tipoConsultaComboCursoInteresseEnum;
	private List<SelectItem> tipoConsultaComboProspectsEnum;
	private List<SelectItem> comboboxRendaProspectEnum;
	private List<SelectItem> comboboxFormacaoAcademicaProspectEnum;
	private List<SelectItem> comboboxTipoEmpresaProspectEnum;
	private List<SelectItem> comboboxTipoProspectEnum;
	private List<SelectItem> comboboxMesEnum;
	private List<SelectItem> comboboxTipoCompromissoEnum;
	private List<SelectItem> comboboxMotivoInsucessoEnum;
	private List<SelectItem> comboboxTipoContatoEnum;
	private List<SelectItem> comboboxSexoEnum;
	private List<SelectItem> tipoCampanhaEnum;
	private List<SelectItem> tipoOrigemCadastroProspectEnum;
	private List<SelectItem> tipoSituacaoReferenteVagaEnum;
	private List<SelectItem> tipoInteracaoEnum;
	private List<SelectItem> tipoSituacaoWorkflowEnum;
	private List<SelectItem> tipoGeracaoEmailGsuiteEnum;
	private List<SelectItem> tipoGeracaoEmailIntegracaoEnum;
	private Locale localeCombobox;
	private List<SelectItem> tipoUploadEnum;
	private List<SelectItem> tipoSituacaoPixEnum;
	private List<SelectItem> statusPixEnum;
	private List<SelectItem> tipoCobrancaPixEnum;
	private List<SelectItem> tipoMotivosPadroesEstagioCasoUsoEnum;
	private List<SelectItem> tipoGradeCurricularEstagioAreaEnum;
	private List<SelectItem> tipoGradeCurricularEstagioQuestionarioEnum;

	private List<SelectItem> tipoConsultaComboTitulacaoCursoEnum;
	private List<SelectItem> tipoConsultaComboTipagemOuvidoriaEnum;
	private List<SelectItem> tipoConsultaComboConfiguracaoAtendimentoEnum;
	private List<SelectItem> tipoConsultaComboOuvidoriaEnum;
	private List<SelectItem> comboboxModalidadeTransferenciaBancariaEnum;
	private List<SelectItem> comboboxTipoIdentificacaoContribuinte;
	private List<SelectItem> comboboxFinalidadeTedEnum;
	private List<SelectItem> comboboxFinalidadeDocEnum;
	private List<SelectItem> comboBoxTipoNivelCentroResultadoEnum;
	private List<SelectItem> comboboxTipoContaEnum;
	private List<SelectItem> comboboxTipoOuvidoriaEnum;
	private List<SelectItem> comboboxTipoOrigemOuvidoriaEnum;
	private List<SelectItem> comboboxSituacaoOuvidoriaEnum;
	private List<SelectItem> comboboxTipoFormaPagamento;
	private List<SelectItem> comboboxTipoMovimentacaoFinanceira;
	private List<SelectItem> comboboxTipoSacadoExtratoContaCorrenteEnum;
	private List<SelectItem> comboboxTipoSacadoPorName;
	private List<SelectItem> comboboxOrigemExtratoContaCorrenteEnum;
	private List<SelectItem> comboboxTipoProdutoServicoEnum;
	private List<SelectItem> comboboxTipoNotaFiscalEntradaEnum;
	private List<SelectItem> comboboxTipoNaturezaOperacaoEnum;
	private List<SelectItem> comboboxTipoOrigemDestinoNaturezaOperacaoEnum;
	private List<SelectItem> comboboxRestricaoUsoCentroResultadoEnum;
	private List<SelectItem> comboboxTipoAutorizacaoRequisicaoEnum;

	private List<SelectItem> comboboxTipoCriacaoContaPagarEnum;
	private List<SelectItem> comboboxTipoGeracaoIntegracaoContabilEnum;
	private List<SelectItem> comboboxTipoContaCorrenteEnum;
	private List<SelectItem> comboboxTipoGeracaoMaterialDidaticoEnum;
	private List<SelectItem> comboboxGestaoContasPagarOperacaoEnum;
	private List<SelectItem> comboboxTipoSalaAulaBlackboardEnum;
	private List<SelectItem> comboboxRegrasSubstituicaoGrupoPessoaItemEnum;
	private List<SelectItem> comboboxRegraContagemPeriodoLetivoEnum;

	private List<SelectItem> tipoConsultaComboCursoEnum;
	private List<SelectItem> comboboxFrequenciaNotificacaoGrupoDestinatarioEnum;
	private List<SelectItem> comboboxTipoClassificacaoEnum;
	private List<SelectItem> comboboxTipoRelatorioEtiquetaEnum;
	private List<SelectItem> comboboxTipoRelatorioEtiquetaAcademicoEnum;
	private List<SelectItem> comboboxLayoutEtiquetaEnum;
	private List<SelectItem> comboboxStatusAtivoInativoEnum;
	private List<SelectItem> comboboxPessoaEnum;
	private List<SelectItem> comboboxSemestreEnum;
	private List<SelectItem> comboboxBimestreEnum;
	private List<SelectItem> comboboxMesesEnum;
	private List<SelectItem> comboboxDisciplinaReprovadas;
	private List<SelectItem> comboBoxDefinicaoTutoriaOnline;
	private List<SelectItem> comboBoxTipoNivelEducacionalEnum;
	private List<SelectItem> comboBoxTipoNivelEducacionalEnumName;
	private List<SelectItem> comboBoxPoliticaSelecaoQuestaoEnum;
	private List<SelectItem> comboBoxRegraDistribuicaoQuestaoEnum;
	private List<SelectItem> comboBoxNivelComplexidadeQuestaoEnum;
	private List<SelectItem> comboboxTipoConsultaLocalArmazenamentoEnum;
	private List<SelectItem> comboboxSituacaoAtivoInativoEnum;
	private List<SelectItem> comboboxSituacaoEnum;
	private List<SelectItem> comboboxSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;

	private List<SelectItem> comboboxFormaEntradaPatrimonioEnum;
	private List<SelectItem> comboboxOrientacaoPaginaEnum;
	private List<SelectItem> comboBoxPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum;
	private List<SelectItem> comboboxSexoEnumCrm;
	private List<SelectItem> comboBoxTipoFinanciamentoEnum;
	private List<SelectItem> comboBoxParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum;
	private List<SelectItem> comboboxPeriodicidadeEnum;
	private List<SelectItem> comboboxTipoSituacaoWorkflowEnum;
	private List<SelectItem> comboboxCSTPISCOFINSEnum;
	private List<SelectItem> comboboxTipoIntervaloParcelaEnum;
	private List<SelectItem> comboboxIntegracaoNegativacaoCobrancaContaReceberEnum;
	private List<SelectItem> comboboxTipoAgenteNegativacaoCobrancaContaReceberEnum;
	private List<SelectItem> comboBoxTipoParcelaNegociarEnum;
	private List<SelectItem> comboBoxTipoContratoAgenteNegativacaoCobrancaEnum;
	private List<SelectItem> comboBoxOperacaoDeVinculoEstagioEnum;

	private List<SelectItem> tipoConsultaComboConfiguracaoSeiGsuite;
	private List<SelectItem> tipoConsultaComboConfiguracaoSeiBlackboard;
	private List<SelectItem> comboboxGrauParentescoEnum;
	private List<SelectItem> comboboxOrigemContaPagarEnum;
	private List<SelectItem> comboboxTipoConsultaGestaoContasPagar;
    private List<SelectItem> comboboxEstadoCivilEnum;
    private List<SelectItem> comboboxAtivoInativoEnum;
    private List<SelectItem> comboboxLocalIncidenciaEnum;
    private List<SelectItem> comboboxTipoLancamentoFolhaPagamentoEnum;
    private List<SelectItem> comboboxTipoEventoFolhaPagamentoEnum;
    private List<SelectItem> comboboxCategoriaEventoFolhaPagamentoEnum;
    private List<SelectItem> comboboxTipoEventoMediaEnum;
    private List<SelectItem> comboboxTipoEntidadeSindicalEnum;
    private List<SelectItem> comboboxSituacaoMarcacaoFeriasEnum;
    private List<SelectItem> comboboxNaturezaEventoFolhaPagamentoEnum;
    private List<SelectItem> comboboxTipoAfastamentoFuncionarioEnum;
    private List<SelectItem> comboboxMotivoAfastamentoFuncionarioEnum;
    private List<SelectItem> comboboxTipoMovimentoPensaoEnum;
    private List<SelectItem> comboboxJobsEnum;
    
    private List<SelectItem> comboboxMotivoMudancaCargo;
    private List<SelectItem> comboboxMotivoMudancaSecao;
    private List<SelectItem> comboboxMotivoMudancaSalarial;

    private List<SelectItem> comboboxRescisao;
    private List<SelectItem> comboboxTipoDemissao;
    private List<SelectItem> comboboxMotivoDemissao;
    private List<SelectItem> comboboxCorRaca;

    private List<SelectItem> comboboxTipoEventoMediaRescisao;
    
    private List<SelectItem> comboboxNaturezaOperacaoEnum;
    private List<SelectItem> comboboxCategoriaDespesaEnum;
    private List<SelectItem> comboboxFormaPagamentoEnum;
    private List<SelectItem> comboboxPermitirCartaoEnum;
    private List<SelectItem> comboboxVisaoParcelarEnum;
    private List<SelectItem> comboboxEmpresaOperadoraCartaoEnum;    
    private List<SelectItem> comboboxTipoFormaArredondamentoEnum;
    private List<SelectItem> comboboxTipoAutenticacaoRegistroRemessaOnlineEnum;
    private List<SelectItem> comboboxAmbienteContaCorrenteEnum;
    private List<SelectItem> comboboxAmbienteEnum;

    private List<SelectItem> comboboxCrosstabEnum;
    private List<SelectItem> comboboxNivelFormacaoAcademica;

    private List<SelectItem> comboboxSituacaoHoraAtividadeExtraClasseEnum;
    private List<SelectItem> comboboxRegimeEspecialTributacaoEnum;
    
    private List<SelectItem> listaSelectItemCalendarioPor;
    
    private List<SelectItem> comboboxFormaGeracaoEventoAulaOnLineGoogleMeet;
    private List<SelectItem> comboboxTipoIdentificacaoChavePixEnum ;
    private List<SelectItem> comboboxFinalidadePixEnum;
    private List<SelectItem> tipoConsultaComboSituacaoNota;
    private List<SelectItem> tipoConsultaComboOperacaoImportacaoProfessor;
    private List<SelectItem> tipoConsultaComboOperacaoImportacaoFacilitador;
    private List<SelectItem> tipoConsultaComboOperacaoImportacaoSupervisor;
    private List<SelectItem> tipoConsultaComboOperacaoImportacaoNota;

    private List<SelectItem> tipoConsultaComboSituacaoAproveitamento;
    private List<SelectItem> listaSelectItemLimitOffset;
    
    private List<SelectItem> comboboxPeriodoLetivoEnum;
    
    
    public List<SelectItem> getListaSelectItemLimitOffset() {
		if (listaSelectItemLimitOffset == null) {
			listaSelectItemLimitOffset = new ArrayList<>();
			listaSelectItemLimitOffset.add(new SelectItem(10, "10"));
			listaSelectItemLimitOffset.add(new SelectItem(25, "25"));
			listaSelectItemLimitOffset.add(new SelectItem(50, "50"));
			listaSelectItemLimitOffset.add(new SelectItem(100, "100"));
			listaSelectItemLimitOffset.add(new SelectItem(250, "250"));
			listaSelectItemLimitOffset.add(new SelectItem(500, "500"));
			listaSelectItemLimitOffset.add(new SelectItem(750, "750"));
			listaSelectItemLimitOffset.add(new SelectItem(1000, "1000"));
		}
		return listaSelectItemLimitOffset;
	}

	public List<SelectItem> getListaSelectFiltroBooleanEnum() {
		if (listaSelectFiltroBooleanEnum == null) {
			listaSelectFiltroBooleanEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FiltroBooleanEnum.class, "name", "valorApresentar", true);
		}
		return listaSelectFiltroBooleanEnum;
	}

	
	public List<SelectItem> getComboboxTipoSituacaoPixEnum() {
		
		if (tipoSituacaoPixEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoSituacaoPixEnum = montarCombobox(SituacaoPixEnum.values(), Obrigatorio.NAO);
		}
		return tipoSituacaoPixEnum;
	}
	
	public List<SelectItem> getComboboxStatusPixEnum() {
		
		if (statusPixEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			statusPixEnum = montarCombobox(StatusPixEnum.values(), Obrigatorio.NAO);
		}
		return statusPixEnum;
	}
	
	public List<SelectItem> getComboboxTipoCobrancaPixEnum() {
		
		if (tipoCobrancaPixEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoCobrancaPixEnum = montarCombobox(TipoCobrancaPixEnum.values(), Obrigatorio.SIM);
		}
		return tipoCobrancaPixEnum;
	}
	
	public List<SelectItem> getComboboxTipoMotivosPadroesEstagioCasoUsoEnum() {
		
		if (tipoMotivosPadroesEstagioCasoUsoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoMotivosPadroesEstagioCasoUsoEnum = montarCombobox(MotivosPadroesEstagioCasoUsoEnum.values(), Obrigatorio.NAO);
		}
		return tipoMotivosPadroesEstagioCasoUsoEnum;
	}
	
	public List<SelectItem> getComboboxTipoGradeCurricularEstagioAreaEnum() {
		
		if (tipoGradeCurricularEstagioAreaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoGradeCurricularEstagioAreaEnum = montarCombobox(GradeCurricularEstagioAreaEnum.values(), Obrigatorio.SIM);
		}
		return tipoGradeCurricularEstagioAreaEnum;
	}
	
	public List<SelectItem> getComboboxTipoGradeCurricularEstagioQuestionarioEnum() {
		
		if (tipoGradeCurricularEstagioQuestionarioEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoGradeCurricularEstagioQuestionarioEnum = montarCombobox(GradeCurricularEstagioQuestionarioEnum.values(), Obrigatorio.NAO);
		}
		return tipoGradeCurricularEstagioQuestionarioEnum;
	}

	

	


	public List<SelectItem> getComboboxTipoHorario() {
		if (comboboxTipoHorario == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoHorario = montarCombobox(TipoHorarioEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoHorario;
	}

	public List<SelectItem> getComboboxTipoArtefatoAjuda() {
		if (comboboxTipoArtefatoAjuda == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoArtefatoAjuda = montarCombobox(TipoArtefatoAjudaEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoArtefatoAjuda;
	}

	

	

	public List<SelectItem> montarComboboxPorName(Enum[] enumeradores, Obrigatorio obrigatorio) {
		List<SelectItem> lista = new ArrayList<>();
		for (Enum enumerador : enumeradores) {
			if (enumerador.toString().equals("NENHUM")) {
				if (obrigatorio == Obrigatorio.NAO) {
					lista.add(new SelectItem(enumerador.name(), ""));
				}
				continue;
			}
			lista.add(new SelectItem(enumerador.name(), internacionalizarEnum(enumerador)));
		}
		return lista;
	}
	
	public List<SelectItem> montarCombobox(Enum[] enumeradores, Obrigatorio obrigatorio) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		for (Enum enumerador : enumeradores) {
			if (enumerador.toString().equals("NENHUM")) {
				if (obrigatorio == Obrigatorio.NAO) {
					lista.add(new SelectItem(enumerador, ""));
				}
				continue;
			}
			lista.add(new SelectItem(enumerador, internacionalizarEnum(enumerador)));
		}
		return lista;
	}

	public List<SelectItem> montarComboboxMes(Enum[] enumeradores, Obrigatorio obrigatorio) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		for (Enum enumerador : enumeradores) {
			if (enumerador.toString().equals("NENHUM")) {
				if (obrigatorio == Obrigatorio.NAO) {
					lista.add(new SelectItem(enumerador, ""));
				}
				continue;
			}
			lista.add(new SelectItem(enumerador, enumerador.toString()));
		}
		return lista;
	}

	public String internacionalizarEnum(Enum enumerador) {
		return UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString());
	}
	
	

	public List<SelectItem> getComboboxProvedorDeAssinaturaEnum() {
		if (comboboxProvedorDeAssinaturaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxProvedorDeAssinaturaEnum = montarCombobox(ProvedorDeAssinaturaEnum.values(), Obrigatorio.SIM);
		}
		return comboboxProvedorDeAssinaturaEnum;
	}
	
	public List<SelectItem> getComboboxTipoProvedorAssinaturaEnum() {
		if (comboboxTipoProvedorAssinaturaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoProvedorAssinaturaEnum = montarCombobox(TipoProvedorAssinaturaEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoProvedorAssinaturaEnum;
	}
	
	public List<SelectItem> getComboboxTipoAssinaturaDocumentoEnum() {
		if (comboboxTipoAssinaturaDocumentoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoAssinaturaDocumentoEnum = montarCombobox(TipoAssinaturaDocumentoEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoAssinaturaDocumentoEnum;
	}

	

	/**
	 * Método responsável por montar combobox de Sexo
	 * 
	 * @return the tipoConsultaComboCategoriaAtividadeEnum
	 */
	public List<SelectItem> getTipoConsultaComboOperadoraCartaoEnum() {
		if (tipoConsultaComboOperadoraCartaoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboOperadoraCartaoEnum = montarCombobox(TipoConsultaComboOperadoraCartaoEnum.values(), Obrigatorio.NAO);
		}
		return tipoConsultaComboOperadoraCartaoEnum;
	}

	public List<SelectItem> getPoliticaNotaSubstitutivaEnum() {
		if (politicaNotaSubstitutivaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			politicaNotaSubstitutivaEnum = montarCombobox(PoliticaNotaSubstitutivaEnum.values(), Obrigatorio.SIM);
		}
		return politicaNotaSubstitutivaEnum;
	}

	public List<SelectItem> getTipoCartaoOperadoraCartaoEnum() {
		if (tipoCartaoOperadoraCartaoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoCartaoOperadoraCartaoEnum = montarCombobox(TipoCartaoOperadoraCartaoEnum.values(), Obrigatorio.SIM);
		}
		return tipoCartaoOperadoraCartaoEnum;
	}

	public List<SelectItem> getTipoConsultaComboArtefatoAjudaEnum() {
		if (tipoConsultaComboArtefatoAjudaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboArtefatoAjudaEnum = montarCombobox(TipoConsultaComboArtefatoAjudaEnum.values(), Obrigatorio.SIM);
		}
		return tipoConsultaComboArtefatoAjudaEnum;
	}


	


	public List<SelectItem> getComboboxTipoMesEnum() {
		if (comboboxMesEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxMesEnum = montarComboboxMes(MesAnoEnum.values(), Obrigatorio.SIM);
		}
		return comboboxMesEnum;
	}

	

	public List<SelectItem> getComboboxTipoIntervaloParcelaEnum() {
		if (comboboxTipoIntervaloParcelaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoIntervaloParcelaEnum = montarCombobox(TipoIntervaloParcelaEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoIntervaloParcelaEnum;
	}

	
	
	public List<SelectItem> getTipoGeracaoEmailGsuiteEnum() {
		if (tipoGeracaoEmailGsuiteEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoGeracaoEmailGsuiteEnum = montarCombobox(TipoGeracaoEmailIntegracaoEnum.values(), Obrigatorio.SIM);
		}
		return tipoGeracaoEmailGsuiteEnum;
	}
	
	public List<SelectItem> getTipoGeracaoEmailIntegracaoEnum() {
		if (tipoGeracaoEmailIntegracaoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoGeracaoEmailIntegracaoEnum = montarCombobox(TipoGeracaoEmailIntegracaoEnum.values(), Obrigatorio.SIM);
		}
		return tipoGeracaoEmailIntegracaoEnum;
	}

	public List<SelectItem> getTipoConsultaComboTitulacaoCursoEnum() {
		if (tipoConsultaComboTitulacaoCursoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboTitulacaoCursoEnum = montarCombobox(TipoConsultaComboTitulacaoCursoEnum.values(), Obrigatorio.SIM);
		}

		return tipoConsultaComboTitulacaoCursoEnum;
	}

	public List<SelectItem> getTipoConsultaComboConfiguracaoAtendimentoEnum() {
		if (tipoConsultaComboConfiguracaoAtendimentoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboConfiguracaoAtendimentoEnum = montarCombobox(TipoConsultaComboConfiguracaoAtendimentoEnum.values(), Obrigatorio.SIM);
		}

		return tipoConsultaComboConfiguracaoAtendimentoEnum;
	}

	public List<SelectItem> getComboboxSituacaoOuvidoriaEnum() {
		if (comboboxSituacaoOuvidoriaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxSituacaoOuvidoriaEnum = montarCombobox(SituacaoAtendimentoEnum.values(), Obrigatorio.SIM);
		}

		return comboboxSituacaoOuvidoriaEnum;
	}

	public List<SelectItem> getComboboxTipoFormaPagamento() {
		if (comboboxTipoFormaPagamento == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoFormaPagamento = montarCombobox(TipoFormaPagamento.values(), Obrigatorio.SIM);
		}

		return comboboxTipoFormaPagamento;
	}

	public List<SelectItem> getComboboxTipoMovimentacaoFinanceira() {
		if (comboboxTipoMovimentacaoFinanceira == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoMovimentacaoFinanceira = montarCombobox(TipoMovimentacaoFinanceira.values(), Obrigatorio.SIM);
		}

		return comboboxTipoMovimentacaoFinanceira;
	}

	public List<SelectItem> getComboboxTipoSacadoExtratoContaCorrenteEnum() {
		if (comboboxTipoSacadoExtratoContaCorrenteEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoSacadoExtratoContaCorrenteEnum = montarCombobox(TipoSacadoExtratoContaCorrenteEnum.values(), Obrigatorio.SIM);
		}

		return comboboxTipoSacadoExtratoContaCorrenteEnum;
	}
	
	public List<SelectItem> getComboboxTipoSacadoPorName() {
		if (comboboxTipoSacadoPorName == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoSacadoPorName = montarComboboxPorName(TipoSacado.values(), Obrigatorio.SIM);
		}
		
		return comboboxTipoSacadoPorName;
	}

	public List<SelectItem> getComboboxOrigemExtratoContaCorrenteEnum() {
		if (comboboxOrigemExtratoContaCorrenteEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxOrigemExtratoContaCorrenteEnum = montarCombobox(OrigemExtratoContaCorrenteEnum.values(), Obrigatorio.SIM);
		}

		return comboboxOrigemExtratoContaCorrenteEnum;
	}

	
	public List<SelectItem> getComboboxTipoFormaArredondamentoEnum() {
		if (comboboxTipoFormaArredondamentoEnum == null) {
			comboboxTipoFormaArredondamentoEnum = montarCombobox(TipoFormaArredondamentoEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoFormaArredondamentoEnum;
	}
	
	
	
	public List<SelectItem> getComboboxTipoAutenticacaoRegistroRemessaOnlineEnum() {
		if (comboboxTipoAutenticacaoRegistroRemessaOnlineEnum == null) {
			comboboxTipoAutenticacaoRegistroRemessaOnlineEnum = montarCombobox(TipoAutenticacaoRegistroRemessaOnlineEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoAutenticacaoRegistroRemessaOnlineEnum;
	}
	
	
	
	

	
	public List<SelectItem> getComboboxRestricaoUsoCentroResultadoEnum() {
		if (comboboxRestricaoUsoCentroResultadoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxRestricaoUsoCentroResultadoEnum = montarCombobox(RestricaoUsoCentroResultadoEnum.values(), Obrigatorio.NAO);
		}

		return comboboxRestricaoUsoCentroResultadoEnum;
	}
	
	



	public List<SelectItem> getComboboxTipoGeracaoMaterialDidaticoEnum() {
		if (comboboxTipoGeracaoMaterialDidaticoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoGeracaoMaterialDidaticoEnum = montarCombobox(TipoGeracaoMaterialDidaticoEnum.values(), Obrigatorio.SIM);
		}

		return comboboxTipoGeracaoMaterialDidaticoEnum;
	}

	public List<SelectItem> getTipoConsultaComboOuvidoriaEnum() {
		if (tipoConsultaComboOuvidoriaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboOuvidoriaEnum = montarCombobox(TipoConsultaComboOuvidoriaEnum.values(), Obrigatorio.SIM);
		}

		return tipoConsultaComboOuvidoriaEnum;
	}

	public List<SelectItem> getTipoConsultaComboTipagemOuvidoriaEnum() {
		if (tipoConsultaComboTipagemOuvidoriaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboTipagemOuvidoriaEnum = montarCombobox(TipoConsultaComboTipagemOuvidoriaEnum.values(), Obrigatorio.SIM);
		}

		return tipoConsultaComboTipagemOuvidoriaEnum;
	}

	public List<SelectItem> getComboboxModalidadeTransferenciaBancariaEnum() {
		if (comboboxModalidadeTransferenciaBancariaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxModalidadeTransferenciaBancariaEnum = montarCombobox(ModalidadeTransferenciaBancariaEnum.values(), Obrigatorio.SIM);
		}

		return comboboxModalidadeTransferenciaBancariaEnum;

	}

	public List<SelectItem> getComboboxTipoIdentificacaoContribuinte() {
		if (comboboxTipoIdentificacaoContribuinte == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoIdentificacaoContribuinte = montarCombobox(TipoIdentificacaoContribuinte.values(), Obrigatorio.SIM);
		}

		return comboboxTipoIdentificacaoContribuinte;
	}

	public List<SelectItem> getComboboxFinalidadeTedEnum() {
		if (comboboxFinalidadeTedEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxFinalidadeTedEnum = montarCombobox(FinalidadeTedEnum.values(), Obrigatorio.SIM);
		}

		return comboboxFinalidadeTedEnum;
	}

	public List<SelectItem> getComboboxFinalidadeDocEnum() {
		if (comboboxFinalidadeDocEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxFinalidadeDocEnum = montarCombobox(FinalidadeDocEnum.values(), Obrigatorio.SIM);
		}

		return comboboxFinalidadeDocEnum;
	}
	
	public List<SelectItem> getComboBoxTipoNivelCentroResultadoEnum() {
		if (comboBoxTipoNivelCentroResultadoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboBoxTipoNivelCentroResultadoEnum = montarCombobox(TipoNivelCentroResultadoEnum.values(), Obrigatorio.SIM);
		}
		
		return comboBoxTipoNivelCentroResultadoEnum;
	}

	public List<SelectItem> getComboboxTipoContaEnum() {
		if (comboboxTipoContaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoContaEnum = montarCombobox(TipoContaEnum.values(), Obrigatorio.SIM);
		}
		
		return comboboxTipoContaEnum;
	}

	public List<SelectItem> getComboboxTipoOuvidoriaEnum() {
		if (comboboxTipoOuvidoriaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoOuvidoriaEnum = montarCombobox(TipoOuvidoriaEnum.values(), Obrigatorio.NAO);
		}

		return comboboxTipoOuvidoriaEnum;
	}

	public List<SelectItem> getComboboxTipoOrigemOuvidoriaEnum() {
		if (comboboxTipoOrigemOuvidoriaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoOrigemOuvidoriaEnum = montarCombobox(TipoOrigemOuvidoriaEnum.values(), Obrigatorio.NAO);
		}

		return comboboxTipoOrigemOuvidoriaEnum;
	}

	public List<SelectItem> getTipoConsultaComboCursoEnum() {

		if (tipoConsultaComboCursoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboCursoEnum = montarCombobox(TipoConsultaComboCursoEnum.values(), Obrigatorio.NAO);
		}

		return tipoConsultaComboCursoEnum;
	}

	

	

	public List<SelectItem> getComboboxFrequenciaNotificacaoGrupoDestinatarioEnum() {
		if (comboboxFrequenciaNotificacaoGrupoDestinatarioEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxFrequenciaNotificacaoGrupoDestinatarioEnum = montarCombobox(FrequenciaNotificacaoGestoresEnum.values(), Obrigatorio.NAO);
		}
		return comboboxFrequenciaNotificacaoGrupoDestinatarioEnum;
	}

	public List<SelectItem> getcomboboxTipoClassificacaoEnum() {
		if (comboboxTipoClassificacaoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoClassificacaoEnum = montarCombobox(TipoClassificacaoEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoClassificacaoEnum;
	}

	public List<SelectItem> getComboboxTipoRelatorioEtiquetaEnum() {
		if (comboboxTipoRelatorioEtiquetaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoRelatorioEtiquetaEnum = montarCombobox(TipoRelatorioEtiquetaEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoRelatorioEtiquetaEnum;
	}

	public List<SelectItem> getComboboxTipoRelatorioEtiquetaAcademicoEnum() {
		if (comboboxTipoRelatorioEtiquetaAcademicoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxTipoRelatorioEtiquetaAcademicoEnum = montarCombobox(TipoRelatorioEtiquetaAcademicoEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoRelatorioEtiquetaAcademicoEnum;
	}

	public List<SelectItem> getComboboxLayoutEtiquetaEnum() {
		if (comboboxLayoutEtiquetaEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxLayoutEtiquetaEnum = montarCombobox(TipoConsultaComboLayoutEtiquetaEnum.values(), Obrigatorio.SIM);
		}
		return comboboxLayoutEtiquetaEnum;
	}
	
	public List<SelectItem> getComboboxStatusAtivoInativoEnum() {
		if (comboboxStatusAtivoInativoEnum == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			comboboxStatusAtivoInativoEnum = montarCombobox(StatusAtivoInativoEnum.values(), Obrigatorio.NAO);
		}
		return comboboxStatusAtivoInativoEnum;
	}
	
	public List<SelectItem> getComboboxPessoaEnum() {
		if(comboboxPessoaEnum == null) {
			comboboxPessoaEnum = new ArrayList<>(0);
			comboboxPessoaEnum.add(new SelectItem("nome", "Nome"));
			comboboxPessoaEnum.add(new SelectItem("cpf", "CPF"));
		}
		return comboboxPessoaEnum;
	}

	public List<SelectItem> getComboboxSemestreEnum() {
		if (comboboxSemestreEnum == null) {
			comboboxSemestreEnum = new ArrayList<>(0);
			comboboxSemestreEnum.add(new SelectItem("", ""));
			comboboxSemestreEnum.add(new SelectItem("1", "1º"));
			comboboxSemestreEnum.add(new SelectItem("2", "2º"));
		}
		return comboboxSemestreEnum;
	}
	
	public List<SelectItem> getComboboxBimestreEnum() {
		if (comboboxBimestreEnum == null) {
			comboboxBimestreEnum = new ArrayList<>(0);
			comboboxBimestreEnum.add(new SelectItem(0, ""));
			comboboxBimestreEnum.add(new SelectItem(1, "1º"));
			comboboxBimestreEnum.add(new SelectItem(2, "2º"));
			comboboxBimestreEnum.add(new SelectItem(3, "3º"));
			comboboxBimestreEnum.add(new SelectItem(4, "4º"));
			comboboxBimestreEnum.add(new SelectItem(5, "5º"));
			comboboxBimestreEnum.add(new SelectItem(6, "6º"));
		}
		return comboboxBimestreEnum;
	}

	public List<SelectItem> getSemestreEnum() {
		if (comboboxSemestreEnum == null) {
			comboboxSemestreEnum = montarComboboxSemOpcaoDeNenhum(SemestreEnum.values(), Obrigatorio.NAO);
		}
		return comboboxSemestreEnum;
	}
	
	public List<SelectItem> getPeriodicidadeEnum() {
		if (comboboxPeriodicidadeEnum == null) {
			comboboxPeriodicidadeEnum = montarComboboxSemOpcaoDeNenhum(PeriodicidadeEnum.values(), Obrigatorio.NAO);
		}
		return comboboxPeriodicidadeEnum;
	}

	public List<SelectItem> getComboboxMesesEnum() {
		if (comboboxMesesEnum == null) {
			comboboxMesesEnum = new ArrayList<>(0);
			comboboxMesesEnum.add(new SelectItem("", ""));
			comboboxMesesEnum.add(new SelectItem("1", "Janeiro"));
			comboboxMesesEnum.add(new SelectItem("2", "Fevereiro"));
			comboboxMesesEnum.add(new SelectItem("3", UteisJSF.internacionalizar("prt_Calendario_marco")));
			comboboxMesesEnum.add(new SelectItem("4", "Abril"));
			comboboxMesesEnum.add(new SelectItem("5", "Maio"));
			comboboxMesesEnum.add(new SelectItem("6", "Junho"));
			comboboxMesesEnum.add(new SelectItem("7", "Julho"));
			comboboxMesesEnum.add(new SelectItem("8", "Agosto"));
			comboboxMesesEnum.add(new SelectItem("9", "Setembro"));
			comboboxMesesEnum.add(new SelectItem("10", "Outubro"));
			comboboxMesesEnum.add(new SelectItem("11", "Novembro"));
			comboboxMesesEnum.add(new SelectItem("12", "Dezembro"));
		}
		return comboboxMesesEnum;
	}
	
	public List<SelectItem> getComboboxDisciplinaReprovadas() {
		if (comboboxDisciplinaReprovadas == null) {
			comboboxDisciplinaReprovadas = new ArrayList<>(0);
			comboboxDisciplinaReprovadas.add(new SelectItem(0, "Todas"));
			comboboxDisciplinaReprovadas.add(new SelectItem(1, "1"));
			comboboxDisciplinaReprovadas.add(new SelectItem(2, "2"));
			comboboxDisciplinaReprovadas.add(new SelectItem(3, "3"));
			comboboxDisciplinaReprovadas.add(new SelectItem(4, "4"));
			comboboxDisciplinaReprovadas.add(new SelectItem(5, "5"));
			comboboxDisciplinaReprovadas.add(new SelectItem(6, "6"));
			comboboxDisciplinaReprovadas.add(new SelectItem(7, "7"));
			comboboxDisciplinaReprovadas.add(new SelectItem(8, "8"));
			comboboxDisciplinaReprovadas.add(new SelectItem(9, "9"));
			comboboxDisciplinaReprovadas.add(new SelectItem(10, "10"));
		}
		return comboboxDisciplinaReprovadas;
	}
	
	public List<SelectItem> getComboBoxDefinicaoTutoriaOnline() {
		if (comboBoxDefinicaoTutoriaOnline == null) {
			comboBoxDefinicaoTutoriaOnline = montarCombobox(DefinicoesTutoriaOnlineEnum.values(), Obrigatorio.SIM);
		}
		return comboBoxDefinicaoTutoriaOnline;
	}

	public List<SelectItem> getComboBoxTipoNivelEducacionalEnum() {
		if (comboBoxTipoNivelEducacionalEnum == null) {
			comboBoxTipoNivelEducacionalEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, "valor", "descricao", true);
		}
		return comboBoxTipoNivelEducacionalEnum;
	}
	
	public List<SelectItem> getComboBoxTipoNivelEducacionalEnumName() {
		if (comboBoxTipoNivelEducacionalEnumName == null) {
			comboBoxTipoNivelEducacionalEnumName = montarCombobox(TipoNivelEducacional.values(), Obrigatorio.SIM);
		}
		return comboBoxTipoNivelEducacionalEnumName;
	}

	public List<SelectItem> getComboBoxPoliticaSelecaoQuestaoEnum() {
		if (comboBoxPoliticaSelecaoQuestaoEnum == null) {
			comboBoxPoliticaSelecaoQuestaoEnum = montarCombobox(PoliticaSelecaoQuestaoEnum.values(), Obrigatorio.SIM);
		}
		return comboBoxPoliticaSelecaoQuestaoEnum;
	}

	public List<SelectItem> getComboBoxRegraDistribuicaoQuestaoEnum() {
		if (comboBoxRegraDistribuicaoQuestaoEnum == null) {
			comboBoxRegraDistribuicaoQuestaoEnum = montarCombobox(RegraDistribuicaoQuestaoEnum.values(), Obrigatorio.SIM);
		}
		return comboBoxRegraDistribuicaoQuestaoEnum;
	}

	public List<SelectItem> getComboBoxNivelComplexidadeQuestaoEnum() {
		if (comboBoxNivelComplexidadeQuestaoEnum == null) {
			comboBoxNivelComplexidadeQuestaoEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelComplexidadeQuestaoEnum.class, "name", "valorApresentar", false);
		}
		return comboBoxNivelComplexidadeQuestaoEnum;
	}

	public List<SelectItem> getComboboxTipoConsultaLocalArmazenamentoEnum() {
		if (comboboxTipoConsultaLocalArmazenamentoEnum == null) {
			comboboxTipoConsultaLocalArmazenamentoEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoConsultaLocalArmazenamentoEnum.class, "name", "valorApresentar", false);
		}
		return comboboxTipoConsultaLocalArmazenamentoEnum;
	}

	public void setComboboxTipoConsultaLocalArmazenamentoEnum(List<SelectItem> comboboxTipoConsultaLocalArmazenamentoEnum) {
		this.comboboxTipoConsultaLocalArmazenamentoEnum = comboboxTipoConsultaLocalArmazenamentoEnum;
	}

	

	public void setComboboxFormaEntradaPatrimonioEnum(List<SelectItem> comboboxFormaEntradaPatrimonioEnum) {
		this.comboboxFormaEntradaPatrimonioEnum = comboboxFormaEntradaPatrimonioEnum;
	}

	public List<SelectItem> getComboboxOrientacaoPaginaEnum() {
		if (comboboxOrientacaoPaginaEnum == null) {
			comboboxOrientacaoPaginaEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(OrientacaoPaginaEnum.class, "key", "value", true);
		}
		return comboboxOrientacaoPaginaEnum;
	}

	public void setComboboxOrientacaoPaginaEnum(List<SelectItem> comboboxOrientacaoPaginaEnum) {
		this.comboboxOrientacaoPaginaEnum = comboboxOrientacaoPaginaEnum;
	}

	public List<SelectItem> getComboBoxPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum() {
		if (comboBoxPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum == null) {
			comboBoxPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum.class, "name", "valorApresentar", false);
		}
		return comboBoxPeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum;
	}

	

	public List<SelectItem> getComboBoxTipoFinanciamentoEnum() {
		return TipoFinanciamentoEnum.getListaSelectItemTipoFinanciamento();
	}

	public void setComboBoxTipoFinanciamentoEnum(List<SelectItem> comboBoxTipoFinanciamentoEnum) {
		this.comboBoxTipoFinanciamentoEnum = comboBoxTipoFinanciamentoEnum;
	}

	public List<SelectItem> getComboBoxParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum() {
		if (comboBoxParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum == null) {
			comboBoxParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(ParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum.class, "name", "valorApresentar", false);
		}
		return comboBoxParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum;
	}

	public void setComboBoxParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum(List<SelectItem> comboBoxParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum) {
		this.comboBoxParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum = comboBoxParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum;
	}

	public List<SelectItem> getComboboxPeriodicidadeEnum() {
		if (comboboxPeriodicidadeEnum == null) {
			comboboxPeriodicidadeEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(PeriodicidadeEnum.class, false);
		}
		return comboboxPeriodicidadeEnum;
	}

	

	private List<SelectItem> comboboxTipoSubTurmaEnum;

	@SuppressWarnings("unchecked")
	public List<SelectItem> getComboboxTipoSubTurmaEnum() {
		if (comboboxTipoSubTurmaEnum == null) {
			comboboxTipoSubTurmaEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoSubTurmaEnum.class, "name", "valorApresentar", false);
			Collections.sort(comboboxTipoSubTurmaEnum, new SelectItemOrdemValor());
		}
		return comboboxTipoSubTurmaEnum;
	}

	public List<SelectItem> getComboboxSituacaoEnum() {
		if (comboboxSituacaoEnum == null)
			comboboxSituacaoEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoEnum.class, "name", "valorApresentar", false);
		return comboboxSituacaoEnum;
	}
	
	public List<SelectItem> getComboboxSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum() {
		if (comboboxSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum == null)
			comboboxSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum = montarCombobox(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.values(), Obrigatorio.NAO);
		return comboboxSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;
	}
	
	public List<SelectItem> getComboboxSituacaoAtivoInativoEnum() {
		if (comboboxSituacaoAtivoInativoEnum == null){
			comboboxSituacaoAtivoInativoEnum = new ArrayList<>();
			comboboxSituacaoAtivoInativoEnum.add(new SelectItem(SituacaoEnum.ATIVO.name(), "Ativo"));
			comboboxSituacaoAtivoInativoEnum.add(new SelectItem(SituacaoEnum.INATIVO.name(), "Inativo"));
		}
		return comboboxSituacaoAtivoInativoEnum;
	}

	private List<SelectItem> comboBoxAmbienteCartaoCreditoEnum;

	public List<SelectItem> getComboBoxAmbienteCartaoCreditoEnum() {
		if (comboBoxAmbienteCartaoCreditoEnum == null) {
			comboBoxAmbienteCartaoCreditoEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(AmbienteCartaoCreditoEnum.class, "name", "valorApresentar", false);
		}
		return comboBoxAmbienteCartaoCreditoEnum;
	}

	private List<SelectItem> listaSelectTipoControleComposicao;

	public List<SelectItem> getListaSelectTipoControleComposicao() {
		if (listaSelectTipoControleComposicao == null) {
			listaSelectTipoControleComposicao = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoControleComposicaoEnum.class);
		}
		return listaSelectTipoControleComposicao;
	}
	
	private List<SelectItem> listaSelectTipoPessoaEnum;
	
	public List<SelectItem> getListaSelectTipoPessoaEnum() {
		if (listaSelectTipoPessoaEnum == null) {
			listaSelectTipoPessoaEnum = new ArrayList<>();
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.NENHUM, ""));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.ALUNO, TipoPessoa.ALUNO.getDescricao()));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.CANDIDATO, TipoPessoa.CANDIDATO.getDescricao()));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.MEMBRO_COMUNIDADE, TipoPessoa.MEMBRO_COMUNIDADE.getDescricao()));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.PROFESSOR, TipoPessoa.PROFESSOR.getDescricao()));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.REQUERENTE, TipoPessoa.REQUERENTE.getDescricao()));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.PARCEIRO, TipoPessoa.PARCEIRO.getDescricao()));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.FUNCIONARIO, TipoPessoa.FUNCIONARIO.getDescricao()));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.FORNECEDOR, TipoPessoa.FORNECEDOR.getDescricao()));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.RESPONSAVEL_FINANCEIRO, TipoPessoa.RESPONSAVEL_FINANCEIRO.getDescricao()));
			listaSelectTipoPessoaEnum.add(new SelectItem(TipoPessoa.COORDENADOR_CURSO, TipoPessoa.COORDENADOR_CURSO.getDescricao()));
		}
		return listaSelectTipoPessoaEnum;
	}

	private List<SelectItem> listaSelectSituacaoForumEnum;
	private List<SelectItem> listaSelectPublicoAlvoForumEnum;
	private List<SelectItem> listaSelectRestricaoPublicoAlvoForumEnum;
	private List<SelectItem> listaSelectFuncaoResponsavelAtaEnum;
	private List<SelectItem> listaSelectTipoDesigneTextoEnum;
	private List<SelectItem> listaSelectAlinhamentoAssinaturaDigitalEnum;
	private List<SelectItem> listaSelectFormaIngresso;

	public List<SelectItem> getListaSelectFormaIngresso() {
		if (listaSelectFormaIngresso == null) {
			listaSelectFormaIngresso = new ArrayList<SelectItem>();
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.NENHUM, FormaIngresso.NENHUM.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.ENTREVISTA, FormaIngresso.ENTREVISTA.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.PORTADOR_DE_DIPLOMA, FormaIngresso.PORTADOR_DE_DIPLOMA.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.TRANSFERENCIA_INTERNA, FormaIngresso.TRANSFERENCIA_INTERNA.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.PROCESSO_SELETIVO, FormaIngresso.PROCESSO_SELETIVO.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.VESTIBULAR, FormaIngresso.VESTIBULAR.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.TRANSFERENCIA_EXTERNA, FormaIngresso.TRANSFERENCIA_EXTERNA.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.REINGRESSO, FormaIngresso.REINGRESSO.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.PROUNI, FormaIngresso.PROUNI.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.ENEM, FormaIngresso.ENEM.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.DECISAO_JUDICIAL, FormaIngresso.DECISAO_JUDICIAL.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.OUTROS_TIPOS_SELECAO, FormaIngresso.OUTROS_TIPOS_SELECAO.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.AVALIACAO_SERIADA, FormaIngresso.AVALIACAO_SERIADA.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.SELECAO_SIMPLIFICADA, FormaIngresso.SELECAO_SIMPLIFICADA.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.TRANSFERENCIA_EXTERNA_OFICIO, FormaIngresso.TRANSFERENCIA_EXTERNA_OFICIO.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.VAGAS_REMANESCENTES, FormaIngresso.VAGAS_REMANESCENTES.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.VAGAS_PROGRAMAS_ESPECIAIS, FormaIngresso.VAGAS_PROGRAMAS_ESPECIAIS.getDescricao()));
			listaSelectFormaIngresso.add(new SelectItem(FormaIngresso.VAGAS_PROGRAMAS_ESPECIAIS_FIES, FormaIngresso.VAGAS_PROGRAMAS_ESPECIAIS_FIES.getDescricao()));
		}
		return listaSelectFormaIngresso;
	}

	public List<SelectItem> getListaSelectSituacaoForumEnum() {
		if (listaSelectSituacaoForumEnum == null) {
			listaSelectSituacaoForumEnum = montarCombobox(SituacaoForumEnum.values(), Obrigatorio.NAO);
		}
		return listaSelectSituacaoForumEnum;
	}

	public List<SelectItem> getListaSelectPublicoAlvoForumEnum() {
		if (listaSelectPublicoAlvoForumEnum == null) {
			listaSelectPublicoAlvoForumEnum = montarCombobox(PublicoAlvoForumEnum.values(), Obrigatorio.NAO);
		}
		return listaSelectPublicoAlvoForumEnum;
	}

	public List<SelectItem> getListaSelectRestricaoPublicoAlvoForumEnum() {
		if (listaSelectRestricaoPublicoAlvoForumEnum == null) {
			listaSelectRestricaoPublicoAlvoForumEnum = montarCombobox(RestricaoPublicoAlvoForumEnum.values(), Obrigatorio.NAO);

		}
		return listaSelectRestricaoPublicoAlvoForumEnum;
	}

	public List<SelectItem> getListaSelectFuncaoResponsavelAtaEnum() {
		if (listaSelectFuncaoResponsavelAtaEnum == null) {
			listaSelectFuncaoResponsavelAtaEnum = montarCombobox(FuncaoResponsavelAtaEnum.values(), Obrigatorio.NAO);
		}
		return listaSelectFuncaoResponsavelAtaEnum;
	}

	public List<SelectItem> getListaSelectTipoDesigneTextoEnum() {
		if (listaSelectTipoDesigneTextoEnum == null) {
			listaSelectTipoDesigneTextoEnum = montarCombobox(TipoDesigneTextoEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectTipoDesigneTextoEnum;
	}

	public List<SelectItem> getListaSelectAlinhamentoAssinaturaDigitalEnum() {
		if (listaSelectAlinhamentoAssinaturaDigitalEnum == null) {
			listaSelectAlinhamentoAssinaturaDigitalEnum = montarCombobox(AlinhamentoAssinaturaDigitalEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectAlinhamentoAssinaturaDigitalEnum;
	}

	private List<SelectItem> listaSelectTipoLivroRegistroDiplomaEnum;

	public List<SelectItem> getListaSelectTipoLivroRegistroDiplomaEnum() {
		if (listaSelectTipoLivroRegistroDiplomaEnum == null) {
			listaSelectTipoLivroRegistroDiplomaEnum = montarCombobox(TipoLivroRegistroDiplomaEnum.values(), Obrigatorio.SIM);

		}
		return listaSelectTipoLivroRegistroDiplomaEnum;
	}

	

	public void setComboboxCSTPISCOFINSEnum(List<SelectItem> comboboxCSTPISCOFINSEnum) {
		this.comboboxCSTPISCOFINSEnum = comboboxCSTPISCOFINSEnum;
	}

	public List<SelectItem> getComboboxIntegracaoNegativacaoCobrancaContaReceberEnum() {
		if (comboboxIntegracaoNegativacaoCobrancaContaReceberEnum == null) {
			comboboxIntegracaoNegativacaoCobrancaContaReceberEnum = montarCombobox(IntegracaoNegativacaoCobrancaContaReceberEnum.values(), Obrigatorio.NAO);
		}
		return comboboxIntegracaoNegativacaoCobrancaContaReceberEnum;
	}

	public List<SelectItem> getComboboxTipoAgenteNegativacaoCobrancaContaReceberEnum() {
		if (comboboxTipoAgenteNegativacaoCobrancaContaReceberEnum == null) {
			comboboxTipoAgenteNegativacaoCobrancaContaReceberEnum = montarCombobox(TipoAgenteNegativacaoCobrancaContaReceberEnum.values(), Obrigatorio.NAO);
		}
		return comboboxTipoAgenteNegativacaoCobrancaContaReceberEnum;
	}
	
	
	
	
	public List<SelectItem> getComboboxTipoContaCorrenteEnum() {
		if (comboboxTipoContaCorrenteEnum == null) {
			comboboxTipoContaCorrenteEnum = montarCombobox(TipoContaCorrenteEnum.values(), Obrigatorio.SIM);
		}
		return comboboxTipoContaCorrenteEnum;
	}
	
	public List<SelectItem> getComboboxGestaoContasPagarOperacaoEnum() {
		if (comboboxGestaoContasPagarOperacaoEnum == null) {
			comboboxGestaoContasPagarOperacaoEnum = montarCombobox(GestaoContasPagarOperacaoEnum.values(), Obrigatorio.SIM);
		}
		return comboboxGestaoContasPagarOperacaoEnum;
	}
	
	public List<SelectItem> getComboboxTipoSalaAulaBlackboardEnum() {
		if (comboboxTipoSalaAulaBlackboardEnum == null) {
			comboboxTipoSalaAulaBlackboardEnum = new ArrayList<>();
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.NENHUM, ""));
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.DISCIPLINA.name(), "Disciplina"));
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.ESTAGIO.name(), "Estágio"));
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO.name(), "Ambientação de Tcc"));
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.TCC.name(), "Sala de Tcc"));
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.TCC_GRUPO.name(), "Grupo de Tcc"));
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_AMBIENTACAO.name(), "Ambientação Projeto Integrador "));
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR.name(), "Sala Projeto Integrador"));
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_GRUPO.name(), "Grupo Projeto Integrador "));
			comboboxTipoSalaAulaBlackboardEnum.add(new SelectItem(TipoSalaAulaBlackboardEnum.IMPORTACAO.name(), "Importacao"));
		}
		return comboboxTipoSalaAulaBlackboardEnum;
	}
	
	public List<SelectItem> getComboboxRegrasSubstituicaoGrupoPessoaItemEnum() {
		if (comboboxRegrasSubstituicaoGrupoPessoaItemEnum == null) {
			comboboxRegrasSubstituicaoGrupoPessoaItemEnum = montarCombobox(RegrasSubstituicaoGrupoPessoaItemEnum.values(), Obrigatorio.SIM);
		}
		return comboboxRegrasSubstituicaoGrupoPessoaItemEnum;
	}
	
	public List<SelectItem> getComboboxRegraContagemPeriodoLetivoEnum() {
		if (comboboxRegraContagemPeriodoLetivoEnum == null) {
			comboboxRegraContagemPeriodoLetivoEnum = montarCombobox(RegraContagemPeriodoLetivoEnum.values(), Obrigatorio.SIM);
		}
		return comboboxRegraContagemPeriodoLetivoEnum;
	}	
	
	public List<SelectItem> getTipoConsultaComboContaCorrenteEnum() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(null, ""));
		itens.add(new SelectItem(TipoContaCorrenteEnum.CAIXA.name(), "Caixa"));
		itens.add(new SelectItem(TipoContaCorrenteEnum.CORRENTE.name(), "Corrente"));
		itens.add(new SelectItem(TipoContaCorrenteEnum.APLICACAO.name(), "Aplicação"));
		return itens;
	}
	
	
	

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(enumCampoConsultaCurso.NOME.name(), "Nome"));
		itens.add(new SelectItem(enumCampoConsultaCurso.CODIGO.name(), "Código"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(enumCampoConsultaTurma.IDENTIFICADOR_TURMA.name(), "Identificador Turma"));
		return itens;
	}

	
	
	
	public List<SelectItem> getComboboxOrigemContaPagarEnum() {
		if(comboboxOrigemContaPagarEnum == null) {
			comboboxOrigemContaPagarEnum = new ArrayList<>();
			for(OrigemContaPagar origemContaPagar: OrigemContaPagar.values()) {
				comboboxOrigemContaPagarEnum.add(new SelectItem(origemContaPagar, origemContaPagar.getDescricao()));
			}
		}
		return comboboxOrigemContaPagarEnum;
	}

	
	
	public List<SelectItem> getTipoConsultaComboConfiguracaoSeiGsuite() {
		if (tipoConsultaComboConfiguracaoSeiGsuite == null) {
			tipoConsultaComboConfiguracaoSeiGsuite = new ArrayList<>();
			tipoConsultaComboConfiguracaoSeiGsuite.add(new SelectItem(enumCampoConsultaConfiguracaoSeiGsuite.CLIENTE_SEI_GSUITE.name(), "Cliente Sei Gsuite"));
			tipoConsultaComboConfiguracaoSeiGsuite.add(new SelectItem(enumCampoConsultaConfiguracaoSeiGsuite.CONTA_EMAIL_GOOGLE.name(), "Conta Email Google"));
			tipoConsultaComboConfiguracaoSeiGsuite.add(new SelectItem(enumCampoConsultaConfiguracaoSeiGsuite.CODIGO.name(), "Código"));
		}
		return tipoConsultaComboConfiguracaoSeiGsuite;
	}
	
	public List<SelectItem> getTipoConsultaComboConfiguracaoSeiBlackboard() {
		if (tipoConsultaComboConfiguracaoSeiBlackboard == null) {
			tipoConsultaComboConfiguracaoSeiBlackboard = new ArrayList<>();			
			tipoConsultaComboConfiguracaoSeiBlackboard.add(new SelectItem(enumCampoConsultaConfiguracaoSeiBlackboard.CODIGO.name(), "Código"));
		}
		return tipoConsultaComboConfiguracaoSeiBlackboard;
	}
	
	public List<SelectItem> getComboboxGrauParentescoEnum() {
		if (comboboxGrauParentescoEnum == null) {
			comboboxGrauParentescoEnum = montarCombobox(GrauParentescoEnum.values(), Obrigatorio.NAO);
		}
		return comboboxGrauParentescoEnum;
	}
	
	public List<SelectItem> getComboboxEstadoCivilEnum() {
		if (comboboxEstadoCivilEnum == null) {
			comboboxEstadoCivilEnum = montarCombobox(EstadoCivilEnum.values(), Obrigatorio.NAO);
		}
		return comboboxEstadoCivilEnum;
	}

	public List<SelectItem> getComboboxAtivoInativoEnum() {
		if (comboboxAtivoInativoEnum == null)
			comboboxAtivoInativoEnum = montarCombobox(AtivoInativoEnum.values(), Obrigatorio.SIM);
		return comboboxAtivoInativoEnum;
	}

	public void setComboboxAtivoInativoEnum(List<SelectItem> comboboxAtivoInativoEnum) {
		this.comboboxAtivoInativoEnum = comboboxAtivoInativoEnum;
	}

	
	public void setComboboxLocalIncidenciaEnum(List<SelectItem> comboboxLocalIncidenciaEnum) {
		this.comboboxLocalIncidenciaEnum = comboboxLocalIncidenciaEnum;
	}

	
	public void setComboboxTipoLancamentoFolhaPagamentoEnum(List<SelectItem> comboboxTipoLancamentoFolhaPagamentoEnum) {
		this.comboboxTipoLancamentoFolhaPagamentoEnum = comboboxTipoLancamentoFolhaPagamentoEnum;
	}

	public void setComboboxTipoEventoFolhaPagamentoEnum(List<SelectItem> comboboxTipoEventoFolhaPagamentoEnum) {
		this.comboboxTipoEventoFolhaPagamentoEnum = comboboxTipoEventoFolhaPagamentoEnum;
	}
	
	
	
	
	public List<SelectItem> getTipoConsultaComboDepartamento() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(EnumCampoConsultaDepartamento.NOME.name(), UteisJSF.internacionalizar("enum_DepartamentoEnum_NOME")));
		itens.add(new SelectItem(EnumCampoConsultaDepartamento.NOME_PESSOA.name(), UteisJSF.internacionalizar("enum_DepartamentoEnum_NOME_PESSOA")));
		itens.add(new SelectItem(EnumCampoConsultaDepartamento.CODIGO.name(), UteisJSF.internacionalizar("enum_DepartamentoEnum_CODIGO")));

		return itens;
	}
	
	public List<SelectItem> getPrevidencia() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem(PrevidenciaEnum.INSS.name(), UteisJSF.internacionalizar("enum_PrevidenciaEnum_INSS")));
		itens.add(new SelectItem(PrevidenciaEnum.PREVIDENCIA_PROPRIA.name(), UteisJSF.internacionalizar("enum_PrevidenciaEnum_PREVIDENCIA_PROPRIA")));
		itens.add(new SelectItem(PrevidenciaEnum.OUTROS.name(), UteisJSF.internacionalizar("enum_PrevidenciaEnum_OUTROS")));
		return itens;
	}
	
	
	
	public List<SelectItem> getSituacoesTiposSituacoesEnum() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(SituacaoTipoAdvertenciaEnum.ATIVO.name(), SituacaoTipoAdvertenciaEnum.ATIVO.getValorApresentar()));
		itens.add(new SelectItem(SituacaoTipoAdvertenciaEnum.INATIVO.name(), SituacaoTipoAdvertenciaEnum.INATIVO.getValorApresentar()));
		itens.add(new SelectItem("TODOS", "Todos"));
		return itens;
	}
	
	
	
	public List<SelectItem> getSituacaoFuncionarioCargo() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(SituacaoFuncionarioEnum.ATIVO.name(), SituacaoFuncionarioEnum.ATIVO.getValorApresentar()));
		itens.add(new SelectItem(SituacaoFuncionarioEnum.DEMITIDO.name(), SituacaoFuncionarioEnum.DEMITIDO.getValorApresentar()));
		itens.add(new SelectItem(SituacaoFuncionarioEnum.LICENCA_MATERNIDADE.name(), SituacaoFuncionarioEnum.LICENCA_MATERNIDADE.getValorApresentar()));
		itens.add(new SelectItem(SituacaoFuncionarioEnum.FERIAS.name(), SituacaoFuncionarioEnum.FERIAS.getValorApresentar()));
		itens.add(new SelectItem(SituacaoFuncionarioEnum.LICENCA_SEM_VENCIMENTO.name(), SituacaoFuncionarioEnum.LICENCA_SEM_VENCIMENTO.getValorApresentar()));
		itens.add(new SelectItem(SituacaoFuncionarioEnum.AFASTAMENTO_PREVIDENCIA.name(), SituacaoFuncionarioEnum.AFASTAMENTO_PREVIDENCIA.getValorApresentar()));
		itens.add(new SelectItem(SituacaoFuncionarioEnum.LICENCA_REMUNERADA.name(), SituacaoFuncionarioEnum.LICENCA_REMUNERADA.getValorApresentar()));
		itens.add(new SelectItem(SituacaoFuncionarioEnum.LICENCA_PATERNIDADE.name(), SituacaoFuncionarioEnum.LICENCA_PATERNIDADE.getValorApresentar()));
		itens.add(new SelectItem(SituacaoFuncionarioEnum.OUTROS.name(), SituacaoFuncionarioEnum.OUTROS.getValorApresentar()));
		itens.add(new SelectItem("TODOS", "Todos"));
		return itens;
	}
	
	
	
	public List<SelectItem> getComboBoxTipoParcelaNegociarEnum() {
		if (comboBoxTipoParcelaNegociarEnum == null) {
			comboBoxTipoParcelaNegociarEnum = new ArrayList<SelectItem>();
			comboBoxTipoParcelaNegociarEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoParcelaNegociarEnum.class, "name", "valorApresentar", false);
		}
		return comboBoxTipoParcelaNegociarEnum;
	}
	
	public List<SelectItem> getComboBoxTipoContratoAgenteNegativacaoCobrancaEnum() {
		if (comboBoxTipoContratoAgenteNegativacaoCobrancaEnum == null) {
			comboBoxTipoContratoAgenteNegativacaoCobrancaEnum = montarCombobox(TipoContratoAgenteNegativacaoCobrancaEnum.values(), Obrigatorio.SIM);
		}
		return comboBoxTipoContratoAgenteNegativacaoCobrancaEnum;
	}
	
	public List<SelectItem> getComboBoxOperacaoDeVinculoEstagioEnum() {
		if (comboBoxOperacaoDeVinculoEstagioEnum == null) {
			comboBoxOperacaoDeVinculoEstagioEnum = montarCombobox(OperacaoDeVinculoEstagioEnum.values(), Obrigatorio.SIM);
		}
		return comboBoxOperacaoDeVinculoEstagioEnum;
	}
	
	
	/***
	 * 
	 * Monta COMBOBOX <br> 
	 * 
	 * Enum nao considera opcao de nenhum para fazer o processamento (vide outros montarCombobox)
	 *  
	 * @param enumeradores
	 * @param obrigatorio
	 * @return
	 */
	public List<SelectItem> montarComboboxSemOpcaoDeNenhum(@SuppressWarnings("rawtypes") Enum[] enumeradores, Obrigatorio obrigatorio) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		
		if (obrigatorio == Obrigatorio.NAO) 
			lista.add(new SelectItem("", ""));
		
		for (Enum<?> enumerador : enumeradores) {
			lista.add(new SelectItem(enumerador, internacionalizarEnum(enumerador)));
		}
		return lista;
	}

	public void setComboBoxTipoParcelaNegociarEnum(List<SelectItem> comboBoxTipoParcelaNegociarEnum) {
		this.comboBoxTipoParcelaNegociarEnum = comboBoxTipoParcelaNegociarEnum;
	}
	
	
	public void setComboboxCategoriaEventoFolhaPagamentoEnum(List<SelectItem> comboboxCategoriaEventoFolhaPagamentoEnum) {
		this.comboboxCategoriaEventoFolhaPagamentoEnum = comboboxCategoriaEventoFolhaPagamentoEnum;
	}

	
	public void setComboboxTipoEventoMediaEnum(List<SelectItem> comboboxTipoEventoMediaEnum) {
		this.comboboxTipoEventoMediaEnum = comboboxTipoEventoMediaEnum;
	}
	

	public void setComboboxTipoEntidadeSindicalEnum(List<SelectItem> comboboxTipoEntidadeSindicalEnum) {
		this.comboboxTipoEntidadeSindicalEnum = comboboxTipoEntidadeSindicalEnum;
	}
	
	
	public List<SelectItem> getTipoConsultaComboSindicato() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("DESCRICAO", UteisJSF.internacionalizar("enum_TipoConsultaComboSecaoFolhaPagamentoEnum_DESCRICAO")));

		return itens;
	}

	
	
	public List<SelectItem> getTipoConsultaCodigoNome() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}
	
	/**
	 * Retorna itens de SelectItem com os dados da Natureza do Evento
	 * @return
	 */
	
	
	public List<SelectItem> getComboboxSituacaoMarcacaoFeriasColetivasEnum() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("DATACOMPETENCIA", UteisJSF.internacionalizar("prt_LancamentoFolhaPagamento_dataCompetencia")));
		return itens;
	}
	
	

	public void setComboboxRescisao(List<SelectItem> comboboxRescisao) {
		this.comboboxRescisao = comboboxRescisao;
	}

	

	public void setComboboxTipoDemissao(List<SelectItem> comboboxTipoDemissao) {
		this.comboboxTipoDemissao = comboboxTipoDemissao;
	}

	

	public void setComboboxMotivoDemissao(List<SelectItem> comboboxMotivoDemissao) {
		this.comboboxMotivoDemissao = comboboxMotivoDemissao;
	}

	
	public void setComboboxTipoEventoMediaRescisao(List<SelectItem> comboboxTipoEventoMediaRescisao) {
		this.comboboxTipoEventoMediaRescisao = comboboxTipoEventoMediaRescisao;
	}

	public List<SelectItem> getComboboxCorRaca() {
		if (comboboxCorRaca == null) {
			comboboxCorRaca = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(CorRaca.class);
		}
		return comboboxCorRaca;
	}

	public void setComboboxCorRaca(List<SelectItem> comboboxCorRaca) {
		this.comboboxCorRaca = comboboxCorRaca;
	}


	
	public void setComboboxCategoriaDespesaEnum(List<SelectItem> comboboxCategoriaDespesaEnum) {
		this.comboboxCategoriaDespesaEnum = comboboxCategoriaDespesaEnum;
	}


	
	public void setComboboxFormaPagamentoPadraoEnum(List<SelectItem> comboboxFormaPagamentoEnum) {
		this.comboboxFormaPagamentoEnum = comboboxFormaPagamentoEnum;
	}
	
	public List<SelectItem> getComboboxEmpresaOperadoraCartaoEnum() {
		if (comboboxEmpresaOperadoraCartaoEnum == null) {
			comboboxEmpresaOperadoraCartaoEnum = montarCombobox(EmpresaOperadoraCartaoEnum.values(), Obrigatorio.NAO);
		}
		return comboboxEmpresaOperadoraCartaoEnum; 
	}
	
	public List<SelectItem> getComboboxPermitirCartaoEnum() {
		if (comboboxPermitirCartaoEnum == null) {
//			comboboxPermitirCartaoEnum = montarCombobox(PermitirCartaoEnum.values(), Obrigatorio.SIM);
			/**
			 *  Temporariamente, enquanto não é permitido débito online,
			 *  usar código abaixo montando apenas crédito,
			 *  depois voltar código acima comentado.
			 */
			comboboxPermitirCartaoEnum = new ArrayList<SelectItem>();
			comboboxPermitirCartaoEnum.add(new SelectItem(PermitirCartaoEnum.CREDITO, internacionalizarEnum(PermitirCartaoEnum.CREDITO)));
		}
		return comboboxPermitirCartaoEnum;
	}
	
	public List<SelectItem> getComboboxVisaoParcelarEnum() {
		if (comboboxVisaoParcelarEnum == null) {
			comboboxVisaoParcelarEnum = montarCombobox(VisaoParcelarEnum.values(), Obrigatorio.SIM);
		}
		return comboboxVisaoParcelarEnum;
	}

	public List<SelectItem> getComboboxCrosstabEnum() {
		if (comboboxCrosstabEnum == null) {
			comboboxCrosstabEnum = new ArrayList<>();
			comboboxCrosstabEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(CrossTabEnum.class, true);
		}
		return comboboxCrosstabEnum;
	}

	public void setComboboxCrosstabEnum(List<SelectItem> comboboxCrosstabEnum) {
		this.comboboxCrosstabEnum = comboboxCrosstabEnum;
	}

	public List<SelectItem> getComboboxNivelFormacaoAcademica() {
		if (comboboxNivelFormacaoAcademica == null) {
			comboboxNivelFormacaoAcademica = new ArrayList<>();
			comboboxNivelFormacaoAcademica = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, true);
		}
		return comboboxNivelFormacaoAcademica;
	}

	public void setComboboxNivelFormacaoAcademica(List<SelectItem> comboboxNivelFormacaoAcademica) {
		this.comboboxNivelFormacaoAcademica = comboboxNivelFormacaoAcademica;
	}
	
	public List<SelectItem> getComboboxAmbienteContaCorrenteEnum() {
		if (comboboxAmbienteContaCorrenteEnum == null) {
			comboboxAmbienteContaCorrenteEnum = montarCombobox(AmbienteContaCorrenteEnum.values(), Obrigatorio.SIM);
		}
		return comboboxAmbienteContaCorrenteEnum;
	}
	
	
	
	
	
	

	public List<SelectItem> getComboboxSituacaoHoraAtividadeExtraClasseEnum() {
		if (comboboxSituacaoHoraAtividadeExtraClasseEnum == null) {
			comboboxSituacaoHoraAtividadeExtraClasseEnum = new ArrayList<>();
//			comboboxSituacaoHoraAtividadeExtraClasseEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoHoraAtividadeExtraClasseEnum.class, true);
		}
		return comboboxSituacaoHoraAtividadeExtraClasseEnum;
	}

	public void setComboboxSituacaoHoraAtividadeExtraClasseEnum(
			List<SelectItem> comboboxSituacaoHoraAtividadeExtraClasseEnum) {
		this.comboboxSituacaoHoraAtividadeExtraClasseEnum = comboboxSituacaoHoraAtividadeExtraClasseEnum;
	}

	

	public List<SelectItem> getComboboxFormaGeracaoEventoAulaOnLineGoogleMeet() {
		if (comboboxFormaGeracaoEventoAulaOnLineGoogleMeet == null) {
			comboboxFormaGeracaoEventoAulaOnLineGoogleMeet = montarCombobox(FormaGeracaoEventoAulaOnLineGoogleMeetEnum.values(), Obrigatorio.NAO);
		}
		return comboboxFormaGeracaoEventoAulaOnLineGoogleMeet;
	}
	
	public List<SelectItem> getComboboxTipoIdentificacaoChavePixEnum() {		
		return montarCombobox(TipoIdentificacaoChavePixEnum.values(), Obrigatorio.SIM);	
	}
	
	public List<SelectItem> getComboboxTipoIdentificacaoChavePixEnumItau() {	
			TipoIdentificacaoChavePixEnum[] array = 
					new TipoIdentificacaoChavePixEnum[]{TipoIdentificacaoChavePixEnum.TELEFONE,
							TipoIdentificacaoChavePixEnum.EMAIL ,
							TipoIdentificacaoChavePixEnum.CPF_CNPJ, 
							TipoIdentificacaoChavePixEnum.CHAVE_ALEATORIA};	
		return montarCombobox(array, Obrigatorio.SIM);
	}


	public List<SelectItem> getComboboxFinalidadePixEnum() {
		 if(comboboxFinalidadePixEnum == null) {
			 comboboxFinalidadePixEnum = montarCombobox(FinalidadePixEnum.values(), Obrigatorio.SIM);
		 }
		return comboboxFinalidadePixEnum;
	}

	public List<SelectItem> getTipoConsultaComboSituacaoNota() {
		if (tipoConsultaComboSituacaoNota == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboSituacaoNota = montarCombobox(SalaBlackboardSituacaoNotaEnum.values(), Obrigatorio.NAO);
		}
		return tipoConsultaComboSituacaoNota;
	}

	public List<SelectItem> getTipoConsultaComboOperacaoImportacaoProfessor() {
		if (tipoConsultaComboOperacaoImportacaoProfessor == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboOperacaoImportacaoProfessor = montarCombobox(OperacaoImportacaoSalaBlackboardEnum.values(), Obrigatorio.SIM);
		}
		return tipoConsultaComboOperacaoImportacaoProfessor;
	}

	public List<SelectItem> getTipoConsultaComboOperacaoImportacaoFacilitador() {
		if (tipoConsultaComboOperacaoImportacaoFacilitador == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboOperacaoImportacaoFacilitador = montarCombobox(OperacaoImportacaoSalaBlackboardEnum.values(), Obrigatorio.SIM);
		}
		return tipoConsultaComboOperacaoImportacaoFacilitador;
	}

	public List<SelectItem> getTipoConsultaComboOperacaoImportacaoSupervisor() {
		if (tipoConsultaComboOperacaoImportacaoSupervisor == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboOperacaoImportacaoSupervisor = montarCombobox(OperacaoImportacaoSalaBlackboardEnum.values(), Obrigatorio.SIM);
		}
		return tipoConsultaComboOperacaoImportacaoSupervisor;
	}

	public List<SelectItem> getTipoConsultaComboOperacaoImportacaoNota() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("INCLUIR", UteisJSF.internacionalizar("enum_OperacaoImportacaoSalaBlackboardEnum_INCLUIR")));
		itens.add(new SelectItem("NENHUMA", UteisJSF.internacionalizar("enum_OperacaoImportacaoSalaBlackboardEnum_NENHUMA")));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSituacaoAproveitamento() {
		if (tipoConsultaComboSituacaoAproveitamento == null || !getLocale().equals(localeCombobox)) {
			localeCombobox = getLocale();
			tipoConsultaComboSituacaoAproveitamento = montarCombobox(TipoConsultaComboSituacaoAproveitamentoEnum.values(), Obrigatorio.NAO);
		}
		return tipoConsultaComboSituacaoAproveitamento;
	}
	
	public List<SelectItem> getComboboxPeriodoLetivoEnum() {
		if (comboboxPeriodoLetivoEnum == null) {
			comboboxPeriodoLetivoEnum = new ArrayList<>();
			comboboxPeriodoLetivoEnum.add(new SelectItem(0, ""));
			comboboxPeriodoLetivoEnum.add(new SelectItem(1, "1"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(2, "2"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(3, "3"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(4, "4"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(5, "5"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(6, "6"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(7, "7"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(8, "8"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(9, "9"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(10, "10"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(11, "11"));
			comboboxPeriodoLetivoEnum.add(new SelectItem(12, "12"));
		}
		return comboboxPeriodoLetivoEnum;
	}
}
