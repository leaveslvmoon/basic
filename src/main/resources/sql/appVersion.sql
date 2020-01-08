#sql("getAppVersionContentList")
SELECT
*,concat(majorNo,'.',minorNo,'.',revisionNo) no
from sys_appversion
#end