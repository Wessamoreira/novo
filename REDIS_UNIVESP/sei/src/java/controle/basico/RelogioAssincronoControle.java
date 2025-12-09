package controle.basico;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.Uteis;

@Controller("RelogioAssincronoControle")
@Scope("session")
@Lazy
public class RelogioAssincronoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getReadyScrpitRelogio() {
		return "$().ready(function() {funcaoValidaSessao();});";
	}

	public String getTempoSessao() {
		HttpSession sessao = (HttpSession) context().getCurrentInstance().getExternalContext().getSession(false);
		return "" + sessao.getLastAccessedTime();
	}

	public String finalizarSessao() {
		HttpSession sessao = (HttpSession) context().getCurrentInstance().getExternalContext().getSession(false);
		sessao.invalidate();
		return Uteis.getCaminhoRedirecionamentoNavegacao("index.xhtml");
	}

}
