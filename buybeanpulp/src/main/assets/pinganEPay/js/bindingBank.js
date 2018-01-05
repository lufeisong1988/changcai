/**
 * Created by niejian on 2017/3/16.
 */

var aLiCode = ['<li data-val="','">','</li>','<ul>','</ul>'];
var $cityList = $('#cityList');
var baseInfoMap = {};//基本存储
var nativeJson = "";
setTimeout(function(){
    nativeJson = ResultModel.jsCallOcGetUserAccountList();
},0.001);


var bool = true;

$(function(){
    //加载银行列表
    loadBankList();
    //银行列表
    mobiscroll.treelist('#cityList', {
        theme: 'ios',
        lang: 'zh',
        display: 'bottom',
        //width: [166, 136, 116],
        placeholder: '选择银行',
        inputClass:'ipt iptBank',
        setText:'确定',
        cancelText:'',
        onSet: function (event) {//选择银行
            var bankName =  $(this).find("li[data-val="+event.valueText+"]").html();
            $("#cityList_dummy").val(bankName)
                .data("key",event.valueText);

            //判断所填是否为平安银行
            var bankVal = $("#cityList_dummy").data("key");
            //if(bankVal === "307"){//平安银行
            //    $(".otherBank").hide();
            //    $("#depositBankBtn").val("");
            //    $("#bankCard").val("").addClass("focusIngIpt");
            //}else{//其他银行
            //    $(".otherBank").show();
            //    $("#depositBankBtn").val("").addClass("focusIngIpt");
            //    $("#bankCard").val("");
            //}
            $(".otherBank").show();
            $("#depositBankBtn").val("").addClass("focusIngIpt");
            $("#bankCard").val("");

            if($("#cityList_dummy").hasClass("focusIngIpt")){
                $("#cityList_dummy").removeClass("focusIngIpt")
            }

            //存储信息
            baseInfoMap.bankClsCode = event.valueText;
            baseInfoMap.bankNameFir = bankName;
            localStorage.setItem("baseInfo",JSON.stringify(baseInfoMap));

        },
        //labels: ['Ingredients']
    });


    setTimeout(function(){
        $.when(getIdentityDetails()).done(function(){
            isSameuser();
            isWellDone();
        });
    },0.001);


    //选择开户行
    $("#depositBankBtn").on("touchend",function(){
        var isHasBank = $("#cityList_dummy").val();
        if(isHasBank){
            window.location = "selectBank.html";
        }else{
            errorFn("请选择银行");
        }

    })

    //银行卡号
    $("#bankCard").on("keyup",function(){
        isWellDone();
        if($(this).hasClass("focusIngIpt")){
            $("#bankCard").removeClass("focusIngIpt");
        }
        if($(this).val() == ""){
            $("#bankCard").addClass("focusIngIpt");
        }

        if($(this).val().length>16){
            baseInfo["bankCardNo"] = $("#bankCard").val();
           localStorage.baseInfo = JSON.stringify(baseInfo);
        }
    })
    //银行预留手机号号
    $("#bankMobile").on("keyup",function(){
        isWellDone();
    })


    //同意协议
    $(".protocol label").on("touchend",function(){
       if($(this).hasClass("checkedLabel")){
           $(".protocol label").removeClass("checkedLabel");
           $(".protocol label i").html("&#xe655;");
       }else{
           $(".protocol label").addClass("checkedLabel");
           $(".protocol label i").html("&#xe650;");
       }
        isWellDone();
    })

    //下一步
    $(".nextStep").on("touchend",".avaiableBtn",function(){
        var bankCard = $("#bankCard").val();//银行卡号
        var mobile = $("#bankMobile").val();

        if(bankCard.length < 16 ){//16-19位数字
            errorFn("银行卡号输入有误，请重新输入");
        }else if(bankCard.length > 19 ){
            errorFn("银行卡号输入有误，请重新输入");
        }else if(mobile.length < 11){
            errorFn("手机号输入有误，请重新输入");
        }else if(mobile.length > 11){
            errorFn("手机号输入有误，请重新输入");
        }else{
            if(bool){
                bool = false;
                firstPhaseRegist();
            }

        }
    })

    //android 键盘挡住input
    $("input[type='number']").on("click",function(){
        var target = this;
        setTimeout(function(){
            target.scrollIntoViewIfNeeded();
        },400)

    })


    //遮罩层阻止滑动
    $("body").on("touchmove",".maskShow",function(e){
        e.preventDefault();
    })
    $(".sureBtn").on("touchend",function(e){
        e.preventDefault();
        $(".mask").removeClass("maskShow");
    })

})

//判断是否填完
function isWellDone(){
    var bankVal = $("#cityList_dummy").data("key");//银行代码
    var depositBank = $("#depositBankBtn").val();//开户行
    var bankCard = $("#bankCard").val();//银行卡号
    var bankMobile = $("#bankMobile").val();
    var isAgree = $(".protocol label").hasClass("checkedLabel");
    if(bankVal === "307" && bankCard && bankMobile && isAgree){//平安
        $("#nextSetpBtn").addClass("avaiableBtn");
    }else if(bankVal && depositBank && bankCard && bankMobile && isAgree){//其他银行
        $("#nextSetpBtn").addClass("avaiableBtn");
    }else{
        $("#nextSetpBtn").removeClass("avaiableBtn");
    }

}

//不同手机号清空信息 首次登陆/非否次
function isSameuser(){
    var bankVal = $("#cityList_dummy").val();//银行
    var bankCard = $("#bankCard").val();//银行卡号
    //var depositBank = $("#depositBankBtn").val();//开户行
    var baseInfo = JSON.parse(localStorage.getItem("baseInfo"));
    if(baseInfo){

        $("#cityList_dummy").val(baseInfo.bankNameFir).data("key",baseInfo.bankClsCode);

        var baseInfoB = JSON.parse(localStorage.getItem("baseInfoB"));
        $(".otherBank").show();
        if(baseInfoB){//选择了开户行
            $("#depositBankBtn").val(baseInfoB.bankNameDetail).data("key",baseInfoB.bankCode);//赋值大小额行号
            $("#depositBankBtn").removeClass("focusIngIpt");
        }

        //卡号是否填写
        if(baseInfo.bankCardNo){
            $("#bankCard").val(baseInfo.bankCardNo);
        }

        var depositBank = $("#depositBankBtn").val();//开户行
        var bankCard = $("#bankCard").val();//银行卡号
        //焦点
        if(depositBank === ""){
            $("#depositBankBtn").addClass("focusIngIpt");
        }else if(bankCard === ""){
            $("#bankCard").addClass("focusIngIpt");
        }

    }else{//首次进入

        //焦点
        if(bankVal === ""){
            $("#cityList_dummy").addClass("focusIngIpt");
        }else if(bankCard === ""){
            $("#bankCard").addClass("focusIngIpt");
        }

    }
}

//获取身份认证信息
function getIdentityDetails(){
    var dtd = $.Deferred();
    var params = {
        "nativeJson": nativeJson,
    }
    localStorage.setItem("nativeJson",JSON.stringify(nativeJson));
    $.ajax({
        url: config.url + "/h5/account/getIdentityDetails.do",
        type: "POST",
        dataType: "json",
        data: {
            "requestJSON": JSON.stringify(params)
        },
        success: function(data, xhr) {
            if(data.errorCode === "0"){
                var data = data.resultObject;
                $("#signName").html(data.userName);
                var code = data.identityCardNo;
                    code = code.substring(0,4) +"***********"+ code.slice(-4);
                $("#signIdCard").html(code);
                $("#bankMobile").val(data.mobilePhone);

                //不同账号清除银行信息
                var userPhone = JSON.parse(localStorage.getItem("userPhone"));
                if(userPhone){
                    if(userPhone != data.mobilePhone){
                        localStorage.removeItem("baseInfo");
                    }
                }
                localStorage.setItem("userPhone",data.mobilePhone);
                dtd.resolve();
            }else{
                errorFn(data.errorDesc);
            }
        },
        error: function(data){
            errorFn(data.errorDesc);
        }
    })
    return dtd.progress();
}




//校验
function firstPhaseRegist(){

    $(".loading").addClass("maskShow");
    var params = {
        "nativeJson": nativeJson,
        "bankClsCode": $("#cityList_dummy").data("key"),
        "bankCardNo": $("#bankCard").val(),
        "bankName": $("#depositBankBtn").val(),
        "bankCode": $("#depositBankBtn").data("key"),
        "mobilePhone": $("#bankMobile").val()
    }
    $.ajax({
        url: config.url + "/h5/pingan/pingAnFirstPhaseRegist.do",
        type: "POST",
        dataType: "json",
        data: {
            "requestJSON": JSON.stringify(params)
        },
        success: function(data, xhr) {
            //存储信息
            baseInfoMap.bankNameFir = $("#cityList_dummy").val();//银行名称
            baseInfoMap.bankClsCode =  $("#cityList_dummy").data("key");//银行代码
            baseInfoMap.bankCardNo = $("#bankCard").val();//银行卡号
            baseInfoMap.bankNameDetail =  $("#depositBankBtn").val();//开户行名称
            baseInfoMap.bankCode =  $("#depositBankBtn").data("key");//开户行大小额行号
            baseInfoMap.mobilePhone = $("#bankMobile").val();//手机号

            if(data.errorCode === "0"){
                localStorage.setItem("baseInfo",JSON.stringify(baseInfoMap));

                localStorage.setItem("serialNo",JSON.stringify(data.resultObject.serialNo));//开户流水号

                window.location = "cardDynamicNumber.html";

                bool = true;
            }else{//errorDesc
                if(data.errorCode == "PINGAN_REGISTER_IDEMPOTENT_ERROR"){//平安重复注册

                    localStorage.setItem("baseInfo",JSON.stringify(baseInfoMap));
                    var serialNo = JSON.parse(localStorage.getItem("serialNo"));

                    window.location = "cardDynamicNumber.html";
                }else{
                    baseInfoMap.errorDesc = data.errorDesc;//错误信息
                    localStorage.setItem("baseInfo",JSON.stringify(baseInfoMap));

                    window.location = "cardFaild.html";
                }

            }
            $(".loading").removeClass("maskShow");
        },
        error: function(data){
            errorFn(data.errorDesc);
            bool = true;
            $(".loading").removeClass("maskShow");
        }
    })


}

//错误提示
function errorFn(txt){
    $(".errorContent").html(txt);
    var vh = document.body.scrollHeight;
    setTimeout(function(){
        $(".mask").height(vh).addClass("maskShow");
    },10)
}

/*加载省市区信息*/
function loadBankList() {
    var sTemp = "";
    for (var i=0,l=bankJSON.length;i<l;i++){
        sTemp += aLiCode[0];
        sTemp += bankJSON[i].bankClsCode;
        sTemp += aLiCode[1];
        sTemp += bankJSON[i].bankName;
        sTemp += aLiCode[2];

    }
    $cityList.append(sTemp);
}