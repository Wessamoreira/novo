package negocio.interfaces.crm;
import java.util.List;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.RegistroEntradaProspectsVO;
import negocio.comuns.crm.RegistroEntradaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface RegistroEntradaInterfaceFacade {
	

    public void persistir(RegistroEntradaVO obj, Boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
    public void excluir(RegistroEntradaVO obj, Boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
    public List consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception;
    public void validarDados(RegistroEntradaVO obj) throws Exception;
    public RegistroEntradaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception;
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception;
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void adicionarObjRegistroEntradaProspectsVOs(RegistroEntradaVO objRegistroEntradaVO, RegistroEntradaProspectsVO obj) throws Exception;
    public void excluirObjRegistroEntradaProspectsVOs(RegistroEntradaVO objRegistroEntradaVO, Integer prospects) throws Exception;
    public RegistroEntradaProspectsVO consultarObjRegistroEntradaProspectsVO(RegistroEntradaVO objRegistroEntradaVO, Integer prospects) throws Exception;
    public List<String> realizarLeituraArquivoExcel(FileUploadEvent upload, RegistroEntradaVO registroEntradaVO, UsuarioVO usuarioLogado) throws Exception;
    public void realizarLeituraArquivoCsv(FileUploadEvent uploadEvent, RegistroEntradaVO entradaVO, String delimitador,UsuarioVO usuarioLogado)throws Exception;
    public List preencherListaDeProspectsParaRelatorio(List<ProspectsVO> prospectsVOs);
    public String designIReportRelatorio();
    public String caminhoBaseRelatorio();
}