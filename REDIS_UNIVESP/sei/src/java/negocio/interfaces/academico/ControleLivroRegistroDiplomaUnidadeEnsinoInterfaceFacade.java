package negocio.interfaces.academico;
import java.util.List;

import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.arquitetura.UsuarioVO;


public interface ControleLivroRegistroDiplomaUnidadeEnsinoInterfaceFacade {
	
	public void persistir(ControleLivroRegistroDiplomaVO obj, UsuarioVO usuario) throws Exception;
	
    public void incluir(ControleLivroRegistroDiplomaUnidadeEnsinoVO obj, UsuarioVO usuario) throws Exception;
    
    public void excluir(Integer codigoControleLivroRegistro, UsuarioVO usuario) throws Exception;

	public void carregarUnidadeEnsinoNaoSelecionado(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO) throws Exception;

	public List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> consultarPorControleLivroRegistroDiploma(Integer codigo) throws Exception;
    
}