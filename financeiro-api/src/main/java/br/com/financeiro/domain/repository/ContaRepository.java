package br.com.financeiro.domain.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.financeiro.domain.model.Conta;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>, ContaRepositoryQueries {	
	
	 @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.dataPagamento BETWEEN :periodoInicial AND :periodoFinal")
	 BigDecimal findTotalValorPagoByPeriodo(LocalDate periodoInicial, LocalDate periodoFinal);

}
