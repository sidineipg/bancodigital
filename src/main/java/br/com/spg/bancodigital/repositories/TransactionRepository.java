package br.com.spg.bancodigital.repositories;

import br.com.spg.bancodigital.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
