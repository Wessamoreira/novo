package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.MaterialAlunoVO;
import negocio.comuns.academico.MaterialProfessorVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LocalAulaInterfaceFacade;

@Repository
@Lazy
public class LocalAula extends ControleAcesso implements LocalAulaInterfaceFacade {

    /**
     *
     */
    private static final long serialVersionUID = 8476062754863707792L;
    private static final String idEntidade = "LocalAula";

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void persistir(LocalAulaVO localAulaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        if (localAulaVO.isNovoObj()) {
            incluir(localAulaVO, usuarioVO, configuracaoGeralSistema);
        } else {
            alterar(localAulaVO, usuarioVO, configuracaoGeralSistema);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void incluir(final LocalAulaVO localAulaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        incluir(LocalAula.idEntidade, usuarioVO);
        validarDados(localAulaVO);
        localAulaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                StringBuilder sql = new StringBuilder("INSERT INTO localAula ");
                sql.append(" ( local, endereco, telefone, situacao, observacao, unidadeEnsino ) VALUES (?, ?, ?, ?, ?, ?) returning codigo ");
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                ps.setString(1, localAulaVO.getLocal());
                ps.setString(2, localAulaVO.getEndereco());
                ps.setString(3, localAulaVO.getTelefone());
                ps.setString(4, localAulaVO.getSituacao().name());
                ps.setString(5, localAulaVO.getObservacao());
				if (localAulaVO.getUnidadeEnsino().getCodigo().intValue() != 0) {
					ps.setInt(6, localAulaVO.getUnidadeEnsino().getCodigo().intValue());
				} else {
					ps.setNull(6, 0);
				}
                return ps;
            }
        }, new ResultSetExtractor<Integer>() {

            @Override
            public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                if (arg0.next()) {
                    return arg0.getInt("codigo");
                }
                return null;
            }
        }));
        getFacadeFactory().getSalaLocalAulaFacade().incluirSalaLocalAula(localAulaVO);
        getFacadeFactory().getMaterialAlunoFacade().incluirMaterialAlunos(localAulaVO.getCodigo(), localAulaVO.getMaterialAlunoVOs(), usuarioVO, configuracaoGeralSistema);
        getFacadeFactory().getMaterialProfessorFacade().incluirMaterialProfessors(localAulaVO.getCodigo(), localAulaVO.getMaterialProfessorVOs(), usuarioVO, configuracaoGeralSistema);
        localAulaVO.setNovoObj(false);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void alterar(final LocalAulaVO localAulaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        alterar(idEntidade, usuarioVO);
        validarDados(localAulaVO);
        if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                StringBuilder sql = new StringBuilder("UPDATE LocalAula SET ");
                sql.append(" local = ?, endereco = ?, telefone = ?, situacao = ?, observacao = ?, unidadeEnsino=? WHERE codigo = ? ");
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                ps.setString(1, localAulaVO.getLocal());
                ps.setString(2, localAulaVO.getEndereco());
                ps.setString(3, localAulaVO.getTelefone());
                ps.setString(4, localAulaVO.getSituacao().name());
                ps.setString(5, localAulaVO.getObservacao());
				if (localAulaVO.getUnidadeEnsino().getCodigo().intValue() != 0) {
					ps.setInt(6, localAulaVO.getUnidadeEnsino().getCodigo().intValue());
				} else {
					ps.setNull(6, 0);
				}
                ps.setInt(7, localAulaVO.getCodigo());
                return ps;
            }
        }) == 0) {
            incluir(localAulaVO, usuarioVO, configuracaoGeralSistema);
            return;
        }
        getFacadeFactory().getSalaLocalAulaFacade().alterarSalaLocalAula(localAulaVO);
        getFacadeFactory().getMaterialAlunoFacade().alterarMaterialAlunos(localAulaVO.getCodigo(), localAulaVO.getMaterialAlunoVOs(), usuarioVO, configuracaoGeralSistema);
        getFacadeFactory().getMaterialProfessorFacade().alterarMaterialProfessors(localAulaVO.getCodigo(), localAulaVO.getMaterialProfessorVOs(), usuarioVO, configuracaoGeralSistema);
        localAulaVO.setNovoObj(false);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void inativar(LocalAulaVO localAulaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            localAulaVO.setSituacao(StatusAtivoInativoEnum.INATIVO);
            for(SalaLocalAulaVO obj: localAulaVO.getSalaLocalAulaVOs()){
            	obj.setInativo(true);
            }
            persistir(localAulaVO, usuarioVO, configuracaoGeralSistema);
        } catch (Exception e) {
            localAulaVO.setSituacao(StatusAtivoInativoEnum.ATIVO);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void ativar(LocalAulaVO localAulaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            localAulaVO.setSituacao(StatusAtivoInativoEnum.ATIVO);
            for(SalaLocalAulaVO obj: localAulaVO.getSalaLocalAulaVOs()){
            	obj.setInativo(false);
            }
            persistir(localAulaVO, usuarioVO, configuracaoGeralSistema);
        } catch (Exception e) {
            localAulaVO.setSituacao(StatusAtivoInativoEnum.INATIVO);
            throw e;
        }
    }

    @Override
    public void adicionarSalaLocalAulaVO(LocalAulaVO localAulaVO, SalaLocalAulaVO salaLocalAulaVO) throws Exception {
        getFacadeFactory().getSalaLocalAulaFacade().validarDados(salaLocalAulaVO);
        for (SalaLocalAulaVO obj : localAulaVO.getSalaLocalAulaVOs()) {
            if (obj.getSala().toUpperCase().trim().equals(salaLocalAulaVO.getSala().toUpperCase().trim())) {
                throw new ConsistirException(UteisJSF.internacionalizar("msg_salaLocalAula_sala_existente"));
            }
        }
        int x = 0;
        for(SalaLocalAulaVO sala: localAulaVO.getSalaLocalAulaVOs()){
        	sala.setOrdem(++x);
        }
        salaLocalAulaVO.setOrdem(localAulaVO.getSalaLocalAulaVOs().size()+1);
        localAulaVO.getSalaLocalAulaVOs().add(salaLocalAulaVO);
    }

    @Override
    public void removerSalaLocalAulaVO(LocalAulaVO localAulaVO, SalaLocalAulaVO salaLocalAulaVO) throws Exception {
        int x = 0;
        for (SalaLocalAulaVO obj : localAulaVO.getSalaLocalAulaVOs()) {
            if (obj.getSala().toUpperCase().trim().equals(salaLocalAulaVO.getSala().toUpperCase().trim())) {
                localAulaVO.getSalaLocalAulaVOs().remove(x);
                int y = 0;
                for(SalaLocalAulaVO sala: localAulaVO.getSalaLocalAulaVOs()){
                	sala.setOrdem(++y);
                }
                return;
            }
            x++;
        }
    }

    @Override
    public void validarDados(LocalAulaVO localAulaVO) throws ConsistirException {
        ConsistirException consistirException = new ConsistirException();
        if (localAulaVO.getLocal().trim().isEmpty()) {
            consistirException.adicionarListaMensagemErro("O campo LOCAL deve ser informado.");
        }
        if (localAulaVO.getEndereco().trim().isEmpty()) {
            consistirException.adicionarListaMensagemErro("O campo ENDEREÇO deve ser informado.");
        }
        if (localAulaVO.getSalaLocalAulaVOs().isEmpty()) {
            consistirException.adicionarListaMensagemErro("O campo SALA deve ser informado.");
        }

        if (!consistirException.getListaMensagemErro().isEmpty()) {
            throw consistirException;
        } else {
            consistirException = null;
        }

    }

    @Override
    public List<LocalAulaVO> consulta(String campoConsulta, String valorConsulta,Integer unidadeEnsinoConsulta , Integer unidadeEnsino, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        consultar(idEntidade, verificarAcesso, usuarioVO);
        StringBuilder sql = new StringBuilder("SELECT * FROM LocalAula ");
        sql.append(" WHERE 1=1 ");
        if (unidadeEnsino > 0 || unidadeEnsinoConsulta > 0) {
        	sql.append(" and (unidadeEnsino = ").append(unidadeEnsino).append(" or (unidadeEnsino = ").append(unidadeEnsinoConsulta).append(" or unidadeEnsino is null))");
        }
        if (campoConsulta.equals("local")) {
            sql.append(" and upper(sem_acentos(local)) like (upper(sem_acentos('%").append(valorConsulta).append("%')))");
            sql.append(" order by local ");
        }
        if (campoConsulta.equals("endereco")) {
            sql.append(" and upper(sem_acentos(endereco)) like (upper(sem_acentos('%").append(valorConsulta).append("%')))");
            sql.append(" order by endereco ");
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
    }

    private List<LocalAulaVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<LocalAulaVO> localAulaVOs = new ArrayList<LocalAulaVO>(0);
        while (rs.next()) {
            localAulaVOs.add(montarDados(rs, nivelMontarDados, usuario));
        }
        return localAulaVOs;
    }

    private LocalAulaVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        LocalAulaVO localAulaVO = new LocalAulaVO();
        localAulaVO.setNovoObj(false);
        localAulaVO.setCodigo(rs.getInt("codigo"));
        localAulaVO.setLocal(rs.getString("local"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return localAulaVO;
        }
        localAulaVO.setEndereco(rs.getString("endereco"));
        localAulaVO.setTelefone(rs.getString("telefone"));
        localAulaVO.setObservacao(rs.getString("observacao"));
        localAulaVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
        montarDadosUnidadeEnsino(localAulaVO, usuario);
        localAulaVO.setSituacao(StatusAtivoInativoEnum.valueOf(rs.getString("situacao")));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return localAulaVO;
        }
        localAulaVO.setSalaLocalAulaVOs(getFacadeFactory().getSalaLocalAulaFacade().consultarPorLocalAula(localAulaVO.getCodigo()));
        localAulaVO.setMaterialAlunoVOs(getFacadeFactory().getMaterialAlunoFacade().consultarPorLocalAula(localAulaVO.getCodigo(), nivelMontarDados, usuario));
        localAulaVO.setMaterialProfessorVOs(getFacadeFactory().getMaterialProfessorFacade().consultarPorLocalAula(localAulaVO.getCodigo(), nivelMontarDados, usuario));
        return localAulaVO;
    }

    @Override
    public List<LocalAulaVO> consultaLocalSalaAulaPorSituacao(StatusAtivoInativoEnum situacao, Integer unidadeEnsino, boolean verificarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM LocalAula ");
        sql.append(" WHERE situacao = '").append(situacao.name()).append("' ");
        if (unidadeEnsino > 0) {
        	sql.append(" and (unidadeEnsino = ").append(unidadeEnsino).append(" or unidadeEnsino is null)");
        }
        sql.append(" order by local ");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, usuarioVO);
    }
    
    @Override
    public List<LocalAulaVO> consultarPorLocal(String local, Integer unidadeEnsino, StatusAtivoInativoEnum situacao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM LocalAula ");
        sql.append(" WHERE local ilike ? ");
        if (unidadeEnsino > 0) {
        	sql.append(" and (unidadeEnsino = ").append(unidadeEnsino).append(" or unidadeEnsino is null)");
        }
        if(Uteis.isAtributoPreenchido(situacao)) {
        	sql.append(" and LocalAula.situacao = '").append(situacao.name()).append("' ");
        }
        sql.append(" order by local ");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), local + PERCENT), nivelMontarDados, usuarioVO);
    }

    @Override
    public LocalAulaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM LocalAula ");
        sql.append(" WHERE codigo = ").append(codigo);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return montarDados(rs, nivelMontarDados, usuario);
        }
        return null;
    }
    
    @Override
    public void realizarAlteracaoOrdem(LocalAulaVO localAulaVO, SalaLocalAulaVO salaLocalAulaVO, boolean subir) {
    	Integer ordemTmp  = salaLocalAulaVO.getOrdem();
    	SalaLocalAulaVO salaLocalAulaVO2 =  null;
    	if(subir){
    		if(salaLocalAulaVO.getOrdem() == 1){
    			return;
    		}
    		salaLocalAulaVO2 = localAulaVO.getSalaLocalAulaVOs().get(salaLocalAulaVO.getOrdem()-2);    		
    	}else{
    		if(salaLocalAulaVO.getOrdem() == localAulaVO.getSalaLocalAulaVOs().size()){
    			return;
    		}
    		salaLocalAulaVO2 = localAulaVO.getSalaLocalAulaVOs().get(salaLocalAulaVO.getOrdem());
    	}
    	
    	salaLocalAulaVO.setOrdem(salaLocalAulaVO2.getOrdem());
    	salaLocalAulaVO2.setOrdem(ordemTmp);
		Ordenacao.ordenarLista(localAulaVO.getSalaLocalAulaVOs(), "ordem");
    }
    
    public static void montarDadosUnidadeEnsino(LocalAulaVO obj, UsuarioVO  usuario) throws Exception{
    	if(Uteis.isAtributoPreenchido(obj) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())){
    	obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
    	}
    }
    
    public void validarMaterialAluno(MaterialAlunoVO materialAlunoVO) throws ConsistirException {
    	if (materialAlunoVO.getDescricao() == "") {
    		throw new ConsistirException("O campo DESCRIÇÃO (Arquivos Disponibilizados para Alunos) deve ser informado");
    	}

    	if (materialAlunoVO.getArquivoVO().getNome() == "") {
    		throw new ConsistirException("O ARQUIVO (Arquivos Disponibilizados para Alunos) deve ser informado");
    	}
    }
    
    public void validarMaterialProfessor(MaterialProfessorVO materialProfessorVO) throws ConsistirException {
    	if (materialProfessorVO.getDescricao() == "") {
    		throw new ConsistirException("O campo DESCRIÇÃO (Arquivos Disponibilizados para Professores) deve ser informado");
    	}

    	if (materialProfessorVO.getArquivoVO().getNome() == "") {
    		throw new ConsistirException("O ARQUIVO (Arquivos Disponibilizados para Professores) deve ser informado");
    	}
    }
}
