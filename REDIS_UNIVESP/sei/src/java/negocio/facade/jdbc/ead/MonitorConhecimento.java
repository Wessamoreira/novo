package negocio.facade.jdbc.ead;

import java.util.List;
import java.util.Random;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GraficoAproveitamentoAvaliacaoVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.MonitorConhecimentoVO;
import negocio.comuns.ead.QuestaoListaExercicioVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.UsoQuestaoEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.MonitorConhecimentoInterfaceFacade;

@Repository
@Lazy
public class MonitorConhecimento extends ControleAcesso implements MonitorConhecimentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public ListaExercicioVO realizarGeracaoListaExerciciMonitorConhecimento(ListaExercicioVO listaExercicioVO, String nome, Integer codigoTemaAssunto, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosListaExercicio(listaExercicioVO);
		listaExercicioVO.setDisciplina(matriculaPeriodoTurmaDisciplinaVO.getDisciplina());
		listaExercicioVO.setTipoGeracaoListaExercicio(TipoGeracaoListaExercicioEnum.RANDOMICO);
		listaExercicioVO.setPoliticaSelecaoQuestaoEnum(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO);
		listaExercicioVO.setDescricao("Lista de Exercício Revisão "+nome);
		List<QuestaoVO> questaoVOs = getFacadeFactory().getQuestaoFacade().consultarQuestoesPorDisciplinaRandomicamente(matriculaPeriodoTurmaDisciplinaVO, codigoTemaAssunto,0,
				listaExercicioVO.getDisciplina().getCodigo(), listaExercicioVO.getQuantidadeNivelQuestaoFacil(),
				listaExercicioVO.getQuantidadeNivelQuestaoMedio(), listaExercicioVO.getQuantidadeNivelQuestaoDificil(),
                listaExercicioVO.getQuantidadeQualquerNivelQuestao(), UsoQuestaoEnum.EXERCICIO, listaExercicioVO.getPoliticaSelecaoQuestaoEnum(), listaExercicioVO.getRegraDistribuicaoQuestaoEnum(), true, matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo(), listaExercicioVO.getCodigo(), listaExercicioVO.getRandomizarApenasQuestoesCadastradasPeloProfessor(), null, usuarioVO);
        QuestaoListaExercicioVO obj = null;
        int x = 1;
        for (QuestaoVO questaoVO : questaoVOs) {
            obj = new QuestaoListaExercicioVO();
            Ordenacao.ordenarLista(questaoVO.getOpcaoRespostaQuestaoVOs(), "ordemApresentacao");
            obj.setQuestao(questaoVO);
            obj.setOrdemApresentacao(x++);
            listaExercicioVO.getQuestaoListaExercicioVOs().add(obj);
        }
        return listaExercicioVO;
	}
	
	public void validarDadosListaExercicio (ListaExercicioVO listaExercicioVO) throws Exception {
		if ((listaExercicioVO.getQuantidadeNivelQuestaoDificil()
                        + listaExercicioVO.getQuantidadeNivelQuestaoFacil()
                        + listaExercicioVO.getQuantidadeNivelQuestaoMedio()
                        + listaExercicioVO.getQuantidadeQualquerNivelQuestao()) == 0) {
            throw new Exception(UteisJSF.internacionalizar("msg_ListaExercicio_informarNumeroQuestaoSorteio"));
        }
	}
	
	@Override
	public MonitorConhecimentoVO realizarGeracaoGraficosMonitorConhecimento(GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO, MonitorConhecimentoVO monitorConhecimentoVO, Integer parametroWidthGraficoAproveitamento, Integer parametroWidthGraficoComparativoMeusColegas, Integer parametroWidthGraficoComparativoAvaliacoesOnlines, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoCurso, UsuarioVO usuarioVO) throws Exception {
		monitorConhecimentoVO.setGraficoAproveitamentoAvaliacaoVO((GraficoAproveitamentoAvaliacaoVO) graficoAproveitamentoAvaliacaoVO.clone());
		Random random = new Random(100000);
		monitorConhecimentoVO.getGraficoAproveitamentoAvaliacaoVO().setOrdem(random.nextInt() * (-1));
		monitorConhecimentoVO.getGraficoAproveitamentoAvaliacaoVO().setParametroWidthGrafico(parametroWidthGraficoAproveitamento);
		monitorConhecimentoVO.getGraficoAproveitamentoAvaliacaoVO().setGraficoAproveitamento(null);
		monitorConhecimentoVO.setGraficoComparativoAvaliacoesOnlinesVOs(getFacadeFactory().getGraficoComparativoAvaliacoesOnlinesFacade().consultarPercentualAcertosComparativoAvaliacoesOnlines(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), monitorConhecimentoVO.getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), usuarioVO));
		monitorConhecimentoVO.setParametroWidthGraficoComparativoAvaliacoesOnlines(parametroWidthGraficoComparativoAvaliacoesOnlines);
		monitorConhecimentoVO.setGraficoComparativoAvaliacoesOnlines(null);
		monitorConhecimentoVO.setParametrosGraficoComparativoMeusColegasVOs(getFacadeFactory().getParametrosGraficoComparativoMeusColegasFacade().consultarParametrosGraficoComparativoMeusColegasVO(matriculaPeriodoTurmaDisciplinaVO, codigoCurso, monitorConhecimentoVO.getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), usuarioVO));
		monitorConhecimentoVO.setParametroWidthGraficoComparativoMeusColegas(parametroWidthGraficoComparativoMeusColegas);
		monitorConhecimentoVO.setGraficoComparativoMeusColegas(null);
		return monitorConhecimentoVO;
	}
}
