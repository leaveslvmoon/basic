#sql("getContentList")
select
ct.*,cct.`name`,(select realName from sys_administrator where id = ct.addUserId) realName
from
content ct LEFT JOIN contentcategories cct ON cct.id = ct.categoriesId
where 1=1
and categoriesId>2
#if(map.name!=null && map.name!="")
  and title like '%#(map.title)%'
#end
#if(map.CategoriesID!=null && map.CategoriesID!="")
  and categoriesId =  #(map.CategoriesID)
#end
order by #if(map.sort!=null&&map.sort!="") #(map.sort) #(map.order),#end id desc
#end


