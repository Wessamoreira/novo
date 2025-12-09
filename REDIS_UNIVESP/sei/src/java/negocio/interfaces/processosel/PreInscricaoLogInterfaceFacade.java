package negocio.interfaces.processosel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.PreInscricaoLogVO;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.processosel.enumeradores.SituacaoLogPreInscricaoEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface PreInscricaoLogInterfaceFacade {

    public void incluir(PreInscricaoLogVO obj) throws Exception;

    List consultarPorUnidadeEnsino(Integer unidadeEnsino, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception;

	List consultarPorCodigoCurso(Integer curso, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao,UsuarioVO usuario) throws Exception;
	
	List consultarPorNomeCurso(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception;

	List consultarPorEmail(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception;

	List consultarPorNomeProspect(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception;

	List consultarPorPeriodo(Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

	Integer consultarTotalRegistrosPorNomeProspect(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception;

	Integer consultarTotalRegistrosPorEmail(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception;

	Integer consultarTotalRegistrosNomeCurso(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception;

	Integer consultarTotalRegistrosPorCodigoCurso(Integer curso, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception;

	Integer consultarTotalRegistrosPorUnidadeEnsino(Integer unidadeEnsino, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception;

	Integer consultarTotalRegistrosPorPeriodo(Date dataInicial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void gravarPreInscricaoComFalhaCadastro(PreInscricaoLogVO preInscricaoLogVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void gravarPreInscricaoComFalhaSelecionadas(List listaPreInscricaoLogVOs, HashMap<Integer, PreInscricaoLogVO> mapLogPreInscricaoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Monta o objeto preInscricaoVO com os dados de uma preInscricaoVO
	 * 
	 * @param preInscricaoVO
	 * @return PreInscricaoLogVO populada com as propriedades de @param || Nova entidade PreInscricaoLogVO caso @param == null
	 *        					
	 */
	public PreInscricaoLogVO montarDados(PreInscricaoVO preInscricaoVO);
	
}