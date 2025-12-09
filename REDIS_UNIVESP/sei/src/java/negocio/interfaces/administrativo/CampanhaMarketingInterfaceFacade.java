package negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.CampanhaMarketingVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface CampanhaMarketingInterfaceFacade {




    
   
    public List consultarPorDataAutorizacao(Date dateTime, Date dateTime0, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDataVinculacao(Date dateTime, Date dateTime0, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public CampanhaMarketingVO novo() throws Exception;
    public void incluir(CampanhaMarketingVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(CampanhaMarketingVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(CampanhaMarketingVO obj) throws Exception;    
    public void indeferirCampanha(CampanhaMarketingVO campanhaMarketingVO, UsuarioVO usuario)throws Exception;
    public void autorizarCampanha(CampanhaMarketingVO obj, UsuarioVO usuario) throws Exception;
    public void finalizarCampanha(CampanhaMarketingVO obj, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;    
    public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)throws Exception;
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDataRequisicao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public CampanhaMarketingVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);

}