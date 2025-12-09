package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GraficoAproveitamentoAssuntoPBLVO;
import negocio.comuns.ead.MonitorConhecimentoPBLVO;

public interface MonitorConhecimentoPBLInterfaceFacade {

	void montarGraficoAproveitamentoAssuntoPBLVO(MonitorConhecimentoPBLVO obj, boolean isOutraTurma, UsuarioVO usuarioLogado) throws Exception;

	void montarGraficoAproveitamentoAssuntoOutraTurma(GraficoAproveitamentoAssuntoPBLVO obj, boolean isOutraTurma, UsuarioVO usuarioLogado) throws Exception;

	void montarGraficoAproveitamentoAssuntoPorTipoNota(GraficoAproveitamentoAssuntoPBLVO obj, boolean isOutraTurma, UsuarioVO usuarioLogado) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> montarAlunosGestaoEventoContudoTurmaAvaliacao(MonitorConhecimentoPBLVO obj, String origem, String cupre, Integer codigoparametro, Integer temaAssunto) throws Exception;

}
