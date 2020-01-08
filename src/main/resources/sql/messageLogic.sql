#sql("getMessageLogicList")
select
m.mobile,sml.*, sm.title,sm.content,sm.sendTime,sm.messageType,sm.receiveType
FROM
sys_messagelogic sml
LEFT JOIN member m on sml.receiveId=m.id
LEFT JOIN sys_message sm ON sm.id = sml.messageId
where 1=1
and mobile like '%#(map.mobile)%'
and content like '%#(map.content)%'
order by
#if(map.sort!=null)
#(map.sort) #(map.order),
#end
sml.id desc
#end