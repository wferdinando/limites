package br.com.wfit.repository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.wfit.entity.Limite;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class LimiteRepository {

    @ConfigProperty(name = "limite.valorTotal")
    BigDecimal valorTotal;

    public Limite inserirLimiteDiario(final Long agencia, Long conta) {

        var limiteDiario = new Limite();
        limiteDiario.setAgencia(agencia);
        limiteDiario.setConta(conta);
        limiteDiario.setValor(valorTotal);
        limiteDiario.setData(LocalDate.now());

        Panache.withTransaction(limiteDiario::persist)
                .replaceWith(limiteDiario)
                .ifNoItem()
                .after(Duration.ofSeconds(5l))
                .fail().onFailure().transform(IllegalArgumentException::new);

        return limiteDiario;
    }

    public Uni<Limite> buscarLimiteDiario(final Long codigoAgencia, final Long codigoConta) {

        return buscarLimitePorData(codigoAgencia, codigoConta, LocalDate.now());

    }

    @WithSession
    public Uni<Limite> buscarLimitePorData(final Long codigoAgencia, final Long codigoConta, final LocalDate data) {
        var params = new HashMap<String, Object>();
        params.put("agencia", codigoAgencia);
        params.put("conta", codigoConta);
        params.put("data", data);

        try {
            return Limite.find("agencia= :agencia and conta= :conta and data= :data", params).firstResult();
        } catch (NoResultException noResultException) {
            return Uni.createFrom().item(inserirLimiteDiario(codigoAgencia, codigoConta));
        }

    }

}
