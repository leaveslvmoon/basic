var XB = {
    //标签页面Form执行的保存方法
    pagesave: function (options) {
        var parameter = {
            "url": "../save",
            "form": "#FF",
            "datalist": "#DataList",
            "divid": "#D1",
            "fn": "",
            "isiframe": false,
            "isClose":true
        };
        if (options) {
            $.extend(parameter, options);
        }
        $.messager.progress();
        $(parameter.form).form('submit', {
            url: parameter.url,
            onSubmit: function () {
                var isValid = $(this).form('enableValidation').form('validate');
                if (!isValid) {
                    $.messager.progress('close');
                }
                return isValid;//返回false终止表单提交
            },
            success: function (data) {
                $.messager.progress('close');
                data = $.parseJSON(data);
                if (typeof (parameter.fn) == "string") {
                    data = JSON.parse(data);
                    if (data.result == 1) {
                        XB.success({
                            message: data.message, fn: function () {

                                XB.findiframe().XB.reload({"datagrid": parameter.datagrid});
                                top.$(parameter.divid).dialog('close');
                            }
                        });
                    }
                    else {
                        XB.warning({message: data.message});
                    }
                }
                else if (typeof (parameter.fn) == "function") {
                    parameter.fn(data);
                }
            }
        });
    },
    //Combobox显示面板前处理函数
    showpanel: function (options) {
        var settings = {
            "current": null,
            "rely": null,
            "message": "很抱歉，请先选择依赖项"
        };
        if (options) {
            $.extend(settings, options);
        }
        if ($(settings.rely).combobox('getValue').length == 0) {
            $(settings.current).combo('hidePanel'); XB.warning({ 'message': settings.message })
        }
    },

    //文件上传
    upload: function (options) {
        var parameter = {
            "id": "#uploader",
            "path": "pic",//保存路径是图片(pic)还是文件(file)
            "ext": "jpg,jpeg,png,gif,bmp,zip,rar,doc,xls,ppt,docx,xlsx,ppts,txt",//扩展名
            "ismulti": true,//是否支持多选
            "file": "pic",
            "maxsize": "10MB",
            "url": "/upload/doUpload"
        };
        if (options) {
            $.extend(parameter, options);
        }
        var uploader = $(parameter.id).pluploadQueue({
            runtimes: 'html5,flash,html4',
            url: parameter.url,
            chunk_size: '10MB',//分块上传，0不分块
            multipart_params: { "Path": parameter.path, "NameAdd": XB.FormatDate(new Date(), "ddhhmmssS_") },//附加参数
            multi_selection: parameter.ismulti,//是否支持多选
            flash_swf_url: '/JS/Plupload/Moxie.swf',
            filters: {
                mime_types: [
                    { title: "select files", extensions: parameter.ext },
                ],
                max_file_size: parameter.maxsize,
                prevent_duplicates: true //是否允许选取重复文件
            },
            init: {
                FilesAdded: function (uploader, files) {
                    if (!parameter.ismulti) {
                        uploader.start();
                    }
                },
                FileUploaded: function (uploader, queuefile, response) {//队列某一文件上传完成触发
                    if (parameter.ismulti) {
                        var $val = parent.$('#BactchPic');
                        if (!$val.hasClass('BactchPic')) {
                            $val.addClass('BactchPic');
                        }

                        $val.prepend('<div><i class="Bactchdel Icon211"></i><img src="/images/' + $.parseJSON(response.response).fileUrl[0] + '" class="Bactchimg" /><input name="PicUrls" value="' + $.parseJSON(response.response).fileUrl[0] + '" type="hidden" /><input name="minPicUrl" value="' + $.parseJSON(response.response).fileUrl[0] + '" type="hidden" /></div>');
                        parent.$("#D1").dialog('close');
                        parent.$("#BactchPic").show();

                    }
                    else {

                        if(top.$("#diframe")[0]){
                            top.$("#diframe")[0].contentWindow.$('#'+parameter.path).textbox('setValue', $.parseJSON(response.response).fileUrl);
                            console.log(top.$("#diframe")[0].contentWindow)
                        }else{
                            XB.findiframe().$('#' + parameter.path).textbox('setValue', $.parseJSON(response.response).fileUrl);
                            // top.$("#mainIframe")[index].contentWindow.$('#'+parameter.path).textbox('setValue', $.parseJSON(response.response).fileUrl);
                        }
                    }
                },
                UploadComplete: function (uploader, files) {//队列所有文件上传完成触发
                    parent.$('#W1').window('close');
                },
                Error: function (uploader, errObject) {
                    if (errObject.code == "-600") {
                        XB.error({ "message": "" + errObject.file.name + "文件超过系统限制的" + parameter.maxsize });
                    }
                    else {
                        XB.error({ "message": errObject.message });
                    }
                }
            }
        });
    },
    //图片预览
    pictips: function (options) {
        var settings = {
            "id": "#dd",
            "path": "#Pic"
        };
        if (options) {
            $.extend(settings, options);
        }
        $(settings.id).tooltip({
            hideEvent: 'none',
            content: '',
            onShow: function () {
                var t = $(this);
                var c = $(settings.path).textbox('getValue');
                if (c.length == 0) {
                    t.tooltip('update', '预览失败，请先上传图片。');
                }
                else {
                    t.tooltip('update', '<a href="'+'/images/'+c + '" target="_blank" title="点击查看原始图片"><img src="' +/images/+ c + '" alt="点击查看原始图片" width="100" height="100" /></a>');
                }
                t.tooltip('tip').focus().unbind().bind('blur', function () {
                    t.tooltip('hide');
                });
            }
        });
    },
    /*//图片预览
    pictips: function (options) {
        var settings = {
            "id": "#dd",
            "path": "#Pic"
        };
        if (options) {
            $.extend(settings, options);
        }
        $(settings.id).tooltip({
            hideEvent: 'none',
            content: '',
            onShow: function () {
                var t = $(this);
                var c = $(settings.path).textbox('getValue');
                if (c.length == 0) {
                    t.tooltip('update', '预览失败，请先上传图片。');
                }
                else {
                    t.tooltip('update', '<a href="' + c + '" target="_blank" title="点击查看原始图片"><img src="' + c + '" alt="点击查看原始图片" width="100" height="100" /></a>');
                }
                t.tooltip('tip').focus().unbind().bind('blur', function () {
                    t.tooltip('hide');
                });
            }
        });
    },*/
    //Int类型的排序默认值处理
    JSSortInt: function (value) {
        console.log(value);
        if (value != 99999) {
            return value;
        }
    },

    //打开对话窗口
    OPUrl: function OPUrl(options) {
        var settings = {
            "url": "",
            "title": "标题",
            "width": 514,
            "height": 210,
            "fn": "",
            "divid": "#D1"
        };
        if (options) {
            $.extend(settings, options);
        }

        XB.creatediv({ "id": settings.divid });
        $(settings.divid).dialog({
            title: settings.title,
            width: settings.width,
            height: settings.height,
            content: '<iframe id="iframe" name="iframe" src="' + settings.url + '" width="100%" height="100%" frameborder="0" scrolling="yes"></iframe>',
            cache: false,
            shadow: false,
            modal: true
        });
    },

    //对话窗口Form提交默认要执行的保存方法
    save: function (options) {
        var parameter = {
            "url": "save",
            "form": "#FF",
            "datagrid": "#datagrid",
            "fn": ""
        };
        if (options) {
            $.extend(parameter, options);
        }
        parent.$.messager.progress();//提交前显示进度条
        if (parameter.url.toLowerCase() == 'save') {
            parameter.url = "../" + parameter.url;
        }
        $(parameter.form).form('submit', {
            url: parameter.url,
            onSubmit: function () {
                var isValid = $(this).form('enableValidation').form('validate');
                if (!isValid) {
                    parent.$.messager.progress('close');//如果表单验证无效的则隐藏进度条
                }
                return isValid;//返回false终止表单提交
            },
            success: function (data) {
                parent.$.messager.progress('close');
                if (typeof (parameter.fn) == "string") {
                    data = JSON.parse(data);
                    if (data.result == 1) {
                        XB.success({
                            message: data.message, fn: function () {

                                XB.findiframe().XB.reload({"datagrid": parameter.datagrid});
                                top.$(parameter.divid).dialog('close');
                            }
                        });
                    }
                    else {
                        XB.warning({message: data.message});
                    }
                }
                else if (typeof (parameter.fn) == "function") {
                    parameter.fn(data);
                }
            }
        });
    },
    //打开窗口
    window: function (options) {
        var parameter = {
            "divid": "#W1",
            "url": "",
            "form": "#FF",
            "datagrid": "#datagrid",
            "title": "标题",
            "width": 750,
            "height": 450,
            "fn": function () {
            }
        };
        if (options) {
            $.extend(parameter, options);
        }
        top.XB.creatediv({"id": parameter.divid});
        top.$(parameter.divid).window({
            title: parameter.title,
            minimizable: false,
            maximizable: false,
            collapsible: false,
            resizable: false,
            width: parameter.width,
            height: parameter.height,
            modal: true,
            content: '<iframe id="wiframe" name="wiframe" src="' + parameter.url + '" width="100%" height="100%" frameborder="0" scrolling="yes"></iframe>',
            onClose: parameter.fn
        });
    },
    //打开对话窗口
    dialog: function (options) {
        var parameter = {
            "url": "",
            "form": "#FF",
            "datagrid": "#datagrid",
            "title": "标题",
            "width": 750,
            "height": 450,
            "oktext": " 保 存 ",
            "canceltext": " 取 消 ",
            "resizable":true,
            "save": {},
            "fn": "",
            "divid": "#D1",

        };
        if (options) {
            $.extend(parameter, options);
        }
        top.XB.creatediv({"id": parameter.divid});
        top.$(parameter.divid).dialog({
            title: parameter.title,
            width: parameter.width,
            height: parameter.height,
            content: '<iframe id="diframe" name="diframe" src="' + parameter.url + '" width="100%" height="100%" frameborder="0" scrolling="yes"></iframe>',
            cache: false,
            shadow: false,
            modal: true,
            "resizable":parameter.resizable,
            buttons: [{
                text: parameter.oktext,
                iconCls: "icon-back",
                handler: function () {
                    if (typeof (parameter.fn) == "string") {
                        parameter.save.datagrid = parameter.datagrid;
                        parameter.save.form = parameter.form;
                        parameter.save.divid = parameter.divid;

                        top.$("#" + $(top.$(parameter.divid).dialog("options").content).attr("id"))[0].contentWindow.XB.save(parameter.save);
                    }
                    else if (typeof (parameter.fn) == "function") {
                        parameter.fn();
                    }
                }
            },
                {
                    text: parameter.canceltext,
                    iconCls: "icon-cancel",
                    handler: function () {
                        top.$(parameter.divid).dialog('close');
                    }
                }]
        });
    },
    //打开窗口引导方法
    open: function (options) {
        var parameter = {
            "datagrid": "#datagrid",
            "type": "add",
            "openmode": '0',
            "form": '#FF',
            "dialog": {},
            "rowid": null,
            "rowindex": null,
            "token": null
        };
        if (options) {
            $.extend(parameter, options);
        }
        parameter.dialog.datagrid = parameter.datagrid;
        parameter.dialog.form = parameter.form;
        switch (parameter.type) {
            case 'add':
                XB.dialog(parameter.dialog);
                break;
            default:
                switch (parameter.openmode) {
                    case '0'://无窗口打开
                        $.ajax({
                            url: parameter.dialog.url,
                            type: "post",
                            cache: false,
                            async: false,
                            data: {},
                            dataType: 'json',
                            success: function (json) {
                                if (json.result == 1) {
                                    XB.success({
                                        message: json.message, fn: function () {
                                            XB.reload({"id": parameter.datalist});
                                        }
                                    });
                                }
                                else {
                                    XB.warning({message: json.message});
                                }
                            },
                            error: function () {
                                XB.error({"message": "操作失败，请刷新后重试"});
                            }
                        });
                        break;
                    case '1'://有交互窗口
                        var ID = parameter.rowid || XB.getrowid({"datagrid": parameter.datagrid});
                        if (ID == 0) {
                            XB.warning({"message": "很抱歉，请选择需要操作的行！"});
                            return false;
                        }
                        parameter.dialog.url = (parameter.dialog.url.indexOf('?') > -1) ? parameter.dialog.url + "&ID=" + ID : parameter.dialog.url + "?ID=" + ID;
                        XB.dialog(parameter.dialog);
                        break;
                    case '2'://无交互的窗口
                        var ID = parameter.rowid || XB.getrowid({"datagrid": parameter.datagrid});
                        if (ID == 0) {
                            XB.warning({"message": "很抱歉，请选择需要操作的行！"});
                            return false;
                        }
                        parameter.dialog.url = (parameter.dialog.url.indexOf('?') > -1) ? parameter.dialog.url + "&ID=" + ID : parameter.dialog.url + "?ID=" + ID;
                        XB.window(parameter.dialog);
                        break;
                    case '3'://带确认的无窗口
                        var ID = parameter.rowid || XB.getallrowid({"datagrid": parameter.datagrid});
                        if (ID == 0) {
                            XB.warning({"message": "很抱歉，请选择需要操作的行！"});
                            return false;
                        }
                        XB.confirm({
                            "message": "你确定操作选中的数据吗？点击确定为继续执行操作，点击取消则终止操作。", "fn": function () {
                                $.ajax({
                                    url: parameter.dialog.url,
                                    type: "post",
                                    cache: false,
                                    async: false,
                                    data: {"ID": ID},
                                    success: function (data) {
                                        if (parameter.dialog.save != null && typeof (parameter.dialog.save.fn) == "function") {
                                            parameter.dialog.save.fn(data);
                                        }
                                        else {
                                            if (data.result) {
                                                XB.success({
                                                    message: data.message, fn: function () {
                                                        XB.reload({"datagrid": parameter.datagrid});
                                                    }
                                                });
                                            }
                                            else {
                                                XB.warning({message: data.message});
                                            }
                                        }
                                    },
                                    error: function () {
                                        XB.error({"message": "操作失败，请刷新后重试"});
                                    }
                                });
                            }
                        });
                        break;
                    case '4'://新标签打开
                        var ID = parameter.rowid || XB.getallrowid({"datagrid": parameter.datagrid});
                        if (ID == 0) {
                            XB.warning({"message": "很抱歉，请选择需要操作的数据行！"});
                            return false;
                        }
                        parameter.dialog.url = (parameter.dialog.url.indexOf('?') > -1) ? parameter.dialog.url + "&ID=" + ID : parameter.dialog.url + "?ID=" + ID;

                        //var tab = top.$('#MTabs').tabs('getSelected');
                        var tab = top.$('.easyui-tabs1').tabs('getSelected');
                        var title = tab.panel('options').title;

                        top.AddTag(title + ':' + parameter.dialog.title, parameter.dialog.url);
                        break;
                    case '5'://表单提交打开
                        $("#FF").attr("method", "post").attr("action", parameter.dialog.url).submit();
                        break;
                    case '6'://地址栏打开
                        console.log('ces');
                        var ID = parameter.rowid || XB.getallrowid({"datagrid": parameter.datagrid});
                        if (ID == 0 && parameter.type) {
                            XB.warning({"message": "很抱歉，请选择要操作的数据行！"});
                            return false;
                        }
                        top.location.href = (parameter.dialog.url.indexOf('?') > -1) ? parameter.dialog.url + "&ID=" + ID : parameter.dialog.url + "?ID=" + ID;
                        break;
                    case 'all':
                        $(parameter.datagrid).datagrid('checkAll');
                        break;
                    case 'clearall':
                        $(parameter.datagrid).datagrid('uncheckAll');
                        break;
                    case 'anti':
                        var datagrid = $(parameter.datagrid);
                        var $selectrows = datagrid.datagrid('getSelections');
                        datagrid.datagrid('checkAll');
                        for (var i = 0; i < $selectrows.length; i++) {
                            datagrid.datagrid('uncheckRow', datagrid.datagrid('getRowIndex', $selectrows[i]));
                        }
                        break;
                }
        }
    },
    //datagrid封装处理函数
    datagrid: function (options) {
        var parameter = {
            "datagrid": "#datagrid",
            "url": "DataList",
            "frozenColumns": [],//列将会被冻结在左侧
            "columns": [],//列字段
            "fitColumns": true,//true 自适应列宽度
            "autoRowHeight": false,//根据行的内容自动设置行的高度，false可以提高负载性能
            "striped": true,//true 显示斑马线效果
            "pagination": true,//true 显示分页
            "rownumbers": false,//true 显示行号
            "pagesize": 20,//每页尺码
            "queryParams": {},//请求数据时附带额外的参数
            "showfooter": false,//true 显示页脚
            "border": true,//true 显示边框
            "singleSelect": false,//true 只能选择一行
            "RowContextMenuID": "#datagridMenu",//右键菜单ID
            "toolID": "#toolsearch",//所在标签工具栏ID
            "onDblClickRow": function (index, row) {//双击数据行执行
                $(this).datagrid('uncheckAll').datagrid('selectRow', index);
                openURL('edit');
            },
            "onRowContextMenu": function (e, index, row) {//右键菜单执行
                e.preventDefault();
                $(this).datagrid('uncheckAll').datagrid('selectRow', index);
                $(parameter.RowContextMenuID).menu({
                    onClick: function (item) {
                        openURL(item.name);
                    }
                }).menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });
            },
            "onLoadSuccess":function(data){//合并相同行
                var mark=1;                                                 //这里涉及到简单的运算，mark是计算每次需要合并的格子数
                for (var i=1; i <data.rows.length; i++) {     //这里循环表格当前的数据
                    if (data.rows[i]['name'] == data.rows[i-1]['name']) {   //后一行的值与前一行的值做比较，相同就需要合并
                        mark += 1;
                        $(this).datagrid('mergeCells',{
                            index: i+1-mark,                 //datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
                            field: 'name',                 //合并单元格的区域，就是clomun中的filed对应的列
                            rowspan:mark                   //纵向合并的格数，如果想要横向合并，就使用colspan：mark
                        });
                    }else{
                        mark=1;                                         //一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
                    }
                }
            }
        };
        if (options) {
            $.extend(parameter, options);
        }
        $(parameter.datagrid).datagrid({
            "url": parameter.url,
            "frozenColumns": [parameter.frozenColumns],
            "columns": [parameter.columns],
            "fitColumns": parameter.fitColumns,
            "autoRowHeight": parameter.autoRowHeight,
            "striped": parameter.striped,
            "pagination": parameter.pagination,
            "rownumbers": parameter.rownumbers,
            "pageSize": parameter.pagesize,
            "queryParams": parameter.queryParams,
            "showFooter": parameter.showfooter,
            "border": parameter.border,
            "singleSelect": parameter.singleSelect,
            "onClickRow": parameter.onClickRow,//点击一行的时候触发
            "onLoadSuccess": parameter.onLoadSuccess,//数据列表加载成功执行
            "onClickCell": parameter.onClickCell,//单击单元格执行
            "onDblClickRow": parameter.onDblClickRow,//双击数据行执行
            "onRowContextMenu": parameter.onRowContextMenu//右键菜单执行
        });
    },
    //顶部搜索处理成Json数据重新加载列表
    search: function (options) {
        var parameter = {
            "datagrid": "#datagrid",
            "search": "#search"
        };
        if (options) {
            $.extend(parameter, options);
        }
        //$(parameter.datagrid).datagrid('load', $(parameter.search).FromDataSearch());
        if ($(parameter.datagrid).datagrid('options')["idField"] == null) {
            $(parameter.datagrid).datagrid('load', $(parameter.search).FromDataSearch());
        }
        else {
            $(parameter.datagrid).treegrid('load', $(parameter.search).FromDataSearch());
        }
    },
    //回车触发搜索条件提交
    enter: function () {
        $(document).keydown(function (e) {
            var e = e || event,
                keycode = e.which || e.keyCode;
            if (keycode == 13) {
                XB.search();
            }
        });
    },
    //获取数据列表中单条记录指定的字段值
    getrowid: function (options) {
        var parameter = {
            "id": "id",
            "datagrid": "#datagrid"
        };
        if (options) {
            $.extend(parameter, options);
        }
        var checkedItems = $(parameter.datagrid).datagrid('getSelected');
        if (checkedItems) {
            return checkedItems[parameter.id];
        }
        else {
            return 0;
        }
    },
    //获取数据列表中多条记录指定的字段值
    getallrowid: function (options) {
        var parameter = {
            "id": "id",
            "datagrid": "#datagrid"
        };
        if (options) {
            $.extend(parameter, options);
        }
        var checkedItems = $(parameter.datagrid).datagrid('getChecked');
        var rows = [];
        $.each(checkedItems, function (index, item) {
            rows.push(item[parameter.id]);
        });
        if (rows.length == 0) {
            return 0;
        }
        else {
            return rows.join(",");
        }
    },
    //创建DIV
    creatediv: function (options) {
        var parameter = {
            "id": "#D1"
        };
        if (options) {
            $.extend(parameter, options);
        }
        var window = $(".window");
        var len = window.length;
        if (len > 0) {
            for (var index = 0; index < len; index++) {
                if ($(window[index]).is(":hidden")) {
                    $(window[index]).remove();
                }
            }
        }
        var windowshadow = $(".window-shadow");
        var len = windowshadow.length;
        if (len > 0) {
            for (var index = 0; index < len; index++) {
                if ($(windowshadow[index]).is(":hidden")) {
                    $(windowshadow[index]).remove();
                }
            }
        }
        var windowmask = $(".window-mask");
        var len = windowmask.length;
        if (len > 0) {
            for (var index = 0; index < len; index++) {
                if ($(windowmask[index]).is(":hidden")) {
                    $(windowmask[index]).remove();
                }
            }
        }

        var div = document.createElement("div");
        div.id = parameter.id.replace("#", "");
        document.body.appendChild(div);
        $(parameter.id).addClass("NoScroll");
    },
    //顶部对话框获得当前选中标签内置元素集
    findiframe: function () {
        //var $tt = top.$('#MTabs');
        var $tt = top.$('.easyui-tabs1');
        var tab = $tt.tabs('getSelected');
        var title = tab.panel('options').title;
        var index = $tt.tabs('getTabIndex', tab);
        if ($tt.tabs('exists', '' + title + '')) {
            $tt.tabs('select', '' + title + '');
            var ParentTab = $tt.tabs('getSelected');
            var ParentIndex = $tt.tabs('getTabIndex', ParentTab);
            ParentTab = top.$(".tabs-panels>div").get(ParentIndex);
            return $(ParentTab).find('iframe')[0].contentWindow;
        }
    },
    //刷新数据列表，保留查询条件
    reload: function (options) {
        var parameter = {
            "datagrid": "#datagrid"
        };
        if (options) {
            $.extend(parameter, options);
        }
        //$(parameter.datagrid).datagrid('reload');
        if ($(parameter.datagrid).length>0) {
            if ($(parameter.datagrid).datagrid('options')["idField"] == null) {
                $(parameter.datagrid).datagrid('reload');
            }
            else {
                XB.reloadtreegrid(parameter);
            }
        }
    },
    //刷新树形数据列表，保留查询条件
    reloadtreegrid: function (options) {
        var parameter = {
            "datagrid": "#DataList"
        };
        if (options) {
            $.extend(parameter, options);
        }
        if ($(parameter.datagrid).length>0) {
            $(parameter.datagrid).treegrid('reload');
        }
    },
    //成功对话框
    success: function (options) {
        var parameter = {
            "title": "成功",
            "message": "恭喜您，操作成功！",
            "fn": function () {
            }
        };
        if (options) {
            $.extend(parameter, options);
        }
        top.$.messager.alert(parameter.title, parameter.message, 'info', parameter.fn);
    },
    //错误对话框
    error: function (options) {
        var parameter = {
            "title": "错误",
            "message": "很抱歉，操作错误，请刷新或稍后重试！",
            "fn": function () {
            }
        };
        if (options) {
            $.extend(parameter, options);
        }
        top.$.messager.alert(parameter.title, parameter.message, 'error', parameter.fn);
    },
    //警示对话框
    warning: function (options) {
        var parameter = {
            "title": "警示",
            "message": "温馨提示，请您谨慎操作！",
            "fn": function () {
            }
        };
        if (options) {
            $.extend(parameter, options);
        }
        top.$.messager.alert(parameter.title, parameter.message, 'warning', parameter.fn);
    },
    //确认对话框
    confirm: function (options) {
        var parameter = {
            "title": "确认对话框",
            "message": "你确定进行此项操作吗？点击确定为继续执行，点击取消则终止操作。",
            "fn": function () {
            }
        };
        if (options) {
            $.extend(parameter, options);
        }
        top.$.messager.confirm(parameter.title, parameter.message, function (r) {
            if (r) {
                parameter.fn();
            }
        });
    },
    //窗口打开方式
    baseData:function () {
        var openMode=[];
    },
    //将日期格式化成指定格式的字符串
    FormatDate: function (date, fmt) {
        /*
         y（年）
         M（月）
         d（日）
         q（季度）
         w（星期）
         H（24小时制的小时）
         h（12小时制的小时）
         m（分钟）
         s（秒）
         S（毫秒）
         另外，字符的个数决定输出字符的长度，如，yy输出16，yyyy输出2016，ww输出周五，www输出星期五
         @param date 必须是日期格式
         @param fmt 字符串格式，默认：yyyy-MM-dd HH:mm:ss
         @returns 返回格式化后的日期字符串
         */

        fmt = fmt || 'yyyy-MM-dd HH:mm:ss';
        var obj =
            {
                'y': date.getFullYear(), // 年份，注意必须用getFullYear
                'M': date.getMonth() + 1, // 月份，注意是从0-11
                'd': date.getDate(), // 日期
                'q': Math.floor((date.getMonth() + 3) / 3), // 季度
                'w': date.getDay(), // 星期，注意是0-6
                'H': date.getHours(), // 24小时制
                'h': date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, // 12小时制
                'm': date.getMinutes(), // 分钟
                's': date.getSeconds(), // 秒
                'S': date.getMilliseconds() // 毫秒
            };
        var week = ['日', '一', '二', '三', '四', '五', '六'];
        for (var i in obj) {
            fmt = fmt.replace(new RegExp(i + '+', 'g'), function (m) {
                var val = obj[i] + '';
                if (i == 'w') return (m.length > 2 ? '星期' : '周') + week[val];
                for (var j = 0, len = val.length; j < m.length - len; j++) val = '0' + val;
                return m.length == 1 ? val : val.substring(val.length - m.length);
            });
        }
        return fmt;
    }
}

$.fn.extend({//搜索扩展
    FromDataSearch: function () {
        var fdata = $(this).find(":input");
        var scheckedbox = fdata.filter(":checkbox,:radio").not("input:checked");
        fdata = fdata.not($(scheckedbox));
        var json = "{";
        var count = fdata.length;
        var spcount = 0;
        fdata.each(function (i, o) {
            if ($(o).attr("name") != undefined && $(o).val() != undefined) {
                var name = $(o).attr("name");
                var dis = fdata.filter("[name=" + name + "]").map(function (elm) {
                    return $.trim($(this).val());
                }).get().join(",");
                json += '"' + name + '":' + JSON.stringify(dis);
                json += ",";
                spcount++;
            }
        });
        if (spcount > 0) {
            json = json.substr(0, json.length - 1);
        }
        json += "};";
        var data = {};
        eval("data =  " + json);
        return data;
    }
});