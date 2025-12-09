package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PerguntaChecklistOrigemVO;
import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemMotivosPadroesEstagioVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemVO;
import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.enumeradores.OperacaoDeVinculoEstagioEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.SituacaoQuestionarioRespostaOrigemEnum;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.estagio.DashboardEstagioVO;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.TotalizadorEstagioSituacaoVO;
import negocio.comuns.estagio.enumeradores.RegrasSubstituicaoGrupoPessoaItemEnum;
import negocio.comuns.estagio.enumeradores.SituacaoAdicionalEstagioEnum;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;
import negocio.comuns.estagio.enumeradores.TipoConsultaComboSituacaoAproveitamentoEnum;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.EstagioInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class Estagio extends ControleAcesso implements EstagioInterfaceFacade {

	private static final long serialVersionUID = -1860619785009717337L;
	protected static String idEntidade = "Estagio";
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(EstagioVO obj) throws Exception {
		if(obj.getSituacaoEstagioEnum().isAguardandoAssinatura()) {
			obj.setCargaHorariaDeferida(obj.getCargaHoraria());	
		}
		obj.setCpfResponsavelConcedente(obj.getCpfResponsavelConcedente().trim());
		obj.setCpfBeneficiario(obj.getCpfBeneficiario().trim());
		obj.setEmailBeneficiario(obj.getEmailBeneficiario().trim());
		obj.setEmailResponsavelConcedente(obj.getEmailResponsavelConcedente().trim());
		obj.setDocenteResponsavelEstagio(obj.getGradeCurricularEstagioVO().getDocenteResponsavelEstagio());
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getGradeCurricularEstagioVO()), UteisJSF.internacionalizar("msg_Estagio_gradeCurricularEstagio"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCargaHoraria()), "O campo Carga Horária deve ser informado");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCargaHorariaDeferida()), "O campo Carga Horária Deferida deve ser informado");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoConcedenteVO()), UteisJSF.internacionalizar("msg_Estagio_tipoConcedente"));		
		Uteis.checkState(obj.getTipoConcedenteVO().isCnpjObrigatorio() && !Uteis.isAtributoPreenchido(obj.getCnpj()), UteisJSF.internacionalizar("msg_Estagio_cnpj"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getConcedente()), UteisJSF.internacionalizar("msg_Estagio_concedente"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTelefone()), UteisJSF.internacionalizar("msg_Estagio_telefone"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCep()), UteisJSF.internacionalizar("msg_Estagio_cep"));
		if(obj.getTipoConcedenteVO().getPermitirCadastroConcedente() && obj.getTipoConcedenteVO().getCodigoMECObrigatorio()) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCodigoEscolaMEC()), UteisJSF.internacionalizar("O campo Código da Escola no MEC deve ser informado."));
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getEndereco()), UteisJSF.internacionalizar("msg_Estagio_endereco"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNumero()), UteisJSF.internacionalizar("msg_Estagio_numero"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getBairro()), UteisJSF.internacionalizar("msg_Estagio_bairro"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCidade()), UteisJSF.internacionalizar("msg_Estagio_cidade"));		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getResponsavelConcedente()), UteisJSF.internacionalizar("msg_estagio_responsavelConcedente"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCpfResponsavelConcedente()), UteisJSF.internacionalizar("msg_estagio_cpfResponsavelConcedente"));
		Uteis.checkState(!Uteis.validaCPF(obj.getCpfResponsavelConcedente()), UteisJSF.internacionalizar("msg_estagio_cpfResponsavelConcedente_invalido"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getEmailResponsavelConcedente()), UteisJSF.internacionalizar("msg_estagio_emailResponsavelConcedente"));
		Uteis.checkState(!Uteis.getValidaEmail(obj.getEmailResponsavelConcedente()), UteisJSF.internacionalizar("msg_estagio_emailResponsavelConcedente_invalido"));
		Uteis.checkState(obj.getEmailResponsavelConcedente().contains("univesp.br"), UteisJSF.internacionalizar("msg_estagio_emailResponsavelConcedente_invalido_univesp"));
		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTelefoneResponsavelConcedente()), UteisJSF.internacionalizar("msg_estagio_telefoneResponsavelConcedente"));
		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getEmailResponsavelConcedente()) && !Uteis.getValidaEmail(obj.getEmailResponsavelConcedente()), UteisJSF.internacionalizar("msg_Estagio_emailValido"));
		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getEmailResponsavelConcedente()) && !Uteis.getValidaEmailInternetAddress(obj.getEmailResponsavelConcedente()), UteisJSF.internacionalizar("msg_Estagio_emailValido"));
		Integer cargaHorarioExistente = consultarCargaHorariaEstagioFracionada(obj.getMatriculaVO().getMatricula(), obj.getGradeCurricularEstagioVO().getCodigo(), obj.getCodigo());
		Uteis.checkState(cargaHorarioExistente + obj.getCargaHoraria() > obj.getGradeCurricularEstagioVO().getCargaHorarioObrigatorio(), "A carga Horária informada não pode ser maior que " + (obj.getGradeCurricularEstagioVO().getCargaHorarioObrigatorio() - cargaHorarioExistente) + " horas.");

		if(obj.getGradeCurricularEstagioVO().getInformarSupervisorEstagio()) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNomeSupervisor()), UteisJSF.internacionalizar("O campo Nome Supervisor deve ser informado."));
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCpfSupervisor()), UteisJSF.internacionalizar("O campo CPF Supervisor deve ser informado"));
			Uteis.checkState(!Uteis.validaCPF(obj.getCpfSupervisor()), UteisJSF.internacionalizar("O CPF do Supervisor é inválido."));
		}
		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getRgBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_rg"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCpfBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_cpf"));
		Uteis.checkState(!Uteis.validaCPF(obj.getCpfBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_cpf_invalido"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNomeBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_nome"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCepBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_cep"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getEnderecoBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_endereco"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCidadeBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_cidade"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getEstadoBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_estado"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNumeroBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_numero"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSetorBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_setor"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTelefoneBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_telefone"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getEmailBeneficiario()), UteisJSF.internacionalizar("msg_Beneficiario_email"));
		Uteis.checkState(obj.getCpfBeneficiario().equals(obj.getCpfResponsavelConcedente()), UteisJSF.internacionalizar("msg_BeneficiarioConcedente_cpf"));
		 
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(EstagioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			validarDados(obj);
			/**
			 * if abaixo comentado pelo seguinte cenario, ao criar um estagio que foi
			 * informado um CONCEDENTE EXISTENTE, mas esse concedente não esta informado o
			 * "Código da Escola no MEC", ao criar esse estagio o aluno esta informando o
			 * "Código da Escola no MEC" mas não esta alterando essa informação no cadastro
			 * de concedente
			 * 
			 * @chamado 42103
			 * @author Felipi Alves
			 */
//			if(!Uteis.isAtributoPreenchido(obj.getConcedenteVO())) {
			if(obj.getTipoConcedenteVO().getPermitirCadastroConcedente()) {
				obj.carregarConcedenteVO();
				getFacadeFactory().getConcedenteFacade().persistir(obj.getConcedenteVO(), verificarAcesso, usuarioVO);
			}
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}	

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final EstagioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			incluir((usuarioVO != null && usuarioVO.getIsApresentarVisaoAluno()) ? "EstagioAluno" : getIdEntidade(), verificarAcesso, usuarioVO);
			obj.setAno(Uteis.getAnoDataAtual());
			obj.setSemestre(Uteis.getSemestreAtual());
			incluir(obj, "estagio", new AtributoPersistencia()
					.add("matricula", obj.getMatriculaVO().getMatricula())
					.add("disciplina", obj.getDisciplina())
					.add("concedente", obj.getConcedenteVO())
					.add("gradeCurricularEstagio", obj.getGradeCurricularEstagioVO())
					.add("documentoAssinado", obj.getDocumentoAssinadoVO())
					.add("tipoEstagio", obj.getTipoEstagio())
					.add("situacaoEstagioEnum", obj.getSituacaoEstagioEnum())
					.add("situacaoAdicionalEstagioEnum", obj.getSituacaoAdicionalEstagioEnum())
					.add("ano", obj.getAno())
					.add("semestre", obj.getSemestre())
					.add("cargaHoraria", obj.getCargaHoraria())
					.add("cargaHorariaDeferida", obj.getCargaHorariaDeferida())
					.add("cargaHorariaDiaria", obj.getCargaHorariaDiaria())
					.add("dataInicioVigencia", obj.getDataInicioVigencia())
					.add("dataFinalVigencia", obj.getDataFinalVigencia())
					.add("dataInicioRenovacaoVigencia", obj.getDataInicioRenovacaoVigencia())
					.add("dataFinalRenovacaoVigencia", obj.getDataFinalRenovacaoVigencia())
					.add("tipoConcedente", obj.getTipoConcedenteVO())
					.add("cnpj", obj.getCnpj()) 
					.add("nomeconcedente", obj.getConcedente()) 
					.add("telefone", obj.getTelefone()) 
					.add("cep", obj.getCep()) 
					.add("endereco", obj.getEndereco()) 
					.add("numero", obj.getNumero()) 
					.add("bairro", obj.getBairro()) 
					.add("cidade", obj.getCidade())
					.add("responsavelConcedente", obj.getResponsavelConcedente()) 
					.add("cpfResponsavelConcedente", obj.getCpfResponsavelConcedente()) 
					.add("emailResponsavelConcedente", obj.getEmailResponsavelConcedente()) 
					.add("telefoneResponsavelConcedente", obj.getTelefoneResponsavelConcedente())
					.add("tipoSituacaoAproveitamentoEnum", obj.getTipoSituacaoAproveitamentoEnum().name())
					
					.add("rgBeneficiario", obj.getRgBeneficiario())
					.add("cpfBeneficiario", obj.getCpfBeneficiario())
					.add("nomeBeneficiario", obj.getNomeBeneficiario())
					.add("emailBeneficiario", obj.getEmailBeneficiario())
					.add("telefoneBeneficiario", obj.getTelefoneBeneficiario())
					.add("cepBeneficiario", obj.getCepBeneficiario())
					.add("cidadeBeneficiario", obj.getCidadeBeneficiario())
					.add("estadoBeneficiario", obj.getEstadoBeneficiario())
					.add("numeroBeneficiario", obj.getNumeroBeneficiario())
					.add("enderecoBeneficiario", obj.getEnderecoBeneficiario())
					.add("complementoBeneficiario", obj.getComplementoBeneficiario())
					.add("setorBeneficiario", obj.getSetorBeneficiario())
					.add("docenteResponsavelEstagio", Uteis.isAtributoPreenchido(obj.getDocenteResponsavelEstagio()) ? obj.getDocenteResponsavelEstagio().getCodigo() : null)
					.add("nomeSupervisor", obj.getNomeSupervisor())
					.add("cpfSupervisor", obj.getCpfSupervisor()),
					
					usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final EstagioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar((usuarioVO != null && usuarioVO.getIsApresentarVisaoAluno()) ?  "EstagioAluno" : getIdEntidade(), verificarAcesso, usuarioVO);
			alterar(obj, "estagio", new AtributoPersistencia()
					.add("matricula", obj.getMatriculaVO().getMatricula())
					.add("disciplina", obj.getDisciplina())
					.add("concedente", obj.getConcedenteVO())
					.add("gradeCurricularEstagio", obj.getGradeCurricularEstagioVO())
					.add("documentoAssinado", obj.getDocumentoAssinadoVO())
					.add("tipoEstagio", obj.getTipoEstagio())
					.add("situacaoEstagioEnum", obj.getSituacaoEstagioEnum())
					.add("situacaoAdicionalEstagioEnum", obj.getSituacaoAdicionalEstagioEnum())
					.add("ano", obj.getAno())
					.add("semestre", obj.getSemestre())
					.add("cargaHoraria", obj.getCargaHoraria())
					.add("cargaHorariaDeferida", obj.getCargaHorariaDeferida())
					.add("cargaHorariaDiaria", obj.getCargaHorariaDiaria())
					.add("dataInicioVigencia", obj.getDataInicioVigencia())
					.add("dataFinalVigencia", obj.getDataFinalVigencia())
					.add("dataInicioRenovacaoVigencia", obj.getDataInicioRenovacaoVigencia())
					.add("dataFinalRenovacaoVigencia", obj.getDataFinalRenovacaoVigencia())
					.add("tipoConcedente", obj.getTipoConcedenteVO())
					.add("cnpj", obj.getCnpj()) 
					.add("nomeconcedente", obj.getConcedente()) 
					.add("telefone", obj.getTelefone()) 
					.add("cep", obj.getCep()) 
					.add("endereco", obj.getEndereco()) 
					.add("numero", obj.getNumero()) 
					.add("bairro", obj.getBairro()) 
					.add("cidade", obj.getCidade())
					.add("responsavelConcedente", obj.getResponsavelConcedente()) 
					.add("cpfResponsavelConcedente", obj.getCpfResponsavelConcedente()) 
					.add("emailResponsavelConcedente", obj.getEmailResponsavelConcedente()) 
					.add("telefoneResponsavelConcedente", obj.getTelefoneResponsavelConcedente())
					.add("tipoSituacaoAproveitamentoEnum", obj.getTipoSituacaoAproveitamentoEnum().name())
					
					.add("rgBeneficiario", obj.getRgBeneficiario())
					.add("cpfBeneficiario", obj.getCpfBeneficiario())
					.add("nomeBeneficiario", obj.getNomeBeneficiario())
					.add("emailBeneficiario", obj.getEmailBeneficiario())
					.add("telefoneBeneficiario", obj.getTelefoneBeneficiario())
					.add("cepBeneficiario", obj.getCepBeneficiario())
					.add("cidadeBeneficiario", obj.getCidadeBeneficiario())
					.add("estadoBeneficiario", obj.getEstadoBeneficiario())
					.add("numeroBeneficiario", obj.getNumeroBeneficiario())
					.add("enderecoBeneficiario", obj.getEnderecoBeneficiario())
					.add("complementoBeneficiario", obj.getComplementoBeneficiario())
					.add("setorBeneficiario", obj.getSetorBeneficiario())
					.add("docenteResponsavelEstagio", Uteis.isAtributoPreenchido(obj.getDocenteResponsavelEstagio()) ? obj.getDocenteResponsavelEstagio().getCodigo() : null)
					.add("nomeSupervisor", obj.getNomeSupervisor())
					.add("cpfSupervisor", obj.getCpfSupervisor()),
					
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarEstagioIndeferimento(EstagioVO obj, UsuarioVO usuarioVO) throws Exception {
		alterar(obj, "estagio", new AtributoPersistencia()
				.add("motivo", obj.getMotivo())
				.add("situacaoEstagioEnum", obj.getSituacaoEstagioEnum()) 
				.add("situacaoAdicionalEstagioEnum", obj.getSituacaoAdicionalEstagioEnum())
				.add("dataIndeferimento", obj.getDataIndeferimento())
				.add("responsavelIndeferimento", obj.getResponsavelIndeferimento())
				.add("dataDeferimento", null)
				.add("responsavelDeferimento", null)
				,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarEstagioAguardandoAssinatura(EstagioVO obj, UsuarioVO usuarioVO) throws Exception {
		if(obj.getTipoEstagio().isTipoObrigatorio()) {
			getFacadeFactory().getDocumentoAssinadoFacade().excluir(obj.getDocumentoAssinadoVO(), false, usuarioVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO));
		} 	
		alterar(obj, "estagio", new AtributoPersistencia()
				.add("motivo", obj.getMotivo())
				.add("situacaoEstagioEnum", obj.getSituacaoEstagioEnum()) 
				.add("situacaoAdicionalEstagioEnum", obj.getSituacaoAdicionalEstagioEnum())
				.add("dataEnvioAssinaturaPendente", obj.getDataEnvioAssinaturaPendente())
				.add("grupoPessoaItem", obj.getGrupoPessoaItemVO())
				.add("dataIndeferimento", obj.getDataIndeferimento())
				.add("responsavelIndeferimento", obj.getResponsavelIndeferimento())
				.add("dataDeferimento", obj.getDataDeferimento())
				.add("responsavelDeferimento", obj.getResponsavelDeferimento())
				.add("dataEnvioAnalise", obj.getDataEnvioAnalise()) 
				.add("dataLimiteAnalise", obj.getDataLimiteAnalise())
				.add("dataEnvioCorrecao", obj.getDataEnvioCorrecao())
				.add("dataLimiteCorrecao", obj.getDataLimiteCorrecao())
				,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCampoSqlMensagem(EstagioVO estagio, String sqlmensagem, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(estagio, "estagio", new AtributoPersistencia().add("sqlmensagem", sqlmensagem),new AtributoPersistencia().add("codigo", estagio.getCodigo()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(EstagioVO obj,  boolean verificarAcesso, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM Estagio WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getDocumentoAssinadoFacade().excluir(obj.getDocumentoAssinadoVO(), false, usuarioVO, configGeralSistema);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarDistribuicaoGrupoPessoaItemPorInativacao(GrupoPessoaItemVO objInativado,  UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT estagio.codigo, estagio.grupopessoaitem,  grupopessoa.codigo as grupopessoa  from estagio inner join grupopessoaitem on grupopessoaitem.codigo = estagio.grupopessoaitem inner join grupopessoa on grupopessoa.codigo = grupopessoaitem.grupopessoa   where grupopessoaitem.pessoa = ? and situacaoestagioenum in ('EM_ANALISE', 'EM_CORRECAO') ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), objInativado.getPessoaVO().getCodigo());
		while (rs.next()) {
			EstagioVO obj = new EstagioVO();
			obj.setCodigo(rs.getInt("codigo"));			
			objInativado.setCodigo(rs.getInt("grupopessoaitem"));
			objInativado.getGrupoPessoaVO().setCodigo(rs.getInt("grupopessoa"));
			obj.setGrupoPessoaItemVO(getFacadeFactory().getGrupoPessoaItemFacade().buscarGrupoPessoaItemDistribuicaoQuantitativoPorGrupoPessoaItemInativo(objInativado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getGrupoPessoaItemVO()), "Não foi encontrado nenhum Avaliador/Facilitador disponível no momento para analisar o Formulário. Por favor tentar novamente daqui alguns minutos.");
			alterar(obj, "estagio", new AtributoPersistencia().add("grupopessoaitem", obj.getGrupoPessoaItemVO()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
			obj = null;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarConfirmacaoEstagioAssinaturaPendente(EstagioVO obj,  boolean verificarAcesso, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception {
		alterar((usuarioVO != null && usuarioVO.getIsApresentarVisaoAluno()) ?  "EstagioAluno" : getIdEntidade(), verificarAcesso, usuarioVO);
		boolean rollBack = !Uteis.isAtributoPreenchido(obj);
		try {
			if(!Uteis.isAtributoPreenchido(obj)) {
				persistir(obj, false, usuarioVO);
			}else{
                alterar(obj, false, usuarioVO);
            }
			String caminhoEstagio = realizarVisualizacaoTermoEstagio(obj, configGeralSistema, usuarioVO);
			getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorEstagio(obj, TipoOrigemDocumentoAssinadoEnum.TERMO_ESTAGIO_OBRIGATORIO, caminhoEstagio, configEstagio, configGeralSistema, usuarioVO);
			obj.setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.ASSINATURA_PENDENTE);
			obj.setDataEnvioAssinaturaPendente(new Date());
			alterar(obj, "estagio", new AtributoPersistencia()
					.add("documentoAssinado", obj.getDocumentoAssinadoVO())
					.add("situacaoAdicionalEstagioEnum", obj.getSituacaoAdicionalEstagioEnum())
					.add("dataEnvioAssinaturaPendente", obj.getDataEnvioAssinaturaPendente()),
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		} catch (Exception e) {
			if(rollBack) {
				obj.setCodigo(0);
				obj.setNovoObj(true);
				obj.setDocumentoAssinadoVO(new DocumentoAssinadoVO());
			}
			obj.setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.PENDENTE_SOLICITACAO_ASSINATURA);
			obj.setDataEnvioAssinaturaPendente(null);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoEstagioRealizando(DocumentoAssinadoVO documentoAssinado, UsuarioVO usuario) throws Exception {
		EstagioVO obj = null;
		try {
			obj = consultarPorDocumentoAssinador(documentoAssinado.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if(Uteis.isAtributoPreenchido(obj) && obj.getSituacaoEstagioEnum().isAguardandoAssinatura()) {
//				getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSalaAulaBlackboardPorGradeCurricularEstagio(obj, usuario);
				obj.setSituacaoEstagioEnum(SituacaoEstagioEnum.REALIZANDO);
				obj.setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.AGUARDANDO_RELATORIO_FINAL);
				alterar(obj, "estagio", new AtributoPersistencia()
						.add("situacaoEstagioEnum", obj.getSituacaoEstagioEnum()) 
						.add("situacaoAdicionalEstagioEnum", obj.getSituacaoAdicionalEstagioEnum()) 
						.add("sqlmensagem", null), 
						new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);	
			}	
		} catch (Exception e) {
			if(Uteis.isAtributoPreenchido(obj)) {
				atualizarCampoSqlMensagem(obj, "realizarAlteracaoEstagioRealizando-"+e.getMessage(), usuario);	
			}
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarConfirmacaoEstagioEmAnalise(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuario) throws Exception {
		QuestionarioRespostaOrigemVO qroRollBack = null;
		try {
			if(obj.getSituacaoEstagioEnum().isEmCorrecao() && obj.getQuestionarioRespostaOrigemUltimaVersao().getSituacaoQuestionarioRespostaOrigemEnum().isEmCorrecao()) {
				qroRollBack = (QuestionarioRespostaOrigemVO) Uteis.clonar(obj.getQuestionarioRespostaOrigemUltimaVersao());
				QuestionarioRespostaOrigemVO qro = obj.getQuestionarioRespostaOrigemUltimaVersao().getClonePorEstagio();
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().executarClonePerguntaRespostaOrigemArquivo(qro, configGeralSistema, usuario);
				obj.setQuestionarioRespostaOrigemUltimaVersao(qro);
			}
			if(obj.getSituacaoEstagioEnum().isEmCorrecao() && obj.getQuestionarioRespostaOrigemUltimaVersao().getSituacaoQuestionarioRespostaOrigemEnum().isEmPreenchimento()) {
				obj.getQuestionarioRespostaOrigemUltimaVersao().setDataEnvioAnalise(null);
				obj.getQuestionarioRespostaOrigemUltimaVersao().setDataLimiteAnalise(null);
				obj.getQuestionarioRespostaOrigemUltimaVersao().setDataEnvioCorrecao(null);
				obj.getQuestionarioRespostaOrigemUltimaVersao().setDataLimiteCorrecao(null);
				obj.getQuestionarioRespostaOrigemUltimaVersao().setObservacaoFinal(null);
				obj.getQuestionarioRespostaOrigemUltimaVersao().setMotivo(null);
				obj.getQuestionarioRespostaOrigemUltimaVersao().getPerguntaRespostaOrigemVOs().stream().forEach(p-> p.setListaPerguntaChecklistOrigem(new ArrayList<>()));
			}
			if(!Uteis.isAtributoPreenchido(obj.getGrupoPessoaItemVO())) {
				obj.setGrupoPessoaItemVO(getFacadeFactory().getGrupoPessoaItemFacade().buscarGrupoPessoaItemDistribuicaoQuantitativoPorEstagio(obj.getTipoEstagio(), obj.getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
				Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getGrupoPessoaItemVO()), "Não foi encontrado nenhum Avaliador disponível no momento para analisar o Formulário. Por favor tentar novamente daqui alguns minuto.");	
			}
			Integer qtdDiasAnalise = 0;
			if(obj.getTipoEstagio() == TipoEstagioEnum.OBRIGATORIO) {
				qtdDiasAnalise =  configEstagio.getQtdDiasMaximoParaAnaliseRelatoriofinal();
			}else if (obj.getTipoEstagio() == TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO) {
				qtdDiasAnalise = configEstagio.getQtdDiasMaximoParaRespostaAnaliseAproveitamento();
			} else if(obj.getTipoEstagio() == TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA) {
				qtdDiasAnalise = configEstagio.getQtdDiasMaximoParaRespostaAnaliseEquivalencia();
			}
			Date dataAnalise = getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(new Date(), qtdDiasAnalise, 0, false, false, ConsiderarFeriadoEnum.ACADEMICO);
			obj.getQuestionarioRespostaOrigemUltimaVersao().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.EM_ANALISE);
			obj.getQuestionarioRespostaOrigemUltimaVersao().setDataEnvioAnalise(new Date());
			obj.getQuestionarioRespostaOrigemUltimaVersao().setDataLimiteAnalise(dataAnalise);
			getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().persistir(obj.getQuestionarioRespostaOrigemUltimaVersao(), usuario);			
			obj.setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_ANALISE);
			obj.setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.AGUARDANDO_RELATORIO_FINAL);
			obj.setDataEnvioAnalise(new Date());
			obj.setDataLimiteAnalise(dataAnalise);
			alterar(obj, "estagio", new AtributoPersistencia()
					.add("grupoPessoaItem", obj.getGrupoPessoaItemVO()) 
					.add("situacaoEstagioEnum", obj.getSituacaoEstagioEnum()) 
					.add("situacaoAdicionalEstagioEnum", obj.getSituacaoAdicionalEstagioEnum()) 
					.add("dataEnvioAnalise", obj.getDataEnvioAnalise()) 
					.add("dataLimiteAnalise", obj.getDataLimiteAnalise())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
			
//			if(obj.getTipoEstagio() == TipoEstagioEnum.OBRIGATORIO) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAnaliseEstagioObrigatorioRelatorioFinal(obj, configEstagio, usuario);
//			}else if (obj.getTipoEstagio() == TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioAvisoPrazoAnaliseAvaliador(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_AVISO_PRAZO_ANALISE_APROVEITAMENTO, obj, configEstagio, usuario);
//			} else if(obj.getTipoEstagio() == TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioAvisoPrazoAnaliseAvaliador(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_AVISO_PRAZO_ANALISE_EQUIVALENCIA, obj, configEstagio, usuario);
//			}
			
			
		} catch (Exception e) {		
			if(Uteis.isAtributoPreenchido(qroRollBack)) {
				obj.setQuestionarioRespostaOrigemUltimaVersao(qroRollBack);
			}
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarConfirmacaoEstagioEmAnaliseAprovEquiv(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuario) throws Exception {		
		boolean rollBack = !Uteis.isAtributoPreenchido(obj);
		try {
			if(!Uteis.isAtributoPreenchido(obj)) {
				Integer cargaHorarioExistente = consultarCargaHorariaEstagioFracionada(obj.getMatriculaVO().getMatricula(), obj.getGradeCurricularEstagioVO().getCodigo(), obj.getCodigo());
				Uteis.checkState(cargaHorarioExistente + obj.getCargaHoraria() > obj.getGradeCurricularEstagioVO().getCargaHorarioObrigatorio(), "A carga Horária informada não pode ser maior que " + (obj.getGradeCurricularEstagioVO().getCargaHorarioObrigatorio() - cargaHorarioExistente) + " horas.");
				obj.setCargaHorariaDeferida(obj.getCargaHoraria());
				incluir(obj, false, usuario);	
			}
			realizarConfirmacaoEstagioEmAnalise(obj, configEstagio, configGeralSistema, usuario);
		} catch (Exception e) {		
			if(rollBack) {
				obj.setNovoObj(Boolean.TRUE);
				obj.setCodigo(0);	
			}			
			throw e;
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarConfirmacaoEstagioEmCorrecao(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		try {
			Uteis.checkState(!obj
			.getQuestionarioRespostaOrigemUltimaVersao()
			.getPerguntaRespostaOrigemVOs()
			.stream()
			.flatMap(p-> p.getListaPerguntaChecklistOrigem().stream()).anyMatch(p-> !p.isChecklist()), "Para solicitar a correção do Formulário deve existir pelo menos uma opção do checklist desmarcada.");
			
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getQuestionarioRespostaOrigemUltimaVersao().getObservacaoFinal()), "O campo Consideração Final deve ser informado.");
			Date dataCorrecao = getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(new Date(), configEstagio.getQtdDiasMaximoParaCorrecaoRelatorioFinal(), 0, false, false, ConsiderarFeriadoEnum.ACADEMICO);
			obj.getQuestionarioRespostaOrigemUltimaVersao().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.EM_CORRECAO);
			obj.getQuestionarioRespostaOrigemUltimaVersao().setDataEnvioCorrecao(new Date());
			obj.getQuestionarioRespostaOrigemUltimaVersao().setDataLimiteCorrecao(dataCorrecao);
			getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().persistir(obj.getQuestionarioRespostaOrigemUltimaVersao(), usuario);
			obj.setSituacaoEstagioEnum(SituacaoEstagioEnum.EM_CORRECAO);
			obj.setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.AGUARDANDO_RELATORIO_FINAL);
			obj.setDataEnvioCorrecao(new Date());
			obj.setDataLimiteCorrecao(dataCorrecao);
			alterar(obj, "estagio", new AtributoPersistencia()
					.add("situacaoEstagioEnum", obj.getSituacaoEstagioEnum()) 
					.add("situacaoAdicionalEstagioEnum", obj.getSituacaoAdicionalEstagioEnum()) 
					.add("dataEnvioCorrecao", obj.getDataEnvioCorrecao())
					.add("dataLimiteCorrecao", obj.getDataLimiteCorrecao())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);	
//			if (obj.getTipoEstagio().isTipoObrigatorioAproveitamento()) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioAproveitamentoSolicitacaoCorrecaoAluno(obj, configEstagio, usuario);
//			} else if(obj.getTipoEstagio().isTipoObrigatorioEquivalencia()) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioEquivalenciaSolicitacaoCorrecaoAluno(obj, configEstagio, usuario);
//			} else if(obj.getTipoEstagio().isTipoObrigatorio()) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioSolicitacaoCorrecaoAluno(obj, configEstagio, usuario);
//			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarConfirmacaoEstagioIndeferido(EstagioVO obj, SituacaoAdicionalEstagioEnum situacaoAdicionalEstagioEnum, Boolean indeferirDocumentoAssinado , UsuarioVO usuario) throws Exception {
		try {
			if(Uteis.isAtributoPreenchido(obj)) {
				Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getMotivo()), "O campo Motivo deve ser informado.");
				if(Uteis.isAtributoPreenchido(obj.getQuestionarioRespostaOrigemUltimaVersao())) {
					obj.getQuestionarioRespostaOrigemUltimaVersao().setMotivo(obj.getMotivo());
					obj.getQuestionarioRespostaOrigemUltimaVersao().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.INDEFERIDO);
					getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().atualizarSituacaoQuestionarioRespostaOrigemVO(obj.getQuestionarioRespostaOrigemUltimaVersao(), usuario);
				}
				obj.setSituacaoEstagioEnum(SituacaoEstagioEnum.INDEFERIDO);
				obj.setSituacaoAdicionalEstagioEnum(situacaoAdicionalEstagioEnum);
				obj.setResponsavelIndeferimento(usuario);
				obj.setDataIndeferimento(new Date());
				atualizarEstagioIndeferimento(obj, usuario);
//				if (obj.getTipoEstagio().isTipoObrigatorioAproveitamento()) {
//					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioAproveitamentoIndeferimento(obj, usuario);
//				} else if(obj.getTipoEstagio().isTipoObrigatorioEquivalencia()) {
//					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioEquivalenciaIndeferimento(obj, usuario);
//				} else if(obj.getTipoEstagio().isTipoObrigatorio()) {
//					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioIndeferimento(obj, usuario);
//				} 
			}
			if(Uteis.isAtributoPreenchido(obj.getDocumentoAssinadoVO()) && indeferirDocumentoAssinado) {
				if (obj.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().isEmpty()) {
					obj.getDocumentoAssinadoVO().setListaDocumentoAssinadoPessoa(getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorDocumentoAssinado(obj.getDocumentoAssinadoVO(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
				}
				if (obj.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorCertisign() || obj.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorImprensaOficial()) {
					ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(obj.getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), false, usuario);
					if (obj.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(p -> p.getSituacaoDocumentoAssinadoPessoaEnum().isPendente())) {
						getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarSituacaoPendenteDocumentoAssinadoPessoaParaRejeitado(obj.getDocumentoAssinadoVO(), obj.getMotivo(), usuario);
					}
					obj.getDocumentoAssinadoVO().setDocumentoAssinadoInvalido(true);
					obj.getDocumentoAssinadoVO().setMotivoDocumentoAssinadoInvalido(obj.getMotivo());
					getFacadeFactory().getDocumentoAssinadoFacade().realizarBloqueioDocument(obj.getDocumentoAssinadoVO(), configGedVO, usuario);
					getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(obj.getDocumentoAssinadoVO().getCodigo(), obj.getDocumentoAssinadoVO().isDocumentoAssinadoInvalido(), obj.getDocumentoAssinadoVO().getMotivoDocumentoAssinadoInvalido(), usuario);
				}
				if (obj.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
					ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(obj.getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), false, usuario);
					if (obj.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(p -> p.getSituacaoDocumentoAssinadoPessoaEnum().isPendente())) {
						getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarSituacaoPendenteDocumentoAssinadoPessoaParaRejeitado(obj.getDocumentoAssinadoVO(), obj.getMotivo(), usuario);
					}
					obj.getDocumentoAssinadoVO().setDocumentoAssinadoInvalido(true);
					obj.getDocumentoAssinadoVO().setMotivoDocumentoAssinadoInvalido(obj.getMotivo());
					getFacadeFactory().getDocumentoAssinadoFacade().realizarBloqueioDocumentTechCert(obj.getDocumentoAssinadoVO(), configGedVO, usuario);
					getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(obj.getDocumentoAssinadoVO().getCodigo(), obj.getDocumentoAssinadoVO().isDocumentoAssinadoInvalido(), obj.getDocumentoAssinadoVO().getMotivoDocumentoAssinadoInvalido(), usuario);
				} 	
			}		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarConfirmacaoEstagioDeferido(EstagioVO obj,ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCargaHorariaDeferida()), "O campo Horas de Estágio Autoriado Pelo Avaliador deve ser informada.");
			Uteis.checkState(obj.getCargaHorariaDeferida() > obj.getCargaHoraria(), "O campo Horas de Estágio Autoriado Pelo Avaliador não pode ser maior que o campo Horas de Estágio Informada Pelo Aluno.");
			if(Uteis.isAtributoPreenchido(obj.getQuestionarioRespostaOrigemUltimaVersao())) {
				Uteis.checkState(obj
						.getQuestionarioRespostaOrigemUltimaVersao()
						.getPerguntaRespostaOrigemVOs()
						.stream()
						.flatMap(p-> p.getListaPerguntaChecklistOrigem().stream()).anyMatch(p-> !p.isChecklist()), "Para solicitar o deferimento todas as opções de checklist deve estar marcadas.");
				obj.getQuestionarioRespostaOrigemUltimaVersao().setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.DEFERIDO);
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().persistir(obj.getQuestionarioRespostaOrigemUltimaVersao(), usuario);
			}
			obj.setSituacaoEstagioEnum(SituacaoEstagioEnum.DEFERIDO);
			obj.setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.DEFERIDO);
			obj.setResponsavelDeferimento(usuario);
			obj.setDataDeferimento(new Date());
			alterar(obj, "estagio", new AtributoPersistencia()
					.add("cargaHorariaDeferida", obj.getCargaHorariaDeferida()) 
					.add("situacaoEstagioEnum", obj.getSituacaoEstagioEnum()) 
					.add("situacaoAdicionalEstagioEnum", obj.getSituacaoAdicionalEstagioEnum())
					.add("dataDeferimento", obj.getDataDeferimento())
					.add("responsavelDeferimento", obj.getResponsavelDeferimento())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);	
//			if (obj.getTipoEstagio().isTipoObrigatorioAproveitamento()) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioAproveitamentoDeferimento(obj, configEstagio, usuario);
//			} else if(obj.getTipoEstagio().isTipoObrigatorioEquivalencia()) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioEquivalenciaDeferimento(obj, configEstagio, usuario);
//			} else if(obj.getTipoEstagio().isTipoObrigatorio()) {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioDeferimento(obj, configEstagio, usuario);
//			} else if(obj.getTipoEstagio().isTipoNaoObrigatorio()) {
//				// Nao Obrigatorio
//			}
			
			if(!obj.getTipoEstagio().isTipoNaoObrigatorio()) {
				getFacadeFactory().getHistoricoFacade().atualizarHistoricosEstagioCursandoParaAprovado(obj.getMatriculaVO());
			}
			
			
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarVerificacaoPersitenciaRelatorioFinalEstagio(EstagioVO obj,  ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuario) throws Exception  {
		QuestionarioRespostaOrigemVO qroRollBack = null;
		try {			
			if(obj.getSituacaoEstagioEnum().isEmCorrecao() && obj.getQuestionarioRespostaOrigemUltimaVersao().getSituacaoQuestionarioRespostaOrigemEnum().isEmCorrecao()) {
				qroRollBack = (QuestionarioRespostaOrigemVO) Uteis.clonar(obj.getQuestionarioRespostaOrigemUltimaVersao());
				QuestionarioRespostaOrigemVO qro = obj.getQuestionarioRespostaOrigemUltimaVersao().getClonePorEstagio();
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().executarClonePerguntaRespostaOrigemArquivo(qro, configGeralSistema, usuario);	
				obj.setQuestionarioRespostaOrigemUltimaVersao(qro);
			}
			getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().persistir(obj.getQuestionarioRespostaOrigemUltimaVersao(), usuario);
		} catch (Exception e) {
			if(Uteis.isAtributoPreenchido(qroRollBack)) {
				obj.setQuestionarioRespostaOrigemUltimaVersao(qroRollBack);	
			}
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarEstornoFaseEstagio(EstagioVO estagio,  ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuario) throws Exception {
		try {
			DocumentoAssinadoVO docTemp = null;
			if(estagio.getSituacaoEstagioEnum().isEmAnalise()) {
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().atualizarSituacaoQuestionarioRespostaOrigemVO(estagio.getQuestionarioRespostaOrigemUltimaVersao(), usuario);	
			}else if(estagio.getSituacaoEstagioEnum().isAguardandoAssinatura() ) {
				docTemp = (DocumentoAssinadoVO) Uteis.clonar(estagio.getDocumentoAssinadoVO());
				if(!docTemp.isDocumentoAssinadoInvalido()) {
					estagio.setDocumentoAssinadoVO(new DocumentoAssinadoVO());	
				}
			}
			estagio.setMotivo("");
			estagio.setDataIndeferimento(null);
			estagio.setResponsavelIndeferimento(null);
			estagio.setDataDeferimento(null);
			estagio.setResponsavelDeferimento(null);
			alterar(estagio, "estagio", new AtributoPersistencia()
					.add("documentoAssinado", estagio.getDocumentoAssinadoVO()) 
					.add("situacaoEstagioEnum", estagio.getSituacaoEstagioEnum()) 
					.add("situacaoAdicionalEstagioEnum", estagio.getSituacaoAdicionalEstagioEnum())
					.add("motivo", estagio.getMotivo())
					.add("dataEnvioAssinaturaPendente", estagio.getDataEnvioAssinaturaPendente())
					.add("dataIndeferimento", estagio.getDataIndeferimento())
					.add("responsavelIndeferimento", estagio.getResponsavelIndeferimento())
					.add("dataDeferimento", estagio.getDataDeferimento())
					.add("responsavelDeferimento", estagio.getResponsavelDeferimento())
					,new AtributoPersistencia().add("codigo", estagio.getCodigo()), usuario);
			if(Uteis.isAtributoPreenchido(docTemp) && docTemp.isDocumentoAssinadoInvalido()) {
				estagio.getDocumentoAssinadoVO().setDocumentoAssinadoInvalido(false);
				estagio.getDocumentoAssinadoVO().setMotivoDocumentoAssinadoInvalido("");
				ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(estagio.getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), usuario);
				if (estagio.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarBloqueioDocument(estagio.getDocumentoAssinadoVO(), configGEDVO, usuario);
				}
				if (estagio.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarBloqueioDocumentTechCert(estagio.getDocumentoAssinadoVO(), configGEDVO, usuario);
				}
				getFacadeFactory().getDocumentoAssinadoFacade().realizarEnvioNotificacaoLembreteDocumentoPendenteAssinatura(estagio.getDocumentoAssinadoVO(), configGEDVO);
				getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(estagio.getDocumentoAssinadoVO().getCodigo(), estagio.getDocumentoAssinadoVO().isDocumentoAssinadoInvalido(), estagio.getDocumentoAssinadoVO().getMotivoDocumentoAssinadoInvalido(), usuario);
				getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarSituacaoRejeitadoDocumentoAssinadoPessoaParaPendente(estagio.getDocumentoAssinadoVO(), usuario);
				
			}else if(Uteis.isAtributoPreenchido(docTemp) && !docTemp.isDocumentoAssinadoInvalido()) {
				getFacadeFactory().getDocumentoAssinadoFacade().excluir(docTemp, false, usuario, configGeralSistema);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirSubstituicaoGrupoPessoaItem(GrupoPessoaItemVO grupoPessoaItemAtual, GrupoPessoaItemVO grupoPessoaItemEspecifico, RegrasSubstituicaoGrupoPessoaItemEnum regrasSubstituicaoGrupoPessoaItemEnum, EstagioVO estagioVO, boolean inativarGrupoPessoaItemGrupoParticipante, UsuarioVO usuarioVO) throws Exception{
		Uteis.checkState(!Uteis.isAtributoPreenchido(grupoPessoaItemAtual.getPessoaVO()), "O campo Avaliador/Facilitador Atual deve ser informado.");
		Uteis.checkState(regrasSubstituicaoGrupoPessoaItemEnum.isFacilitadorEspecifico() && !Uteis.isAtributoPreenchido(grupoPessoaItemEspecifico), "O campo Avaliador/Facilitador Específico deve ser informado.");
		Uteis.checkState(regrasSubstituicaoGrupoPessoaItemEnum.isFacilitadorEspecifico() && grupoPessoaItemAtual.getCodigo().equals(grupoPessoaItemEspecifico.getCodigo()), "Os Avaliadores/Facilitadores devem ser diferentes.");
//		grupoPessoaItemAtual = getFacadeFactory().getGrupoPessoaItemFacade().consultarPorChavePrimaria(grupoPessoaItemAtual.getPessoaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		switch (regrasSubstituicaoGrupoPessoaItemEnum) {
		case FACILITADOR_ESPECIFICO:
			StringBuilder sql = new StringBuilder("update estagio set grupopessoaitem = ").append(grupoPessoaItemEspecifico.getCodigo());
			if(Uteis.isAtributoPreenchido(estagioVO)) {
				sql.append(" WHERE codigo = ").append(estagioVO.getCodigo()).append(" ");
			}else {
				sql.append(" from grupopessoaitem  WHERE grupopessoaitem.codigo = estagio.grupopessoaitem and grupopessoaitem.pessoa = ").append(grupoPessoaItemAtual.getPessoaVO().getCodigo()).append(" and situacaoestagioenum in ('EM_ANALISE', 'EM_CORRECAO') ");
				
			}
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString());
			break;
		case ENTRE_GRUPOPESSOA:
			realizarDistribuicaoGrupoPessoaItemPorInativacao(grupoPessoaItemAtual, usuarioVO);
			grupoPessoaItemAtual.setCodigo(0);
			break;
		}
		if(inativarGrupoPessoaItemGrupoParticipante) {
			getFacadeFactory().getGrupoPessoaItemFacade().atualizarCampoStatus(grupoPessoaItemAtual, StatusAtivoInativoEnum.INATIVO, usuarioVO);
		}		
	}
	
	@Override
	public String realizarVisualizacaoTermoEstagio(EstagioVO obj, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception {
//		obj.getMatriculaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
//		getFacadeFactory().getMatriculaFacade().carregarDados(obj.getMatriculaVO(), NivelMontarDados.TODOS, usuarioVO);
//		try{
//			obj.getMatriculaVO().getCurso().setFuncionarioResponsavelAssinaturaTermoEstagioVO(getFacadeFactory().getFuncionarioFacade().consultaFuncionarioResponsavelAssinaturaTermoEstagioRapidaPorCurso(obj.getMatriculaVO().getCurso().getCodigo(), usuarioVO));
//		} catch (Exception e) {
//			throw new ConsistirException("O funcionário responsável por assinar o termo de estágio não foi informado no cadastro do curso. Procure o departamento acadêmico.");
//		}
//		obj.getGradeCurricularEstagioVO().setTextoPadraoDeclaracaoVO(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(obj.getGradeCurricularEstagioVO().getTextoPadraoDeclaracaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
//		obj.getGradeCurricularEstagioVO().getTextoPadraoDeclaracaoVO().setAssinarDigitalmenteTextoPadrao(false); 
//		ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
//		impressaoContratoVO.setEstagioVO(obj);
//		impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoAtivaPorMatricula(obj.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
//		impressaoContratoVO.setMatriculaVO(obj.getMatriculaVO());
//		obj.getGradeCurricularEstagioVO().getTextoPadraoDeclaracaoVO().substituirTag(impressaoContratoVO, usuarioVO);
//		return getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, obj.getGradeCurricularEstagioVO().getTextoPadraoDeclaracaoVO(), "", true, configGeralSistema, usuarioVO);	
		return "";
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarNovoTermoAssinaturaEstagio(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getDocumentoAssinadoFacade().excluir(obj.getDocumentoAssinadoVO(), false, usuarioVO, configGeralSistema);
		String caminhoEstagio = realizarVisualizacaoTermoEstagio(obj, configGeralSistema, usuarioVO);			
		getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorEstagio(obj, TipoOrigemDocumentoAssinadoEnum.TERMO_ESTAGIO_OBRIGATORIO, caminhoEstagio, configEstagio, configGeralSistema, usuarioVO);
		alterar(obj, "estagio", new AtributoPersistencia().add("documentoAssinado", obj.getDocumentoAssinadoVO()),new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);	
	}
	
	@Override
	public String realizarVisualizacaoTermoEstagioNaoObrigatorio(RequerimentoVO obj, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception {
//		obj.getMatricula().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
//		getFacadeFactory().getMatriculaFacade().carregarDados(obj.getMatricula(), NivelMontarDados.TODOS, usuarioVO);
//		obj.getMatricula().getCurso().setFuncionarioResponsavelAssinaturaTermoEstagioVO(getFacadeFactory().getFuncionarioFacade().consultaFuncionarioResponsavelAssinaturaTermoEstagioRapidaPorCurso(obj.getMatricula().getCurso().getCodigo(), usuarioVO));
//		obj.getTipoRequerimento().setTextoPadraoVO(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(obj.getTipoRequerimento().getTextoPadraoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
//		obj.getTipoRequerimento().getTextoPadraoVO().setAssinarDigitalmenteTextoPadrao(true);
//		ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
//		impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoAtivaPorMatricula(obj.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
//		impressaoContratoVO.setMatriculaVO(obj.getMatricula());
//		impressaoContratoVO.setRequerimentoVO(obj);
//		impressaoContratoVO.setGerarNovoArquivoAssinado(true);
////		obj.getTipoRequerimento().getTextoPadraoVO().substituirTag(impressaoContratoVO, usuarioVO);
//		return getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, obj.getTipoRequerimento().getTextoPadraoVO(), "", true, configGeralSistema, usuarioVO);
		return "";
	}
	
	@Override
	public boolean realizarVerificacaoSeExisteEstagioAproveitamentoOuEquivalencia(String matricula, Integer gradeCurricularEstagio, TipoEstagioEnum tipoEstagio) throws Exception {
		StringBuilder sql = new StringBuilder(" select count(estagio.codigo) QTDE from estagio ");
		sql.append(" inner join gradecurricularestagio on gradecurricularestagio.codigo  = estagio.gradecurricularestagio ");
		sql.append(" where gradecurricularestagio.codigo = ?  and estagio.matricula = ? ");
		sql.append(" and estagio.situacaoEstagioEnum != '").append(SituacaoEstagioEnum.INDEFERIDO).append("' ");
		sql.append(" and estagio.tipoEstagio = '").append(tipoEstagio).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), gradeCurricularEstagio, matricula);
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public boolean realizarVerificacaoCargaHorarioDeferidaCompleta(String matricula, Integer gradeCurricularEstagio) throws Exception {
		StringBuilder sql = new StringBuilder(" select (sum(estagio.cargaHorariaDeferida) >= gradecurricularestagio.cargahorarioobrigatorio) as cargahorariacomprida,  ");
		sql.append(" gradecurricularestagio.codigo from estagio ");
		sql.append(" inner join gradecurricularestagio on gradecurricularestagio.codigo  = estagio.gradecurricularestagio ");
		sql.append(" where gradecurricularestagio.codigo = ?  and estagio.matricula = ? ");
		sql.append(" and estagio.situacaoEstagioEnum != '").append(SituacaoEstagioEnum.INDEFERIDO).append("' ");
		sql.append(" group by gradecurricularestagio.cargahorarioobrigatorio, gradecurricularestagio.codigo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), gradeCurricularEstagio, matricula);
		if (rs.next()) {
			return rs.getBoolean("cargahorariacomprida");
		}
		return false;
		
	}
	
	@Override
	public boolean realizarVerificacaoEstornoEstagioPorCargaHorario(Integer codigoEstagio, Integer gradeCurricularEstagio, String matricula) throws Exception {
		StringBuilder sql = new StringBuilder(" select case when cargahoraria > cargahorarioobrigatorio then false else true end as estornoPermitido from ( ");
		sql.append(" select sum(estagio.cargahoraria) as cargahoraria ,  gradecurricularestagio.cargahorarioobrigatorio from estagio ");
		sql.append(" inner join gradecurricularestagio on gradecurricularestagio.codigo = estagio.gradecurricularestagio ");
		sql.append(" where estagio.matricula =  ? ");
		sql.append(" and gradecurricularestagio.codigo =  ? ");
		sql.append(" and (estagio.situacaoEstagioEnum not in('INDEFERIDO') or estagio.codigo = ?) ");
		sql.append(" group by gradecurricularestagio.cargahorarioobrigatorio ");
		sql.append(" ) as t ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, gradeCurricularEstagio, codigoEstagio);
		if (rs.next()) {
			return rs.getBoolean("estornoPermitido");
		}
		return false;
		
	}
	
	@Override
	public Integer consultarCargaHorariaEstagioFracionadaAproveitamentoOuEquivalencia(String matricula, Integer gradeCurricularEstagio, Integer codigo) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT sum(cargaHorariaDeferida) as cargaHorariaDeferida from estagio where matricula = ? and gradecurricularestagio = ?");
		sql.append(" and estagio.tipoEstagio in ('OBRIGATORIO_APROVEITAMENTO', 'OBRIGATORIO_EQUIVALENCIA') ");
		sql.append(" and estagio.situacaoEstagioEnum != '").append(SituacaoEstagioEnum.INDEFERIDO).append("' ");
		if(Uteis.isAtributoPreenchido(codigo)) {
			sql.append(" and codigo !=  ").append(codigo);	
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, gradeCurricularEstagio);
		if (rs.next()) {
			return rs.getInt("cargaHorariaDeferida");
		}
		return 0;
	}
	
	@Override
	public Integer consultarCargaHorariaEstagioFracionada(String matricula, Integer gradeCurricularEstagio, Integer codigo) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT sum(cargaHorariaDeferida) as cargaHorariaDeferida from estagio where matricula = ? and gradecurricularestagio = ?");
		sql.append(" and estagio.situacaoEstagioEnum != '").append(SituacaoEstagioEnum.INDEFERIDO).append("' ");
		if(Uteis.isAtributoPreenchido(codigo)) {
			sql.append(" and codigo !=  ").append(codigo);	
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, gradeCurricularEstagio);
		if (rs.next()) {
			return rs.getInt("cargaHorariaDeferida");
		}
		return 0;
	}
	
	@Override
	public Integer consultarCargaHorariaRealizadaEstagioMatricula(String matricula) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT sum(cargaHorariaDeferida) as cargaHorariaDeferida from estagio where matricula = ? ");
		sql.append(" and estagio.situacaoEstagioEnum ='").append(SituacaoEstagioEnum.DEFERIDO).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		if (rs.next()) {
			return rs.getInt("cargaHorariaDeferida");
		}
		return 0;
	}
	@Override
	public Integer consultarCargaHorariaEmRealizacaoEstagioMatricula(String matricula) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT sum(cargaHorariaDeferida) as cargaHorariaDeferida from estagio where matricula = ? ");
		sql.append(" and estagio.situacaoEstagioEnum  not in('").append(SituacaoEstagioEnum.INDEFERIDO).append("','").append(SituacaoEstagioEnum.DEFERIDO).append("') ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		if (rs.next()) {
			return rs.getInt("cargaHorariaDeferida");
		}
		return 0;
	}
	
	@Override
	public boolean realizarVerificacaoUsuarioFacilitador(Integer codigoPessoa) throws Exception {
		StringBuilder sql = new StringBuilder(" select count(estagio.codigo) as QTDE ");
		sql.append(" from estagio ");
		sql.append(" inner join grupopessoaitem on grupopessoaitem.codigo = estagio.grupopessoaitem ");
		sql.append(" where grupopessoaitem.pessoa = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPessoa);
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
		
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public EstagioVO consultarPorDocumentoAssinador(Integer codigoDocumentoAssinado,int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" AND documentoAssinado.codigo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoDocumentoAssinado);
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuario);
		}
		return new EstagioVO();
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public EstagioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" AND estagio.codigo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm);
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuario);
		}
		return new EstagioVO();
	}
	
	public StringBuilder consultaTotalizadoresPadraoEstagio(String campoTotalizado, boolean isVisaoAdministrativa) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select ");
		sql.append(" sum(case when situacaoEstagioEnum = 'AGUARDANDO_ASSINATURA' then ").append(campoTotalizado).append(" else 0 end) as AGUARDANDO_ASSINATURA,");
		sql.append(" sum(case when situacaoEstagioEnum = 'REALIZANDO' then ").append(campoTotalizado).append(" else 0 end) as REALIZANDO,");
		sql.append(" sum(case when situacaoEstagioEnum = 'EM_ANALISE' then ").append(campoTotalizado).append(" else 0 end) as EM_ANALISE,");
		sql.append(" sum(case when situacaoEstagioEnum = 'EM_CORRECAO' then ").append(campoTotalizado).append(" else 0 end) as EM_CORRECAO,");
		sql.append(" sum(case when situacaoEstagioEnum = 'DEFERIDO' then ").append(campoTotalizado).append(" else 0 end) as DEFERIDO,");
		sql.append(" sum(case when situacaoEstagioEnum = 'INDEFERIDO' then ").append(campoTotalizado).append(" else 0 end) as INDEFERIDO,");
		if(isVisaoAdministrativa) {
			sql.append(" sum(case when situacaoEstagioEnum = 'AGUARDANDO_ASSINATURA' and situacaoAdicionalEstagioEnum= 'PENDENTE_SOLICITACAO_ASSINATURA' and tipoEstagio = 'OBRIGATORIO' then 1 else 0 end) as aguardandoAssinatura_pendente, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'AGUARDANDO_ASSINATURA' and situacaoAdicionalEstagioEnum= 'ASSINATURA_PENDENTE'  and tipoEstagio = 'OBRIGATORIO' then 1 else 0 end) as aguardandoAssinatura_assinatura, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_ANALISE' and tipoEstagio = 'OBRIGATORIO' and datalimiteanalise >= current_date then 1 else 0 end) as analise_no_prazo, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_ANALISE' and tipoEstagio = 'OBRIGATORIO' and datalimiteanalise < current_date then 1 else 0 end) as analise_atrasado, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_ANALISE' and tipoEstagio = 'OBRIGATORIO_APROVEITAMENTO' and datalimiteanalise >= current_date then 1 else 0 end) as analise_aproveitamento_no_prazo, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_ANALISE' and tipoEstagio = 'OBRIGATORIO_APROVEITAMENTO' and datalimiteanalise < current_date then 1 else 0 end) as analise_aproveitamento_atrasado, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_ANALISE' and tipoEstagio = 'OBRIGATORIO_EQUIVALENCIA' and datalimiteanalise >= current_date then 1 else 0 end) as analise_equivalencia_no_prazo, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_ANALISE' and tipoEstagio = 'OBRIGATORIO_EQUIVALENCIA' and datalimiteanalise < current_date then 1 else 0 end) as analise_equivalencia_atrasado, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_CORRECAO' and tipoEstagio = 'OBRIGATORIO' and datalimitecorrecao >= current_date then 1 else 0 end) as correcao_no_prazo, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_CORRECAO' and tipoEstagio = 'OBRIGATORIO' and datalimitecorrecao < current_date then 1 else 0 end) as correcao_atrasado, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_CORRECAO' and tipoEstagio = 'OBRIGATORIO_APROVEITAMENTO' and datalimitecorrecao >= current_date then 1 else 0 end) as correcao_aproveitamento_no_prazo, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_CORRECAO' and tipoEstagio = 'OBRIGATORIO_APROVEITAMENTO' and datalimitecorrecao < current_date then 1 else 0 end) as correcao_aproveitamento_atrasado, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_CORRECAO' and tipoEstagio = 'OBRIGATORIO_EQUIVALENCIA' and datalimitecorrecao >= current_date then 1 else 0 end) as correcao_equivalencia_no_prazo, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'EM_CORRECAO' and tipoEstagio = 'OBRIGATORIO_EQUIVALENCIA' and datalimitecorrecao < current_date then 1 else 0 end) as correcao_equivalencia_atrasado, ");
			sql.append(" sum(case when situacaoEstagioEnum = 'DEFERIDO' and tipoEstagio = 'OBRIGATORIO'  then 1  else 0 end) as deferido_final, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'DEFERIDO' and tipoEstagio = 'OBRIGATORIO_APROVEITAMENTO'  then 1  else 0 end) as deferido_aproveitamento, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'DEFERIDO' and tipoEstagio = 'OBRIGATORIO_EQUIVALENCIA' then 1  else 0 end) as deferido_equivalencia, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'INDEFERIDO' and tipoEstagio = 'OBRIGATORIO'  then 1  else 0 end) as indeferido_final, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'INDEFERIDO' and tipoEstagio = 'OBRIGATORIO_APROVEITAMENTO'  then 1  else 0 end) as indeferido_aproveitamento, ");	
			sql.append(" sum(case when situacaoEstagioEnum = 'INDEFERIDO' and tipoEstagio = 'OBRIGATORIO_EQUIVALENCIA' then 1  else 0 end) as indeferido_equivalencia ");
		}else {
			sql.append(" (select sum(gradecurricularestagio.cargahorarioobrigatorio) from gradecurricularestagio where gradecurricularestagio.gradecurricular = matricula.gradecurricularatual) as cargaHorariaExigida");	
		}
		if(isVisaoAdministrativa) {
			sql.append(" from estagio ");
			sql.append(" left join matricula on matricula.matricula = estagio.matricula ");
			sql.append(" left join grupopessoaitem on grupopessoaitem.codigo = estagio.grupopessoaitem");
			sql.append(" left join pessoa as pessoa_gpi on pessoa_gpi.codigo = grupopessoaitem.pessoa");
		}else {
			sql.append(" from matricula ");
			sql.append(" left join estagio on matricula.matricula = estagio.matricula ");
			sql.append(" left join grupopessoaitem on grupopessoaitem.codigo = estagio.grupopessoaitem");
			sql.append(" left join pessoa as pessoa_gpi on pessoa_gpi.codigo = grupopessoaitem.pessoa");
		}
		sql.append(" where 1=1 ");
		return sql;
	}
	
	
	public StringBuilder consultaPadraoEstagio() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT count(*) over() as totalRegistroConsulta, estagio.codigo, estagio.matricula,");
		sql.append(" estagio.cargahoraria, estagio.situacaoEstagioEnum, estagio.situacaoAdicionalEstagioEnum, ");
		sql.append(" estagio.dataInicioVigencia, estagio.dataFinalVigencia, ");
		sql.append(" estagio.dataInicioRenovacaoVigencia, estagio.dataFinalRenovacaoVigencia, ");
		sql.append(" estagio.ano, estagio.semestre, ");
		sql.append(" estagio.cargaHorariaDiaria, estagio.tipoEstagio, ");
		sql.append(" estagio.cnpj, estagio.nomeconcedente,  estagio.telefone,  ");
		sql.append(" estagio.cep, estagio.endereco, estagio.numero,  estagio.bairro,  ");
		sql.append(" estagio.responsavelConcedente, estagio.cpfResponsavelConcedente, estagio.emailResponsavelConcedente,  estagio.telefoneResponsavelConcedente,  ");
		sql.append(" estagio.nomeSupervisor, estagio.cpfSupervisor,  ");
		sql.append(" estagio.cargahorariadeferida, estagio.motivo, ");
		sql.append(" estagio.dataEnvioAnalise, estagio.dataLimiteAnalise, ");
		sql.append(" estagio.dataEnvioCorrecao, estagio.dataLimiteCorrecao, estagio.tipoSituacaoAproveitamentoEnum, ");
		sql.append(" estagio.cidade, estagio.rgBeneficiario, estagio.cpfBeneficiario, estagio.nomeBeneficiario, estagio.emailBeneficiario, ");
		sql.append(" estagio.telefoneBeneficiario, estagio.cepBeneficiario, estagio.cidadeBeneficiario, estagio.estadoBeneficiario, estagio.numeroBeneficiario, ");
		sql.append(" estagio.enderecoBeneficiario, estagio.complementoBeneficiario, estagio.setorBeneficiario, ");
		sql.append(" estagio.created as \"estagio.created\",  ");
		sql.append(" estagio.sqlMensagem, ");
		
		sql.append(" matricula.matricula as \"matricula.matricula\",  ");		
		sql.append(" matricula.gradecurricularatual as \"matricula.gradecurricularatual\",  ");
		sql.append(" matricula.unidadeEnsino as \"matricula.unidadeEnsino\",  ");
		
		sql.append(" curso.codigo as \"curso.codigo\",  ");
		sql.append(" curso.nome as \"curso.nome\", ");
		
		sql.append(" pessoa_mat.codigo as \"pessoa_mat.codigo\",  ");
		sql.append(" pessoa_mat.nome as \"pessoa_mat.nome\", ");
		
		sql.append(" estagio.concedente as \"estagio.concedente\",  ");
		
		sql.append(" gradeCurricularEstagio.codigo as \"gradeCurricularEstagio.codigo\",  ");
		sql.append(" gradeCurricularEstagio.nome as \"gradeCurricularEstagio.nome\", ");
		sql.append(" gradeCurricularEstagio.cargaHorarioObrigatorio as \"gradeCurricularEstagio.cargaHorarioObrigatorio\", ");
		sql.append(" gradeCurricularEstagio.textoPadraoDeclaracao as \"gradeCurricularEstagio.textoPadraoDeclaracao\", ");
		sql.append(" gradeCurricularEstagio.permiteHorasFragmentadas as \"gradeCurricularEstagio.permiteHorasFragmentadas\", ");
		
		sql.append(" qrf.codigo as \"qrf.codigo\", qrf.descricao as \"qrf.descricao\", ");		
		sql.append(" qadr.codigo as \"qadr.codigo\", qadr.descricao as \"qadr.descricao\", ");		
		sql.append(" qal.codigo as \"qal.codigo\", qal.descricao as \"qal.descricao\", ");		
		sql.append(" qe.codigo as \"qe.codigo\", qe.descricao as \"qe.descricao\", ");
		
		sql.append(" tipoConcedente.codigo as \"tipoConcedente.codigo\",  ");
		sql.append(" tipoConcedente.nome as \"tipoConcedente.nome\", ");
		sql.append(" tipoConcedente.cnpjObrigatorio as \"tipoConcedente.cnpjObrigatorio\", ");
				
		sql.append(" documentoAssinado.codigo as \"documentoAssinado.codigo\",  ");
		
		sql.append(" rd.codigo as \"rd.codigo\", rd.nome as \"rd.nome\", estagio.datadeferimento, ");
		sql.append(" ri.codigo as \"ri.codigo\", ri.nome as \"ri.nome\",  estagio.dataindeferimento, ");
		
		sql.append(" grupopessoaitem.codigo as \"grupopessoaitem.codigo\",  ");
		sql.append(" pessoa_gpi.codigo as \"pessoa_gpi.codigo\",  ");
		sql.append(" pessoa_gpi.nome as \"pessoa_gpi.nome\", ");
		sql.append(" pessoa_gpi.email as \"pessoa_gpi.email\", ");
		sql.append(" pessoa_gpi.celular as \"pessoa_gpi.celular\", ");
		
		sql.append(" (select case when count(questionariorespostaorigem.codigo) > 1 then true else false end from questionariorespostaorigem  where questionariorespostaorigem.estagio = estagio.codigo) as existeVersoes, ");
		sql.append(" docenteResponsavelEstagio.codigo AS codigoDocenteResponsavelEstagio, docenteResponsavelEstagio.nome AS nomeDocenteResponsavelEstagio, ");
		sql.append(" funcionario.codigo AS \"funcionario.codigo\", orientadorpadraoestagio.codigo AS codigoOrientadorpadraoestagio, orientadorpadraoestagio.nome AS nomeOrientadorpadraoestagio ");
		sql.append(" from estagio ");
		sql.append(" left join matricula on estagio.matricula = matricula.matricula ");
		sql.append(" left join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");		
		sql.append(" left join funcionario on funcionario.codigo = unidadeensino.orientadorpadraoestagio");
		sql.append(" left join pessoa orientadorpadraoestagio on orientadorpadraoestagio.codigo = funcionario.pessoa");
		sql.append(" left join curso on curso.codigo = matricula.curso ");		
		sql.append(" left join pessoa as pessoa_mat on pessoa_mat.codigo = matricula.aluno ");		
		sql.append(" left join gradeCurricularEstagio  on gradeCurricularEstagio.codigo = estagio.gradeCurricularEstagio");
		sql.append(" left join tipoconcedente  on tipoconcedente.codigo = estagio.tipoconcedente");		
		sql.append(" left join documentoAssinado on documentoAssinado.codigo = estagio.documentoAssinado");
		sql.append(" left join grupopessoaitem on grupopessoaitem.codigo = estagio.grupopessoaitem");
		sql.append(" left join pessoa as pessoa_gpi on pessoa_gpi.codigo = grupopessoaitem.pessoa");
		sql.append(" left join questionario as qrf on qrf.codigo = gradeCurricularEstagio.questionarioRelatoriofinal ");
		sql.append(" left join questionario as qadr on qadr.codigo = gradeCurricularEstagio.questionarioAproveitamentoPorDocenteRegular ");
		sql.append(" left join questionario as qal on qal.codigo = gradeCurricularEstagio.questionarioAproveitamentoPorLicenciatura ");
		sql.append(" left join questionario as qe on qe.codigo = gradeCurricularEstagio.questionarioequivalencia");
		sql.append(" left join usuario as rd on rd.codigo = estagio.responsaveldeferimento");
		sql.append(" left join usuario as ri on ri.codigo = estagio.responsavelindeferimento");
		sql.append(" left join pessoa as docenteResponsavelEstagio on docenteResponsavelEstagio.codigo = estagio.docenteResponsavelEstagio");
		sql.append(" WHERE 1=1 ");
		return sql;
	}

	

	public  List<EstagioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<EstagioVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public  EstagioVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		EstagioVO obj = new EstagioVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(rs.getInt("codigo"));
		obj.setCargaHoraria(rs.getInt("cargaHoraria"));
		obj.setCargaHorariaDeferida(rs.getInt("cargahorariadeferida"));
		obj.setCargaHorariaDiaria(rs.getInt("cargaHorariaDiaria"));
		obj.setSituacaoEstagioEnum(SituacaoEstagioEnum.valueOf(rs.getString("situacaoEstagioEnum")));
		obj.setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.valueOf(rs.getString("situacaoAdicionalEstagioEnum")));
		obj.setTipoEstagio(TipoEstagioEnum.getEnum(rs.getString("tipoEstagio")));
		obj.setAno(rs.getString("ano"));
		obj.setSemestre(rs.getString("semestre"));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setCreated(rs.getDate("estagio.created"));
		obj.setDataInicioVigencia(rs.getDate("dataInicioVigencia"));
		obj.setDataFinalVigencia(rs.getDate("dataFinalVigencia"));
		obj.setDataInicioRenovacaoVigencia(rs.getDate("dataInicioRenovacaoVigencia"));
		obj.setDataFinalRenovacaoVigencia(rs.getDate("dataFinalRenovacaoVigencia"));
		obj.setCnpj(rs.getString("cnpj"));
		obj.setConcedente(rs.getString("nomeconcedente"));
		obj.setTelefone(rs.getString("telefone"));
		obj.setCep(rs.getString("cep"));
		obj.setEndereco(rs.getString("endereco"));
		obj.setNumero(rs.getString("numero"));
		obj.setBairro(rs.getString("bairro"));
		obj.setCidade(rs.getString("cidade"));
		obj.setResponsavelConcedente(rs.getString("responsavelConcedente"));
		obj.setCpfResponsavelConcedente(rs.getString("cpfResponsavelConcedente"));
		obj.setEmailResponsavelConcedente(rs.getString("emailResponsavelConcedente"));
		obj.setTelefoneResponsavelConcedente(rs.getString("telefoneResponsavelConcedente"));
		obj.setMotivo(rs.getString("motivo"));
		obj.setDataLimiteAnalise(rs.getDate("dataLimiteAnalise"));
		obj.setDataLimiteCorrecao(rs.getDate("dataLimiteCorrecao"));
		obj.setDataEnvioAnalise(rs.getDate("dataEnvioAnalise"));
		obj.setDataEnvioCorrecao(rs.getDate("dataEnvioCorrecao"));
		
		obj.setRgBeneficiario(rs.getString("rgBeneficiario"));
		obj.setCpfBeneficiario(rs.getString("cpfBeneficiario"));
		obj.setNomeBeneficiario(rs.getString("nomeBeneficiario"));
		obj.setEmailBeneficiario(rs.getString("emailBeneficiario"));
		obj.setTelefoneBeneficiario(rs.getString("telefoneBeneficiario"));
		obj.setCepBeneficiario(rs.getString("cepBeneficiario"));
		obj.setCidadeBeneficiario(rs.getString("cidadeBeneficiario"));
		obj.setEstadoBeneficiario(rs.getString("estadoBeneficiario"));
		obj.setNumeroBeneficiario(rs.getString("numeroBeneficiario"));
		obj.setEnderecoBeneficiario(rs.getString("enderecoBeneficiario"));
		obj.setComplementoBeneficiario(rs.getString("complementoBeneficiario"));
		obj.setSetorBeneficiario(rs.getString("setorBeneficiario"));
		
		obj.setSqlMensagem(rs.getString("sqlMensagem") );	
		
		if(rs.getString("tipoSituacaoAproveitamentoEnum") != null) {
			obj.setTipoSituacaoAproveitamentoEnum(TipoConsultaComboSituacaoAproveitamentoEnum.valueOf(rs.getString("tipoSituacaoAproveitamentoEnum")));	
		}
		obj.getDocenteResponsavelEstagio().setCodigo(rs.getInt("codigoDocenteResponsavelEstagio"));
		obj.getDocenteResponsavelEstagio().setNome(rs.getString("nomeDocenteResponsavelEstagio"));
		obj.getMatriculaVO().setMatricula(rs.getString("matricula.matricula"));
		obj.getMatriculaVO().getGradeCurricularAtual().setCodigo(rs.getInt("matricula.gradecurricularatual"));
		obj.getMatriculaVO().getAluno().setCodigo(rs.getInt("pessoa_mat.codigo"));
		obj.getMatriculaVO().getAluno().setNome(rs.getString("pessoa_mat.nome"));
		obj.getMatriculaVO().getCurso().setCodigo(rs.getInt("curso.codigo"));
		obj.getMatriculaVO().getCurso().setNome(rs.getString("curso.nome"));
		obj.getMatriculaVO().getUnidadeEnsino().setCodigo(rs.getInt("matricula.unidadeEnsino"));
		if (Uteis.isAtributoPreenchido(rs.getInt("funcionario.codigo"))) {
			obj.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().setCodigo(rs.getInt("funcionario.codigo"));
			obj.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().setCodigo(rs.getInt("codigoOrientadorpadraoestagio"));
			obj.getMatriculaVO().getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().setNome(rs.getString("nomeOrientadorpadraoestagio"));
		}
		obj.getGradeCurricularEstagioVO().setCodigo(rs.getInt("gradeCurricularEstagio.codigo"));
		obj.getGradeCurricularEstagioVO().setNome(rs.getString("gradeCurricularEstagio.nome"));
		obj.getGradeCurricularEstagioVO().setCargaHorarioObrigatorio(rs.getInt("gradeCurricularEstagio.cargaHorarioObrigatorio"));		
		obj.getGradeCurricularEstagioVO().setPermiteHorasFragmentadas(rs.getBoolean("gradeCurricularEstagio.permiteHorasFragmentadas"));		
		obj.getGradeCurricularEstagioVO().getTextoPadraoDeclaracaoVO().setCodigo(rs.getInt("gradeCurricularEstagio.textoPadraoDeclaracao"));
//		obj.getGradeCurricularEstagioVO().getQuestionarioRelatorioFinal().setCodigo(rs.getInt("qrf.codigo"));
//		obj.getGradeCurricularEstagioVO().getQuestionarioRelatorioFinal().setDescricao(rs.getString("qrf.descricao"));
//		obj.getGradeCurricularEstagioVO().getQuestionarioAproveitamentoPorDocenteRegular().setCodigo(rs.getInt("qadr.codigo"));
//		obj.getGradeCurricularEstagioVO().getQuestionarioAproveitamentoPorDocenteRegular().setDescricao(rs.getString("qadr.descricao"));
//		obj.getGradeCurricularEstagioVO().getQuestionarioAproveitamentoPorLicenciatura().setCodigo(rs.getInt("qal.codigo"));
//		obj.getGradeCurricularEstagioVO().getQuestionarioAproveitamentoPorLicenciatura().setDescricao(rs.getString("qal.descricao"));
//		obj.getGradeCurricularEstagioVO().getQuestionarioEquivalencia().setCodigo(rs.getInt("qe.codigo"));
//		obj.getGradeCurricularEstagioVO().getQuestionarioEquivalencia().setDescricao(rs.getString("qe.descricao"));
		
		obj.getDocumentoAssinadoVO().setCodigo(rs.getInt("documentoAssinado.codigo"));
		
		
		obj.getGrupoPessoaItemVO().setCodigo(rs.getInt("grupopessoaitem.codigo"));
		obj.getGrupoPessoaItemVO().getPessoaVO().setCodigo(rs.getInt("pessoa_gpi.codigo"));		
		obj.getGrupoPessoaItemVO().getPessoaVO().setNome(rs.getString("pessoa_gpi.nome"));		
		obj.getGrupoPessoaItemVO().getPessoaVO().setEmail(rs.getString("pessoa_gpi.email"));		
		obj.getGrupoPessoaItemVO().getPessoaVO().setCelular(rs.getString("pessoa_gpi.celular"));
		
		obj.getConcedenteVO().setCodigo(rs.getInt("estagio.concedente"));
		if(Uteis.isAtributoPreenchido(obj.getConcedenteVO())) {
			obj.setConcedenteVO(getFacadeFactory().getConcedenteFacade().consultarPorChavePrimaria(obj.getConcedenteVO().getCodigo(), false, nivelMontarDados, usuario));	
		}
		obj.getTipoConcedenteVO().setCodigo(rs.getInt("tipoconcedente.codigo"));
		obj.getTipoConcedenteVO().setNome(rs.getString("tipoconcedente.nome"));
		obj.getTipoConcedenteVO().setCnpjObrigatorio(rs.getBoolean("tipoConcedente.cnpjObrigatorio"));
		obj.setExisteVersoeEstagio(rs.getBoolean("existeVersoes"));
		obj.setNomeSupervisor(rs.getString("nomeSupervisor"));
		obj.setCpfSupervisor(rs.getString("cpfSupervisor"));
		
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSDOCUMENTOASSINADO) {
			if(Uteis.isAtributoPreenchido(obj.getDocumentoAssinadoVO())) {
				obj.setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(obj.getDocumentoAssinadoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
			}
			return obj;
		}
		if(Uteis.isAtributoPreenchido(obj.getDocumentoAssinadoVO())) {
			obj.setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(obj.getDocumentoAssinadoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		obj.setQuestionarioRespostaOrigemUltimaVersao(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioUltimaVersaoPorEstagio(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		
		if(rs.getInt("rd.codigo") != 0) {
			obj.getResponsavelDeferimento().setCodigo(rs.getInt("rd.codigo"));
			obj.getResponsavelDeferimento().setNome(rs.getString("rd.nome"));
			obj.setDataDeferimento(rs.getDate("datadeferimento"));
		}
		if(rs.getInt("ri.codigo") != 0) {
			obj.getResponsavelIndeferimento().setCodigo(rs.getInt("ri.codigo"));
			obj.getResponsavelIndeferimento().setNome(rs.getString("ri.nome"));
			obj.setDataIndeferimento(rs.getDate("dataindeferimento"));
		}		
		return obj;
	}
	
	public void  montarDadosDashboardEstagio(DashboardEstagioVO obj, boolean isVisaoAdministrativa, SqlRowSet rs,  UsuarioVO usuario) throws Exception {		
		obj.setCargaHorariaAguardandoAssinatura(rs.getInt("AGUARDANDO_ASSINATURA"));
		obj.setCargaHorariaRealizando(rs.getInt("REALIZANDO"));
		obj.setCargaHorariaEmAnalise(rs.getInt("EM_ANALISE"));
		obj.setCargaHorariaEmCorrecaoAluna(rs.getInt("EM_CORRECAO"));
		obj.setCargaHorariaDeferido(rs.getInt("DEFERIDO"));
		obj.setCargaHorariaIndeferido(rs.getInt("INDEFERIDO"));
		obj.getListaTotalizadorAguardandoAnalise().clear();
		obj.getListaTotalizadorEmAnalise().clear();
		obj.getListaTotalizadorEmCorrecao().clear();
		obj.getListaTotalizadorDeferido().clear();
		obj.getListaTotalizadorIndeferido().clear();
		if(isVisaoAdministrativa) {
			TotalizadorEstagioSituacaoVO tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO, SituacaoAdicionalEstagioEnum.PENDENTE_SOLICITACAO_ASSINATURA,  rs.getInt("aguardandoAssinatura_pendente"), 0, 0);
			obj.getListaTotalizadorAguardandoAnalise().add(tse);
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO, SituacaoAdicionalEstagioEnum.ASSINATURA_PENDENTE, rs.getInt("aguardandoAssinatura_assinatura"), 0, 0);
			obj.getListaTotalizadorAguardandoAnalise().add(tse);
		
			
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("analise_atrasado")+rs.getInt("analise_no_prazo"), rs.getInt("analise_atrasado"), rs.getInt("analise_no_prazo"));
			obj.getListaTotalizadorEmAnalise().add(tse);
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("analise_aproveitamento_atrasado")+rs.getInt("analise_aproveitamento_no_prazo"), rs.getInt("analise_aproveitamento_atrasado"), rs.getInt("analise_aproveitamento_no_prazo"));
			obj.getListaTotalizadorEmAnalise().add(tse);
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("analise_equivalencia_atrasado")+rs.getInt("analise_equivalencia_no_prazo"), rs.getInt("analise_equivalencia_atrasado"), rs.getInt("analise_equivalencia_no_prazo"));
			obj.getListaTotalizadorEmAnalise().add(tse);
			
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("correcao_atrasado")+rs.getInt("correcao_no_prazo"), rs.getInt("correcao_atrasado"), rs.getInt("correcao_no_prazo"));
			obj.getListaTotalizadorEmCorrecao().add(tse);
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("correcao_aproveitamento_atrasado")+rs.getInt("correcao_aproveitamento_no_prazo"), rs.getInt("correcao_aproveitamento_atrasado"), rs.getInt("correcao_aproveitamento_no_prazo"));
			obj.getListaTotalizadorEmCorrecao().add(tse);
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("correcao_equivalencia_atrasado")+rs.getInt("correcao_equivalencia_no_prazo"), rs.getInt("correcao_equivalencia_atrasado"), rs.getInt("correcao_equivalencia_no_prazo"));
			obj.getListaTotalizadorEmCorrecao().add(tse);
			
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("deferido_final"),0,0);
			obj.getListaTotalizadorDeferido().add(tse);
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("deferido_aproveitamento"),0,0);
			obj.getListaTotalizadorDeferido().add(tse);
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("deferido_equivalencia"),0,0);
			obj.getListaTotalizadorDeferido().add(tse);
			
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("indeferido_final"),0,0);
			obj.getListaTotalizadorIndeferido().add(tse);
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("indeferido_aproveitamento"),0,0);
			obj.getListaTotalizadorIndeferido().add(tse);
			tse = new TotalizadorEstagioSituacaoVO(TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA, SituacaoAdicionalEstagioEnum.NENHUM, rs.getInt("indeferido_equivalencia"),0,0);
			obj.getListaTotalizadorIndeferido().add(tse);
		}else {
			obj.setCargaHorariaExigida(rs.getInt("cargaHorariaExigida"));
			obj.setCargaHorariaPendentes(obj.getCargaHorariaExigida()- (obj.getCargaHorariaAguardandoAssinatura() + obj.getCargaHorariaRealizando() +  obj.getCargaHorariaEmAnalise()+ obj.getCargaHorariaEmCorrecaoAluna() + obj.getCargaHorariaDeferido()));	
		}
	}
	
	private void montarFiltrosConsultaEstagio(EstagioVO estagio,  DashboardEstagioVO  dashboardEstagioVO, TipoEstagioEnum tipoEstagio, String ano, String semestre, UsuarioVO usuario, StringBuilder sql) {
//		if(dashboardEstagioVO == null) {
//			if(usuario.getIsApresentarVisaoAdministrativa()) {
//				sql.append(" AND (estagio.situacaoEstagioEnum in(").append(UteisTexto.converteListaEnumParaCondicaoIn(SituacaoEstagioEnum.getListaFiltroSituacaoEstagio())).append(")");
//				//sql.append(" or ( estagio.situacaoEstagioEnum = '").append(SituacaoEstagioEnum.AGUARDANDO_ASSINATURA.name()).append("' and situacaoAdicionalEstagioEnum='").append(SituacaoAdicionalEstagioEnum.ASSINATURA_PENDENTE.name()).append("')");
//				sql.append(" )");
//			}		
//			if(usuario.getIsApresentarVisaoAluno()) {
//				sql.append(" AND estagio.situacaoEstagioEnum in(").append(UteisTexto.converteListaEnumParaCondicaoIn(SituacaoEstagioEnum.getListaFiltroSituacaoEstagioVisaoAluno())).append(")");
//			}
//		}
		
		if(dashboardEstagioVO != null) {
			if(Uteis.isAtributoPreenchido(dashboardEstagioVO.getSituacaoEstagioEnum())) {
				sql.append(" AND estagio.situacaoEstagioEnum = '").append(dashboardEstagioVO.getSituacaoEstagioEnum().name()).append("'");
			}
			if(Uteis.isAtributoPreenchido(dashboardEstagioVO.getSituacaoEstagioEnum()) && dashboardEstagioVO.getSituacaoEstagioEnum().isAguardandoAssinatura() && Uteis.isAtributoPreenchido(dashboardEstagioVO.getSituacaoAdicionalEstagioEnum())) {
				sql.append(" AND estagio.situacaoAdicionalEstagioEnum='").append(dashboardEstagioVO.getSituacaoAdicionalEstagioEnum().name()).append("'");			
			}
			if(dashboardEstagioVO.getSituacaoEstagioEnum().isEmAnalise() && dashboardEstagioVO.isEstagioAtrasado()) {
				sql.append(" AND datalimiteanalise < current_date");	
			}
			if(dashboardEstagioVO.getSituacaoEstagioEnum().isEmCorrecao() && dashboardEstagioVO.isEstagioAtrasado()) {
				sql.append(" AND datalimitecorrecao < current_date");	
			}
			if(dashboardEstagioVO.getSituacaoEstagioEnum().isEmAnalise() && dashboardEstagioVO.isEstagioNoPrazo()) {
				sql.append(" AND datalimiteanalise >= current_date");	
			}
			if(dashboardEstagioVO.getSituacaoEstagioEnum().isEmCorrecao() && dashboardEstagioVO.isEstagioNoPrazo()) {
				sql.append(" AND datalimitecorrecao >= current_date");	
			}
		}
		
		if(Uteis.isAtributoPreenchido(tipoEstagio)) {
			sql.append(" AND estagio.tipoEstagio = '").append(tipoEstagio.name()).append("'");
		}
		
		if(estagio != null) {
			if(Uteis.isAtributoPreenchido(estagio.getMatriculaVO().getMatricula())) {
				sql.append(" AND matricula.matricula = '").append(estagio.getMatriculaVO().getMatricula()).append("'");	
			}	
			
			if(Uteis.isAtributoPreenchido(estagio.getMatriculaVO().getUnidadeEnsino().getCodigo())) {
				sql.append(" AND matricula.unidadeensino = ").append(estagio.getMatriculaVO().getUnidadeEnsino().getCodigo()).append("");	
			}
			
			if(Uteis.isAtributoPreenchido(estagio.getMatriculaVO().getCurso().getCodigo())) {
				sql.append(" AND matricula.curso = ").append(estagio.getMatriculaVO().getCurso().getCodigo()).append("");	
			}
			
			if(Uteis.isAtributoPreenchido(estagio.getGrupoPessoaItemVO().getPessoaVO().getCodigo())) {
				sql.append(" AND pessoa_gpi.codigo  = ").append(estagio.getGrupoPessoaItemVO().getPessoaVO().getCodigo());	
			}
			if(Uteis.isAtributoPreenchido(estagio.getGradeCurricularEstagioVO().getCodigo())) {
				sql.append(" AND estagio.gradecurricularestagio = ").append(estagio.getGradeCurricularEstagioVO().getCodigo());	
			}
		}
		
		if(Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" AND exists (select matriculaperiodo.codigo from matriculaperiodo where matriculaperiodo.matricula = estagio.matricula and matriculaperiodo.ano = '").append(ano).append("' and matriculaperiodo.semestre = '").append(semestre).append("')");	
		}
	}

	@Override
	public List<EstagioVO> consultarEstagio(EstagioVO estagio, DashboardEstagioVO  dashboardEstagioVO, TipoEstagioEnum tipoEstagio, String ano, String semestre, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		montarFiltrosConsultaEstagio(estagio, dashboardEstagioVO, tipoEstagio, ano, semestre, usuario, sql);		
		sql.append(" order by situacaoEstagioEnum, codigo ");
//		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		montarTotalizadorConsultaBasica(dataModelo, rs);
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}	
	
	@Override
	public void executarInicializacaoTotalizadoresDashboardEstagio(DashboardEstagioVO  dashboardEstagioVO, EstagioVO estagio, String ano, String semestre, UsuarioVO usuario) throws Exception {
		StringBuilder sql = consultaTotalizadoresPadraoEstagio("1", true);
		montarFiltrosConsultaEstagio(estagio, null, null, ano, semestre, usuario, sql);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			montarDadosDashboardEstagio(dashboardEstagioVO, true, rs, usuario);
		}
	}
	
	@Override
	public List<EstagioVO> consultarPorMatriculaAluno(String matricula, SituacaoEstagioEnum situacaoEstagioEnum, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		List<Object> listaFiltro = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(situacaoEstagioEnum)) {
			listaFiltro.add(situacaoEstagioEnum.name());
			sql.append(" and estagio.situacaoEstagioEnum = ? ");
		}
		listaFiltro.add(matricula);
		sql.append(" AND matricula.matricula = ?");
		sql.append(" order by situacaoEstagioEnum, codigo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), listaFiltro.toArray());
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}
	
	@Override
	public void executarInicializacaoTotalizadoresDashboardEstagioPorMatricula(DashboardEstagioVO  dashboardEstagioVO, String matricula, UsuarioVO usuario) throws Exception {
		StringBuilder sql = consultaTotalizadoresPadraoEstagio("estagio.cargahorariadeferida", false);
		sql.append(" and matricula.matricula = ? ");
		sql.append(" group by matricula.gradecurricularatual ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		if (rs.next()) {
			montarDadosDashboardEstagio(dashboardEstagioVO, false, rs, usuario);
		}
	}

	public static String getIdEntidade() {
		return Estagio.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Estagio.idEntidade = idEntidade;
	}
	
	public void carregarUltimoBeneficiarioEstagio(EstagioVO obj) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select rgBeneficiario, cpfBeneficiario, nomeBeneficiario, enderecoBeneficiario, numeroBeneficiario, setorBeneficiario,  ");
		sql.append(" cepBeneficiario, estadoBeneficiario, telefoneBeneficiario, emailBeneficiario, complementoBeneficiario, cidadeBeneficiario, cidadeBeneficiario ");
		sql.append(" from estagio ");
		sql.append(" where (rgBeneficiario <> '' or cpfBeneficiario <> '' or nomeBeneficiario <> '' )");
		sql.append(" and estagio.matricula = ? ");		
		sql.append(" order by codigo desc limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getMatriculaVO().getMatricula());
		if (rs.next()) {
			obj.setRgBeneficiario(rs.getString("rgBeneficiario"));
			obj.setCpfBeneficiario(rs.getString("cpfBeneficiario"));
			obj.setNomeBeneficiario(rs.getString("nomeBeneficiario"));
			obj.setEnderecoBeneficiario(rs.getString("enderecoBeneficiario"));
			obj.setNumeroBeneficiario(rs.getString("numeroBeneficiario"));
			obj.setSetorBeneficiario(rs.getString("setorBeneficiario"));
			obj.setCepBeneficiario(rs.getString("cepBeneficiario"));
			obj.setTelefoneBeneficiario(rs.getString("telefoneBeneficiario"));
			obj.setEmailBeneficiario(rs.getString("emailBeneficiario"));
			obj.setComplementoBeneficiario(rs.getString("complementoBeneficiario"));
			obj.setCidadeBeneficiario(rs.getString("cidadeBeneficiario"));
			obj.setEstadoBeneficiario(rs.getString("estadoBeneficiario"));
		}
	}

	public List<EstagioVO> consultaEstagioPeriodoAnaliseAproveitamentoEncerrado(Integer parametro) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" and estagio.situacaoEstagioEnum = ? ");
		sql.append(" and tipoestagio = ?");
		sql.append(" and datalimiteanalise < current_date ");
		sql.append(" and case when (datalimiteanalisenotificacao is null) then (datalimiteanalise::date + 1) = current_date::date else ");
		sql.append(" (datalimiteanalisenotificacao::date + ?) = current_date::date end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoEstagioEnum.EM_ANALISE.name(), TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO.name(), parametro);
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
	}

	public List<EstagioVO> consultaEstagioPeriodoAnaliseRelatorioFinalEncerrado(Integer parametro) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" and estagio.situacaoEstagioEnum = ? ");
		sql.append(" and tipoestagio = ?");
		sql.append(" and datalimiteanalise < current_date ");
		sql.append(" and case when (datalimiteanalisenotificacao is null) then (datalimiteanalise::date + 1) = current_date::date else ");
		sql.append(" (datalimiteanalisenotificacao::date + ?) = current_date::date end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoEstagioEnum.EM_ANALISE.name(), TipoEstagioEnum.OBRIGATORIO.name(), parametro);
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
	}

	public List<EstagioVO> consultaEstagioPeriodoAnaliseEquivalenciaEncerrado(Integer parametro) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" and estagio.situacaoEstagioEnum = ? ");
		sql.append(" and tipoestagio = ?");
		sql.append(" and datalimiteanalise < current_date ");
		sql.append(" and case when (datalimiteanalisenotificacao is null) then (datalimiteanalise::date + 1) = current_date::date else ");
		sql.append(" (datalimiteanalisenotificacao::date + ?) = current_date::date end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoEstagioEnum.EM_ANALISE.name(), TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA.name(), parametro);
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataLimiteAnaliseNotificacao(final EstagioVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(obj, "estagio", new AtributoPersistencia()
					.add("dataLimiteAnaliseNotificacao", new Date()),					
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<EstagioVO> consultaEstagioCorrecaoAproveitamentoEncerrado(Integer parametro) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" and estagio.situacaoEstagioEnum = ? ");
		sql.append(" and tipoestagio = ?");
		sql.append(" and datalimitecorrecao < current_date ");
		sql.append(" and case when (datalimitecorrecaonotificacao is null) then (datalimitecorrecao::date + 1) = current_date::date else ");
		sql.append(" (datalimitecorrecaonotificacao::date + ?) = current_date::date end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoEstagioEnum.EM_CORRECAO.name(), TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO.name(), parametro);
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<EstagioVO> consultaEstagioCorrecaoEquivalenciaEncerrado(Integer parametro) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" and estagio.situacaoEstagioEnum = ? ");
		sql.append(" and tipoestagio = ?");
		sql.append(" and datalimitecorrecao < current_date ");
		sql.append(" and case when (datalimitecorrecaonotificacao is null) then (datalimitecorrecao::date + 1) = current_date::date else ");
		sql.append(" (datalimitecorrecaonotificacao::date + ?) = current_date::date end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoEstagioEnum.EM_CORRECAO.name(), TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA.name(), parametro);
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<EstagioVO> consultaEstagioCorrecaoRelatorioFinalEncerrado(Integer parametro) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" and estagio.situacaoEstagioEnum = ? ");
		sql.append(" and tipoestagio = ?");
		sql.append(" and datalimitecorrecao < current_date ");
		sql.append(" and case when (datalimitecorrecaonotificacao is null) then (datalimitecorrecao::date + 1) = current_date::date else ");
		sql.append(" (datalimitecorrecaonotificacao::date + ?) = current_date::date end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoEstagioEnum.EM_CORRECAO.name(), TipoEstagioEnum.OBRIGATORIO.name(), parametro);
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<EstagioVO> consultaEstagioAguardandoAssinatura(Integer parametro) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" and estagio.situacaoEstagioEnum = ? ");
		sql.append(" and estagio.situacaoAdicionalEstagioEnum = ? ");
		sql.append(" and (CURRENT_DATE::date - estagio.dataEnvioAssinaturaPendente::date) > 0 ");
		sql.append(" and MOD((CURRENT_DATE::date - estagio.dataEnvioAssinaturaPendente::date), ?) = 0 ");
		sql.append(" and EXISTS (select dap.codigo from documentoassinadopessoa dap where dap.documentoassinado = documentoassinado.codigo ");
		sql.append(" and dap.tipopessoa in ('").append(TipoPessoa.MEMBRO_COMUNIDADE.name()).append("','").append(TipoPessoa.ALUNO.name()).append("')");
		sql.append(" and dap.situacaodocumentoassinadopessoa = '").append(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE.name()).append("' ");
		sql.append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoEstagioEnum.AGUARDANDO_ASSINATURA.name(), SituacaoAdicionalEstagioEnum.ASSINATURA_PENDENTE.name(), parametro);
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_TODOS, null);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<EstagioVO> consultaEstagioParaCancelamentoPorFaltaDeAssinatura(Integer qtdDiasMaximoParaAssinaturaEstagio) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
		sql.append(" and estagio.situacaoEstagioEnum = ? ");
		sql.append(" and estagio.situacaoAdicionalEstagioEnum = ? ");
		sql.append(" and (CURRENT_DATE::date - estagio.dataEnvioAssinaturaPendente::date) > ? ");
		sql.append(" and EXISTS (select dap.codigo from documentoassinadopessoa dap where dap.documentoassinado = documentoassinado.codigo ");
		sql.append(" and dap.tipopessoa in ('").append(TipoPessoa.MEMBRO_COMUNIDADE.name()).append("','").append(TipoPessoa.ALUNO.name()).append("')");
		sql.append(" and dap.situacaodocumentoassinadopessoa = '").append(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE.name()).append("' ");
		sql.append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoEstagioEnum.AGUARDANDO_ASSINATURA.name(), SituacaoAdicionalEstagioEnum.ASSINATURA_PENDENTE.name(), qtdDiasMaximoParaAssinaturaEstagio);
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_TODOS, null);
	}
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultaSeExisteEstagioParaCancelamentoPorFaltaDeAssinatura(Integer qtdDiasMaximoParaAssinaturaEstagio, Integer documentoAssinado) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT count(distinct estagio.codigo) QTDE ");
		sql.append(" from estagio ");
		sql.append(" inner join documentoAssinado on documentoAssinado.codigo = estagio.documentoAssinado ");
		sql.append(" and estagio.situacaoEstagioEnum = ? ");
		sql.append(" and estagio.situacaoAdicionalEstagioEnum = ? ");
		sql.append(" and documentoassinado.codigo = ? ");
		sql.append(" and (CURRENT_DATE::date - estagio.dataEnvioAssinaturaPendente::date) > ? ");
		sql.append(" and EXISTS (select dap.codigo from documentoassinadopessoa dap where dap.documentoassinado = documentoassinado.codigo ");
		sql.append(" and dap.tipopessoa in ('").append(TipoPessoa.MEMBRO_COMUNIDADE.name()).append("','").append(TipoPessoa.ALUNO.name()).append("')");
		sql.append(" and dap.situacaodocumentoassinadopessoa = '").append(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE.name()).append("' ");
		sql.append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), SituacaoEstagioEnum.AGUARDANDO_ASSINATURA.name(), SituacaoAdicionalEstagioEnum.ASSINATURA_PENDENTE.name(), documentoAssinado, qtdDiasMaximoParaAssinaturaEstagio);
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarOperacaoEstagioEmLoteSelecionados(EstagioVO estagio, OperacaoDeVinculoEstagioEnum operacaoDeVinculoEstagioEnum, String motivo, UsuarioVO usuario, ConfiguracaoEstagioObrigatorioVO configEstagio) throws Exception{
		try {
			switch (operacaoDeVinculoEstagioEnum) {
			case INDEFERIR:
				Uteis.checkState(!Uteis.isAtributoPreenchido(motivo), "O campo Motivo deve ser informado para que seja realizado o indeferimento do Estágio.");
				estagio.setMotivo(motivo);
				realizarConfirmacaoEstagioIndeferido(estagio, SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR, true ,usuario);
				break;
			case AGUARDANDO_ASSINATURA:
				Uteis.checkState(!Uteis.isAtributoPreenchido(estagio), "O Estágio vinculado ao Documento Assinado não foi encontrado para realizar essa operação.");
				estagio.setMotivo("");
				estagio.setSituacaoEstagioEnum(SituacaoEstagioEnum.AGUARDANDO_ASSINATURA);
				estagio.setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.PENDENTE_SOLICITACAO_ASSINATURA);
				estagio.setDataEnvioAssinaturaPendente(null);
				estagio.setGrupoPessoaItemVO(null);
				estagio.setDataIndeferimento(null);
				estagio.setResponsavelIndeferimento(null);
				estagio.setDataDeferimento(null);
				estagio.setResponsavelDeferimento(null);
				estagio.setDataEnvioAnalise(null);
				estagio.setDataLimiteAnalise(null);
				estagio.setDataEnvioCorrecao(null);
				estagio.setDataLimiteCorrecao(null);
				atualizarEstagioAguardandoAssinatura(estagio, usuario);
				break;
			case NOVO_TERMO_ESTAGIO_ASSINATURA:
				Uteis.checkState(!Uteis.isAtributoPreenchido(estagio), "O Estágio vinculado ao Documento Assinado não foi encontrado para realizar essa operação.");
				realizarNovoTermoAssinaturaEstagio(estagio, configEstagio, getAplicacaoControle().getConfiguracaoGeralSistemaVO(estagio.getMatriculaVO().getUnidadeEnsino().getCodigo(), usuario), usuario);
				break;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
	public void realizarProcessamentoNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio(ConfiguracaoEstagioObrigatorioVO config, UsuarioVO usuarioOperacaoExterna, EstagioVO estagio) throws Exception {
		boolean existePendente = false;
		estagio.setMotivo("Cancelamento automático por falta de assinaturas no prazo configurado.");
		estagio.setDataIndeferimento(new Date());
		estagio.setResponsavelIndeferimento(usuarioOperacaoExterna);
		estagio.setSituacaoEstagioEnum(SituacaoEstagioEnum.INDEFERIDO);
		estagio.setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR);
		getFacadeFactory().getEstagioFacade().atualizarEstagioIndeferimento(estagio, usuarioOperacaoExterna);

		for (DocumentoAssinadoPessoaVO dap : estagio.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa()) {
			if (getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultaSeExisteDocumentoAssinadoPessoaPorCodigoPorSituacao(dap.getCodigo(), SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) {
				DocumentoAssinadoPessoaVO clone = (DocumentoAssinadoPessoaVO) Uteis.clonar(dap);
				clone.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
				clone.setDataRejeicao(new Date());
				clone.setMotivoRejeicao("Bloqueado automático por falta de assinaturas no prazo configurado.");
				getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicao(clone);
				existePendente = true;
			}
		}
		if (existePendente && estagio.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
			estagio.getDocumentoAssinadoVO().setDocumentoAssinadoInvalido(true);
			estagio.getDocumentoAssinadoVO().setMotivoDocumentoAssinadoInvalido("Bloqueado automático por falta de assinaturas no prazo configurado");
			ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(estagio.getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), usuarioOperacaoExterna);
			getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(estagio.getDocumentoAssinadoVO().getCodigo(), estagio.getDocumentoAssinadoVO().isDocumentoAssinadoInvalido(), estagio.getDocumentoAssinadoVO().getMotivoDocumentoAssinadoInvalido(), usuarioOperacaoExterna);
			getFacadeFactory().getDocumentoAssinadoFacade().realizarBloqueioDocument(estagio.getDocumentoAssinadoVO(), configGEDVO, usuarioOperacaoExterna);
		}
		if (existePendente && estagio.getDocumentoAssinadoVO().getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
			estagio.getDocumentoAssinadoVO().setDocumentoAssinadoInvalido(true);
			estagio.getDocumentoAssinadoVO().setMotivoDocumentoAssinadoInvalido("Bloqueado automático por falta de assinaturas no prazo configurado");
			ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(estagio.getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), usuarioOperacaoExterna);
			getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(estagio.getDocumentoAssinadoVO().getCodigo(), estagio.getDocumentoAssinadoVO().isDocumentoAssinadoInvalido(), estagio.getDocumentoAssinadoVO().getMotivoDocumentoAssinadoInvalido(), usuarioOperacaoExterna);
			getFacadeFactory().getDocumentoAssinadoFacade().realizarBloqueioDocumentTechCert(estagio.getDocumentoAssinadoVO(), configGEDVO, usuarioOperacaoExterna);
		}
//		getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioNotificacaoDeCancelamentoPorFaltaDeAssinatura(estagio, config, usuarioOperacaoExterna);

	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataLimiteCorrecaoNotificacao(final EstagioVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(obj, "estagio", new AtributoPersistencia()
					.add("dataLimiteCorrecaoNotificacao", new Date()),					
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void processarEstagioSituacaoAguardandoParaRealizando(String codigoEstagios, UsuarioVO usuario) throws Exception {
		try {
			String[] listaCodigos = codigoEstagios.split(";");
			for (String codigo : listaCodigos) {
				try {
					DocumentoAssinadoVO doc = new DocumentoAssinadoVO();
					doc.setCodigo(Integer.parseInt(codigo.trim()));
					if(Uteis.isAtributoPreenchido(doc.getCodigo())) {
						realizarAlteracaoEstagioRealizando(doc, usuario);	
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarCorrecaoBancoDadosPorEstagioCancelado(String codigoEstagios, UsuarioVO usuario) throws Exception {
		try {
			StringBuilder sql = consultaPadraoEstagio();
			sql.append(" and estagio.codigo in ").append(codigoEstagios);
			SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			ConfiguracaoEstagioObrigatorioVO configEstagio = getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadrao(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario);
			List<EstagioVO> lista = montarDadosConsulta(resultado, Uteis.NIVELMONTARDADOS_TODOS, null);
			for (EstagioVO estagioVO : lista) {
				if(!Uteis.isAtributoPreenchido(estagioVO.getDocumentoAssinadoVO())) {
					processarCorrecaoEstagio(usuario, configEstagio, estagioVO);	
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	private void processarCorrecaoEstagio(UsuarioVO usuario, ConfiguracaoEstagioObrigatorioVO configEstagio, EstagioVO estagio) {
		ConfiguracaoGeralSistemaVO config = null;
		try {
			config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(estagio.getMatriculaVO().getUnidadeEnsino().getCodigo(), usuario);
			realizarConfirmacaoEstagioAssinaturaPendente(estagio, false, configEstagio, config, usuario);
			alterar(estagio, "estagio", new AtributoPersistencia().add("situacaoEstagioEnum", SituacaoEstagioEnum.AGUARDANDO_ASSINATURA).add("motivo", null),new AtributoPersistencia().add("codigo", estagio.getCodigo()), usuario);
		} catch (Exception e) {
			try {
				atualizarCampoSqlMensagem(estagio, "processarCorrecaoEstagio-"+e.getMessage(), usuario);	
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			config = null;
		}
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void atualizarEmailResponsavelConcedente(final EstagioVO obj) throws Exception {
		final String sql = "UPDATE estagio set emailResponsavelConcedente=?   WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);				
				sqlAlterar.setString(1, obj.getEmailResponsavelConcedente());			
				sqlAlterar.setInt(2, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<Integer> consultarFacilitadosInativos() throws Exception {
		List<Integer> grupoPessoaItemInativos = new ArrayList<>();
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct grupopessoaitem.codigo  from estagio inner join grupopessoaitem on grupopessoaitem.codigo = estagio.grupopessoaitem  where grupopessoaitem.statusativoinativoenum = 'INATIVO' and situacaoestagioenum in ('EM_ANALISE', 'EM_CORRECAO', 'AGUARDANDO_ASSINATURA', 'REALIZANDO') ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			grupoPessoaItemInativos.add(rs.getInt("codigo"));
		}
		return grupoPessoaItemInativos;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<EstagioVO> consultarEstagioPorSituacoesMatricula(List<SituacaoEstagioEnum> listaSituacaoEstagio , String matricula ,int nivelMontarDados , UsuarioVO usuarioVO ) throws Exception {
		StringBuilder sql = consultaPadraoEstagio();
//		if(listaSituacaoEstagio != null) {
//			sql.append(" AND estagio.situacaoEstagioEnum in(").append(UteisTexto.converteListaEnumParaCondicaoIn(listaSituacaoEstagio)).append(")");
//		}
		sql.append(" and estagio.matricula = ? ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarAproveitamentoEstagioDaGradeCurricularAntigaParaNovaGradeCurricularPorTransferenciaEntrada(MatriculaVO matriculaAtual , TransferenciaEntradaVO transferenciaEntrada ,String motivoAproveitamento , ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception {
		if(!matriculaAtual.getCurso().getCodigo().equals(transferenciaEntrada.getMatricula().getCurso().getCodigo()) || 
				matriculaAtual.getUnidadeEnsino().getCodigo().equals(transferenciaEntrada.getMatricula().getUnidadeEnsino().getCodigo())) {
			return ;
		}	
		
		List<GradeCurricularEstagioVO> listaGradeCurricularEstagioVO =  getFacadeFactory().getGradeCurricularEstagioFacade().consultarPorGradeCurricularVO(matriculaAtual.getGradeCurricularAtual(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		List<SituacaoEstagioEnum> listaSituacaoEstagio  = SituacaoEstagioEnum.getListaFiltroSituacaoEstagio();
		listaSituacaoEstagio.removeIf( s-> s.isIndeferido());
		List<EstagioVO> estagios = getFacadeFactory().getEstagioFacade().consultarEstagioPorSituacoesMatricula(listaSituacaoEstagio, transferenciaEntrada.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);

		if(Uteis.isAtributoPreenchido(estagios) && Uteis.isAtributoPreenchido(listaGradeCurricularEstagioVO)) {
			for(GradeCurricularEstagioVO objGradeDisciplina :listaGradeCurricularEstagioVO) {
				Iterator<EstagioVO> i = estagios.iterator();
	        	while(i.hasNext()) {
	        		EstagioVO estagio = (EstagioVO) i.next();
	        		if(Uteis.removeCaractersEspeciais(objGradeDisciplina.getNome().toLowerCase().trim()).equals(Uteis.removeCaractersEspeciais(estagio.getGradeCurricularEstagioVO().getNome().toLowerCase().trim()))) {
	        			estagio.setListaQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorQuestionarioEstagio(estagio.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));

	        			EstagioVO estagioClone  = estagio.getClonePorNovoEstagioAproveitamento();	        			
	        			estagioClone.setGradeCurricularEstagioVO(objGradeDisciplina);
	        			estagioClone.setMatriculaVO(matriculaAtual);
	        			incluir(estagioClone, false, usuarioVO);
	        			for(QuestionarioRespostaOrigemVO objQuestionario : estagioClone.getListaQuestionarioRespostaOrigemVO()) {
	        				
	        			//if(Uteis.isAtributoPreenchido(estagio.getQuestionarioRespostaOrigemUltimaVersao())) {
	        				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().executarClonePerguntaRespostaOrigemArquivo(objQuestionario, configGeralSistema, usuarioVO);		    					    			
		    			    getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().persistir(objQuestionario, usuarioVO);
		    			//}
	        			}
	    				 realizarRemoverVinculoDoumentoAssinadoEstagio(estagio, usuarioVO);
	    			    
						if (estagio.getSituacaoEstagioEnum().isRealizando()	|| 
							estagio.getSituacaoEstagioEnum().isAguardandoAssinatura() || 
							estagio.getSituacaoEstagioEnum().isEmAnalise() || 
							estagio.getSituacaoEstagioEnum().isEmCorrecao()) {

							estagio.setMotivo(motivoAproveitamento);
							getFacadeFactory().getEstagioFacade().realizarConfirmacaoEstagioIndeferido(estagio,  SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR,  false , usuarioVO);
						}
						//break;
	        		}        		
	        	}				
			}       	
        }	
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRemoverVinculoDoumentoAssinadoEstagio(EstagioVO obj , UsuarioVO usuario) throws Exception {
			alterar(obj, "estagio", new AtributoPersistencia().add("documentoAssinado",null),new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);	
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoGrupoPessoaItemEstagioPorRedistribuicaoFacilitadores(Integer codigo,	List<Integer> listaCodigosEstagios, boolean controlarAcesso, UsuarioVO usuarioLogado) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append( "UPDATE estagio set grupopessoaitem=? " );
//		sqlStr.append( " WHERE codigo in(" ).append(UteisTexto.converteListaInteiroParaString(listaCodigosEstagios)).append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado)) ;		
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());				
				sqlAlterar.setInt(1, codigo.intValue());			
				return sqlAlterar;
			}
		});	
	}	

	@Override
	public Integer consultarCargaHorariaCumpridaPorMatriculaEGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select sum(cargahoraria) as cargahoraria from (");
		sql.append(" select distinct codigo, cargahoraria from estagio ");
		sql.append(" inner join matricula on matricula.matricula = estagio.matricula ");
		sql.append(" where matricula.gradecurricularatual = ").append(gradeCurricular);
		sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		sql.append(" ) as t ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("cargahoraria");
		}
		return 0;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAproveitamentoEstagioTransferenciaUnidadeEnsino(MatriculaVO matriculaNova, MatriculaVO matriculaAproveitar, UsuarioVO usuario) throws Exception {
		getConexao().getJdbcTemplate().update("update estagio set matricula =  ?  where matricula = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), matriculaNova.getMatricula(), matriculaAproveitar.getMatricula());
//		List<SituacaoEstagioEnum> situacaoEstagio = new ArrayList<SituacaoEstagioEnum>(0); 
//		situacaoEstagio.add(SituacaoEstagioEnum.DEFERIDO);		
//		List<EstagioVO> estagioVOs = consultarEstagioPorSituacoesMatricula(null, matriculaAproveitar.getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
//		for(EstagioVO estagioVO: estagioVOs) {
//			estagioVO.setMatriculaVO(matriculaNova);
//			estagioVO.setCodigo(0);
//			estagioVO.setNovoObj(false);
//			estagioVO.setMatriculaVO(matriculaNova);
//			incluir(estagioVO, false, usuario);
//			for(QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO: estagioVO.getListaQuestionarioRespostaOrigemVO()) {
//				questionarioRespostaOrigemVO.setEstagioVO(estagioVO);
//				questionarioRespostaOrigemVO.setCodigo(0);
//				questionarioRespostaOrigemVO.setNovoObj(true);
//				for(QuestionarioRespostaOrigemMotivosPadroesEstagioVO questionarioRespostaOrigemMotivosPadroesEstagioVO: questionarioRespostaOrigemVO.getQuestionarioRespostaOrigemMotivosPadroesEstagioVOs()) {
//					questionarioRespostaOrigemMotivosPadroesEstagioVO.setCodigo(0);
//					questionarioRespostaOrigemMotivosPadroesEstagioVO.setNovoObj(true);
//					questionarioRespostaOrigemMotivosPadroesEstagioVO.setQuestionarioRespostaOrigemVO(questionarioRespostaOrigemVO);
//				}
//				for(PerguntaRespostaOrigemVO perguntaRespostaOrigemVO: questionarioRespostaOrigemVO.getPerguntaRespostaOrigemVOs()){
//					perguntaRespostaOrigemVO.setCodigo(0);
//					perguntaRespostaOrigemVO.setQuestionarioRespostaOrigemVO(questionarioRespostaOrigemVO);
//					perguntaRespostaOrigemVO.setNovoObj(true);
//					for(RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO: perguntaRespostaOrigemVO.getRespostaPerguntaRespostaOrigemVOs()) {
//						respostaPerguntaRespostaOrigemVO.setCodigo(0);
//						respostaPerguntaRespostaOrigemVO.setPerguntaRespostaOrigemVO(perguntaRespostaOrigemVO);
//						respostaPerguntaRespostaOrigemVO.setNovoObj(true);
//					}
//					for (List<PerguntaItemRespostaOrigemVO> listPerguntaItemRespostaOrigemVO :  perguntaRespostaOrigemVO.getPerguntaItemRespostaOrigemAdicionadaVOs()) {
//		        		for (PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO : listPerguntaItemRespostaOrigemVO) {
//		        			perguntaItemRespostaOrigemVO.setCodigo(0);
//		        			perguntaItemRespostaOrigemVO.setPerguntaRespostaOrigemVO(perguntaRespostaOrigemVO);
//		        			perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO().setQuestionarioRespostaOrigemVO(questionarioRespostaOrigemVO);
//		        			perguntaItemRespostaOrigemVO.setNovoObj(true);
//		        		}
//					}
//					for(PerguntaChecklistOrigemVO perguntaChecklistOrigemVO: perguntaRespostaOrigemVO.getListaPerguntaChecklistOrigem()) {
//						perguntaChecklistOrigemVO.setCodigo(0);
//						perguntaChecklistOrigemVO.setPerguntaRespostaOrigemVO(perguntaRespostaOrigemVO);
//						perguntaChecklistOrigemVO.setNovoObj(true);
//					}
//					for(ArquivoVO arquivoVO: perguntaRespostaOrigemVO.getListaArquivoVOs()) {
//						arquivoVO.setCodigo(0);
//						arquivoVO.setNovoObj(true);
//					}
//					perguntaRespostaOrigemVO.getArquivoRespostaVO().setCodigo(0);
//					perguntaRespostaOrigemVO.getArquivoRespostaVO().setNovoObj(true);
//				}
//				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().persistir(questionarioRespostaOrigemVO, usuario);
//			}
//			DocumentoAssinadoVO documentoAssinadoVO = estagioVO.getDocumentoAssinadoVO();
//			if(Uteis.isAtributoPreenchido(documentoAssinadoVO)) {
//				documentoAssinadoVO.setCodigo(0);
//				documentoAssinadoVO.setCodigoOrigem(estagioVO.getCodigo());
//				documentoAssinadoVO.setNovoObj(false);
//				for(DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO: documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
//					documentoAssinadoPessoaVO.setCodigo(0);
//					documentoAssinadoPessoaVO.setNovoObj(true);
//					documentoAssinadoPessoaVO.setDocumentoAssinadoVO(documentoAssinadoVO);
//				}
//				getFacadeFactory().getDocumentoAssinadoFacade().incluir(documentoAssinadoVO, false, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(matriculaNova.getUnidadeEnsino().getCodigo(), usuario));
//			}
//		}
	}
	
	@Override
	public Integer consultarCargaHorariaEstagioRealizadoMatriculaVinculadosOutrasDisciplinas(String matricula, List<Integer> disciplinas) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT sum(cargahorariadeferida) as cargaHoraria from estagio where matricula = ?  ")
				.append(" AND situacaoestagioenum = 'DEFERIDO' ");
		if (Uteis.isAtributoPreenchido(disciplinas)) {
			sql.append(" and (disciplina is null or disciplina not in (");
			disciplinas.forEach(d -> {
				sql.append(d + ", ");
			});
			sql.append("0))");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		if (rs.next()) {
			return rs.getInt("cargaHoraria");
		}
		return 0;
	}
	
	@Override
	public Boolean validarAlunoUtilizaComponenteEstagio(String matricula) throws Exception {
		String sql = "SELECT codigo FROM estagio WHERE matricula = ? AND situacaoestagioenum = 'DEFERIDO'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		return tabelaResultado.next();
	}
}