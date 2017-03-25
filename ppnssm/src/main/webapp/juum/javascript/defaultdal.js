/**
 * 功能：实现页面与底层（Service层）的通讯，包括soap模式和rest模式两种，返回数据格式支持xml和json，支持同步和异步模式
 * 实现页面的数据通用查询、新增、编辑、删除功能； 
 * 建议:系统内部查询采用rest协议查询，与第三方系统交互的页面可以采用soap协议 
 * 作者：李文刚 
 * 修改日期：2013-12-12 
 * 2014-06-09 梁洪杰 适配后台2.0框架，增加json处理
 * 2014-06-14 梁洪杰 增加异步处理，增加参数对象化接口，代码规范化
 */
/**
 * 构造函数
 * @param wsName WEB服务名称,默认BaseService
 * @param wsMode 通信方式：WEBSERVICE[soap]、 HTTP_JQUERY[rest],默认HTTP_JQUERY
 * @param async 异步模式：false[同步]、true[异步],默认false
 * @param getOrPost 访问模式:GET\POST,默认post
 * @param dataType 数据类型:text、xml、json等,默认json
 * @param basePath 基础路径：默认jetsennet.appPath + "../../services/"
 */
DefaultDal = defaultdal = function(wsName, wsMode, async, getOrPost, dataType, basePath) {
    this.wsName = wsName ? wsName : "BaseService";
    this.wsMode = wsMode ? wsMode : "HTTP_JQUERY";
    this.async = async ? async : false;
    this.getOrPost = getOrPost ? getOrPost : "POST";
    this.dataType = dataType ? dataType : "json";
    this.basePath = basePath ? basePath : ("");
    
    var $this = this;
    this.success = function (sResult, options) {
        if (options && options.success) {
            try {
                options.success(sResult);
            } catch (ex) {
                $this.error(-1, "处理结果异常：" + ex, options);
            }
        }
    };
};

/**
 * 执行调用,实现增删改查
 * @param method 方法名
 * @param param 下发参数,可为：null|HashMap对象|对象[只能用于http方式]|数组[只能用于soap方式],后台会用到
 * @param options 下发选项,实现单次下发定制化控制,如:{wsMode:"WEBSERVICE", async:true, getOrPost:"get", dataType:"xml", cacheLevel:2, success:function(resultVal)...}
 * @return WSResult WSResult:<errorCode、errorString、resultVal>
 */
defaultdal.prototype.execute = function(method, param, options) {
    var result = {
        errorCode : -1,
        resultVal : "",
        errorString : ""
    };
    var $this = this;
    try {
        var wsMode = valueOf(options, "wsMode", this.wsMode);
        if (wsMode == "HTTP_JQUERY") {
            //param = param ? (param.toJson ? param.toJson() : param) : {};// 转换成JSON数组
            $.ajax({
                url :  method,// 后台处理方法
                async : valueOf(options, "async", this.async),
                type : valueOf(options, "getOrPost", this.getOrPost),
                dataType : "json",
                headers : {
                    "DataType" : valueOf(options, "dataType", this.dataType)
                },
                data : param,
                success : function(sResult) {
                    result = sResult;
                    if (sResult) {
                    	 $this.success(sResult, options);
                    } else {
                    }
                },
                error : function(obj, ex) {
                }
            });
        }
    } catch (ex) {
    }
    return result;
};

