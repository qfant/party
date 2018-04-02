/**
 * Created by Struggle on 2017/9/7.
 */
var ajax = function ajax() {
    var _ajax = function () {
            return ('XMLHttpRequest' in window) ? function () {
                    return new XMLHttpRequest();
                } : function () {
                    return new ActiveXObject("Microsoft.XMLHTTP");
                }
        }(),
        formatData = function (fd) {
            var res = '';
            for (var f in fd) {
                res += f + '=' + fd[f] + '&';
            }
            return res.slice(0, -1);
        },
        AJAX = function (ops) {
            var root = this,
                req = _ajax();
            root.url = ops.url;
            root.type = ops.type || 'responseText';
            root.method = ops.method || 'GET';
            root.async = ops.async !== false;
            root.data = ops.data || {};
            root.dataType = ops.dataType || 'text';
            root.beforeSend = ops.beforeSend || function () {
                };
            root.complete = ops.complete || function () {
                };
            root.success = ops.success || function () {
                };
            root.error = ops.error || function (s) {
                    console.error(root.url + '->status:' + s + 'error!')
                };
            root.abort = req.abort;
            root.setData = function (data) {
                for (var d in data) {
                    root.data[d] = data[d];
                }
            };

            root.send = function () {
                var datastring = formatData(root.data),
                    sendstring,
                    get = false,
                    async = root.async,
                    dataType = root.dataType,
                    beforeSend = root.beforeSend,
                    complete = root.complete,
                    method = root.method.toUpperCase();
                beforeSend();
                if (method === 'GET') {
                    if(datastring) {
                        root.url += '?' + datastring;
                    }
                    get = true;
                }
                req.open(method, root.url, async);
                if (!get) {
                    req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                    sendstring = datastring;
                }
                //在send之前重置onreadystatechange方法,否则会出现新的同步请求会执行两次成功回调(chrome等在同步请求时也会执行onreadystatechange)
                req.onreadystatechange = function () {
                        // console.log('async true');
                        if (req.readyState == 4) {
                            complete();
                            if (req.status == 200) {
                                if (dataType == "text" || dataType == "TEXT") {
                                    if (root.success != null) {
                                        root.success(req.responseText);//普通文本
                                    }
                                } else if (dataType == "xml" || dataType == "XML") {
                                    if (root.success != null) {
                                        root.success(req.responseXML);//接收xml文档
                                    }
                                } else if (dataType == "json" || dataType == "JSON") {
                                    if (root.success != null) {
                                        root.success(eval("(" + req.responseText + ")"));//将json字符串转换为js对象
                                    }
                                }
                            } else {
                                root.error(req.status);
                            }
                        }
                    };
                req.send(sendstring);

                if (!async) {
                    complete();
                    req.onreadystatechange()
                }
            };
            root.url && root.send();
        };
    return function (ops) {
        return new AJAX(ops);
    }
}()
