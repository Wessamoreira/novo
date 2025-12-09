package relatorio.negocio.interfaces.processosel;

import java.util.List;

import relatorio.negocio.comuns.processosel.ChamadaCandidatoAprovadoRelVO;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ProcSeletivoVO;

public interface ChamadaCandidatoAprovadoRelInterfaceFacade {
	
	public List montarListaSelectItemProcSeletivo(ProcSeletivoVO procSeletivoVO, UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
	public List<ChamadaCandidatoAprovadoRelVO> consultarCandidatosAprovados(ProcSeletivoVO procSeletivoVO, Integer itemProcSeletivoDataProvaVO, Integer curso, Integer numeroVagas, Integer nrCandidatosMatriculados,  Integer qtdeCandidatoChamar, Integer quantidadeCasaDecimalConsiderarNotaProcessoSeletivo,  UsuarioVO usuarioVO) throws Exception;
	public List montarListaSelectItemUnidadeEnsino(ProcSeletivoVO procSeletivoVO, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioVO) throws Exception;
	public Integer consultarQtdeCandidatosMatriculadosPorProcSeletivoCurso(Integer procSeletivo, Integer curso, Integer unidadeEnsino, UsuarioVO usuarioVO);
	public String getDesignIReportRelatorio();
	public String getCaminhoBaseRelatorio();
	public void alterarSituacaoCandidatoParaConvocado(final List<ChamadaCandidatoAprovadoRelVO> listaChamadaCandidatoAprovadoRelVOs, int chamada, UsuarioVO usuarioVO) throws Exception;
	public Integer consultarQtdeCandidatosConvocadoPorProcSeletivoCurso(Integer procSeletivo, Integer curso, Integer unidadeEnsino, UsuarioVO usuarioVO);
	public Integer consultarQtdeCandidatosAprovadosNaoMatriculadoPorProcSeletivoCurso(Integer procSeletivo, Integer curso, Integer unidadeEnsino, UsuarioVO usuarioVO);
	public void validarDados(Integer curso, UsuarioVO usuarioVO) throws Exception;

}
