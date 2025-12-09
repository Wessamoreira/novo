package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.HistoricoNotaParcialVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface HistoricoNotaParcialInterfaceFacade {
	
	public void incluir(final HistoricoNotaParcialVO historicoNotaParcialVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<HistoricoNotaParcialVO> consultarPorHistorico(HistoricoVO historicoVO, String tipoNota, UsuarioVO usuarioVO, String ano, String semestre, int nivelMontarDados)throws Exception;
	
	public void alterar(HistoricoNotaParcialVO historicoNotaParcialVO, UsuarioVO usuario) throws Exception;
	
	public List<HistoricoNotaParcialVO> consultarPorHistoricoTurmaDisciplinaNotaParcialTurmaDisciplinaNotaTitulo(HistoricoNotaParcialVO historicoNotaParcialVO, UsuarioVO usuarioVO, int nivelMontarDados)throws Exception;

	void carregarHistoricoNotaParcialPorHistorico(HistoricoVO historicoVO, int nivelMontarDados) throws Exception;

	void carregarHistoricoNotaParcialPorHistorico(List<HistoricoVO> historicoVOs, int nivelMontarDados)
			throws Exception;
	
	public void alterarHistoricosNotaParcial(List<HistoricoNotaParcialVO> historicoNotaParcialVOs, HistoricoVO historicoVO, UsuarioVO usuario) throws Exception;
	
	public List<HistoricoNotaParcialVO> consultarPorCodigoHistorico(Integer codigoHistorico, UsuarioVO usuarioVO, int nivelMontarDados)throws Exception;
	
	
}
