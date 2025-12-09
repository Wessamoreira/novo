package negocio.interfaces.academico;
import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.TextoEnadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface EnadeInterfaceFacade {
	

    public EnadeVO novo() throws Exception;
    public void incluir(EnadeVO obj,UsuarioVO usuario) throws Exception;
    public void alterar(EnadeVO obj,UsuarioVO usuario) throws Exception;
    public void excluir(EnadeVO obj,UsuarioVO usuario) throws Exception;
    public EnadeVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados ,UsuarioVO usuario) throws Exception;
    public List<EnadeVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados ,UsuarioVO usuario) throws Exception;
    public List<EnadeVO> consultarPorTituloEnade(String valorConsulta, boolean controlarAcesso, int nivelMontarDados ,UsuarioVO usuario) throws Exception;
    public List<EnadeVO> consultarPorDataPublicacaoPortariaDOU(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados ,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	void adicionarTextoEnadeVOs(EnadeVO enadeVO, TextoEnadeVO textoEnadeVO) throws Exception;
	void excluirTextoEnadeVOs(EnadeVO enadeVO, TextoEnadeVO textoEnadeVO) throws Exception;
	public void adicionarEnadeCursoVOs(EnadeVO enadeVO, EnadeCursoVO enadeCursoVO) throws Exception;
	public void excluirEnadeCursoVOs(EnadeVO enadeVO, EnadeCursoVO enadeCursoVO) throws Exception;
	public List consultarEnade(DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public MapaConvocacaoEnadeVO consultaRapidaPorEnadeParaTXT(String valorConsulta,  boolean controlarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer codigoCurso, String tipoAluno) throws Exception;
	public List<MapaConvocacaoEnadeVO> consultarPorCodigoEnade(String campoConsulta, Integer valorConsulta,  UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados) throws Exception;
}