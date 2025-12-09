package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MaterialRequerimentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface MaterialRequerimentoInterfaceFacade {

    public void incluir(final MaterialRequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void alterar(final MaterialRequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void excluir(MaterialRequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public MaterialRequerimentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception;

    public List consultarPorRequerimento(Integer codigoRequerimento, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void incluirMaterialRequerimentos(Integer RequerimentoPrm, List<MaterialRequerimentoVO> objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void alterarMaterialRequerimentos(Integer Requerimento, List<MaterialRequerimentoVO> objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void excluirMaterialRequerimentos(List<MaterialRequerimentoVO> materialRequerimentoVOs, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    
    public void alterarDescricaoMaterialRequerimento(MaterialRequerimentoVO obj, UsuarioVO usuario) throws Exception;
    
    public void alterarDisponibilizarParaRequerenteMaterialRequerimento(MaterialRequerimentoVO obj, UsuarioVO usuario) throws Exception;    
    
    public List consultarPorRequerimentoHistorico(Integer codigoRequerimentoHistorico, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void permitirExcluirArquivoMaterialRequerimento(List<MaterialRequerimentoVO> materialRequerimentoVOs,UsuarioVO usuarioLogado)  throws Exception;
    
}
