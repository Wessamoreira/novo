package negocio.facade.jdbc.bancocurriculum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.LinkVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.LinkInterfaceFacade;

@Repository
@Lazy
public class Link extends ControleAcesso implements LinkInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -399437303211845282L;
    protected static String idEntidade;

    public Link() throws Exception {
        super();
        setIdEntidade("Link");
    }

    @Override
    public void persistir(LinkVO linkVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        validarDados(linkVO);
        if (linkVO.isNovoObj()) {
            incluir(linkVO, usuarioLogado, configuracaoGeralSistema);
        } else {
            alterar(linkVO, usuarioLogado, configuracaoGeralSistema);
        }

    }

    @SuppressWarnings({"unchecked"})
    private void incluir(final LinkVO linkVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {

        try {
            Link.incluir(getIdEntidade(), true, usuarioLogado);
            if (linkVO.getIcone().getPastaBaseArquivoEnum() != null) {
                getFacadeFactory().getArquivoFacade().incluir(linkVO.getIcone(), false, usuarioLogado, configuracaoGeralSistema);
            }
            final String sql = "INSERT INTO Link( dataInicio, dataFim, unidadeEnsino, icone, escopo, link,"
                    + " situacao) VALUES ( ?, ?, ?, ?, ?, ?, ?) returning codigo";
            linkVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(linkVO.getDataInicio()));
                    if (linkVO.getDataFim() != null) {
                        sqlInserir.setDate(2, Uteis.getDataJDBC(linkVO.getDataFim()));
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    
                    if (linkVO.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, linkVO.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    if (linkVO.getIcone().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, linkVO.getIcone().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setString(5, linkVO.getEscopo());
                    sqlInserir.setString(6, linkVO.getLink());
                    sqlInserir.setString(7, linkVO.getSituacao().toString());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        linkVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            linkVO.setNovoObj(true);
            throw e;
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final LinkVO linkVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            Link.alterar(getIdEntidade(), true, usuarioLogado);
            if (linkVO.getIcone().getPastaBaseArquivoEnum() != null) {
                if (linkVO.getIcone().getCodigo() == 0) {
                    getFacadeFactory().getArquivoFacade().incluir(linkVO.getIcone(), false, usuarioLogado, configuracaoGeralSistema);
                } else {
                    getFacadeFactory().getArquivoFacade().alterar(linkVO.getIcone(), false, usuarioLogado, configuracaoGeralSistema);
                }
            }
            final String sql = "UPDATE Link set dataInicio=?, dataFim=?, unidadeEnsino=?, icone=?, escopo=?, link=?, situacao=? "
                    + " WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(linkVO.getDataInicio()));
                    if (linkVO.getDataFim() != null) {
                        sqlAlterar.setDate(2, Uteis.getDataJDBC(linkVO.getDataFim()));
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }

                    if (linkVO.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, linkVO.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    if (linkVO.getIcone().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, linkVO.getIcone().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setString(5, linkVO.getEscopo());
                    sqlAlterar.setString(6, linkVO.getLink());
                    sqlAlterar.setString(7, linkVO.getSituacao().toString());
                    sqlAlterar.setInt(8, linkVO.getCodigo());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    public void validarDados(LinkVO linkVO) throws ConsistirException {
        if (linkVO.getLink() == null || linkVO.getLink().equals("")) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_Link_link"));
        }
        if (linkVO.getLink().contains("http")) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_Link_linkHttp"));
        }
//        if (linkVO.getUnidadeEnsino() == null && linkVO.getUnidadeEnsino().getCodigo().intValue() != 0) {
//            throw new ConsistirException(UteisJSF.internacionalizar("msg_Link_unidadeEnsino"));
//        }
        if (linkVO.getEscopo().equals("")) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_Link_escopo"));
        }
        if (linkVO.getDataInicio() == null) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_Link_dataInicio"));
        }
    }

    public List<LinkVO> consultar(Date dataInicio, Date dataFim, String link, String escopo, StatusAtivoInativoEnum situacao, Integer unidadeEnsino, boolean validarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
        consultar(getIdEntidade(), validarAcesso, usuario);
        StringBuilder sb = new StringBuilder(getSqlSelectConsulta());
        sb.append(" WHERE 1=1 ");
        if (dataInicio != null && dataFim != null) {
            sb.append(" and (link.dataInicio) >= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
            sb.append(" and ((link.dataFim) <= '").append(Uteis.getDataJDBC(dataFim)).append("' or datafim is null)");
        } else {
            if (dataInicio != null) {
                sb.append(" and (link.dataInicio) <= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
            } else if (dataFim != null) {
                sb.append(" and ((link.dataFim) <= '").append(Uteis.getDataJDBC(dataFim)).append("' or datafim is null)");
            }
        }
        if (link != null && !link.trim().isEmpty()) {
            sb.append(" and upper(sem_acentos(link)) ilike(sem_acentos('%").append(link).append("%')) ");
        }
        if (situacao != null) {
            sb.append(" and Link.situacao = '").append(situacao.toString()).append("' ");
        }
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sb.append(" and (Link.unidadeEnsino = ").append(unidadeEnsino).append(" or link.unidadeEnsino is null) ");
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), conf);
    }

    public List<LinkVO> consultarLinkApresentarVisao(String escopo, StatusAtivoInativoEnum situacao, Integer unidadeEnsino, ConfiguracaoGeralSistemaVO conf) throws Exception {
        StringBuilder sb = new StringBuilder(getSqlSelectConsulta());
        sb.append(" WHERE link.situacao = '").append(situacao.toString()).append("' ");
        sb.append(" and (link.dataInicio) <= current_date ");
        sb.append(" and (link.dataFim >= current_date or link.datafim is null)");
        sb.append(" and (link.escopo = 'AM' or link.escopo = '").append(escopo).append("') ");
        if (!escopo.equals("PA")) {
            sb.append(" and (link.unidadeEnsino = ").append(unidadeEnsino).append(" or link.unidadeensino is null) ");
        } 
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), conf);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAtivacao(LinkVO linkVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        linkVO.setSituacao(StatusAtivoInativoEnum.ATIVO);
        alterar(linkVO, usuarioLogado, configuracaoGeralSistema);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarInativacao(LinkVO linkVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        linkVO.setSituacao(StatusAtivoInativoEnum.INATIVO);
        alterar(linkVO, usuarioLogado, configuracaoGeralSistema);
    }

    private List<LinkVO> montarDadosConsulta(SqlRowSet rs, ConfiguracaoGeralSistemaVO conf) throws Exception {
        List<LinkVO> linkVOs = new ArrayList<LinkVO>(0);
        while (rs.next()) {
            linkVOs.add(montarDados(rs, conf));
        }
        return linkVOs;
    }

    private LinkVO montarDados(SqlRowSet rs, ConfiguracaoGeralSistemaVO conf) throws Exception {
        LinkVO linkVO = new LinkVO();
        linkVO.setCodigo(rs.getInt("codigo"));
        linkVO.setLink(rs.getString("link"));
        linkVO.setDataInicio(rs.getDate("dataInicio"));
        linkVO.setDataFim(rs.getDate("dataFim"));
        linkVO.setSituacao(StatusAtivoInativoEnum.valueOf(rs.getString("situacao")));
        linkVO.setEscopo(rs.getString("escopo"));
        linkVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
        linkVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
        linkVO.getIcone().setNome(rs.getString("icone.nome"));
        linkVO.getIcone().setCodigo(rs.getInt("icone.codigo"));
        linkVO.getIcone().setPastaBaseArquivo(rs.getString("icone.pastaBaseArquivo"));
        linkVO.setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(linkVO.getIcone(), PastaBaseArquivoEnum.IMAGEM.getValue(), conf, getCaminhoPastaWeb(), "tranparente.png", false));

        linkVO.setNovoObj(false);
        return linkVO;
    }

    private String getSqlSelectConsulta() {
        StringBuilder sb = new StringBuilder("");
        sb.append(" SELECT Link.*, unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
        sb.append(" icone.codigo as \"icone.codigo\", icone.pastaBaseArquivo as \"icone.pastaBaseArquivo\", icone.nome as \"icone.nome\" ");
        sb.append(" from Link ");
        sb.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = Link.unidadeEnsino  ");
        sb.append(" left join arquivo icone on icone.codigo = Link.icone ");
        return sb.toString();
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PaizVO</code> através de sua chave primária.
     * 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public LinkVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = getSqlSelectConsulta() + " WHERE Link.codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados(Link).");
        }
        return (montarDados(tabelaResultado, conf));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Link.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com
     * objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Link.idEntidade = idEntidade;
    }
}
