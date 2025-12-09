package negocio.facade.jdbc.recursoshumanos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorCursoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.interfaces.recursoshumanos.AtividadeExtraClasseProfessorCursoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>AtividadeExtraClasseProfessorCursoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>AtividadeExtraClasseProfessorCursoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class AtividadeExtraClasseProfessorCurso extends SuperFacade<AtividadeExtraClasseProfessorCursoVO> implements AtividadeExtraClasseProfessorCursoInterfaceFacade<AtividadeExtraClasseProfessorCursoVO> {

	private static final long serialVersionUID = 3293411158333469691L;

	protected static String idEntidade;

	@PostConstruct
	public void init() {
		setIdEntidade("AtividadeExtraClasseProfessorCurso");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(AtividadeExtraClasseProfessorCursoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AtividadeExtraClasseProfessorCurso.incluir(getIdEntidade(), validarAcesso, usuarioVO);

		incluir(obj, "AtividadeExtraClasseProfessorCurso",
				new AtributoPersistencia()
						.add("horaPrevista", obj.getHoraPrevista())
						.add("curso", obj.getCursoVO())
						.add("atividadeextraclasseprofessor", obj.getAtividadeExtraClasseProfessorVO()),
				usuarioVO);
		obj.setNovoObj(Boolean.TRUE); 
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(AtividadeExtraClasseProfessorCursoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AtividadeExtraClasseProfessorCurso.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "AtividadeExtraClasseProfessorCurso",
				new AtributoPersistencia()
						.add("horaPrevista", obj.getHoraPrevista())
						.add("curso", obj.getCursoVO())
						.add("atividadeextraclasseprofessor", obj.getAtividadeExtraClasseProfessorVO()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AtividadeExtraClasseProfessorCursoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		AtividadeExtraClasseProfessorCurso.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM AtividadeExtraClasseProfessorCurso WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorAtividadeExtraClasse(AtividadeExtraClasseProfessorVO obj, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM AtividadeExtraClasseProfessorCurso WHERE atividadeextraclasseprofessor = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public AtividadeExtraClasseProfessorCursoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM AtividadeExtraClasseProfessorCurso WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public AtividadeExtraClasseProfessorCursoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		AtividadeExtraClasseProfessorCursoVO obj = new AtividadeExtraClasseProfessorCursoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setHoraPrevista(tabelaResultado.getInt("horaPrevista"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("curso"))) {	
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("curso"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("atividadeExtraClasseProfessor"))) {			
			obj.setAtividadeExtraClasseProfessorVO(getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("atividadeExtraClasseProfessor")));
		}

		return obj;
	}

	@Override
	public void validarDados(AtividadeExtraClasseProfessorCursoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getCursoVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Forum_curso"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getHoraPrevista())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_horaPrevista"));
		}
		
		if (obj.getHoraPrevista() <= 0) {			
			throw new ConsistirException(UteisJSF.internacionalizar("msg_HorasAtividadeExtraClasseProfessor_horaPrevistaMenorQueZero"));
		}
	}

	public class ComparatorDate implements Comparator<Date> {
	    private DateFormat dataFormat = new SimpleDateFormat("MM/yyyy");

	    public int compare(Date d1, Date d2) {
	        return dataFormat.format(d1).compareTo(dataFormat.format(d2));
	    }
	}

	@Override
	public void persistir(AtividadeExtraClasseProfessorCursoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRES_NEW)
	public void persistirTodos(List<AtividadeExtraClasseProfessorCursoVO> listaAtividadeExtraClasseProfessorCurso,
			boolean validarAcesso, UsuarioVO usuarioVO, AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO) throws Exception {
		for (AtividadeExtraClasseProfessorCursoVO AtividadeExtraClasseProfessorCursoVO : listaAtividadeExtraClasseProfessorCurso) {
			if (AtividadeExtraClasseProfessorCursoVO.getHoraPrevista() < AtividadeExtraClasseProfessorCursoVO.getTotalHorasAprovadas()) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_AtividadeExtraClasseProfessor_erroHoraPrevistaMenorAprovada"));
			}
			AtividadeExtraClasseProfessorCursoVO.setAtividadeExtraClasseProfessorVO(atividadeExtraClasseProfessorVO);
			persistir(AtividadeExtraClasseProfessorCursoVO, validarAcesso, usuarioVO);
		}
	}

	/**
	 * Monta a lista de {@link AtividadeExtraClasseProfessorCursoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	public List<AtividadeExtraClasseProfessorCursoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<AtividadeExtraClasseProfessorCursoVO> listaHoraAtividadeExtraClasseProfessorCurso = new ArrayList<>();

        while(tabelaResultado.next()) {
        	listaHoraAtividadeExtraClasseProfessorCurso.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaHoraAtividadeExtraClasseProfessorCurso;
	}

	/**
	 * Adiciona a {@link AtividadeExtraClasseProfessorCursoVO}
	 */
	@Override
	public void adicionarAtividadeExtraClasseCurso(
			List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor,
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoVO,
			AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO) throws Exception {
		validarDados(atividadeExtraClasseProfessorCursoVO);
		
		boolean naoExisteAtividadeExtraClasseParaCurso = atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs().stream().noneMatch(
				p -> (p.getCursoVO().getCodigo().equals(atividadeExtraClasseProfessorCursoVO.getCursoVO().getCodigo()) &&
						(UteisData.getMesData(p.getAtividadeExtraClasseProfessorVO().getData()) == UteisData.getMesData(atividadeExtraClasseProfessorVO.getData()) &&
						UteisData.getAnoData(p.getAtividadeExtraClasseProfessorVO().getData()) == UteisData.getAnoData(atividadeExtraClasseProfessorVO.getData()))));

		if (naoExisteAtividadeExtraClasseParaCurso) {
			atividadeExtraClasseProfessorCursoVO.setAtividadeExtraClasseProfessorVO(atividadeExtraClasseProfessorVO);
			atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs().add(atividadeExtraClasseProfessorCursoVO);
		} else {
			return;
		}

		int index = 0;
		for (AtividadeExtraClasseProfessorVO obj : listaAtividadeExtraClasseProfessor) {
			if (obj.getCodigo().equals(atividadeExtraClasseProfessorVO.getCodigo())) {
				break;
			}
			index++;
		}

		if (index >= 0) {
			listaAtividadeExtraClasseProfessor.set(index, atividadeExtraClasseProfessorVO);
		}

		Ordenacao.ordenarLista(atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs(), "atividadeExtraClasseProfessorVO.data");
	}

	@Override
	public void adicionarAtividadeExtraClasseCursoReplicarTodosMeses(List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor,
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoVO, AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorSelecionadoVO) throws Exception {
		validarDados(atividadeExtraClasseProfessorCursoVO);

		for (AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO : listaAtividadeExtraClasseProfessor) {
			
			if (!Uteis.isAtributoPreenchido(atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs())) {
				atividadeExtraClasseProfessorVO.setAtividadeExtraClasseProfessorCursoVOs(
						getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().consultarPorAtividadeExtraClasse(atividadeExtraClasseProfessorVO, null));
			}
			
			boolean adicionarCurso = atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs().stream()
				.noneMatch(p -> p.getCursoVO().getCodigo().equals(atividadeExtraClasseProfessorCursoVO.getCursoVO().getCodigo()));

			if (adicionarCurso || !Uteis.isAtributoPreenchido(atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs())) {
				AtividadeExtraClasseProfessorCursoVO obj = new AtividadeExtraClasseProfessorCursoVO();
				obj.setAtividadeExtraClasseProfessorVO(atividadeExtraClasseProfessorVO);
				obj.setHoraPrevista(atividadeExtraClasseProfessorCursoVO.getHoraPrevista());
				obj.setCursoVO(atividadeExtraClasseProfessorCursoVO.getCursoVO());
				atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs().add(obj);
				
				if ( (UteisData.getMesData(atividadeExtraClasseProfessorSelecionadoVO.getData()) == UteisData.getMesData(atividadeExtraClasseProfessorVO.getData()) &&
						 UteisData.getAnoData(atividadeExtraClasseProfessorSelecionadoVO.getData()) == UteisData.getAnoData(atividadeExtraClasseProfessorVO.getData())) ) {
					atividadeExtraClasseProfessorSelecionadoVO.setAtividadeExtraClasseProfessorCursoVOs(atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs());
				}
			}
		}
	}

	@Override
	public void removerTodosMesesAtividadeExtraClasseCurso(AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoVO,
			List<AtividadeExtraClasseProfessorCursoVO> listaAtividadeExtraClasseProfessorCurso, List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor) throws Exception {

		for (AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessor : listaAtividadeExtraClasseProfessor) {

			if ( !Uteis.isAtributoPreenchido(atividadeExtraClasseProfessor.getAtividadeExtraClasseProfessorCursoVOs())) {
				atividadeExtraClasseProfessor.setAtividadeExtraClasseProfessorCursoVOs(
						getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().consultarPorAtividadeExtraClasse(atividadeExtraClasseProfessor, null));
			}

			listaAtividadeExtraClasseProfessorCurso.removeIf(p -> p.getCursoVO().getCodigo().equals(atividadeExtraClasseProfessorCursoVO.getCursoVO().getCodigo()));
			atividadeExtraClasseProfessor.getAtividadeExtraClasseProfessorCursoVOs().removeIf(p -> p.getCursoVO().getCodigo().equals(atividadeExtraClasseProfessorCursoVO.getCursoVO().getCodigo()));
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alteraHorasPrevistaCurso(List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor,
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoSelecionadoVO) throws Exception {
		
		alterar(atividadeExtraClasseProfessorCursoSelecionadoVO, false, null);

		for (AtividadeExtraClasseProfessorVO object : listaAtividadeExtraClasseProfessor) {
			if (!Uteis.isAtributoPreenchido(object.getAtividadeExtraClasseProfessorCursoVOs())) {
				object.setAtividadeExtraClasseProfessorCursoVOs(getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().consultarPorAtividadeExtraClasse(object, null));
			}
			if (object.getCodigo().equals(atividadeExtraClasseProfessorCursoSelecionadoVO.getAtividadeExtraClasseProfessorVO().getCodigo())) {
				int totalHorasRealizadas = consultarTotalPorAtividadeExtraClasseProfessor(object);
				object.setHoraPrevista(totalHorasRealizadas);
				getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().alterarHoraPrevista(object.getCodigo(), totalHorasRealizadas);
			}
		}
	}
	
	private Integer consultarTotalPorAtividadeExtraClasseProfessor(AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(horaprevista) as qtde from atividadeextraclasseprofessorcurso");
		sql.append(" where atividadeextraclasseprofessor = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  atividadeExtraClasseProfessorVO.getCodigo());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alteraHorasPrevistaCursoTodosMeses(List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor,
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoSelecionadoVO,
			FuncionarioCargoVO funcionarioCargo, UsuarioVO usuarioVO) throws Exception {

		for (AtividadeExtraClasseProfessorVO object : listaAtividadeExtraClasseProfessor) {
			if (!Uteis.isAtributoPreenchido(object.getAtividadeExtraClasseProfessorCursoVOs())) {
				object.setAtividadeExtraClasseProfessorCursoVOs(getFacadeFactory().getAtividadeExtraClasseProfessorCursoInterfaceFacade().consultarPorAtividadeExtraClasse(object, usuarioVO));
			}
			
			for(AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoVO: object.getAtividadeExtraClasseProfessorCursoVOs()) {
				
				if (atividadeExtraClasseProfessorCursoVO.getCursoVO().getCodigo().equals(atividadeExtraClasseProfessorCursoSelecionadoVO.getCursoVO().getCodigo())) {
					atividadeExtraClasseProfessorCursoVO.setHoraPrevista(atividadeExtraClasseProfessorCursoSelecionadoVO.getHoraPrevista());
					break;
				}
			}
		}

		getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade().atualizarValorHoraPrevista(listaAtividadeExtraClasseProfessor);
		getFacadeFactory().getAtividadeExtraClasseProfessorInterfaceFacade()
			.persistirTodos(listaAtividadeExtraClasseProfessor, funcionarioCargo, false, usuarioVO);
		
	}

	@Override
	public List<AtividadeExtraClasseProfessorCursoVO> consultarPorAtividadeExtraClasse(AtividadeExtraClasseProfessorVO obj, UsuarioVO usuarioLogado)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT AtividadeExtraClasseProfessorCurso.codigo, AtividadeExtraClasseProfessorCurso.horaprevista, AtividadeExtraClasseProfessorCurso.curso, AtividadeExtraClasseProfessorCurso.atividadeextraclasseprofessor FROM AtividadeExtraClasseProfessorCurso ");
		sql.append(" inner join curso on atividadeextraclasseprofessorcurso.curso = curso.codigo");
		sql.append(" WHERE atividadeExtraClasseProfessor = ? order by curso.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCodigo());
		return montarDadosLista(tabelaResultado);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAtividadeExtraClasseProfessorCurso(AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("DELETE FROM AtividadeExtraClasseProfessorCurso WHERE (AtividadeExtraClasseProfessor = ?) and codigo not in (0 ");
        Iterator<AtividadeExtraClasseProfessorCursoVO> e = atividadeExtraClasseProfessorVO.getAtividadeExtraClasseProfessorCursoVOs().iterator();
        while (e.hasNext()) {
        	AtividadeExtraClasseProfessorCursoVO obj = (AtividadeExtraClasseProfessorCursoVO) e.next();         
            if(Uteis.isAtributoPreenchido(obj.getCodigo())){
            	sql.append(", ").append(obj.getCodigo());
            }
        }
        sql.append(") ");
        sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        getConexao().getJdbcTemplate().update(sql.toString(), atividadeExtraClasseProfessorVO.getCodigo());
	
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AtividadeExtraClasseProfessorCurso.idEntidade = idEntidade;
	}
}