package negocio.interfaces.protocolo;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface RequerimentoHistoricoInterfaceFacade {
	

    public RequerimentoHistoricoVO novo() throws Exception;
    public void incluir(RequerimentoHistoricoVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(RequerimentoHistoricoVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(RequerimentoHistoricoVO obj, UsuarioVO usuario) throws Exception;
    public void excluirRequerimentoHistoricoVOs(Integer requerimento, List<RequerimentoHistoricoVO> requerimentoHistoricoVOs, UsuarioVO usuario) throws Exception;
    public void alterarRequerimentoHistoricoVOs(RequerimentoVO requerimento, List<RequerimentoHistoricoVO> requerimentoHistoricoVOs, UsuarioVO usuario) throws Exception;
    public void incluirRequerimentoHistoricoVOs(Integer requerimento, List<RequerimentoHistoricoVO> requerimentoHistoricoVOs, UsuarioVO usuario) throws Exception;
    public List<RequerimentoHistoricoVO> consultarPorCodigoRequerimento(Integer codigoRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public RequerimentoHistoricoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	void gravarRespostaQuestionario(RequerimentoHistoricoVO obj, UsuarioVO usuario) throws Exception;
	void alterarSituacaoDepartamento(RequerimentoHistoricoVO requerimentoHistoricoVO, RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<RequerimentoHistoricoVO> consultarPorCodigoRequerimentoFiltrarPorUltimoHistoricoDepartamento(Integer codigoRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}