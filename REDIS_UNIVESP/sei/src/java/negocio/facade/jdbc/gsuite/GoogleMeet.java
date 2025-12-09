package negocio.facade.jdbc.gsuite;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import controle.arquitetura.DataModelo;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.gsuite.GoogleMeetInterfaceFacade;
import webservice.servicos.objetos.AgendaAlunoRSVO;
import webservice.servicos.objetos.DataEventosRSVO;

@Repository
@Scope("singleton")
@Lazy
public class GoogleMeet extends ControleAcesso implements GoogleMeetInterfaceFacade {

	private static final long serialVersionUID = -6665818685157851935L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarExclusaoEventoGoogleMeetVisaoProfessor(DataEventosRSVO dataEvento, AgendaAlunoRSVO agenda, boolean isTodosHorarios, UsuarioVO usuario) throws Exception {
		List<HorarioTurmaDiaItemVO> listaHorarioEvento = new ArrayList<>();
		listaHorarioEvento.add(agenda.getHorarioProfessorDiaItemVO().getHorarioTurmaDiaItemVO());
		if(isTodosHorarios) {
			List<HorarioProfessorDiaItemVO> listaTemp = dataEvento.getAgendaAlunoRSVOs().stream().flatMap(p-> Stream.of(p.getHorarioProfessorDiaItemVO())).collect(Collectors.toList());
			Ordenacao.ordenarLista(listaTemp, "nrAula");		
			executarMontagemHorarioTurmaDiaSubsequente(agenda, listaHorarioEvento, listaTemp, true);
		}
		realizarOperacaoGoogleMeetPorListaHorarioTurmaDiaItem(listaHorarioEvento, true, usuario);
		forHTDI:for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : listaHorarioEvento) {
			for (AgendaAlunoRSVO agendaExistente : dataEvento.getAgendaAlunoRSVOs()) {
				if(agendaExistente.getHorarioProfessorDiaItemVO().getHorarioTurmaDiaItemVO().getCodigo().equals(horarioTurmaDiaItemVO.getCodigo())) {
					agendaExistente.getHorarioProfessorDiaItemVO().getHorarioTurmaDiaItemVO().setGoogleMeetVO(new GoogleMeetVO());
					agendaExistente.setGoogleMeetVO(new GoogleMeetVO());
					continue forHTDI;
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoEventoGoogleMeetVisaoProfessor(DataEventosRSVO dataEvento, AgendaAlunoRSVO agenda,  boolean isTodosHorarios, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {		
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuario);
		Uteis.checkState(!agenda.getHorarioProfessorDiaItemVO().isHorarioProfessorDiaItemDentroDoLimiteFinalDeRealizacao(), "Não é possível lançar um evento Retroativo.");
		List<HorarioTurmaDiaItemVO> listaHorarioEvento = new ArrayList<>();
		listaHorarioEvento.add(agenda.getHorarioProfessorDiaItemVO().getHorarioTurmaDiaItemVO());
		if(isTodosHorarios) {
			List<HorarioProfessorDiaItemVO> listaTemp = dataEvento.getAgendaAlunoRSVOs().stream().flatMap(p-> Stream.of(p.getHorarioProfessorDiaItemVO())).collect(Collectors.toList());
			Ordenacao.ordenarLista(listaTemp, "nrAula");		
			executarMontagemHorarioTurmaDiaSubsequente(agenda, listaHorarioEvento, listaTemp, false);	
		}
		executarGeracaoGoogleMeetSubSequente(listaHorarioEvento, true, configSeiGsuiteVO, configuracaoGeralSistemaVO, usuario);
		forHTDI:for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : listaHorarioEvento) {
			for (AgendaAlunoRSVO agendaExistente : dataEvento.getAgendaAlunoRSVOs()) {
				if(agendaExistente.getHorarioProfessorDiaItemVO().getHorarioTurmaDiaItemVO().getCodigo().equals(horarioTurmaDiaItemVO.getCodigo())) {
					agendaExistente.getHorarioProfessorDiaItemVO().getHorarioTurmaDiaItemVO().setGoogleMeetVO(horarioTurmaDiaItemVO.getGoogleMeetVO());
					agendaExistente.setGoogleMeetVO(horarioTurmaDiaItemVO.getGoogleMeetVO());
					continue forHTDI;
				}
			}
		}
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarOperacaoGoogleMeetVisaoAdministrativa(HorarioTurmaDiaItemVO htdiSelecionado,  HorarioTurmaVO horarioTurma, HorarioTurmaDiaVO horarioTurmaDiaVO, boolean isExclusao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			for (HorarioTurmaDiaVO htd : horarioTurma.getHorarioTurmaDiaVOs()) {
				if (htd.getCodigo().equals(horarioTurmaDiaVO.getCodigo())) {
					htdiSelecionado.setHorarioTurmaDiaVO(htd);
					htdiSelecionado.getHorarioTurmaDiaVO().setHorarioTurma(horarioTurma);
					List<HorarioTurmaDiaItemVO> listaHtdiGoogleMeet = new ArrayList<>();
					listaHtdiGoogleMeet.add(htdiSelecionado);
					if(htdiSelecionado.getGoogleMeetVO().isEventoAulasSubsequentes()) {
						List<HorarioTurmaDiaItemVO> listaTemp = htd.getHorarioTurmaDiaItemVOs().stream()
								.filter(p-> Uteis.isAtributoPreenchido(p.getDisciplinaVO()) && p.getDisciplinaVO().getCodigo().equals(htdiSelecionado.getDisciplinaVO().getCodigo())
										&& Uteis.isAtributoPreenchido(p.getFuncionarioVO()) && p.getFuncionarioVO().getCodigo().equals(htdiSelecionado.getFuncionarioVO().getCodigo()))
								.collect(Collectors.toList());
						Ordenacao.ordenarLista(listaTemp, "nrAula");
						executarMontagemHorarioTurmaDiaSubsequente(htdiSelecionado, listaHtdiGoogleMeet, listaTemp, isExclusao);
					}
					if(!isExclusao) {
						executarGeracaoGoogleMeetSubSequente(listaHtdiGoogleMeet, false, configSeiGsuiteVO, configuracaoGeralSistemaVO, usuarioVO);
					}else {
						realizarOperacaoGoogleMeetPorListaHorarioTurmaDiaItem(listaHtdiGoogleMeet, isExclusao, usuarioVO);
					}	
					executarAtualizacaoDaListaHorarioTurmaDiaItem(htd, listaHtdiGoogleMeet, isExclusao);
					executarAtualizacaoDaListaHorarioTurmaDiaItem(horarioTurmaDiaVO, listaHtdiGoogleMeet, isExclusao);
					break;
				}
			}
		} finally {
			getFacadeFactory().getHorarioTurmaFacade().inicializarListaHorarioTurmaDisciplinaProgramada(horarioTurma, true, false, usuarioVO);	
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoEventoGoogleMeetIncluirVisaoAdministrativa(HorarioTurmaVO horarioTurma, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			for (HorarioTurmaDiaVO htd : horarioTurma.getHorarioTurmaDiaVOs()) {
				if (htd.getGerarEventoAulaOnLineGoogleMeet()) {
					Map<Integer, List<HorarioTurmaDiaItemVO>> mapaValidacao = new HashMap<Integer, List<HorarioTurmaDiaItemVO>>();
					List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs = htd.getHorarioTurmaDiaItemVOs().stream()
							.filter(p -> p.getGerarEventoAulaOnLineGoogleMeet() && !p.getCodigo().equals(0) &&p.getGoogleMeetVO().getCodigo().equals(0))
							.collect(Collectors.toList());
					Ordenacao.ordenarLista(horarioTurmaDiaItemVOs, "nrAula");
					if(!horarioTurmaDiaItemVOs.isEmpty()) {
						executarVerificacaoHorarioTurmaDiaContinuaSubsequente(horarioTurma, htd, mapaValidacao, horarioTurmaDiaItemVOs);
						for (Entry<Integer, List<HorarioTurmaDiaItemVO>> map : mapaValidacao.entrySet()) {						
							executarGeracaoGoogleMeetSubSequente(map.getValue(), false, configSeiGsuiteVO, configuracaoGeralSistemaVO, usuarioVO);
							executarAtualizacaoDaListaHorarioTurmaDiaItem(htd, map.getValue(), false);
						}	
					}
				}
			}	
		} finally {
			getFacadeFactory().getHorarioTurmaFacade().inicializarListaHorarioTurmaDisciplinaProgramada(horarioTurma, true, false, usuarioVO);
		}	
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoEventoGoogleMeetIncluirVisaoAdministrativa(HorarioTurmaVO horarioTurma, HorarioTurmaDiaVO horarioTurmaDiaVO, Integer professor, Integer disciplina,  boolean isExclusao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			for (HorarioTurmaDiaVO htd : horarioTurma.getHorarioTurmaDiaVOs()) {
				if (horarioTurmaDiaVO == null ||  htd.getCodigo().equals(horarioTurmaDiaVO.getCodigo())) {
					Map<Integer, List<HorarioTurmaDiaItemVO>> mapaValidacao = new HashMap<Integer, List<HorarioTurmaDiaItemVO>>();
					List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs = new ArrayList<>();
					if(Uteis.isAtributoPreenchido(professor) && Uteis.isAtributoPreenchido(disciplina)) {
						horarioTurmaDiaItemVOs = htd.getHorarioTurmaDiaItemVOs().stream().filter(p -> !p.getCodigo().equals(0)  && ((p.getGoogleMeetVO().getCodigo().equals(0) && !isExclusao) || (!p.getGoogleMeetVO().getCodigo().equals(0) && isExclusao)) && p.getFuncionarioVO().getCodigo().equals(professor) && p.getDisciplinaVO().getCodigo().equals(disciplina)).collect(Collectors.toList());
					}else {
						horarioTurmaDiaItemVOs = htd.getHorarioTurmaDiaItemVOs().stream().filter(p -> !p.getCodigo().equals(0) && ((p.getGoogleMeetVO().getCodigo().equals(0) && !isExclusao) || (!p.getGoogleMeetVO().getCodigo().equals(0) && isExclusao))).collect(Collectors.toList());
					}
					Ordenacao.ordenarLista(horarioTurmaDiaItemVOs, "nrAula");
					if(!horarioTurmaDiaItemVOs.isEmpty()) {
						executarVerificacaoHorarioTurmaDiaContinuaSubsequente(horarioTurma, htd, mapaValidacao, horarioTurmaDiaItemVOs);
						for (Entry<Integer, List<HorarioTurmaDiaItemVO>> map : mapaValidacao.entrySet()) {
							if(!isExclusao) {
								executarGeracaoGoogleMeetSubSequente(map.getValue(), false, configSeiGsuiteVO, configuracaoGeralSistemaVO, usuarioVO);
							}else {
								realizarOperacaoGoogleMeetPorListaHorarioTurmaDiaItem(map.getValue(), isExclusao, usuarioVO);
							}
							executarAtualizacaoDaListaHorarioTurmaDiaItem(htd, map.getValue(), isExclusao);
						}	
					}
					if(Uteis.isAtributoPreenchido(horarioTurmaDiaVO)) {
						break;
					}
				}
			}
		} finally {
			getFacadeFactory().getHorarioTurmaFacade().inicializarListaHorarioTurmaDisciplinaProgramada(horarioTurma, true, false, usuarioVO);
		}
	}
	
	
	private void executarAtualizacaoDaListaHorarioTurmaDiaItem(HorarioTurmaDiaVO horarioTurmaDiaVO, List<HorarioTurmaDiaItemVO> listaHtdiGoogleMeet, boolean isExclusao) {
		forHTDI:for (HorarioTurmaDiaItemVO htdiGoogleMeet : listaHtdiGoogleMeet) {
			for (HorarioTurmaDiaItemVO htdi : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
				if(htdiGoogleMeet.getCodigo().equals(htdi.getCodigo()) && !isExclusao) {
					htdi.setGoogleMeetVO(htdiGoogleMeet.getGoogleMeetVO());
					htdi.setGerarEventoAulaOnLineGoogleMeet(false);
					continue forHTDI;
				}else if(htdiGoogleMeet.getCodigo().equals(htdi.getCodigo()) && isExclusao) {
					htdi.setGoogleMeetVO(new GoogleMeetVO());
					htdi.setGerarEventoAulaOnLineGoogleMeet(false);
					continue forHTDI;
				}
			}
		}
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void executarVerificacaoHorarioTurmaDiaContinuaSubsequente(HorarioTurmaVO horarioTurma, HorarioTurmaDiaVO horarioTurmaDiaVO, Map<Integer, List<HorarioTurmaDiaItemVO>> mapaValidacao, List<HorarioTurmaDiaItemVO> listaHorarioExistente) {
		int ordenador = 0;
		for (HorarioTurmaDiaItemVO htdiExistente : listaHorarioExistente) {
			if (!mapaValidacao.entrySet().stream().anyMatch(p -> p.getValue().stream().anyMatch(pp -> pp.getCodigo().equals(htdiExistente.getCodigo())))) {
				htdiExistente.setHorarioTurmaDiaVO(horarioTurmaDiaVO);
				htdiExistente.getHorarioTurmaDiaVO().setHorarioTurma(horarioTurma);
				List<HorarioTurmaDiaItemVO> listaNova = new ArrayList<>();
				listaNova.add(htdiExistente);
				mapaValidacao.put(ordenador, listaNova);
				executarSeparacaoHorarioTurmaNrAulaSubsequente(htdiExistente, listaHorarioExistente, mapaValidacao, ordenador);
				ordenador++;
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void executarSeparacaoHorarioTurmaNrAulaSubsequente(HorarioTurmaDiaItemVO htdiExistente, List<HorarioTurmaDiaItemVO> listaTemp, Map<Integer, List<HorarioTurmaDiaItemVO>> mapaValidacao, int ordenador) {
		Integer nrAulaAuxiliar = htdiExistente.getNrAula();
		for (HorarioTurmaDiaItemVO obj : listaTemp) {
			if ((Uteis.isAtributoPreenchido(obj.getDisciplinaVO()) && obj.getDisciplinaVO().getCodigo().equals(htdiExistente.getDisciplinaVO().getCodigo()))
					&& (Uteis.isAtributoPreenchido(obj.getFuncionarioVO()) && obj.getFuncionarioVO().getCodigo().equals(htdiExistente.getFuncionarioVO().getCodigo()))
					&& obj.getNrAula() == nrAulaAuxiliar + 1) {
				obj.setHorarioTurmaDiaVO(htdiExistente.getHorarioTurmaDiaVO());
				mapaValidacao.get(ordenador).add(obj);
				nrAulaAuxiliar++;
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void executarMontagemHorarioTurmaDiaSubsequente(HorarioTurmaDiaItemVO htdiSelecionado, List<HorarioTurmaDiaItemVO> listaHorarioEvento, List<HorarioTurmaDiaItemVO> listaTemp, boolean isExclusao) {
		Integer nrAulaAuxiliar = htdiSelecionado.getNrAula();
		for (HorarioTurmaDiaItemVO obj : listaTemp) {
			if(((isExclusao && Uteis.isAtributoPreenchido(obj.getGoogleMeetVO()))
						||	(!isExclusao && !Uteis.isAtributoPreenchido(obj.getGoogleMeetVO())))
					&& obj.getNrAula() == nrAulaAuxiliar+1) {
				obj.setHorarioTurmaDiaVO(htdiSelecionado.getHorarioTurmaDiaVO());
				listaHorarioEvento.add(obj);
				nrAulaAuxiliar++;
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void executarMontagemHorarioTurmaDiaSubsequente(AgendaAlunoRSVO agenda, List<HorarioTurmaDiaItemVO> listaHorarioEvento, List<HorarioProfessorDiaItemVO> listaTemp, boolean isExclusao) {
		Integer nrAulaAuxiliar = agenda.getHorarioProfessorDiaItemVO().getNrAula();
		for (HorarioProfessorDiaItemVO obj : listaTemp) {
			if(obj.getHorarioProfessorDiaVO().getCodigo().equals(agenda.getHorarioProfessorDiaItemVO().getHorarioProfessorDiaVO().getCodigo())
					&& obj.getTurmaVO().getCodigo().equals(agenda.getHorarioProfessorDiaItemVO().getTurmaVO().getCodigo())
					&& obj.getDisciplinaVO().getCodigo().equals(agenda.getHorarioProfessorDiaItemVO().getDisciplinaVO().getCodigo())
					&& obj.getCodProfessor().equals(agenda.getHorarioProfessorDiaItemVO().getCodProfessor())
					&& ((isExclusao && Uteis.isAtributoPreenchido(obj.getHorarioTurmaDiaItemVO().getGoogleMeetVO()))
						||	(!isExclusao && !Uteis.isAtributoPreenchido(obj.getHorarioTurmaDiaItemVO().getGoogleMeetVO())))
					&& obj.getNrAula() == nrAulaAuxiliar+1) {
				listaHorarioEvento.add(obj.getHorarioTurmaDiaItemVO());
				nrAulaAuxiliar++;
			}
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarGeracaoGoogleMeetSubSequente(List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs, boolean isProcessado, ConfiguracaoSeiGsuiteVO configSeiGsuiteVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(horarioTurmaDiaItemVOs), "A lista de Horário do Google Meet esta vazia.");
		GoogleMeetVO googleMeet = new GoogleMeetVO();
		googleMeet.getGoogleMeetConvidadoVOs().clear();
		googleMeet.setHorarioTurmaDiaItemVOs(horarioTurmaDiaItemVOs);
		googleMeet.setProcessado(isProcessado);
		Gson gson = inicializaGson();
		String json = gson.toJson(googleMeet);
		HttpResponse<JsonNode> jsonResponse  = unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_GOOGLE_MEET + UteisWebServiceUrl.URL_GOOGLE_MEET_PERSISTIR, json, RequestMethod.POST, null,usuarioVO);
		GoogleMeetVO googleMeetPersistido =  gson.fromJson(jsonResponse.getBody().toString(), GoogleMeetVO.class);
		horarioTurmaDiaItemVOs.stream().forEach(p-> p.setGoogleMeetVO(googleMeetPersistido));
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarOperacaoGoogleMeetPorListaHorarioTurmaDiaItem(List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs, boolean isExclusao, UsuarioVO usuarioVO) throws Exception {	
		Uteis.checkState(!Uteis.isAtributoPreenchido(horarioTurmaDiaItemVOs), "A lista de Horário do Google Meet esta vazia.");
		Map<Integer, List<HorarioTurmaDiaItemVO>> mapaValidacao = horarioTurmaDiaItemVOs.stream().collect(Collectors.groupingBy(p -> p.getGoogleMeetVO().getCodigo()));
		for (Entry<Integer, List<HorarioTurmaDiaItemVO>>  map  : mapaValidacao.entrySet()) {
			if(map.getKey() != 0) {
				GoogleMeetVO obj = (GoogleMeetVO) Uteis.clonar(map.getValue().get(0).getGoogleMeetVO());
				Uteis.checkState(!Uteis.isAtributoPreenchido(obj), "O Horário Turma dia item informado não tem vinculo com google meet.");
				obj.getHorarioTurmaDiaItemVOs().addAll(map.getValue());
				obj.getGoogleMeetConvidadoVOs().clear();
				if(!isExclusao) {
					executarAtualizacaoGoogleMeet(obj, usuarioVO);
				}else {
					realizarExclusaoGoogleMeet(obj, usuarioVO);	
				}
			}	
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void executarAtualizacaoGoogleMeet(GoogleMeetVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);		 
		unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_GOOGLE_MEET + UteisWebServiceUrl.URL_GOOGLE_MEET_ATUALIZACAO, json, RequestMethod.PUT, null, usuarioVO);
	}	
		
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarExclusaoGoogleMeet(GoogleMeetVO obj , UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		obj.getGoogleMeetConvidadoVOs().clear();
		Gson gson = inicializaGson();
		String json = gson.toJson(obj);
		unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_GOOGLE_MEET + UteisWebServiceUrl.URL_GOOGLE_MEET_EXCLUIR, json, RequestMethod.DELETE, null, usuarioVO);
	}
	
	@Override
	public void realizarGeracaoEventoGoogleMeetConfiguracaoSeiGsuite(GoogleMeetVO googleMeetVO,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO)throws Exception {
		googleMeetVO.getGoogleMeetConvidadoVOs().clear();
		ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Gson gson = inicializaGson();
		String json = gson.toJson(googleMeetVO);
		HttpResponse<JsonNode> jsonResponse  = unirestBody(configuracaoSeiGsuiteVO, UteisWebServiceUrl.URL_GOOGLE_MEET + UteisWebServiceUrl.URL_GOOGLE_MEET_GERAR_EVENTOS_CALENDAR, json, RequestMethod.POST, null, usuarioVO);
		googleMeetVO = gson.fromJson(jsonResponse.getBody().toString(), GoogleMeetVO.class);
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void correcaoDonoMeet(UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		unirestHeaders(configSeiGsuiteVO, UteisWebServiceUrl.URL_GOOGLE_MEET + UteisWebServiceUrl.URL_GOOGLE_MEET_CORRECAO_DONO, RequestMethod.GET, null, usuarioVO);		
	}

//	@Override
//	public void realizarAtualizacaoGoogleMeetConvidado(GoogleMeetVO googleMeetVO, UsuarioVO usuarioVO)throws Exception {
//		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
//		Gson gson = inicializaGson();
//		String json = gson.toJson(googleMeetVO);
//		HttpResponse<JsonNode> jsonResponse  = unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_GOOGLE_MEET + UteisWebServiceUrl.URL_GOOGLE_MEET_REALIZA_ATUALIZACAO_EVENTOS_CONVIDADO, json, RequestMethod.PUT, null,usuarioVO);
//		googleMeetVO = gson.fromJson(jsonResponse.getBody().toString(), GoogleMeetVO.class);
//	}
	
	private void validarDadosGoogleMeetAVulso(GoogleMeetVO obj) throws ParseException {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDiaEvento()), "O campo Dia do Evento deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getHorarioInicio()) || !Uteis.isAtributoPreenchido(obj.getHorarioTermino()), "O campo Horário deve ser informado.");
		Uteis.checkState(UteisData.getCompareDataComHora(obj.getDiaEventoHorarioInicio(), obj.getDiaEventoHorarioTermino()) > 0, "O campo Horário Início não pode ser Maior que Horário Término.");
		Uteis.checkState(UteisData.getCompareDataComHora(obj.getDiaEventoHorarioTermino(), obj.getDiaEventoHorarioInicio()) < 0, "O campo Horário Término não pode ser menor que Horário Início.");
		Uteis.checkState(!obj.isGoogleMeetDentroDoLimiteFinalDeRealizacao(), "Não é possível lançar um evento Retroativo.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTurmaVO()), "O campo Turma deve ser informado");
		Uteis.checkState((obj.getTurmaVO().getCurso().getAnual() || obj.getTurmaVO().getCurso().getSemestral()) && !Uteis.isAtributoPreenchido(obj.getAno()), "O campo Ano deve ser informado");
		Uteis.checkState(obj.getTurmaVO().getCurso().getSemestral() && !Uteis.isAtributoPreenchido(obj.getSemestre()), "O campo Semestre deve ser informado");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDisciplinaVO()), "O campo Disciplina deve ser informado");
		boolean programacaoTutoriaValido = getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().isProgramacaoTutoriaOnlineValidoHorario(obj.getProfessorVO().getCodigo(), 0, 0, obj.getTurmaVO().getCodigo(), obj.getDisciplinaVO().getCodigo(), obj.getDiaEvento(), obj.getDiaEvento());
		Uteis.checkState(!programacaoTutoriaValido, "O Dia do Evento não esta dentro do período valido na programação de tutoria online ou não foi localizado para esse professor.");
		obj.setListaChoqueHorarioAluno(getFacadeFactory().getHorarioAlunoFacade().consultarChoqueHorarioCalendarioAluno(obj.getTurmaVO().getCodigo(), obj.getAno(), obj.getSemestre(), obj.getDisciplinaVO().getCodigo(), obj.getProfessorVO().getCodigo(), obj.getDiaEvento(), obj.getHorarioInicio(), obj.getHorarioTermino()));
		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getListaChoqueHorarioAluno()), "Existe choque de horário para os seguintes alunos.");
		obj.setListaTurmaDisciplinaChoqueHorarioProfessor(getFacadeFactory().getHorarioProfessorFacade().consultarChoqueHorarioCalendarioProfessor(obj.getProfessorVO().getCodigo(), obj.getDiaEvento(), obj.getHorarioInicio(), obj.getHorarioTermino()));
		Uteis.checkState(Uteis.isAtributoPreenchido(obj.getListaChoqueHorarioAluno()), "Existe choque de horário para o professor nas seguintes turmas e disciplina.");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public GoogleMeetVO realizarPersistenciaGoogleMeetAvulso(GoogleMeetVO googleMeetVO, TurmaVO turma, String ano, String semestre, DisciplinaVO disciplina,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {		
		googleMeetVO.getProfessorVO().setCodigo(usuarioVO.getPessoa().getCodigo());
		googleMeetVO.getProfessorVO().setNome(usuarioVO.getPessoa().getNome());
		googleMeetVO.setAno(ano);
		googleMeetVO.setSemestre(semestre);
		googleMeetVO.setDisciplinaVO(disciplina);
		googleMeetVO.setTurmaVO(turma);
		googleMeetVO.setEventoAulasSubsequentes(false);
		validarDadosGoogleMeetAVulso(googleMeetVO);
		googleMeetVO.setProcessado(true);
		googleMeetVO.getGoogleMeetConvidadoVOs().clear();
		ConfiguracaoSeiGsuiteVO configSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		Gson gson = inicializaGson();
		String json = gson.toJson(googleMeetVO);
		HttpResponse<JsonNode> jsonResponse  = unirestBody(configSeiGsuiteVO, UteisWebServiceUrl.URL_GOOGLE_MEET + UteisWebServiceUrl.URL_GOOGLE_MEET_AVULSO, json, RequestMethod.POST, null, usuarioVO);
		googleMeetVO = gson.fromJson(jsonResponse.getBody().toString(), GoogleMeetVO.class);
		return googleMeetVO;
	}
	
	

	@Override
	public GoogleMeetVO consultarPorHorarioTurmaDiaItem(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO, int nivelMontarDados) {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" inner join horarioturmadiaitem horario on horario.googlemeet = googlemeet.codigo");
		sql.append(" where horario.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), horarioTurmaDiaItemVO.getCodigo());
		if(tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados);	
		}
		return new GoogleMeetVO();
	}
	
	
	@Override
	public List<GoogleMeetVO> consultarPorProfessorTurmaDiscipinaData(Integer professor, Integer turma, Integer disciplina, Date data) {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" where googlemeet.processado  ");
		sql.append(" and (diaEvento between '").append(data + "'").append(" and '" + data + "')");
		sql.append(" and googlemeet.googlemeetavulso  ");
		if(Uteis.isAtributoPreenchido(turma)) {
			sql.append(" and googlemeet.turma =  ").append(turma);	
		}
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sql.append(" and googlemeet.disciplina = ").append(disciplina);	
		}
		if(Uteis.isAtributoPreenchido(professor)) {
			sql.append(" and googlemeet.professor = ").append(professor);	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosBasico(tabelaResultado);
	}	
	
	@Override
	public List<GoogleMeetVO> consultarPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date data) {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.turma = turma.codigo and matriculaperiodoturmadisciplina.disciplina = disciplina.codigo and matriculaperiodoturmadisciplina.professor = pessoa.codigo  ");
		sql.append(" where googlemeet.processado  ");
		sql.append(" and googlemeet.googlemeetavulso  ");
		sql.append(" and matriculaperiodoturmadisciplina.codigo in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(matriculaPeriodoTurmaDisciplinaVOs)).append(")");
		sql.append(" and diaEvento ='").append(Uteis.getDataJDBC(data)).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosBasico(tabelaResultado);
	}
	
	@Override
	public List<DataEventosRSVO> consultarCalendarioPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select googlemeet.diaEvento  ");
		sql.append(" from googlemeet ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.turma = googlemeet.turma and matriculaperiodoturmadisciplina.disciplina = googlemeet.disciplina and matriculaperiodoturmadisciplina.professor = googlemeet.professor  ");
		sql.append(" where googlemeet.processado  ");
		sql.append(" and googlemeet.googlemeetavulso  ");
		sql.append(" and matriculaperiodoturmadisciplina.codigo in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(matriculaPeriodoTurmaDisciplinaVOs)).append(")");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosBasicoDataEventoRSVO(rs);
	}
	
	@Override
	public List<DataEventosRSVO> consultarCalendarioPorProfessor(Integer professor) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select googlemeet.diaEvento  ");
		sql.append(" from googlemeet ");
		sql.append(" where googlemeet.processado  ");
		sql.append(" and googlemeet.professor = ").append(professor);
		sql.append(" and googlemeet.googlemeetavulso  ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosBasicoDataEventoRSVO(rs);
	}

	private List<DataEventosRSVO> montarDadosBasicoDataEventoRSVO(SqlRowSet rs) {
		List<DataEventosRSVO> dataEventosRSVOs = new ArrayList<>();
		while (rs.next()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate("diaEvento"));
			DataEventosRSVO dataEventosRSVO = new DataEventosRSVO();
			dataEventosRSVO.setAno(calendar.get(Calendar.YEAR));
			dataEventosRSVO.setMes(calendar.get(Calendar.MONTH));
			dataEventosRSVO.setDia(calendar.get(Calendar.DAY_OF_MONTH));
			dataEventosRSVO.setColor("#b8b8b8");
			dataEventosRSVO.setTextColor("#000000");
			dataEventosRSVO.setStyleClass("horarioRegistroLancado");
			dataEventosRSVO.setData(rs.getDate("diaEvento"));
			dataEventosRSVOs.add(dataEventosRSVO);
		}
		return dataEventosRSVOs;
	}
	
	
	private List<GoogleMeetVO> montarDadosBasico(SqlRowSet tabelaResultado) {
		List<GoogleMeetVO> lista = new ArrayList<GoogleMeetVO>();
		while (tabelaResultado.next()) {
			GoogleMeetVO obj = new GoogleMeetVO();
			obj.setCodigo((tabelaResultado.getInt("googlemeet.codigo")));
			obj.setAno(tabelaResultado.getString("googlemeet.ano"));
			obj.setSemestre(tabelaResultado.getString("googlemeet.semestre"));
			obj.setLinkGoogleMeet(tabelaResultado.getString("googlemeet.linkGoogleMeet"));
			obj.setHorarioInicio(tabelaResultado.getString("googlemeet.horarioInicio"));
			obj.setHorarioTermino(tabelaResultado.getString("googlemeet.horariotermino"));
			obj.setDiaEvento(tabelaResultado.getDate("googlemeet.diaevento"));
			obj.setProcessado(tabelaResultado.getBoolean("googlemeet.processado"));
			obj.setGoogleMeetAvulso(tabelaResultado.getBoolean("googlemeet.googleMeetAvulso"));
			obj.setIdEventoCalendar(tabelaResultado.getString("googlemeet.idEventoCalendar"));
			obj.getProfessorVO().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getProfessorVO().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma.codigo"));
			obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turma.identificadorTurma"));
			obj.getTurmaVO().getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeensino.codigo"));
			obj.getTurmaVO().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeensino.nome"));
			obj.getTurmaVO().getUnidadeEnsino().setRazaoSocial(tabelaResultado.getString("unidadeensino.razaoSocial"));
			if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("classroomGoogle.codigo"))) {
				obj.getClassroomGoogleVO().setCodigo((tabelaResultado.getInt("classroomGoogle.codigo")));
				obj.getClassroomGoogleVO().setAno(tabelaResultado.getString("classroomGoogle.ano"));
				obj.getClassroomGoogleVO().setSemestre(tabelaResultado.getString("classroomGoogle.semestre"));				
				obj.getClassroomGoogleVO().getProfessorEad().setCodigo((tabelaResultado.getInt("classroomGoogle.professoread")));
				obj.getClassroomGoogleVO().setLinkClassroom(tabelaResultado.getString("classroomGoogle.linkClassroom"));
				obj.getClassroomGoogleVO().setIdClassRoomGoogle(tabelaResultado.getString("classroomGoogle.idClassRoomGoogle"));
				obj.getClassroomGoogleVO().setIdCalendario(tabelaResultado.getString("classroomGoogle.idCalendario"));
				obj.getClassroomGoogleVO().setIdTurma(tabelaResultado.getString("classroomGoogle.idTurma"));
				obj.getClassroomGoogleVO().setEmailTurma(tabelaResultado.getString("classroomGoogle.emailTurma"));
				obj.getClassroomGoogleVO().setTurmaVO(obj.getTurmaVO());
				obj.getClassroomGoogleVO().setDisciplinaVO(obj.getDisciplinaVO());
			}
			lista.add(obj);
		}
		return lista;
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select googlemeet.codigo as \"googlemeet.codigo\", googlemeet.horarioInicio as \"googlemeet.horarioInicio\", googlemeet.horarioTermino as \"googlemeet.horarioTermino\" ,  ");
		sql.append(" googlemeet.idEventoCalendar as \"googlemeet.idEventoCalendar\",  googlemeet.linkGoogleMeet as \"googlemeet.linkGoogleMeet\",  ");
		sql.append(" googlemeet.diaEvento as \"googlemeet.diaEvento\",  googlemeet.ano as \"googlemeet.ano\",  ");
		sql.append(" googlemeet.semestre as \"googlemeet.semestre\",  ");
		sql.append(" googlemeet.processado as \"googlemeet.processado\",  ");
		sql.append(" googlemeet.googleMeetAvulso as \"googlemeet.googleMeetAvulso\",  ");
		
		sql.append(" classroomGoogle.codigo as \"classroomGoogle.codigo\", classroomGoogle.ano as \"classroomGoogle.ano\", ");
		sql.append(" classroomGoogle.semestre as \"classroomGoogle.semestre\",  classroomGoogle.linkClassroom as \"classroomGoogle.linkClassroom\", ");
		sql.append(" classroomGoogle.idClassRoomGoogle as \"classroomGoogle.idClassRoomGoogle\",  classroomGoogle.idCalendario as \"classroomGoogle.idCalendario\", ");		
		sql.append(" classroomGoogle.idTurma as \"classroomGoogle.idTurma\",   ");
		sql.append(" classroomGoogle.emailTurma as \"classroomGoogle.emailTurma\",   ");
		sql.append(" classroomGoogle.professoread as \"classroomGoogle.professoread\",   ");
		
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");
		
		sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", unidadeensino.razaosocial as \"unidadeensino.razaosocial\" ");
		sql.append(" from googlemeet ");
		sql.append(" left join classroomGoogle on classroomGoogle.codigo = googlemeet.classroomGoogle ");
		sql.append(" inner join turma on turma.codigo = googlemeet.turma ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sql.append(" inner join disciplina on disciplina.codigo = googlemeet.disciplina ");
		sql.append(" inner join pessoa on pessoa.codigo = googlemeet.professor ");
		sql.append(" ");
		return sql;
	}
	
	private StringBuilder getSQLTotalPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(googlemeet.codigo) as qtde from googlemeet ");
		sql.append(" inner join turma on turma.codigo = googlemeet.turma");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sql.append(" inner join disciplina on disciplina.codigo = googlemeet.disciplina ");
		sql.append(" inner join pessoa on pessoa.codigo = googlemeet.professor ");
		
		return sql;
	}
	
	
	@Override
	public void consultarGoogleMeetPorDataModelo(DataModelo dataModeloGoogleMeet, TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		dataModeloGoogleMeet.setListaConsulta(consultarGoogleMeet(dataModeloGoogleMeet,turmaVO, ano, semestre, usuarioLogado));
		dataModeloGoogleMeet.setTotalRegistrosEncontrados(consultarTotalGoogleMeet(dataModeloGoogleMeet,turmaVO, ano, semestre, usuarioLogado));
	}

	private List<GoogleMeetVO> consultarGoogleMeet(DataModelo dataModeloGoogleMeet, TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = getSQLPadraoConsultaBasica();
		dataModeloGoogleMeet.getListaFiltros().clear();
		montarFiltrosConsultaPadrao(dataModeloGoogleMeet, turmaVO, ano, semestre, sql);
		sql.append(" order by googlemeet.codigo desc");
		UteisTexto.addLimitAndOffset(sql, dataModeloGoogleMeet.getLimitePorPagina(), dataModeloGoogleMeet.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModeloGoogleMeet.getListaFiltros().toArray());

		return montarDadosConsulta(tabelaResultado, dataModeloGoogleMeet.getNivelMontarDados());
	}	

	private Integer consultarTotalGoogleMeet(DataModelo dataModeloGoogleMeet, TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = getSQLTotalPadraoConsultaBasica();
		dataModeloGoogleMeet.getListaFiltros().clear();
		montarFiltrosConsultaPadrao(dataModeloGoogleMeet, turmaVO, ano, semestre, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModeloGoogleMeet.getListaFiltros().toArray());

		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	
	private void montarFiltrosConsultaPadrao(DataModelo dataModeloGoogleMeet, TurmaVO turmaVO, String ano, String semestre, StringBuilder sql) {
		sql.append(" WHERE 1 = 1");
		if (Uteis.isAtributoPreenchido(turmaVO.getIdentificadorTurma())) {
			sql.append(" and turma.identificadorTurma = ? ");
			dataModeloGoogleMeet.getListaFiltros().add(turmaVO.getIdentificadorTurma());
		}

		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and googlemeet.ano = ? ");
			dataModeloGoogleMeet.getListaFiltros().add(ano);
		}
		
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and googlemeet.semestre = ? ");
			dataModeloGoogleMeet.getListaFiltros().add(semestre);
		}
	}
	
	
	public List<GoogleMeetVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<GoogleMeetVO> vetResultado = new ArrayList<GoogleMeetVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}

	private GoogleMeetVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados)  {
		GoogleMeetVO obj = new GoogleMeetVO();
		obj.setNovoObj(false);
		obj.setCodigo((dadosSQL.getInt("googlemeet.codigo")));
		obj.setAno(dadosSQL.getString("googlemeet.ano"));
		obj.setSemestre(dadosSQL.getString("googlemeet.semestre"));
		obj.setLinkGoogleMeet(dadosSQL.getString("googlemeet.linkGoogleMeet"));
		obj.setHorarioInicio(dadosSQL.getString("googlemeet.horarioInicio"));
		obj.setHorarioTermino(dadosSQL.getString("googlemeet.horariotermino"));
		obj.setDiaEvento(dadosSQL.getDate("googlemeet.diaevento"));
		obj.setProcessado(dadosSQL.getBoolean("googlemeet.processado"));
		obj.setGoogleMeetAvulso(dadosSQL.getBoolean("googlemeet.googleMeetAvulso"));
		obj.setIdEventoCalendar(dadosSQL.getString("googlemeet.idEventoCalendar"));
		obj.getProfessorVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getProfessorVO().setNome((dadosSQL.getString("pessoa.nome")));
		obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));		
		obj.getTurmaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getTurmaVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.getTurmaVO().getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino.razaoSocial"));
		obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
		obj.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("classroomGoogle.codigo"))) {
			obj.getClassroomGoogleVO().setCodigo((dadosSQL.getInt("classroomGoogle.codigo")));
			obj.getClassroomGoogleVO().setAno(dadosSQL.getString("classroomGoogle.ano"));
			obj.getClassroomGoogleVO().setSemestre(dadosSQL.getString("classroomGoogle.semestre"));
			obj.getClassroomGoogleVO().setLinkClassroom(dadosSQL.getString("classroomGoogle.linkClassroom"));			
			obj.getClassroomGoogleVO().getProfessorEad().setCodigo((dadosSQL.getInt("classroomGoogle.professoread")));
			obj.getClassroomGoogleVO().setIdClassRoomGoogle(dadosSQL.getString("classroomGoogle.idClassRoomGoogle"));
			obj.getClassroomGoogleVO().setIdTurma(dadosSQL.getString("classroomGoogle.idTurma"));
			obj.getClassroomGoogleVO().setEmailTurma(dadosSQL.getString("classroomGoogle.emailTurma"));
			obj.getClassroomGoogleVO().setIdCalendario(dadosSQL.getString("classroomGoogle.idCalendario"));
			obj.getClassroomGoogleVO().setTurmaVO(obj.getTurmaVO());
			obj.getClassroomGoogleVO().setDisciplinaVO(obj.getDisciplinaVO());
		}
		return obj;	
	}

	
	
}
