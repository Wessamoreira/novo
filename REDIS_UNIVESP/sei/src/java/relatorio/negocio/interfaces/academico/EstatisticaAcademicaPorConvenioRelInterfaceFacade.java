package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.EstatisticaAcademicaPorConvenioRelVO;

public interface EstatisticaAcademicaPorConvenioRelInterfaceFacade {

	public List<EstatisticaAcademicaPorConvenioRelVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer turma, Integer parceiro, Integer convenio, String ano, String semestre, Double mediaAproveitamentoIni, Double mediaAproveitamentoFim, Double mediaFrequenciaIni, Double mediaFrequenciaFim, Double mediaNotaIni, Double mediaNotaFim, String tipoLayout) throws Exception;

	public void executarEnvioComunicadoInternoAluno(List<EstatisticaAcademicaPorConvenioRelVO> estatisticaAcademicaPorConvenioRelVOs, ComunicacaoInternaVO comunicacaoInternaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

}
