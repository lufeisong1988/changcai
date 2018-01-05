/**
 * Created by changcai on 2017/8/9.
 */
// 基于准备好的dom，初始化echarts实例

$(function() {
    var myChart = echarts.init(document.getElementById('echartsTable'));
    myChart.showLoading();
    var timer;
    timer = setTimeout(function() {
        var result = ResourcesOfferModel.jsCallObjcAcquireSpotGoodsNumber();
//        alert(result);
        window.sessionStorage.result = result;
        $.ajax({
            url: config.url + "/getSpotIndex",
            type: "POST",
            dataType: "json",
            data: {
                "requestJSON": result
            },
            success: function(data, xhr) {

                if (data.errorCode == 0) {
                      dataSource = data.resultObject.spotIndex;

                      drawLineChart(dataSource.xDatas,dataSource.datas,myChart);

                }else{
                    myChart.hideLoading();
                    alert(data.errorDesc);
                }
            },
            error:function(){
                myChart.hideLoading();
                // MonthlyAnalysisModel.jsCallOcShowCurrentMonthData("您的网络情况不稳定！");
                //clearInterval(timer);
            }
        })
    }, 100);

})

function drawLineChart(xAxis,seriesLine,myChart) {
// 指定图表的配置项和数据
    var option = {
        //图表的标题配置
        title: {
            text: '', //设置图标标题
            //标题位置
            textAlign: 'center',
            textStyle: {
                fontWeight: 'normal',
                fontFamily: 'MicrosoftYaHei',
                fontSize: 30,
                color: '#26272a'
            },
            left: "50%",
            top: 5
        },
        grid: {
            show: false,
            top:48,
            bottom:30,
            left:'12%',
            right:'5%'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'line',
            },
            textStyle:{
                fontSize:28
            }
        },
        
        xAxis: [
            {
                type: 'category',
                data: null,
                axisLine: {
                    lineStyle: {
                        onZero: false,
                        color: '#408AFF'
                    }
                },
                axisLabel: {
                    textStyle: {
                        color: '#9FA4AB',
                        fontFamily: 'MicrosoftYaHei',
                        fontSize: 20
                    }
                },
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale:true,
                axisLine: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: '#26272A',
                        fontFamily: 'MicrosoftYaHei',
                        fontSize: 18
                    }
                },
                axisTick: {
                    show: false
                },
                splitLine: {
                    lineStyle: {
                        color: '#D2D2DA',
                        width: 1
                    }
                }
            }
        ],
        series: [
            {
                name: '',
                type: 'line',
                smooth: true,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        color: '#408aff'
                    }
                },
                lineStyle: {
                    normal: {
                        color: '#408aff'
                    }
                },
                data:seriesLine
            }
        ]
    };
    option.xAxis[0].data = xAxis;
    //保证屏幕上只会显示5个
    var intervalFunction = function(index, value) {
        var t = Math.ceil((xAxis.length - 1) / 4);
        if ((index) % t === 0) {
            return true;
        }
    }
    
    option.xAxis[0].axisLabel = {
         interval: intervalFunction,
         textStyle: {
             color: '#9FA4AB',
             fontFamily: 'MicrosoftYaHei',
             fontSize: 20
         }
    };
    var arr = [];
    for (var temp in option.series[0].data) {
        arr[temp] =Number(option.series[0].data[temp]);
    }
    option.title.text = '买豆粕现货指数'+'(' + ResourcesOfferModel.jsCallObjcAcquireCurrentRegion()+'区域'+')';
    arr = arr.sort(sortNumber);
    option.yAxis[0].min = Number(Math.floor((arr[0] - 100) / 100) * 100)< 0?0:Number(Math.floor((arr[0] - 100) / 100) * 100);
    option.yAxis[0].max = Number(Math.ceil((arr[arr.length - 1] + 100) / 100) * 100);
//使用刚指定的配置项和数据显示图表。
    myChart.hideLoading();
    myChart.setOption(option);
}
function sortNumber(a,b)
{
    return a - b
}