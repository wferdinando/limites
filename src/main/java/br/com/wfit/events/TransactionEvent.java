package br.com.wfit.events;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import br.com.wfit.business.LimiteDiarioBusiness;
import br.com.wfit.model.Transaction;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransactionEvent{

    @Inject
    LimiteDiarioBusiness limiteDiarioBusiness;

    
    @Incoming("transacao")
    public void processarTransacao(final Transaction transaction){
        Log.info(transaction);
        limiteDiarioBusiness.limiteDiario(transaction);
    }
}
