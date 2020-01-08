#sql("getPostFront")
select p.*,b.name from bbs_post p
left join bbs_block b on p.blockId=b.id
where p.status=1 and p.blockId=#(map.blockId)
  #if(map.isTop!=null&&map.isTop!="")
  and isTop=#(map.isTop)
  #end
order by p.id desc
#end

#sql("getTopPostFront")
select p.*,b.name from bbs_post p
left join bbs_block b on p.blockId=b.id
where p.status=1 and p.isTop=1
#end

#sql("getNewPostFront")
select p.*,b.name,u.userName,u.img from bbs_post p
left join bbs_block b on p.blockId=b.id
left join bbs_user u on p.createrId=u.id
where p.status=1
order by id desc limit 10
#end
