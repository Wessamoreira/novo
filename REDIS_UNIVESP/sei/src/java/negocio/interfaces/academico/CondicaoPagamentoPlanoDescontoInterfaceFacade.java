package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CondicaoPagamentoPlanoDescontoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface CondicaoPagamentoPlanoDescontoInterfaceFacade {

	public CondicaoPagamentoPlanoDescontoVO novo() throws Exception;
	public void incluir(CondicaoPagamentoPlanoDescontoVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(CondicaoPagamentoPlanoDescontoVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(CondicaoPagamentoPlanoDescontoVO obj, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List<CondicaoPagamentoPlanoDescontoVO> consultarPorCondicaoPagamentoPlanoFinanceiroCurso(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void alterarPlanoDescontoPadraoCondicaoPagamentoVOs(Integer condicaoPagamento, List<CondicaoPagamentoPlanoDescontoVO> condicaoPagamentoPlanoDescontoVOs, UsuarioVO usuarioVO) throws Exception;

}
