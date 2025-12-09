package negocio.interfaces.financeiro;

import java.util.List;

import jobs.enumeradores.JobsEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.enumerador.IntegracaoNegativacaoCobrancaContaReceberEnum;

public interface AgenteNegativacaoCobrancaContaReceberInterfaceFacade {
	
    public AgenteNegativacaoCobrancaContaReceberVO novo() throws Exception;
    public void incluir(AgenteNegativacaoCobrancaContaReceberVO obj,  UsuarioVO usuarioVO) throws Exception;
    public void alterar(AgenteNegativacaoCobrancaContaReceberVO obj,  UsuarioVO usuarioVO) throws Exception;
    public void excluir(AgenteNegativacaoCobrancaContaReceberVO obj,  UsuarioVO usuarioVO) throws Exception;
    public AgenteNegativacaoCobrancaContaReceberVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso,int nivelMontarDados , UsuarioVO usuario) throws Exception;
    public List<AgenteNegativacaoCobrancaContaReceberVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<AgenteNegativacaoCobrancaContaReceberVO> consultarPorNome(String valorConsulta,  boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<AgenteNegativacaoCobrancaContaReceberVO> consultarPorTipo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List<AgenteNegativacaoCobrancaContaReceberVO> consultarAgenteNegativacaoSerasaApiGeo(IntegracaoNegativacaoCobrancaContaReceberEnum integracaoNegativacaoCobrancaContaReceberEnum, UsuarioVO usuario) throws Exception;
    
    public void criarCronogramaExecucaoJobSerasaApiGeo(JobsEnum jobsEnum , String horaExecutarJob , int codigoAgenteNegativacaoCobrancaContaReceberVO );
    void executarEnvioIntegracaoWebService(AgenteNegativacaoCobrancaContaReceberVO anccr, RegistroNegativacaoCobrancaContaReceberItemVO rnccri, JobsEnum jobsEnum, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

}