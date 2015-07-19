delete from ponto where id in (select max(p.id) as id from ponto p
group by p.funcionario_id, p.apontamento_id
having count(1) > 1
order by p.apontamento_id)

delete from apontamento where id in (select max(a.id) as id from Apontamento a
group by a.data, a.obra_id
HAVING count(1) > 1 and a.obra_id is not null)