/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.crm;

import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CompromissoCampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;

/**
 *
 * @author edigarjr
 */
@Repository
@Scope("singleton")
@Lazy
public interface CompromissoCampanhaPublicoAlvoProspectInterfaceFacade {

    public void persistir(CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception;

    public void excluir(CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Carlos Eugênio - 10/11/2016
	 * @param codigoCompromisso
	 * @param dataCompromisso
	 * @param horaCompromisso
	 * @param agendapessoahorario
	 * @param usuarioVO
	 * @throws Exception
	 */
	void alterarDataCompromissoRealizado(Integer codigoCompromisso, Date dataCompromisso, String horaCompromisso, Integer agendapessoahorario, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Carlos Eugênio - 10/11/2016
	 * @param campanha
	 * @param dataCompromisso
	 * @param usuario
	 * @param prospect
	 * @param horaGeracaoProximaAgenda
	 * @return
	 * @throws Exception
	 */
	CompromissoCampanhaPublicoAlvoProspectVO executarGeracaoCompromissoRotacionamentoSimulacao(CampanhaVO campanha, Date dataCompromisso, UsuarioVO usuario, ProspectsVO prospect, String horaGeracaoProximaAgenda) throws Exception;

	/**
	 * @author Carlos Eugênio - 10/11/2016
	 * @param obj
	 * @param usuarioVO
	 * @throws Exception
	 */
	void incluirSemValidarDados(CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Carlos Eugênio - 10/11/2016
	 * @param obj
	 * @param usuarioVO
	 * @throws Exception
	 */
	void alterarSemValidarDados(CompromissoCampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Carlos Eugênio - 10/11/2016
	 * @param codigoPrm
	 * @param nivelMontarDados
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
	CompromissoCampanhaPublicoAlvoProspectVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * @author Carlos Eugênio - 29/11/2016
	 * @param campanha
	 * @throws Exception
	 */
	void excluirCompromissoCampanhaPublicoAlvoPorCampanha(Integer campanha, UsuarioVO usuarioLogado) throws Exception;

	/**
	 * @author Carlos Eugênio - 16/12/2016
	 * @param campanha
	 * @param prospect
	 * @param usuarioVO
	 * @throws Exception
	 */
	void excluirCompromissoCampanhaPublicoAlvoPorCampanhaProspect(Integer campanha, Integer prospect, UsuarioVO usuarioVO) throws Exception;

	void excluirCompromissoCampanhaPublicoAlvoProspectQuandoExcluidoCompromissoConsultor(Integer prospect,
			TipoSituacaoCompromissoEnum tipoSituacaoCompromissoEnum, UsuarioVO usuarioVO);

	void excluirCompromissoCampanhaPublicoAlvoProspectQuandoExcluidoCompromissoConsultor(
			CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO, UsuarioVO usuarioVO);

	public void alterarProspectCompromissoCampanhaPublicoAlvoProspect(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuario) throws Exception;
	
	public void executarCancelamentoCompromissoCampanhaPublicoAlvoProspectNaoIniciacaoCampanha(Integer codCampanha,  UsuarioVO usuario) throws Exception;
			
}