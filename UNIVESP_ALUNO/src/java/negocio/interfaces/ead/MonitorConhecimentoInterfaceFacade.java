package negocio.interfaces.ead;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GraficoAproveitamentoAvaliacaoVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.MonitorConhecimentoVO;

public interface MonitorConhecimentoInterfaceFacade {

	ListaExercicioVO realizarGeracaoListaExerciciMonitorConhecimento(ListaExercicioVO listaExercicioVO, String nome, Integer codigoTemaAssunto, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	MonitorConhecimentoVO realizarGeracaoGraficosMonitorConhecimento(GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO, MonitorConhecimentoVO monitorConhecimentoVO, Integer parametroWidthGraficoAproveitamento, Integer parametroWidthGraficoComparativoMeusColegas, Integer parametroWidthGraficoComparativoAvaliacoesOnlines, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoCurso, UsuarioVO usuarioVO) throws Exception;

}
