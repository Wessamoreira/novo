package negocio.interfaces.basico;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.DataComemorativaVO;
import negocio.comuns.basico.TipoDestinatarioDataComemorativaEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;


public interface DataComemorativaInterfaceFacade {
    
    
    void persistir(DataComemorativaVO dataComemorativaVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    
    List<DataComemorativaVO> consultar(Date dataInicio, Date dataTermino, String assunto,  TipoDestinatarioDataComemorativaEnum tipoDestinatarioDataComemorativaEnum, 
            StatusAtivoInativoEnum status, Integer unidadeEnsino, Integer departamento, Integer cargo, Integer areaConhecimento,
            Integer areaProfissinal, boolean validarAcesso, UsuarioVO usuario) throws Exception;
    
    void realizarAtivacao(DataComemorativaVO dataComemorativaVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    
    void realizarInativacao(DataComemorativaVO dataComemorativaVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public DataComemorativaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarDataComemorativaDataAtualTipoMensagemTopo() throws Exception;
}
