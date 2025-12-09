package negocio.interfaces.academico;

import java.util.List;

import jakarta.faces. model.SelectItem;
import jobs.enumeradores.TipoUsoNotaEnum;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface ConfiguracaoAcademicoNotaInterfaceFacade {

	public void incluirConfiguracaoAcademicoNotaVOs(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;

	public void alterarConfiguracaoAcademicoNotaVOs(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;

	public void excluirConfiguracaoAcademicoNotaVOs(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;

	public List<ConfiguracaoAcademicaNotaVO> consultarPorConfiguracaoAcademico(Integer configuracaoAcademicoVO) throws Exception;

	public void carregarDadosConfiguracaoAcademicoNotaVOs(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;

	void validarDados(ConfiguracaoAcademicoVO configuracaoAcademicaVO, ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO) throws ConsistirException;

	/**
	 * @author Wellington Rodrigues - 21 de jul de 2015
	 * @param configuracaoAcademicoVO
	 * @param notaRecuperacao
	 * @return
	 * @throws Exception
	 */
	List<ConfiguracaoAcademicaNotaVO> consultarPorConfiguracaoAcademicoNotaRecuperacao(Integer configuracaoAcademicoVO, boolean notaRecuperacao) throws Exception;
	
	
	ConfiguracaoAcademicaNotaVO consultarPorConfiguracaoAcademicoPorTipoUsoNotaEnum(ConfiguracaoAcademicoVO configuracaoAcademico, TipoUsoNotaEnum tipoUsoNotaEnum);

	/**
	 * @author Rodrigo Wind - 29/07/2015
	 * @param configuracaoAcademicoVO
	 * @param configuracaoAcademicaNotaVO
	 * @param numeroNota
	 * @throws Exception
	 */
	void replicarInformacaoConfiguracaoAcademicoParaConfiguracaoAcademicoNota(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 19/10/2015
	 * @param configuracaoAcademicoVO
	 * @param notaRecuperacao
	 * @return
	 * @throws Exception
	 */
	List<ConfiguracaoAcademicaNotaVO> consultarPorConfiguracaoAcademicoNotaPermiteLancamentoNota(Integer configuracaoAcademicoVO, boolean notaRecuperacao) throws Exception;

	ConfiguracaoAcademicaNotaVO consultarPorConfiguracaoAcademicoPorTipoUsoNotaEnum(ConfiguracaoAcademicoVO configuracaoAcademico);

	List<SelectItem> consultarConfiguracaoAcademicaNotaPorMatriculaDisciplina(String matricula, Integer disciplina)
			throws Exception;

	List<SelectItem> consultarConfiguracaoAcademicaNotaPorDisciplina(Integer disciplina, String ano, String semestre) throws Exception;

}
