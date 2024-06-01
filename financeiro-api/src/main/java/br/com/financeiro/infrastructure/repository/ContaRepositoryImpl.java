package br.com.financeiro.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import br.com.financeiro.domain.filter.ContaFilter;
import br.com.financeiro.domain.model.Conta;
import br.com.financeiro.domain.repository.ContaRepositoryQueries;
import br.com.financeiro.infrastructure.repository.paginacao.PaginacaoUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
@Repository
public class ContaRepositoryImpl implements ContaRepositoryQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Override
	public Page<Conta> filtrar(ContaFilter filter, Pageable pageable) {	
		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<Conta> criteria = builder.createQuery(Conta.class);
		Root<Conta> root = criteria.from(Conta.class);

		List<Predicate> predicates = adicionarFiltro(filter, builder, root);
		criteria.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Conta> query = this.manager.createQuery(criteria);
		this.paginacaoUtil.preparar(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(filter));
		

	}
	
	private List<Predicate> adicionarFiltro(ContaFilter filter, CriteriaBuilder builder, Root<Conta> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();		
		
		if(filter.getDataVencimento() != null) {
			predicates.add(builder.equal(root.get("dataVencimento"), filter.getDataVencimento()));
		}
		
		if(StringUtils.hasText(filter.getDescricao())) {
			predicates.add(builder.like(root.get("descricao"), "%" + filter.getDescricao() + "%"));
		}
		
		return predicates;
	}
	
	private Long total(ContaFilter filtro) {
		CriteriaBuilder criteriaBuilder = this.manager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<Conta> root = countQuery.from(Conta.class);

		List<Predicate> listaPredicates = adicionarFiltro(filtro, criteriaBuilder, root);
		countQuery.select(criteriaBuilder.count(root));
		countQuery.where(criteriaBuilder.and(listaPredicates.toArray(new Predicate[listaPredicates.size()])));
		TypedQuery<Long> typedQuery = this.manager.createQuery(countQuery);

		return typedQuery.getSingleResult();
	}

}
