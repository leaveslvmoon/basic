#sql("getMenuList")
  select mu.*,CONCAT('fa ',mu.icon) iconCls, (SELECT GROUP_CONCAT('fa ',sb.icon,'#',sb.name) buttonName from sys_menubutton smb LEFT JOIN sys_button sb on smb.buttonId = sb.id
   where smb.menuId = mu.id ) operation from sys_menu mu where mu.name like '%#(map.name)%'
  #if(map.status!=null&&map.status!="")
    and status = #(map.status)
  #end
  order by
  #if(map.sort==null)
    sort
  #end
  #if(map.sort!=null)
    #(map.sort)
    #(map.order),
  #end

#end