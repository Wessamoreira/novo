package negocio.facade.jdbc.sentry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.postgresql.util.PSQLException;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Repository;

import io.sentry.Sentry;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.sentry.ServicoIntegracaoSentryInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ServicoIntegracaoSentry implements ServicoIntegracaoSentryInterfaceFacade {

	private Boolean isSentryException(Throwable exception) {
		if (exception == null){ return false;}
		Class<?>[] classException = {
				StringIndexOutOfBoundsException.class,
				BadSqlGrammarException.class,
				ClassCastException.class,
				FileNotFoundException.class,
				NullPointerException.class,
				NumberFormatException.class,
				PSQLException.class,
				ParseException.class,
				SQLException.class,
				ArithmeticException.class,
				DataIntegrityViolationException.class,
				ArrayIndexOutOfBoundsException.class,
				IllegalArgumentException.class,
				IllegalStateException.class,
				ConcurrentModificationException.class,
				IOException.class
		};
		// Quais exception deve ignorar
		switch (exception.getClass().getSimpleName()) {
			case "ClientAbortException": return false;
			case "DuplicateKeyException": return false;
			case "FileNotFoundException": return false;
		}
		for (Class<?> clazz : classException) {
			if (clazz.isInstance(exception)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void registrarExceptionSentry(Throwable e, List<String> listaInformacaoAdicional, Boolean enviarSentry, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) {
		try {
			if (!(enviarSentry && Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO) && configuracaoGeralSistemaVO.getHabilitarMonitoramentoSentry() && Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getTokenSentry()) && !Uteis.isVersaoDev())) {
				return;
			}
			if (isSentryException(e)) {
				if (!Sentry.isEnabled()) {
					Sentry.init(options -> {
						options.setDsn(configuracaoGeralSistemaVO.getTokenSentry());
						options.setTracesSampleRate(1.0);
						options.setEnableUncaughtExceptionHandler(true);
						options.setDebug(Uteis.isVersaoDev() ? true : false);
					});
				}
				if (Sentry.isEnabled()) {
					if (Uteis.isAtributoPreenchido(usuarioVO)) {
						Sentry.setExtra("codigoUsuario", usuarioVO.getCodigo().toString());
					}
					if (Uteis.isAtributoPreenchido(listaInformacaoAdicional)) {
						for (int i = 0; i < listaInformacaoAdicional.size(); i++) {
							Sentry.setExtra("informacaoAdicional" + i, listaInformacaoAdicional.get(i));
						}
					}
					Sentry.captureException(e);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}