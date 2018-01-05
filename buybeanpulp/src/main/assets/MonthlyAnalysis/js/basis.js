/**
 * Created by changcai on 2017/8/9.
 */
// 基于准备好的dom，初始化echarts实例


$(function() {
  var myChart = echarts.init(document.getElementById('echartsTableB'));
  myChart.showLoading();
  var timer;
  timer = setTimeout(function() {
                      var result = MonthlyAnalysisModel.jsCallOcAcquireMonth();
                      window.sessionStorage.result = result;
                      $.ajax({
                             url: config.url + "/getTopFive.do",
                             type: "POST",
                             dataType: "json",
                             data: {
                                "requestJSON": result
                             },
                             success: function(data, xhr) {
                                 if (data.errorCode == 0) {
                                     dataSource = data.resultObject;
                                     if(dataSource.topFiveSalesByMonth.x.length === 0 && dataSource.topFiveSalesByMonth.y.length === 0){
                                          myChart.hideLoading();
                                          MonthlyAnalysisModel.jsCallOcShowCurrentMonthData("暂无数据");
                                     }else{
                                         MonthlyAnalysisModel.jsCallOcShowCurrentMonthData("请求成功");
//                                         var myChart = echarts.init(document.getElementById('echartsTableB'));
                                         //alert(dataSource.topFiveSalesByMonth.ring);
                                         drawChart(dataSource.topFiveSalesByMonth.x,dataSource.topFiveSalesByMonth.y,dataSource.topFiveSalesByMonth.ring,myChart);
                                     }

                                 }else{
                                      myChart.hideLoading();
                                      //alert(data.errorDesc);
                                 }
                                 //clearInterval(timer);
                             },
                             error:function(){
                                  myChart.hideLoading();
                                  MonthlyAnalysisModel.jsCallOcShowCurrentMonthData("您的网络情况不稳定");
                                 //clearInterval(timer);
                             }
                          })
                }, 100);

})

function drawChart(xAxis,seriesBar,seriesLine,myChart){
//    myChart.showLoading();
    // 指定图表的配置项和数据
    var option  = {
    title: {
        text: '销售排名 Top5', //设置图标标题
        //标题位置
        textAlign: 'center',
        textStyle:{
            fontWeight:'normal',
            fontFamily:'PingFangSC-Light',
            fontSize: 40,
            color: '#26272a'
        },
        left:"50%",
        top: "0%"
    },
    grid: {
       show: false,
       left:'15%',
       right:'12%',
       top:100,
       bottom:40
    },
    legend: {
         data:['销售额','环比'],
         textStyle:{
              fontFamily:'MicrosoftYaHei',
              fontSize:27,
              color: '#26272a'
         },
         orient: 'horizontal',
         selectedMode: false,//鼠标不可选
         top:10,
         left:'5%'//垂直居中
    },
    xAxis: [//x轴数据
                {
                    axisLine: {
                        show:true,
                        lineStyle:{
                            color:'#D2D2DA',
                            width:1
                         }
                    },
                    axisLabel:{
                       interval:0,
                       textStyle:{
                            fontFamily:'PingFangSC-Light',
                            fontSize: 27,
                            color: '#408AFF'
                       }
                    },
                    axisTick: {
                        show: false
                    },
                    type: 'category',
                    data: null,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
    yAxis: [    //y轴数据
                {
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    type: 'value',
                    //name: '水量',
        //            min: 0,
        //            max: 250,
                    //interval: 50,
                    axisLabel: {//左侧数据
                        textStyle:{
                            fontFamily:'PingFangSC-Light',
                            fontSize:27,
                            color: '#757a81'
                        },
                        formatter: '{value}万'
                    },
                    splitLine: {
                        lineStyle: {
                            color: '#D2D2DA',
                            width:1
                        }
                    }
                },
                {
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel:{
                        textStyle:{
                            fontFamily:'PingFangSC-Light',
                            fontSize: 20,
                            color: '#757A81'
                        }
                    },
                    type: 'value',
        //            min: 0,
        //            max: 25,
                    //interval: 5,
                    axisLabel: {//右侧数据
                        textStyle:{
                            fontFamily:'PingFangSC-Light',
                            fontSize: 27,
                            color: '#757a81'
                        },
                        formatter: '{value}%'
                    },
                    splitLine: {
                        show:false,
                        lineStyle: {
                             color: '#D2D2DA',
                             width:1
                        }
                    }
                }
            ],
    series: [//标题
                 {
                     name:'销售额',
                     type:'bar',
                     barWidth:64,
                     barGap:'39',
                     itemStyle:{
                     normal: {
                          color:"#D1E8FF",
                          borderColor:"#408AFF",
                          borderWidth:0.5
                     }
                     },
                     data:seriesBar
                 },
                 {
                     name:'环比',
                     type:'line',
                     yAxisIndex: 1,
                     data:seriesLine,
                     itemStyle:{
                        normal: {
                        color:"#F78730"
                     }
                     },
                     label: {
                         normal: {
                             show: true,
                             textStyle:{fontSize: 27,color:"#26272A"}//字体颜色
                         }
                     },
                     itemStyle: {
                         normal: {
                             color: new echarts.graphic.LinearGradient(
                                                                   0, 0, 0, 1,
                                                                   [
                                                                    {offset: 0, color: '#FCB452'},
                                                                    {offset: 0.5, color: '#F78730'},
                                                                    {offset: 1, color: '#FCB452'}
                                                                    ]
                                                                   )
                         },
                         emphasis: {
                             color: new echarts.graphic.LinearGradient(
                                                                   0, 0, 0, 1,
                                                                   [
                                                                    {offset: 0, color: '#F78730'},
                                                                    {offset: 0.7, color: '#FCB452'},
                                                                    {offset: 1, color: '#FCB452'}
                                                                    ]
                                                                   )
                         }
                     },
                 }
             ]
    };
    var temp = [];
            for(var i = 0; i <xAxis.length;i++){
                var str = xAxis[i];
                temp.push(str.substr(0,5));
            }
    option.xAxis[0].data = temp;
    if(screen.height === 568){
        option.legend.itemGap = 5;
        option.legend.itemWidth = 15;
        //option.legend.itemWidth = 10;
        option.grid.left = '20%';
        //option.grid.top = 30;
        option.grid.bottom = 18;
    }
    //使用刚指定的配置项和数据显示图表。
    myChart.hideLoading();
    myChart.setOption(option);
}

