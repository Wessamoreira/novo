package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.MetaItemVO;
import negocio.comuns.crm.MetaVO;

public interface MetaItemInterfaceFacade {

    public List montarListaMetaItem(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void alterarMetaItens(Integer workflow, List objetos, UsuarioVO usuario) throws Exception;

    public void adicionarObjMetaItemVOs(MetaVO metaVO, MetaItemVO obj) throws Exception;

    public void excluirObjMetaItemVOs(MetaVO metaVO, MetaItemVO metaItemVO) throws Exception;

    public void incluirMetaItens(Integer meta, List objetos, UsuarioVO usuario) throws Exception;

    public void excluirMetaItens(Integer meta, UsuarioVO usuario) throws Exception;
}
