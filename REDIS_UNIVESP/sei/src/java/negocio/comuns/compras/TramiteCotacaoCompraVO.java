package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import com.google.common.base.Preconditions;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class TramiteCotacaoCompraVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;

	private String nome = "";

	private SituacaoTramiteEnum situacaoTramite = SituacaoTramiteEnum.EM_CONSTRUCAO;

	private boolean tramitePadrao = false;

	private List<DepartamentoTramiteCotacaoCompraVO> listaDepartamentoTramite;

	private UsuarioVO responsavel;
	
	private UnidadeEnsinoVO unidadeEnsinoPadrao;

	// transiente
	private boolean tramiteUsado;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public SituacaoTramiteEnum getSituacaoTramite() {
		return situacaoTramite;
	}

	public void setSituacaoTramite(SituacaoTramiteEnum situacaoTramite) {
		this.situacaoTramite = situacaoTramite;
	}

	public boolean isTramitePadrao() {
		return tramitePadrao;
	}

	public void setTramitePadrao(boolean tramitePadrao) {
		this.tramitePadrao = tramitePadrao;
	}

	public boolean adicionarDepartamentoTramite(DepartamentoTramiteCotacaoCompraVO departamentoTramite) {
		int index=0;
		for (DepartamentoTramiteCotacaoCompraVO objExistente : getListaDepartamentoTramite()) {
			if(objExistente.getOrdem().equals(departamentoTramite.getOrdem())){
				getListaDepartamentoTramite().set(index, departamentoTramite);
				return true;
			}
			index++;
		}
		departamentoTramite.setTramiteVO(this);
		if (Objects.isNull(departamentoTramite.getOrdem()) || departamentoTramite.getOrdem() == 0) {
			departamentoTramite.setOrdem(this.getListaDepartamentoTramite().size() + 1);
		}
		return this.getListaDepartamentoTramite().add(departamentoTramite);
	}

	public boolean isTramiteUsado() {
		return tramiteUsado;
	}

	public void setTramiteUsado(boolean tramiteUsado) {
		this.tramiteUsado = tramiteUsado;
	}

	public void aumentarPrioridade(DepartamentoTramiteCotacaoCompraVO departamentoTramiteCotacaoCompra) {
		this.mudarPrioridade(departamentoTramiteCotacaoCompra, p -> p.getOrdem() == departamentoTramiteCotacaoCompra.getOrdem() - 1);
	}

	public void diminuirPrioridade(DepartamentoTramiteCotacaoCompraVO departamentoTramiteCotacaoCompra) {
		this.mudarPrioridade(departamentoTramiteCotacaoCompra, p -> p.getOrdem() == departamentoTramiteCotacaoCompra.getOrdem() + 1);
	}

	private void mudarPrioridade(DepartamentoTramiteCotacaoCompraVO departamentoTramiteCotacaoCompra, Predicate<DepartamentoTramiteCotacaoCompraVO> predicate) {
		if (!this.getListaDepartamentoTramite().contains(departamentoTramiteCotacaoCompra)) {
			return;
		}

		this.getListaDepartamentoTramite().stream().filter(predicate).findFirst().ifPresent(p -> {
			this.getListaDepartamentoTramite().remove(p);
			this.getListaDepartamentoTramite().remove(departamentoTramiteCotacaoCompra);
			Integer ordem = departamentoTramiteCotacaoCompra.getOrdem();
			departamentoTramiteCotacaoCompra.setOrdem(p.getOrdem());
			p.setOrdem(ordem);

			this.getListaDepartamentoTramite().add(p);
			this.getListaDepartamentoTramite().add(departamentoTramiteCotacaoCompra);
		});
		this.listaDepartamentoTramite.forEach(System.out::println);
	}

	public boolean removerDepartamentoTramite(DepartamentoTramiteCotacaoCompraVO departamentoTramite) {
		boolean status = this.getListaDepartamentoTramite().remove(departamentoTramite);
		this.reordenarPrioridade(this.getListaDepartamentoTramite());
		return status;
	}

	private void reordenarPrioridade(List<DepartamentoTramiteCotacaoCompraVO> listaDepartamentoTramiteCotacaoCompra) {
		int ordem = 1;
		for (DepartamentoTramiteCotacaoCompraVO p : listaDepartamentoTramiteCotacaoCompra) {
			p.setOrdem(ordem++);
		}
	}

	public void validarDados() throws Exception {

		Preconditions.checkState(Objects.nonNull(this.getNome()) && !this.getNome().isEmpty(), "O campo NOME (Trâmite Cotação Compra) deve ser informado");
		Preconditions.checkState(Objects.nonNull(this.getListaDepartamentoTramite()) && !this.getListaDepartamentoTramite().isEmpty(), "Deve ser adicionado pelo menos, um item na aba departamentos! ");

		for (DepartamentoTramiteCotacaoCompraVO departamentoTramiteVO : this.getListaDepartamentoTramite()) {
			departamentoTramiteVO.validarDados();
		}

	}

	public List<DepartamentoTramiteCotacaoCompraVO> getListaDepartamentoTramite() {
		this.listaDepartamentoTramite = Optional.ofNullable(this.listaDepartamentoTramite).orElse(new ArrayList<DepartamentoTramiteCotacaoCompraVO>());
		Collections.sort(this.listaDepartamentoTramite);
		return listaDepartamentoTramite;
	}

	public void setResponsavel(UsuarioVO usuarioLogado) {
		this.responsavel = usuarioLogado;
	}

	public UsuarioVO getResponsavel() {
		this.responsavel = Optional.ofNullable(this.responsavel).orElse(new UsuarioVO());
		return responsavel;
	}
		

	public UnidadeEnsinoVO getUnidadeEnsinoPadrao() {
		this.unidadeEnsinoPadrao = Optional.ofNullable(this.unidadeEnsinoPadrao).orElse(new UnidadeEnsinoVO());
		return unidadeEnsinoPadrao;
	}

	public void setUnidadeEnsinoPadrao(UnidadeEnsinoVO unidadeEnsinoPadrao) {
		this.unidadeEnsinoPadrao = unidadeEnsinoPadrao;
	}

}
