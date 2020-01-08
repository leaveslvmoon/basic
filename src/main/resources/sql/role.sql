#sql("getRoleList")
select r.*, s.name as sname from sys_role r left join sys_menu s on r.menuId = s.id
where FIND_IN_SET(r.id,getRoleChildLst(#(map.id)))
and r.name  like '%#(map.name)%'
#if(map.status!=null&&map.status!="")
and r.status = #(map.status)
#end
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
r.id desc
#end