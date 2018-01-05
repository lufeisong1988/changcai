var aDivs = ["<div class=\"allRecords\"><ul><li>", "</li><li>", "</li></ul></div>"],
     nativeJson = window.sessionStorage.result;
     //nativeJson = 'Owkm2OUzWhj8Yby5nJpuBW4k/BLGUMZLtxKhEpy3XdwGfYVXfuHDXLksXx4JIqf1Vf4NfYYMwO1EGDl6X0h9I+Th0G0FfbMfT9Z8BoAO56A=',
    allRecordsData = {
        "nativeJson": nativeJson,
        "currentPage": "1",
        "perPageRecord": "10"
    },
    allFreezeInTheCapital = {
        "nativeJson": nativeJson,
        "currentPage": "1",
        "perPageRecord": "10"
    },

    allWithDrawalRecord = {
        "nativeJson": nativeJson,
        "currentPage": "1",
        "perPageRecord": "10"
    };
var oUl = $('.superposedLayer').find('ul')[0];

$(document).ready(function() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth();
    month++;
    for (var i = 1; i < 13; i++) {
        if (!(month > 0)) {
            year--;
            month = 12;
        }
        $('li').eq(i).html(year + "年" + month + "月" + "<i class=\"icon iconfont\">&#xe641;</i>").addClass('dateLi')
        month--;
    };
    $('.myProperty').find('.money').eq(0).on('click', function() {
        window.location = "./freezeInTheCapital.html"
    });
    $('.myProperty').find('.money').eq(1).on('click', function() {
        window.location = "./withdrawalRecord.html"
    });

    $('#main').on("click", ".withdrawalRecord", function() {
        if ($(this).find('i').hasClass('zhuandong')) {
            $(this).find('i').removeClass("zhuandong");
            $(this).find('li').eq(2).hide();
        } else {
            $(this).find('i').addClass("zhuandong");
            $(this).find('li').show();
        }
    });
    $('.records').on('click', function() {

        window.location = './allRecords.html'
    });
    document.addEventListener('touchstart', handler, {
        passive: true
    });
    $('.header').find("span").on('click', function() {
        var z = parseInt($('.bottom').css('z-index'));
        if (z == -1) {
            $('.superposedLayer').show();
            $('.bottom').css('z-index', 2).show();
        } else {
            $('.superposedLayer').hide();
            $('.bottom').css('z-index', -1).hide();
        }
        if ($(this).find('i').hasClass('zhuandong2')) {
            $(this).find('i').removeClass("zhuandong2");
            $(this).css('color','#999');
            $(this).find('li').eq(2).hide();
        } else {
            $(this).find('i').addClass("zhuandong2");
            $(this).css('color','#408aff');
            $(this).find('li').show();
        }
    })


    $('.dateList li:not(".noDoLi")').on('click', searchByDate);
 //   $('.bottom').addEventListener('touchmove','handle',false);
    $('.bottom').on('click', function() {
        $('.superposedLayer').hide();
        $('.bottom').css('z-index', -1).hide();
    });

    $('.bottom').on('touchstart',setTimeout(function(event) {
        $('.superposedLayer').hide();
        $('.bottom').css('z-index', -1).hide();
       
           

    },320));
    

    
})

//

function handler() {}
function handle(event) {event.stopPropagation();}

function onAllRecords() {
    getAllRecordsData();

    $(window).scroll(function() {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight == scrollHeight) {
            getAllRecordsData();
        }
    });
}

function saveToLocalStorage(sourceNo, createTime, tradeParty, displayMoney,
    displayReason) {
    window.sessionStorage.sourceNo = sourceNo;
    window.sessionStorage.createTime = createTime;
    window.sessionStorage.tradeParty = tradeParty;
    window.sessionStorage.displayMoney = displayMoney;
    window.sessionStorage.displayReason = displayReason;
}

function getFromLocalStorage() {
    var thisDetail = $('.paymentDetails').find('td');
    thisDetail.eq(1).text(window.sessionStorage.sourceNo);
    thisDetail.eq(3).text(window.sessionStorage.createTime);
    thisDetail.eq(5).text(window.sessionStorage.tradeParty);
    thisDetail.eq(7).find('b').text(window.sessionStorage.displayMoney);
    thisDetail.eq(9).text(window.sessionStorage.displayReason);
}

function onPaymentDetails() {

    getFromLocalStorage();
    $('#main').on("click", ".withdrawalRecord", function() {
        if ($(this).find('i').hasClass('zhuandong')) {
            $(this).find('i').removeClass("zhuandong");
            //$(this).find('li').eq(2).slideUp();
            $(this).find('li').css('height', '60px');
        } else {
            $(this).find('i').addClass("zhuandong");
            //$(this).find('li').slideDown();
            $(this).find('li').css('height', 'auto');
        }
    })

}

function onFreezeInTheCapital() {
    getFreezeInTheCapitalData();
    $(window).scroll(function() {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight == scrollHeight) {
            getFreezeInTheCapitalData();
        }
    });
}

function withdrawalRecord() {
    getWithdrawalRecord();
    $(window).scroll(function() {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight == scrollHeight) {
            
            getWithdrawalRecord();
        }
    });
}

function getLastDay(year, month) {
    var new_year = year; //取当前的年份          
    var new_month = month++; //取下一个月的第一天，方便计算（最后一天不固定）          
    if (month > 12) {
        new_month -= 12; //月份减          
        new_year++; //年份增          
    }
    var new_date = new Date(new_year, new_month, 1); //取当年当月中的第一天          
    return new Date(new_date.getTime() - 1000 * 60 * 60 * 24); //获取当月最后一天日期          
}

function searchByDate() {
    var _this = $(this),
        count = 0,
        time = _this.text(),
        year = time.substr(0, 4),
        month = time.substr(5, 2);
    month = bzero(month);
    var day = getLastDay(year, month).toString().substr(8, 2);
    $('.superposedLayer').find('.dateLi').removeClass('blue');
    _this.addClass('blue');
    $('.superposedLayer').find('i').hide();
    _this.find('i').show();

    var title = $('title').text();
    $('.superposedLayer').hide();
    $('.bottom').hide();
    $('.bottom').css('z-index', -1).hide();
    allFreezeInTheCapital.frozenStartDate = year + "-" + month + "-01 00:00:01";
    allFreezeInTheCapital.frozenEndDate  = year + "-" + month + "-" + day + " 23:59:59";
    allRecordsData.payStartDate = year + "-" + month + "-01 00:00:01";
    allRecordsData.payEndDate = year + "-" + month + "-" + day + " 23:59:59";
    allWithDrawalRecord.drawStartDate  = year + "-" + month + "-01";
    allWithDrawalRecord.drawEndDate = year + "-" + month + "-" + day + " 23:59:59";
    allRecordsData.currentPage = '1';
    allWithDrawalRecord.currentPage = '1';
    allFreezeInTheCapital.currentPage = '1';
    $('#main').empty();
    $('#main1').empty();
    $('.header').find('span').html(
        year + '年' + month + '月' + "<i class=\"icon iconfont\">&#xe609;</i>");
    if (isNaN(month)) {
        allFreezeInTheCapital.frozenStartDate = "";
        allFreezeInTheCapital.frozenEndDate = "";
        allRecordsData.payStartDate = "";
        allRecordsData.payEndDate = "";
        allWithDrawalRecord.drawStartDate  = "";
        allWithDrawalRecord.drawEndDate  = "";
        $('.header').find('span').html("全部" + "<i class=\"icon iconfont\">&#xe609;</i>");
    }

    if (title == "冻结中的资金") {

        getFreezeInTheCapitalData();
    } else if (title == "提现记录") {

        getWithdrawalRecord();
    } else if (title == "收支记录") {

        getAllRecordsData();
    }
}

function bzero(month) {
    var i = parseInt(month);
    if (i < 10) {
        return "0" + i;
    }
    return i + "";
}
// 提现
function getWithdrawalRecord() {
    $.ajax({
        url: config.url + "/getAccountDrawList",
        type: "POST",
        dataType: "json",
        data: {
            "requestJSON": JSON.stringify(allWithDrawalRecord)
        },success: function(data) {
            if (data.errorCode === "0") {
            	//console.log(allWithDrawalRecord)
                var main = $('#main');
                if (data.resultObject.length === 0) {
                    if (allWithDrawalRecord.currentPage == '1') {
                        var str = '<div class="screeningDate_noRecord"><i class="icon iconfont">&#xe652;</i><span>您还没有相关记录</span></div>';
                        main.html(str);
                    }
                } else {

                    var tempCheckStr = $('.last').text();
                    if (tempCheckStr == "已显示所有记录") {
                        return;
                    }
                    var str = "";
                    $('.last').remove();
                    for (var i = 0, l = data.resultObject.length; i < l; i++) {
                        isdrag = 1;
                        
                        str += "<div class=\"withdrawalRecord\"><ul><li>"
                        str += data.resultObject[i].amountZh;
                        str += "</li><li>";
                        str += data.resultObject[i].createTime;
                        str += "<i class=\"icon iconfont\">&#xe60d;</i></li><li>";
                        str += data.resultObject[i].createMemo;
                        str += "</li></ul><span>";
                        str += data.resultObject[i].statusZh;
                        str += "</span></div>";
                        
                    }
                    main.append(str);
                    //var temp = main.html();
                    //    main.html(temp + str);
                    main.append("<div class=\"last\">正在加载中</div>");
                    if (data.resultObject.length === 0) { // && isdrag === 1) {
                        isdrag++;
                        $('.last').remove();
                        main.append("<div class=\"last\">已显示所有记录</div>");
                    }
                    if (data.resultObject.length < 20 && isdrag === 1) {
                        $('.last').remove();
                        main.append("<div class=\"last\">已显示所有记录</div>");
                    }
                    var i = parseInt(allWithDrawalRecord.currentPage);
                    i++;
                    allWithDrawalRecord.currentPage = i.toString();
                }


            } else {
                alert(data.errorDesc);
            }

        }

    })
}
// 冻结
function getFreezeInTheCapitalData() {
    $.ajax({
        url: config.url + "/getFrozenAmountList",
        type: "POST",
        dataType: "json",
        data: {
            "requestJSON": JSON.stringify(allFreezeInTheCapital)
        },
        success: function(data) {
            if (data.errorCode === "0") {
                var main = $('#main');
                if (data.resultObject.length === 0) {
                    if (allFreezeInTheCapital.currentPage == '1') {
                        var str = '<div class="screeningDate_noRecord"><i class="icon iconfont">&#xe652;</i><span>您还没有相关记录</span></div>';
                        main.html(str);
                    }
                } else {
                    var tempCheckStr = $('.last').text();
                    if (tempCheckStr == "已显示所有记录") {
                        return;
                    }
                    $('.last').remove();
                    var str = "";
                    for (var i = 0, l = data.resultObject.length; i < l; i++) {
                        isdrag = 1;
                        
                        str += "<div class=\"freezeInTheCapital\"><ul><li>"
                        str += data.resultObject[i].orderNo;
                        str += "</li><li>";
                        str += data.resultObject[i].frozenAmountZH;
                        str += "</li><li>";
                        str += data.resultObject[i].tradeEnterName;
                        str += "</li><li>";
                        str += data.resultObject[i].createTime;
                        str += "</li></ul></div>";
                        
                    }
                    main.append(str);
                    //var temp = main.html();
                    //    main.html(temp + str);
                    main.append("<div class=\"last\">正在加载中</div>");
                    if (data.resultObject.length === 0) {

                        isdrag++;
                        $('.last').remove();
                        main.append("<div class=\"last\">已显示所有记录</div>");
                    }
                    if (data.resultObject.length < 20 && isdrag === 1) {
                        $('.last').remove();
                        main.append("<div class=\"last\">已显示所有记录</div>");
                    }
                    var i = parseInt(allFreezeInTheCapital.currentPage);
                    i++;
                    allFreezeInTheCapital.currentPage = i.toString();
                }


            } else {
                alert(data.errorDesc);
            }

        }
        
    })
}



// 全部记录
function getAllRecordsData() {
    $.ajax({
        url: config.url + "/getAccountPaymentList",
        type: "POST",
        dataType: "json",
        data: {
            "requestJSON": JSON.stringify(allRecordsData)
        },
        success: function(data) {
            if (data.errorCode === "0") {
                var main = $('#main1');
                if (data.resultObject.length === 0) {
                    if (allRecordsData.currentPage == '1') {
                        var str = '<div class="screeningDate_noRecord"><i class="icon iconfont">&#xe652;</i><span>您还没有相关记录</span></div>';
                        main.html(str);
                    }
                } else {
                    var tempCheckStr = $('.last').text();
                    if (tempCheckStr == "已显示所有记录") {
                        return;
                    }
                   // console.log(data.resultObject.length);
                    $('.last').remove();
                    var str = "";
                    for (var i = 0, l = data.resultObject.length; i < l; i++) {
                        isdrag = 1;
                        
                        str += aDivs[0];
                        str += data.resultObject[i].createTime;
                        str += aDivs[1];
                        str += data.resultObject[i].tradeParty;
                        str += aDivs[1];
                        str += data.resultObject[i].displayMoney;

                        str += "<span>" + data.resultObject[i].sourceNo + "</span><span>" + data.resultObject[i].displayReason + "</span>";
                        str += aDivs[2];
                       
                    }
                    main.append(str);
                    // var temp = main.html();
                    //    main.html(temp + str);
                    main.append("<div class=\"last\">正在加载中</div>");
                    if (data.resultObject.length !== 10) {
                        isdrag++;
                        $('.last').remove();
                        main.append("<div class=\"last\">已显示所有记录</div>");
                    }
                    var i = parseInt(allRecordsData.currentPage);
                    i++;
                    allRecordsData.currentPage = i.toString();
                }


            } else {
                alert(data.errorDesc);
            }

        }
    })
}
