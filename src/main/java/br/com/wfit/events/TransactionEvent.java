package br.com.wfit.events;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import br.com.wfit.model.Transaction;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransactionEvent{

    @Incoming("transacao")
    public void processarTransacao(final Transaction transaction){
        Log.info(transaction);
    }
}
