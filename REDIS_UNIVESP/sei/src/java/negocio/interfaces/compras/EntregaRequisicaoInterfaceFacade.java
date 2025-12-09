package negocio.interfaces.compras;
import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.EntregaRequisicaoVO;

public interface EntregaRequisicaoInterfaceFacade {
    
    public void incluir(EntregaRequisicaoVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(EntregaRequisicaoVO obj, UsuarioVO usuario) throws Exception;
    public EntregaRequisicaoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	void consultar(Integer unidadeEnsino, DataModelo dataModelo);
	public void atualizarRequisicaoItem(EntregaRequisicaoVO obj, UsuarioVO usuario) throws Exception;
}