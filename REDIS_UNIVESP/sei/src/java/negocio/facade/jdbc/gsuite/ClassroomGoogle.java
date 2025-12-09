package negocio.facade.jdbc.gsuite;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.gsuite.ClassroomGoogleInterfaceFacade;
import webservice.arquitetura.InfoWSVO;

@Repository
@Scope("singleton")
@Lazy
public class ClassroomGoogle extends ControleAcesso implements ClassroomGoogleInterfaceFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2446343796966925380L;
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ClassroomGoogleVO realizarGeracaoClassroomGooglePorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO pto, ClassroomGoogleVO obj , UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTurmaVO()), "O campo Turma deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDisciplinaVO()), "O campo Disciplina deve ser informado.");
		boolean turmaDisciplinaEad = getFacadeFactory().getTurmaDisciplinaFacade().consultarSeTurmaDisciplinaSaoEadPorUnidadeEnsinoPorCursoPorNivelEducacionalPorTurmaPorDisciplina(pto.getUnidadeEnsinoVO().getCodigo(), pto.getCursoVO().getCodigo(), pto.getNivelEducacional(), obj.getTurmaVO().getCodigo(), obj.getDisciplinaVO().getCodigo(), usuarioVO); 
		Uteis.checkState((obj.getTurmaVO().getAnual() || obj.getTurmaVO().getSemestral()) && !Uteis.isAtributoPreenchido(obj.getAno()), "O campo Ano (Classroom) deve ser informado.");
		Uteis.checkState((obj.getTurmaVO().getSemestral()) && !Uteis.isAtributoPreenchido(obj.getSemestre()), "O campo Semestre (Classroom) deve ser informado.");		
		Uteis.checkState(!turmaDisciplinaEad && Uteis.isAtributoPreenchido(pto.getUnidadeEnsinoVO().getCodigo()), "Não foi encontrado para a TURMA "+obj.getTurmaVO().getIdentificadorTurma()+" informada na Unidade Ensino "+ pto.getUnidadeEnsinoVO().getNome()+" a disciplina "+obj.getDisciplinaVO().getNome()+" com a Regra de Definição de Tutoria Online Dimânica.");
		Uteis.checkState(!turmaDisciplinaEad && Uteis.isAtributoPreenchido(pto.getCursoVO().getCodigo()), "Não foi encontrado para a TURMA "+obj.getTurmaVO().getIdentificadorTurma()+" informada no Curso "+ pto.getCursoVO().getNome()+" a disciplina "+obj.getDisciplinaVO().getNome()+" com a Regra de Definição de Tutoria Online Dimânica.");
		Uteis.checkState(!turmaDisciplinaEad && Uteis.isAtributoPreenchido(pto.getCursoVO().getNivelEducacional()), "Não foi encontrado para a TURMA "+obj.getTurmaVO().getIdentificadorTurma()+" informada no Curso "+ pto.getCursoVO().getNome()+" com o Nível Educacional "+pto.getCursoVO().getNivelEducacional_Apresentar()+" a disciplina "+obj.getDisciplinaVO().getNome()+" com a Regra de Definição de Tutoria Online Dimânica.");
		Uteis.checkState(!turmaDisciplinaEad, "Não existe para a TURMA informada a disciplina "+obj.getDisciplinaVO().getNome()+" com a Regra de Definição de Tutoria Online Dimânica.");
		Uteis.checkState(!obj.getTurmaVO().getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL), "Não é possível gerar Classroom de uma Subturma.");
		return realizarGeracaoClassroomGoogle(obj, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ClassroomGoogleVO realizarGeracaoClassroomGoogle(ClassroomGoogleVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTurmaVO()), "O campo Turma deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDisciplinaVO()), "O campo Disciplina deve ser informado.");
		Uteis.checkState((!Uteis.isAtributoPreenchido(obj.getTurmaVO().getCurso().getIntegral()) && !Uteis.isAtributoPreenchido(obj.getProfessorEad()) && !Uteis.isAtributoPreenchido(obj.getAno())), "O campo Ano deve ser informado.");
		Uteis.checkState((!Uteis.isAtributoPreenchido(obj.getTurmaVO().getCurso().getIntegral()) && !Uteis.isAtributoPreenchido(obj.getTurmaVO().getCurso().getAnual()) && !Uteis.isAtributoPreenchido(obj.getProfessorEad()) && !Uteis.isAtributoPreenchido(obj.getSemestre())), "O campo Semestre deve ser informado.");
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);
		HttpResponse<JsonNode> jsonResponse = unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_PERSISTIR, json, RequestMethod.POST, null, usuarioVO);
		return gson.fromJson(jsonResponse.getBody().toString(), ClassroomGoogleVO.class);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarAtualizacaoProfessorAuxiliarClassroomPorLista(List<ClassroomGoogleVO> lista , ProgressBarVO progressBarVO, boolean isIncluir, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		String operacao =  isIncluir ? "INCLUIR": "EXCLUIR";
		Map<String, String> headers = new HashMap<String, String>();
		progressBarVO.setMsgLogs("");
		progressBarVO.setMsgLogs(progressBarVO.preencherCabecalhoOperacao("Log Professor Auxiliar Operação:"+operacao));
		for (ClassroomGoogleVO classroomVO : lista) {
			if (classroomVO.isSelecionado()) {
				try {
					StringBuilder sb = new StringBuilder();
					sb.append(" Processando ").append(progressBarVO.getProgresso()).append(" de ").append(progressBarVO.getMaxValue());
					sb.append(" - (Turma Atual = ").append(classroomVO.getTurmaVO().getIdentificadorTurma()).append(") ");
					progressBarVO.setStatus(sb.toString());
					headers.clear();
					headers.put(UteisWebServiceUrl.code, classroomVO.getIdClassRoomGoogle());
					headers.put(UteisWebServiceUrl.operacao, operacao );
					HttpResponse<JsonNode> jsonResponse = unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_ATUALIZAR_PROFESSOR_AUXILIAR, RequestMethod.PUT, headers, usuarioVO);
					InfoWSVO rep = new Gson().fromJson(jsonResponse.getBody().toString(), InfoWSVO.class);
					if (jsonResponse.getStatus() != (HttpStatus.OK.value())) {				
						tratarMensagemErroWebService(rep, String.valueOf(jsonResponse.getStatus()), jsonResponse.getBody().toString());
					}else {
						progressBarVO.setMsgLogs(progressBarVO.getMsgLogs() + progressBarVO.getPreencherCamposDescricaoOperacaoLog("Turma : "+classroomVO.getTurmaVO().getIdentificadorTurma()+" - OK"));
					}
				} catch (Exception e) {
					progressBarVO.setMsgLogs(progressBarVO.getMsgLogs() + progressBarVO.getPreencherCamposDescricaoOperacaoLog("Turma : "+classroomVO.getTurmaVO().getIdentificadorTurma()+ " - Erro" + System.lineSeparator() + e.getMessage()));
				} finally {
					progressBarVO.incrementar();
				}
			}
		}		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public kong.unirest.HttpResponse<JsonNode> realizarAtualizacaoProfessorAuxiliarClassroom(ClassroomGoogleVO obj , boolean isIncluir, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.code, obj.getIdClassRoomGoogle());
		headers.put(UteisWebServiceUrl.operacao, isIncluir ? "INCLUIR": "EXCLUIR" );	
		return unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_ATUALIZAR_PROFESSOR_AUXILIAR, RequestMethod.PUT, headers, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ClassroomGoogleVO realizarRevisaoClassroom(ClassroomGoogleVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), NivelMontarDados.BASICO, usuarioVO));
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);
		HttpResponse<JsonNode> jsonResponse = unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_REALIZAR_REVISAO, json, RequestMethod.PUT, null, usuarioVO);
		return gson.fromJson(jsonResponse.getBody().toString(), ClassroomGoogleVO.class);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarRevisaoClassroomPorTurma(TurmaVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		if(Uteis.isAtributoPreenchido(configSeiGsuiteVO)) {
			Gson gson = inicializaGson();
			String json = gson.toJson(obj);
			unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_REALIZAR_REVISAO_TURMA, json, RequestMethod.PUT, null, usuarioVO);	
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarRevisaoClassroomPorTurmaPorDisciplinaPorAnoPorSemestrePorProfessor(TurmaVO turma, DisciplinaVO displina , String ano, String semestre, Integer professor, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		if(Uteis.isAtributoPreenchido(configSeiGsuiteVO)) {
			ClassroomGoogleVO classroom = consultarSeExisteClassroom(turma.getCodigo(), displina.getCodigo(), ano, semestre, professor);
			if(Uteis.isAtributoPreenchido(classroom)) {
				classroom.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(classroom.getTurmaVO().getCodigo(), NivelMontarDados.BASICO, usuarioVO));
				Gson gson = inicializaGson();
				String json = gson.toJson(classroom);
				unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_REALIZAR_REVISAO, json, RequestMethod.PUT, null, usuarioVO);
			}	
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarExclusaoClassroomGooglePorHorarioTurmaDisciplinaProgramada(HorarioTurmaDisciplinaProgramadaVO obj, HorarioTurmaVO horarioTurma, HorarioTurmaDiaVO horarioTurmaDia , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Gson gson = inicializaGson();
		String json = gson.toJson(obj.getClassroomGoogleVO());
		unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_EXCLUIR, json, RequestMethod.DELETE, null, usuarioVO);
		obj.getClassroomGoogleVO().setCodigo(0);
		obj.getClassroomGoogleVO().setLinkClassroom("");
		executarAtualizacaoDoGoogleMeetVinculadoAoClassroom(obj, horarioTurma);
		executarAtualizacaoDoGoogleMeetVinculadoAoClassroom(obj, horarioTurmaDia);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void executarAtualizacaoDoGoogleMeetVinculadoAoClassroom(HorarioTurmaDisciplinaProgramadaVO obj, HorarioTurmaVO horarioTurma) {
		for (HorarioTurmaDiaVO htd : horarioTurma.getHorarioTurmaDiaVOs()) {
			executarAtualizacaoDoGoogleMeetVinculadoAoClassroom(obj, htd);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void executarAtualizacaoDoGoogleMeetVinculadoAoClassroom(HorarioTurmaDisciplinaProgramadaVO obj, HorarioTurmaDiaVO horarioTurmaDia) {
			for (HorarioTurmaDiaItemVO htdi : horarioTurmaDia.getHorarioTurmaDiaItemVOs()) {
				if(htdi.getDisciplinaVO().getCodigo().equals(obj.getCodigoDisciplina())) {
					htdi.setGoogleMeetVO(new GoogleMeetVO());
					htdi.setGerarEventoAulaOnLineGoogleMeet(false);
				}
			}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarExclusaoClassroomGoogle(ClassroomGoogleVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);
		unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_EXCLUIR, json, RequestMethod.DELETE, null, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarExclusaoClassroomGoogleEad(ClassroomGoogleVO objFiltro , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		if(Uteis.isAtributoPreenchido(configSeiGsuiteVO)) {
			Gson gson = inicializaGson();
			String json = gson.toJson(objFiltro);
			unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_EAD_EXCLUIR, json, RequestMethod.DELETE, null, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarBuscaAlunoClassroom(ClassroomGoogleVO obj,  ConfiguracaoGeralSistemaVO confGeral, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.code, obj.getIdClassRoomGoogle());
		HttpResponse<JsonNode> jsonResponse = unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_CONSULTAR_STUDENTS, RequestMethod.GET, headers, usuarioVO);
		Gson gson = inicializaGson();
		Type listaType = new TypeToken<ArrayList<PessoaGsuiteVO>>(){}.getType();
		List<PessoaGsuiteVO> listaEstudante =  gson.fromJson(jsonResponse.getBody().toString(), listaType);
		obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), NivelMontarDados.BASICO, usuarioVO));
		List<PessoaGsuiteVO> listaEstudanteHistorico = new ArrayList<>();
		if(Uteis.isAtributoPreenchido(obj.getProfessorEad())) {
			listaEstudanteHistorico =  getFacadeFactory().getPessoaGsuiteFacade().consultarAlunosDoEadTurmaDisciplinaDisponivelGsuite(obj.getTurmaVO(), obj.getDisciplinaVO(), obj.getAno(), obj.getSemestre(), obj.getProfessorEad().getCodigo(), false, usuarioVO);
		}else {
			listaEstudanteHistorico =  getFacadeFactory().getPessoaGsuiteFacade().consultarAlunosDoHorarioTurmaDisciplinaDisponivelGsuite(0, obj.getTurmaVO().getCurso().getCodigo(), obj.getTurmaVO(), obj.getDisciplinaVO().getCodigo(), obj.getAno(), obj.getSemestre(), 0, false,  confGeral, usuarioVO);
		}	
		listaEstudanteHistorico.stream()
		.filter(p-> Uteis.isAtributoPreenchido(p.getEmail()) && listaEstudante.stream().anyMatch(pp-> pp.getEmail().equals(p.getEmail())))
		.forEach(p-> p.setPessoaGsuiteClassroom(true));
		obj.setClassroomStudentVOs(listaEstudanteHistorico);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarEnvioConviteAlunoClassroom(ClassroomGoogleVO obj,  PessoaGsuiteVO pessoaGsuite, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(pessoaGsuite.getEmail()), "O Conta de Email no Gsuite deve ser informado para o Aluno "+pessoaGsuite.getPessoaVO().getNome());
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.code, obj.getIdClassRoomGoogle());
		headers.put(UteisWebServiceUrl.emailpessoagsuite, pessoaGsuite.getEmail());
		unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_ADICIONAR_STUDENTS, RequestMethod.PUT, headers, usuarioVO);
		obj.getClassroomStudentVOs().stream().filter(p-> p.getEmail().equals(pessoaGsuite.getEmail())).forEach(p-> p.setPessoaGsuiteClassroom(true));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarAtualizacaoAlunoClassroom(ClassroomGoogleVO obj,  UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);
		return unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_ATUALIZACAO_STUDENTS, json, RequestMethod.PUT, null, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarVerificacaoSeAlunoEstaVinculadoAoClassroomGoogle(ClassroomGoogleVO obj,  PessoaGsuiteVO pessoaGsuite, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.code, obj.getIdClassRoomGoogle());
		headers.put(UteisWebServiceUrl.emailpessoagsuite, pessoaGsuite.getEmail());
		HttpResponse<JsonNode> jsonResponse = unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_CONSULTAR_STUDENT, RequestMethod.GET, headers, usuarioVO);
		Gson gson = inicializaGson();
		PessoaGsuiteVO pg =  gson.fromJson(jsonResponse.getBody().toString(), PessoaGsuiteVO.class);
		if(pg == null || !Uteis.isAtributoPreenchido(pg.getEmail())) {
			realizarEnvioConviteAlunoClassroom(obj, pessoaGsuite, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoClassroomGooglePorHorarioTurma(HorarioTurmaVO obj , UsuarioVO usuarioVO) throws Exception {
		ClassroomGoogleVO c = consultarSeExisteClassroom(obj.getTurma().getCodigo(),  obj.getDisciplina().getCodigo(), obj.getAnoVigente(), obj.getSemestreVigente(), null);
		if(!Uteis.isAtributoPreenchido(c)) {
			c = new ClassroomGoogleVO(obj.getTurma(), obj.getDisciplina(), obj.getAnoVigente(), obj.getSemestreVigente());
			realizarGeracaoClassroomGoogle(c, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarProcessamentoLoteClassroom(UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		return unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_CONTA_LOTE, RequestMethod.POST, null, usuarioVO);		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarProcessamentoLoteClassroomPorHorarioTurma(HorarioTurmaVO obj, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.horarioTurma, obj.getCodigo().toString());
		return unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_CONTA_LOTE_HORARIO_TURMA, RequestMethod.POST, headers, usuarioVO);		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarClassroomOperacao(UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_OPERACAO_STUDENTS, RequestMethod.PUT, null, usuarioVO);		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarAlteracaoDonoDriveClassroom(ClassroomGoogleVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(UteisWebServiceUrl.code, obj.getIdClassRoomGoogle());
		headers.put(UteisWebServiceUrl.data, Uteis.getData(new Date(), "yyyy-MM-dd HH:mm:ss"));
		unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_ALTERACAO_DONO_DRIVE, RequestMethod.PUT, headers, usuarioVO);
	}

	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void correcaoNomeClassroom(UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_CLASSROOM_GOOGLE + UteisWebServiceUrl.URL_CLASSROOM_GOOGLE_CONTA_LOTE_CORRECAO_NOME, RequestMethod.GET, null, usuarioVO);		
	}
	
	
//	@Override
//	public void consultarClassroomPorDataModelo(DataModelo dataModeloGoogleMeet, TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
//		dataModeloGoogleMeet.setListaConsulta(consultarClassroom(dataModeloGoogleMeet,turmaVO, ano, semestre, usuarioLogado));
//		
//	}

	@Override
	public List<ClassroomGoogleVO> consultarClassroom(TurmaVO turmaVO, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		List<Object> listaFiltros = new ArrayList<>();
		montarFiltrosConsultaPadrao(listaFiltros, turmaVO, ano, semestre, sql);
		sql.append(" order by classroomGoogle.codigo desc");
		//UteisTexto.addLimitAndOffset(sql, dataModeloGoogleMeet.getLimitePorPagina(), dataModeloGoogleMeet.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), listaFiltros.toArray());		
		return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}
	
	
	private void montarFiltrosConsultaPadrao(List<Object> listaFiltros, TurmaVO turmaVO, String ano, String semestre, StringBuilder sql) {
		sql.append(" WHERE 1 = 1");
		if (Uteis.isAtributoPreenchido(turmaVO.getIdentificadorTurma())) {
			sql.append(" and turma.identificadorTurma = ? ");
			listaFiltros.add(turmaVO.getIdentificadorTurma());
		}

		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and classroomGoogle.ano = ? ");
			listaFiltros.add(ano);
		}
		
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and classroomGoogle.semestre = ? ");
			listaFiltros.add(semestre);
		}
	}
	
	
	public List<ClassroomGoogleVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<ClassroomGoogleVO> vetResultado = new ArrayList<ClassroomGoogleVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}	
	
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) over() as totalRegistroConsulta,  classroomGoogle.codigo as \"classroomGoogle.codigo\", classroomGoogle.ano as \"classroomGoogle.ano\", ");
		sql.append(" classroomGoogle.semestre as \"classroomGoogle.semestre\",  classroomGoogle.linkClassroom as \"classroomGoogle.linkClassroom\", ");
		sql.append(" classroomGoogle.idClassRoomGoogle as \"classroomGoogle.idClassRoomGoogle\",  classroomGoogle.idCalendario as \"classroomGoogle.idCalendario\", ");		
		sql.append(" classroomGoogle.idTurma as \"classroomGoogle.idTurma\",  ");
		sql.append(" classroomGoogle.emailTurma as \"classroomGoogle.emailTurma\",  ");
		sql.append(" classroomGoogle.professoread as \"classroomGoogle.professoread\",  ");
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
		sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.razaosocial as \"unidadeensino.razaosocial\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\" ");
		sql.append(" from classroomGoogle ");
		sql.append(" inner join turma on turma.codigo =  classroomGoogle.turma ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo =  turma.unidadeensino ");
		sql.append(" inner join disciplina on disciplina.codigo =  classroomGoogle.disciplina ");
		sql.append(" left join pessoa on pessoa.codigo =  classroomGoogle.professoread ");		
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<ClassroomGoogleVO> consultarClassroomEad(Integer unidadeEnsino, Integer curso, String nivelEducacional, Integer turma, Integer disciplina, String ano, String semestre, Integer professorEad,  int nivelMontarDados ) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" left join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo = (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo limit 1 ) ) )");
		sqlStr.append(" where 1=1 ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		if(Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso.codigo = ").append(curso).append(" ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" and curso.niveleducacional = '").append(nivelEducacional).append("' ");
		}
		if(Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and turma.codigo = ").append(turma).append(" ");
		}
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina).append(" ");
		}
		if(Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and classroomGoogle.ano = '").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and classroomGoogle.semestre = '").append(semestre).append("' ");
		}
		if(Uteis.isAtributoPreenchido(professorEad)) {
			sqlStr.append(" and classroomGoogle.professoread =  ").append(professorEad).append(" ");	
		}		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ClassroomGoogleVO consultarSeExisteClassroom(Integer turma, Integer disciplina, String ano, String semestre, Integer professorEad) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" where turma.codigo =  ? ");
		sqlStr.append(" and disciplina.codigo = ? ");
		sqlStr.append(" and classroomGoogle.ano = ? ");
		sqlStr.append(" and classroomGoogle.semestre = ? ");
		if(Uteis.isAtributoPreenchido(professorEad)) {
			sqlStr.append(" and classroomGoogle.professoread =  ").append(professorEad).append(" ");	
		}		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { turma, disciplina,  ano, semestre});
		ClassroomGoogleVO obj = new ClassroomGoogleVO();
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
		}
		return obj;
	}
	
	@Override
	public Boolean consultarSeExisteAlgumClassroomGoogleTutoriaOnline(PessoaVO pessoa, String ano, String semestre,UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSqlConsultaClassroomGoogleTutoriaOnline(pessoa, ano, semestre);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return dadosSQL.next();
	}
	
	@Override
	public List<ClassroomGoogleVO> consultarClassroomGoogleTutoriaOnline(PessoaVO pessoa, String ano, String semestre,UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSqlConsultaClassroomGoogleTutoriaOnline(pessoa, ano, semestre);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ClassroomGoogleVO> vetResultado = new ArrayList<ClassroomGoogleVO>(0);
		while (dadosSQL.next()) {
			ClassroomGoogleVO obj = new ClassroomGoogleVO();
			obj.setCodigo(dadosSQL.getInt("codigoClassroom"));
			obj.setIdClassRoomGoogle(dadosSQL.getString("idClassRoomGoogle"));
			obj.setLinkClassroom(dadosSQL.getString("linkClassroom"));
			obj.setIdTurma(dadosSQL.getString("idTurma"));
			obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
			obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
			obj.getTurmaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
			obj.getTurmaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
			obj.getTurmaVO().getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino.razaoSocial"));
			obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
			obj.setAno(dadosSQL.getString("ano"));
			obj.setSemestre(dadosSQL.getString("semestre"));			
			obj.setProfessorEad(pessoa);
			
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private StringBuilder getSqlConsultaClassroomGoogleTutoriaOnline(PessoaVO pessoa, String ano, String semestre) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" (select ");
		sqlStr.append(" classroomGoogle.codigo as codigoClassroom, classroomGoogle.idClassRoomGoogle,  ");
		sqlStr.append(" classroomGoogle.linkClassroom,  classroomGoogle.idTurma, ");
		sqlStr.append(" classroomGoogle.ano, classroomGoogle.semestre, ");
		sqlStr.append(" turma.codigo as \"turma.codigo\", turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sqlStr.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", ");
		sqlStr.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", unidadeEnsino.razaoSocial as \"unidadeEnsino.razaoSocial\"  ");
		sqlStr.append(" from classroomGoogle ");
		sqlStr.append(" inner join turma on turma.codigo = classroomGoogle.turma  ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = classroomGoogle.disciplina ");
		sqlStr.append(" inner join unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
		sqlStr.append(" where classroomGoogle.professoread = ").append(pessoa.getCodigo());
		if(Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and classroomGoogle.ano = '").append(ano).append("' ");
			sqlStr.append(" and classroomGoogle.semestre = '").append(semestre).append("'");
		}
		sqlStr.append(" ) ");
		sqlStr.append(" union all ");
		sqlStr.append(" (select ");
		sqlStr.append(" 0 as codigoClassroom, '' as idClassRoomGoogle,  ");
		sqlStr.append(" '' as linkClassroom, '' as idTurma, ");
		sqlStr.append(" matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre, ");
		sqlStr.append(" turma.codigo as \"turma.codigo\", turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sqlStr.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", ");
		sqlStr.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\" , unidadeEnsino.razaoSocial as \"unidadeEnsino.razaoSocial\" ");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma and turma.situacao = 'AB' ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append(" inner join curso on curso.codigo = turma.curso ");
		sqlStr.append(" inner join unidadeEnsino on unidadeEnsino.codigo = turma.unidadeEnsino ");
		sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.codigo = matriculaperiodoturmadisciplina.programacaotutoriaonlineprofessor ");
		sqlStr.append(" inner join programacaotutoriaonline on programacaotutoriaonline.codigo = programacaotutoriaonlineprofessor.programacaotutoriaonline  and programacaotutoriaonline.executarclassroomautomatico ");		
		sqlStr.append(" inner join turmadisciplina on  turmadisciplina.turma =  turma.codigo   ");
		sqlStr.append(" and  turmadisciplina.disciplina =  disciplina.codigo ");
		sqlStr.append(" and  turmadisciplina.definicoestutoriaonline  = 'DINAMICA'  ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.professor = ").append(pessoa.getCodigo());
		if(Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and (");
			sqlStr.append(" 	(curso.periodicidade = 'IN')");
			sqlStr.append(" 	or (curso.periodicidade = 'AN' and turma.anual and matriculaperiodoturmadisciplina.ano='").append(ano).append("')");
			sqlStr.append(" 	or (curso.periodicidade = 'SE' and turma.semestral and matriculaperiodoturmadisciplina.ano= '").append(ano).append("' and matriculaperiodoturmadisciplina.semestre= '").append(semestre).append("') ");
			sqlStr.append(" )");	
		}
		sqlStr.append(" and not exists (   ");
		sqlStr.append(" select classroomgoogle.codigo from classroomgoogle where classroomgoogle.turma =  turma.codigo  ");
		sqlStr.append(" and  classroomgoogle.disciplina =  disciplina.codigo ");
		sqlStr.append(" and  classroomgoogle.ano =  matriculaperiodoturmadisciplina.ano ");
		sqlStr.append(" and  classroomgoogle.semestre =  matriculaperiodoturmadisciplina.semestre ");
		sqlStr.append(" and  classroomgoogle.professoread =  ").append(pessoa.getCodigo());
		sqlStr.append(" )  ");
		sqlStr.append(" group by   ");
		sqlStr.append(" turma.codigo, ");
		sqlStr.append(" turma.identificadorTurma, ");
		sqlStr.append(" disciplina.codigo, ");
		sqlStr.append(" disciplina.nome, ");
		sqlStr.append(" unidadeEnsino.codigo, ");
		sqlStr.append(" unidadeEnsino.nome, ");
		sqlStr.append(" unidadeEnsino.razaoSocial, ");
		sqlStr.append(" matriculaperiodoturmadisciplina.ano, ");
		sqlStr.append(" matriculaperiodoturmadisciplina.semestre, ");
		sqlStr.append(" idClassRoomGoogle, ");
		sqlStr.append(" linkClassroom, ");
		sqlStr.append(" idTurma, ");
		sqlStr.append(" codigoClassroom ");
		sqlStr.append(" ) ");
		sqlStr.append(" order by \"turma.identificadorTurma\", \"disciplina.nome\" ") ;
		return sqlStr;
	}
	
	private ClassroomGoogleVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) {
		ClassroomGoogleVO obj = new ClassroomGoogleVO();
		obj.setNovoObj(false);
		obj.setCodigo(dadosSQL.getInt("classroomGoogle.codigo"));
		obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
		obj.setAno(dadosSQL.getString("classroomGoogle.ano"));
		obj.setSemestre(dadosSQL.getString("classroomGoogle.semestre"));
		obj.setLinkClassroom(dadosSQL.getString("classroomGoogle.linkClassroom"));
		obj.setIdClassRoomGoogle(dadosSQL.getString("classroomGoogle.idClassRoomGoogle"));
		obj.setIdTurma(dadosSQL.getString("classroomGoogle.idTurma"));
		obj.setEmailTurma(dadosSQL.getString("classroomGoogle.emailTurma"));
		obj.setIdCalendario(dadosSQL.getString("classroomGoogle.idCalendario"));
		obj.getProfessorEad().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getProfessorEad().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getTurmaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getTurmaVO().getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino.razaosocial"));
		return obj;
	}

}
