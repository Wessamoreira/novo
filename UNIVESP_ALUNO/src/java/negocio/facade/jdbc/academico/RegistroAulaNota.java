package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.FechamentoPeriodoLetivoException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.RegistroAulaNotaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RegistroAulaNota extends ControleAcesso implements RegistroAulaNotaInterfaceFacade {

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(List<RegistroAulaVO> registroAulaVOs, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean permiteLancamentoAulaFutura, UsuarioVO usuarioLogado) throws Exception {
        List<HistoricoVO> historicoVOs = new ArrayList<HistoricoVO>(0);
        TurmaVO turma = new TurmaVO();
        Integer professor = 0;
        Integer disciplina = 0;
        //calcularMedia(registroAulaVOs, configuracaoAcademicoVO, usuarioLogado);
        for (RegistroAulaVO registroAulaVO : registroAulaVOs) {    
        	registroAulaVO.getResponsavelRegistroAula().setCodigo(usuarioLogado.getCodigo());
            if (registroAulaVO.getCodigo() == 0) {
                getFacadeFactory().getRegistroAulaFacade().incluir(registroAulaVO, permiteLancamentoAulaFutura, usuarioLogado, true);
            } else {
                getFacadeFactory().getRegistroAulaFacade().alterar(registroAulaVO, permiteLancamentoAulaFutura, usuarioLogado);
            }
            if (registroAulaVO.getDisciplina().getNome().equals("") && !registroAulaVO.getDisciplina().getCodigo().equals(0)) {
                registroAulaVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaSemExcecao(registroAulaVO.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
            }
            if (turma.getCodigo().equals(0)) {
                turma = registroAulaVO.getTurma();
            }
            if (!registroAulaVO.getTurma().getCodigo().equals(0) && registroAulaVO.getTurma().getIdentificadorTurma().equals("")) {
                if (!turma.getCodigo().equals(0) && turma.getIdentificadorTurma().equals("")) {
                    getFacadeFactory().getTurmaFacade().carregarDados(turma, usuarioLogado);
                }
                registroAulaVO.setTurma(turma);
            }
            if (usuarioLogado.getVisaoLogar().equals("professor")) {
                getFacadeFactory().getLogRegistroAulaFacade().registrarLogRegistroAula(registroAulaVO, registroAulaVO.getDisciplina().getNome(), "Inserção pela Visão do Professor", usuarioLogado);
            } else {
                getFacadeFactory().getLogRegistroAulaFacade().registrarLogRegistroAula(registroAulaVO, registroAulaVO.getDisciplina().getNome(), "Inserção pela Secretaria", usuarioLogado);
            }
            professor = registroAulaVO.getProfessor().getCodigo();
            disciplina = registroAulaVO.getDisciplina().getCodigo();
        }
        RegistroAulaVO regAuxVO = registroAulaVOs.get(registroAulaVOs.size() - 1);
      
        getFacadeFactory().getHistoricoFacade().incluirListaHistorico(historicoVOs, "RegistroAulaNota", usuarioLogado, "Visão Administrativa", true, TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS);
        if (usuarioLogado.getVisaoLogar().equals("professor")) {
            getFacadeFactory().getLogLancamentoNotaFacade().registrarLogLancamentoNota(historicoVOs, "Alteração pela Visão do Professor", usuarioLogado);
        } else {
            getFacadeFactory().getLogLancamentoNotaFacade().registrarLogLancamentoNota(historicoVOs, "Alteração pela Secretaria", usuarioLogado);
        }
       
    }

    public void marcarDesmarcarAlunoPresenteAula(Boolean controlarMarcarDesmarcarTodos, RegistroAulaVO registroAula, String origemRegistro, UsuarioVO usuario) throws Exception {
//        for (FrequenciaAulaVO fa : registroAula.getFrequenciaAulaVOs()) {
//        	if(!fa.getHistoricoVO().getIsProfessorNaoPodeAlterarRegistro() || (usuario.getIsApresentarVisaoAdministrativa() && origemRegistro.equals("registroAulaNotaForm"))) {
//        		fa.setPresente(controlarMarcarDesmarcarTodos);
//        	}
//        }
    }
    
    @Override
    public void calcularMedia(List<RegistroAulaVO> listaAulas, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception {
//        try {
//
//            RegistroAulaVO regisAulaVO = listaAulas.get(listaAulas.size() - 1);
//            for (FrequenciaAulaVO freAulaVO : regisAulaVO.getFrequenciaAulaVOs()) {
//                if (!freAulaVO.getFrequenciaOculta()) {
//                    boolean resultado = false;
//                    try {
//                        getFacadeFactory().getConfiguracaoAcademicoFacade().prepararVariaveisNotaParaSubstituicaoFormulaNota(configuracaoAcademicoVO, freAulaVO.getHistoricoVO(), usuarioVO);                        
//                        // Este montar das frequencias aqui é importante pois podem ser utilizadas variaveis da formula de calculo da nota
//                        // que referenciam a frequencia do aluno no periodo ou no bimeste
//                        getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(freAulaVO.getHistoricoVO(), configuracaoAcademicoVO, usuarioVO);
//                        resultado = freAulaVO.getHistoricoVO().getConfiguracaoAcademico().substituirVariaveisFormulaPorValores(freAulaVO.getHistoricoVO(), null, true);
//                    } catch (FechamentoPeriodoLetivoException e) {
//                        freAulaVO.getHistoricoVO().setMediaFinal(null);
//                        getFacadeFactory().getLogFechamentoFacade().realizarRegistroLogFechamento(
//                                freAulaVO.getHistoricoVO().getMatricula().getMatricula());
//                    }
//                    if (freAulaVO.getHistoricoVO().getMediaFinal() != null) {
//                    	
////                      montarFreguenciaAluno(freAulaVO.getHistoricoVO(), listaAulas, freAulaVO.getHistoricoVO().getConfiguracaoAcademico(), usuarioVO);
//                        verificaAlunoReprovadoFalta(freAulaVO.getHistoricoVO(), freAulaVO.getHistoricoVO().getConfiguracaoAcademico(), usuarioVO);
//                        if ((!freAulaVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor()))
//                                && (!freAulaVO.getHistoricoVO().getSituacao().equals(SituacaoHistorico.ISENTO.getValor()))
//                                && !freAulaVO.getHistoricoVO().getSituacao().equals("")) {
//                            if (resultado) {
//                                freAulaVO.getHistoricoVO().setSituacao(SituacaoHistorico.APROVADO.getValor());
//                            } else {
//                                freAulaVO.getHistoricoVO().setSituacao(SituacaoHistorico.REPROVADO.getValor());
//                            }
//                        }
//                    } else {
//                        freAulaVO.getHistoricoVO().setSituacao(SituacaoHistorico.CURSANDO.getValor());
//                    }
//                }
//            }
//            
//        } catch (Exception e) {
//            throw e;
//        }
    }
    
    public void verificaAlunoReprovadoFalta(HistoricoVO obj, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception {
        try {
            getFacadeFactory().getHistoricoFacade().verificaAlunoReprovadoFalta(obj, configuracaoAcademicoVO, usuarioVO);
        } catch (Exception e) {
           throw e;
        }
    }
    
    public void montarFreguenciaAluno(HistoricoVO obj, List<RegistroAulaVO> listaAulas, ConfiguracaoAcademicoVO configuracaoAcademicoVO, UsuarioVO usuarioVO) throws Exception {
//    	HashMap<String, Double> hashFrequencia = new HashMap<String, Double>(0);
//        Double cargaHorariaAluno = 0.0;
//        for (RegistroAulaVO regAulaVO : listaAulas) {
//            for (FrequenciaAulaVO frequenciaAulaVO : regAulaVO.getFrequenciaAulaVOs()) {
//                if ((frequenciaAulaVO.getPresente()) || (frequenciaAulaVO.getAbonado())) {
//                    if (hashFrequencia.get(frequenciaAulaVO.getMatricula().getMatricula()) == null) {
//                        cargaHorariaAluno = regAulaVO.getCargaHoraria().doubleValue();
//                    } else {
//                        cargaHorariaAluno = hashFrequencia.get(frequenciaAulaVO.getMatricula().getMatricula()) + regAulaVO.getCargaHoraria();
//                    }
//                    hashFrequencia.put(frequenciaAulaVO.getMatricula().getMatricula(), cargaHorariaAluno);
//                }
//            }
////            cargaHorariaAluno = 0.0;
//        }
//        
//		RegistroAulaVO regAux = listaAulas.get(listaAulas.size() - 1);
//		for (FrequenciaAulaVO freqAulaVO : regAux.getFrequenciaAulaVOs()) {
//			if (hashFrequencia.get(freqAulaVO.getMatricula().getMatricula()) != null) {
////				Boolean existeRegistroAula = getFacadeFactory().getRegistroAulaFacade().consultarExistenciaRegistroAula(freqAulaVO.getMatricula().getMatricula(), regAux.getTurma().getCodigo(), regAux.getDisciplina().getCodigo(), null, "", "");
////				if (!existeRegistroAula) {
////					freguencia = getFacadeFactory().getHistoricoFacade().calcularFrequenciaAlunoPosGraduacao(regAux.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplina().getSemestre(), obj.getMatriculaPeriodoTurmaDisciplina().getAno(), obj.getMatricula().getMatricula(), obj.getDisciplina().getCargaHoraria(), hashFrequencia.get(freqAulaVO.getMatricula().getMatricula()), listaAulas.size(), regAux.getCargaHoraria(), usuarioVO);
////				} else {
////					Integer somaPresencaAluno = getFacadeFactory().getFrequenciaAulaFacade().consultarSomaFrequenciaAlunoEspecifico(freqAulaVO.getMatricula().getMatricula(), regAux.getSemestre(), regAux.getAno(), regAux.getTurma().getCodigo(), regAux.getDisciplina().getCodigo(), true, false, usuarioVO);
////					freguencia = getFacadeFactory().getHistoricoFacade().calcularFrequenciaAlunoPosGraduacao(regAux.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplina().getSemestre(), obj.getMatriculaPeriodoTurmaDisciplina().getAno(), obj.getMatricula().getMatricula(), obj.getDisciplina().getCargaHoraria(), somaPresencaAluno.doubleValue(), listaAulas.size(), regAux.getCargaHoraria(), usuarioVO);
////				}
//				Double freguencia = 0.0;
//				Double somaPresencaAluno = hashFrequencia.get(freqAulaVO.getMatricula().getMatricula()) + getFacadeFactory().getRegistroAulaFacade().consultarSomaCargaHorarioDisciplinaMinistradaPorOutroProfessor(regAux.getTurma().getCodigo(), regAux.getProfessor().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplina().getSemestre(), obj.getMatriculaPeriodoTurmaDisciplina().getAno(), obj.getDisciplina().getCodigo(), false, usuarioVO);
//				
//				freguencia = getFacadeFactory().getHistoricoFacade().calcularFrequenciaAlunoPosGraduacao(regAux.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), obj.getMatriculaPeriodoTurmaDisciplina().getSemestre(), obj.getMatriculaPeriodoTurmaDisciplina().getAno(), obj.getMatricula().getMatricula(), obj.getGradeDisciplinaVO().getCargaHoraria(), somaPresencaAluno, listaAulas.size(), regAux.getCargaHoraria(), freqAulaVO.getHistoricoVO().getConfiguracaoAcademico(), usuarioVO);
//				freqAulaVO.getHistoricoVO().setFreguencia(Uteis.arredondar(freguencia, 2, 0));
//			} else {
//				freqAulaVO.getHistoricoVO().setFreguencia(0.0);
//			}
//		}
//		        hashFrequencia.clear();
//        //return hashFrequencia;
    }
    
    @Override
    public Boolean setarNotasDeAcordoComMedia(List<HistoricoVO> listaNotas, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean listasIguais) throws Exception {
//        try {
//
//            
//
//            if (frAulaVO.getHistoricoVO().getMediaFinal() != null) {
//                listasIguais = false;
//                frAulaVO.getHistoricoVO().setMediaFinal(Math.round(frAulaVO.getHistoricoVO().getMediaFinal() * 10) / 10.0);
//                if (frAulaVO.getHistoricoVO().getMediaFinal() != null) {
//                    if (frAulaVO.getHistoricoVO().getMediaFinal() > 10) {
//                        if (frAulaVO.getHistoricoVO().getMediaFinal() < 101) {
//                            frAulaVO.getHistoricoVO().setMediaFinal(Math.round(frAulaVO.getHistoricoVO().getMediaFinal()) / 10.0);
//                        } else {
//                            frAulaVO.getHistoricoVO().setMediaFinal(Math.round(frAulaVO.getHistoricoVO().getMediaFinal()) / 100.0);
//                        }
//                        frAulaVO.getHistoricoVO().setMediaFinal(Uteis.arredondar(frAulaVO.getHistoricoVO().getMediaFinal(), 1, 0));
//                    }
//                    if(configuracaoAcademicoVO.getUtilizarArredondamentoMediaParaMais()){
//                    	frAulaVO.getHistoricoVO().setMediaFinal(Uteis.arredondarMultiploDeCincoParaCima(frAulaVO.getHistoricoVO().getMediaFinal()));
//                    }else if (configuracaoAcademicoVO.getNotasDeCincoEmCincoDecimos() || configuracaoAcademicoVO.getNotasDeCincoEmCincoDecimosApenasMedia()) {
//                        frAulaVO.getHistoricoVO().setMediaFinal(Math.round(2 * frAulaVO.getHistoricoVO().getMediaFinal()) / 2.0);
//                    }
//                }
//
//                for (int i = 1; i <= 13; i++) {
//                    UtilReflexao.invocarMetodo(frAulaVO.getHistoricoVO(), "setNota" + i, frAulaVO.getHistoricoVO().getMediaFinal());
//                    UtilReflexao.invocarMetodo(frAulaVO.getHistoricoVO(), "setNota" + i + "Lancada", true);
//                    for (HistoricoVO historicoVO : listaNotas) {
//                        if (historicoVO.getMatricula().getMatricula().equals(frAulaVO.getMatricula().getMatricula())) {
//                            historicoVO.setMediaFinal(frAulaVO.getHistoricoVO().getMediaFinal());
//                            UtilReflexao.invocarMetodo(historicoVO, "setNota" + i, frAulaVO.getHistoricoVO().getMediaFinal());
//                        }
//                    }
//                }
//            } else {
//                for (int i = 1; i <= 13; i++) {
//                    UtilReflexao.invocarMetodoSetParametroNull(frAulaVO.getHistoricoVO(), "Nota" + i);
//                    UtilReflexao.invocarMetodo(frAulaVO.getHistoricoVO(), "Nota" + i + "Lancada", false);
//                    
//                    for (HistoricoVO historicoVO : listaNotas) {
//                        if (historicoVO.getMatricula().getMatricula().equals(frAulaVO.getMatricula().getMatricula())) {
//                            historicoVO.setMediaFinal(frAulaVO.getHistoricoVO().getMediaFinal());
//                            UtilReflexao.invocarMetodoSetParametroNull(historicoVO, "Nota" + i);
//                        }
//                    }
//				}
//            }
//
//            return listasIguais;
//        } catch (Exception e) {
//            throw e;
//        }
    	return false;
    }
    
}
