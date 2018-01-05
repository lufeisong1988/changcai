var aLiCode = ['<li data-val="','">','</li>','<ul>','</ul>','<div>','</div>'];
var $cityList = $('#cityList');
var cityCode = "";//城市码
var baseInfoMapB = {};//基本存储
var nativeJson = JSON.parse(localStorage.getItem("nativeJson"));

$(function(){
    loadCityList();
    var instance = mobiscroll.treelist('#cityList', {
        theme: 'ios',
        lang: 'zh',
        display: 'bottom',
        width: [166, 136, 116],
        placeholder: '所在地区',
        inputClass:'ipt iptBank',
        setText:'确定',
        cancelText:'',
        onSet: function (event, inst) {
            var n = event.valueText.split(' ');
            var prov =  $(this).find("li[data-val="+n[0]+"]").find("div").html();
            var city = $(this).find("li[data-val="+n[0]+"]").find("li[data-val="+n[1]+"]").find("div").html();
            if(n[2] !== undefined){
                var county = $(this).find("li[data-val="+n[2]+"]").find("div").html();
                $("#cityList_dummy").val(prov + " " + city + " " +  county).data("key",event.valueText);
            }else{
                $("#cityList_dummy").val(prov + " " + city)
                    .data("key",event.valueText);
            }

        }
        //labels: ['Region', 'Supervisor', 'Tech']
    });




    //搜索
    $("#searchBankBtn").on("touchend",function(){
        var area = $("#cityList_dummy").val();
        var bankName = $("#depositBankBtn").val();
        if(area === "" && bankName === ""){//都无值
            $(".defaultTip").hide();
            $(".noResultTip").show();
        }else{
            searchList ();
        }
    });

    //选择银行
    $("#bankUl").on("click","li",function(){
        //var bankClsCode = $(this).data("bankClsCode");
        var bankCode = $(this).data("bankcode");//大小额行号
        var bankNameDetail = $(this).find("p").text();
        //存储信息
        baseInfoMapB.bankCode = bankCode;
        baseInfoMapB.bankNameDetail = bankNameDetail;
        localStorage.setItem("baseInfoB",JSON.stringify(baseInfoMapB));
        window.location = "bindingBank.html";

    })


})

//银行搜索列表
function searchList(){
    var baseInfo = JSON.parse(localStorage.getItem("baseInfo"));
    var bankName = $("#depositBankBtn").val();
    var area = $("#cityList_dummy").val();
    if(area === ""){
        cityCode = "";
    }else{
        var cityCode =  $("#cityList_dummy").data("key").split(" ");

        cityCode = parseInt(cityCode[cityCode.length-1]);

    }

    var params = {
        "nativeJson": nativeJson,
        "cityCode": cityCode,
        "bankClsCode": baseInfo.bankClsCode,
        "bankName": bankName
    }

    $.ajax({
        url: config.url + "/h5/pingan/getBankInfoByClsCodeAndCityCode.do",
        type: "POST",
        dataType: "json",
        data: {
            "requestJSON": JSON.stringify(params)
        },
        success: function(data, xhr) {
            if(data.errorCode === "0"){
                if(data.resultObject.length > 0){
                    var data = data.resultObject;
                    var temp = ""
                    for(i in data){
                        temp += "<li data-bankClsCode="+data[i].bankClsCode+" data-bankCode="+data[i].bankCode+"><p>"+data[i].bankName+"</p></li>"
                    }
                    $("#bankUl").html(temp).show();
                    $(".defaultTip,.noResultTip").hide();
                }else{//无结果
                    $(".defaultTip").hide();
                    $(".noResultTip").show();
                    $("#bankUl").hide();

                }
            }else{
                alert(data.errorDesc);
            }
        },
        error: function(data){
            alert(data.errorDesc);
        }
    })
}

/*加载省市区信息*/
function loadCityList() {
    var sTemp = "";
    for (var i=0,l=oCityJSON.length;i<l;i++){          //
        sTemp += aLiCode[0];
        sTemp += oCityJSON[i].cityNodeCode;
        sTemp += aLiCode[1];
        sTemp += aLiCode[5];
        sTemp += oCityJSON[i].cityAreaname;
        sTemp += aLiCode[6];
        sTemp += aLiCode[3];
        for (var j=0,k = oCityJSON[i].subCityInfoList.length;j<k;j++){
            sTemp += aLiCode[0];
            sTemp += oCityJSON[i].subCityInfoList[j].cityOraareacode;
            sTemp += aLiCode[1];
            sTemp += aLiCode[5];
            sTemp += oCityJSON[i].subCityInfoList[j].cityAreaname;
            sTemp += aLiCode[6];
            var p = oCityJSON[i].subCityInfoList[j].subCityInfoList.length;
            if(p > 0){
                sTemp += aLiCode[3];
                for (var o=0; o<p;o++){
                    sTemp += aLiCode[0];
                    sTemp += oCityJSON[i].subCityInfoList[j].subCityInfoList[o].cityOraareacode + getRanNum();
                    sTemp += aLiCode[1];
                    sTemp += aLiCode[5];
                    sTemp += oCityJSON[i].subCityInfoList[j].subCityInfoList[o].cityAreaname;
                    sTemp += aLiCode[6];
                    sTemp += aLiCode[2];
                }
                sTemp += aLiCode[4];
            }
        }
        sTemp += aLiCode[4];
        sTemp += aLiCode[2];

    }

    $cityList.append(sTemp);
}


//随机生成一个字母
function getRanNum(){
    var ranNum1 = Math.ceil(Math.random() * 25);
    var ranNum2 = Math.ceil(Math.random() * 25);
    var ranA = String.fromCharCode(65+ranNum1) + String.fromCharCode(65+ranNum2);
    return ranA;

}

