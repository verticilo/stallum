select a.data, a.id, o.nome, c.nome, f.nome from Ponto p
inner join Funcionario f on f.id = p.funcionario_id
inner join Apontamento a on a.id = p.apontamento_id
left outer join CentroCusto c on c.id = a.centroCusto_id
left outer join Obra o on o.id = a.obra_id
where a.data >= to_date('20 Aug 2014', 'dd Mon yyyy')
order by a.data, a.id, f.nome

select a.data, a.id, o.nome, c.nome from Apontamento a
left outer join CentroCusto c on c.id = a.centroCusto_id
left outer join Obra o on o.id = a.obra_id
where a.data >= to_date('20 Aug 2014', 'dd Mon yyyy')
order by a.data, a.id

# REMOVE APONTAMENTOS SEM PONTO
select a.data, a.id, o.nome, c.nome,a.feriado from Apontamento a
left outer join CentroCusto c on c.id = a.centroCusto_id
left outer join Obra o on o.id = a.obra_id
where not exists (select 1 from Ponto pt where pt.apontamento_id = a.id)
  and a.feriado is not true
order by a.data, a.id

delete from Apontamento a
where not exists (select 1 from Ponto pt where pt.apontamento_id = a.id)
  and a.feriado is not true

#-----
delete from ponto where id in (select max(p.id) as id from ponto p
group by p.funcionario_id, p.apontamento_id
HAVING count(1) > 1
ORDER BY P.APONTAMENTO_ID)

delete from Ponto where id = 38698