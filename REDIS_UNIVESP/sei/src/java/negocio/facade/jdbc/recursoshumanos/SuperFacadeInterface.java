package negocio.facade.jdbc.recursoshumanos;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface SuperFacadeInterface <T extends SuperVO> {

	public abstract void persistir(T obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public abstract void incluir(T obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public abstract void alterar(T obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public abstract void excluir(T obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	public abstract T consultarPorChavePrimaria(Long id) throws Exception;

	public abstract void validarDados(T obj) throws ConsistirException;

	public abstract T montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception;

}