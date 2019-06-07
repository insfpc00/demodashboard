var stompClient = null;
var isConnected= false;
function setConnected(connected) {
	isConnected=connected;
	if (connected) {$("#onoff").addClass('btn-info');}
	else {$("#onoff").removeClass('btn-info')};
	
}

function onoff() {
	if (!isConnected){
		connect();
	}
	else {
		disconnect();
	}
}


//function hashCode(str) { 
//    var hash = 0;
//    for (var i = 0; i < str.length; i++) {
//       hash = str.charCodeAt(i) + ((hash << 5) - hash);
//    }
//    return hash;
//} 
//
//function intToRGB(i){
//    var c = (i & 0x00FFFFFF)
//        .toString(16)
//        .toUpperCase();
//
//    return "00000".substring(0, 6 - c.length) + c;
//}
//
//function stringToColor(str){
//	return intToRGB(hashCode(str));
//}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/items', function (sale) {
            var saleJSON=JSON.parse(sale.body);
        	showItem(saleJSON);
            refreshCharts(saleJSON);
        });
    },function(message) {
        setConnected(false);
    });
    
    }

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showItem(sale) {
	
	for(it of sale.itemsSold){
		salesDataTable.row.add([it.productName,it.saleDate,it.categories.join(),sale.customerName]).draw();
		}
}

function refreshCharts(sale){
	updateOneDimensionChart(sale.salesGroupByProduct,'Product',salesByProductChart);
	updateOneDimensionChart(sale.salesGroupByCategory,'Category',salesByCategoryChart);
	updateTwoDimensionChartWrap(sale.salesGroupByProductAndMinute,salesByProductAndMinuteChart);
	updateTwoDimensionChartWrap(sale.salesGroupByCategoryAndMinute,salesByCategoryAndMinuteChart);
	updateGeoChart(sale);
	updateChartSalesByCustomer(sale);
}

function updateGeoChart(sale){
	var isPresent=false;
	if (sale.customerName!=null){
		for (address of imageSeries.data){
			if (address.latitude==sale.customerLatitude && address.longitude==sale.customerLongitude){
				isPresent=true;
			}
		}
		if (!isPresent){
		imageSeries.data.push({
		 	  "latitude": sale.customerLatitude,
		 	  "longitude": sale.customerLongitude,
		 	  "title": sale.customerName
		 	});
		imageSeries.data=imageSeries.data;
		}
	}
}

function updateChartSalesByCustomer(sale){
	
	//var chartData=[];
	
	for (minute in sale.salesGroupByCustomerAndMinute){
		for (customer in sale.salesGroupByCustomerAndMinute[minute]){
			if (!seriesTags.includes(customer)){
				seriesTags.push(customer);
				var series = xychart.series.push(new am4charts.LineSeries());
			 	series.name=customer;
				series.dataFields.valueY = customer;
			 	series.dataFields.valueX = "minute";
			 	series.strokeWidth = 2;
			 	series.tensionX = 0.98;
			 	series.tensionY = 0.98;
			}
		}
	}
	var maxMinute = 0;

	for (var minute in sale.salesGroupByCustomerAndMinute) {
	  maxMinute = (maxMinute < parseFloat(minute)) ? parseFloat(minute) : maxMinute;
	}
	var minMinute=0;
	
	if (xychart.data.length>0){
		minMinute=xychart.data[xychart.data.length-1].minute;
	}
	for (var minute=minMinute;minute<=maxMinute;minute++){
		var statObj={};
		
		for (customer of seriesTags){
			if (minute in sale.salesGroupByCustomerAndMinute && customer in sale.salesGroupByCustomerAndMinute[minute]){
			statObj[customer]=sale.salesGroupByCustomerAndMinute[minute][customer];
			}
			else {
				statObj[customer]=0;
			}
		}
		statObj["minute"]=minute;
		xychart.data.push(statObj);
	}   
	
	xychart.data=xychart.data;
}

//function updateChartSalesByCustomer(sale){
//	
//	var chartData=[];
//	
//	for (minute in sale.salesGroupByCustomerAndMinute){
//		for (customer in sale.salesGroupByCustomerAndMinute[minute]){
//			if (!seriesTags.includes(customer)){
//				seriesTags.push(customer);
//				var series = xychart.series.push(new am4charts.LineSeries());
//			 	series.name=customer;
//				series.dataFields.valueY = customer;
//			 	series.dataFields.valueX = "minute";
//			 	series.strokeWidth = 2;
//			 	series.tensionX = 0.98;
//			 	series.tensionY = 0.98;
//
//			 	//series.bullets.push(new am4charts.CircleBullet());
//			}
//		}
//	}
//
//	var maxMinute = 0;
//
//	for (var minute in sale.salesGroupByCustomerAndMinute) {
//	  maxMinute = (maxMinute < parseFloat(minute)) ? parseFloat(minute) : maxMinute;
//	}
//	
//	for (var minute=0;minute<maxMinute;minute++){
//		var statObj={};
//		
//		for (customer of seriesTags){
//			if (minute in sale.salesGroupByCustomerAndMinute && customer in sale.salesGroupByCustomerAndMinute[minute]){
//			statObj[customer]=sale.salesGroupByCustomerAndMinute[minute][customer];
//			}
//			else {
//				statObj[customer]=0;
//			}
//		}
//		statObj["minute"]=minute;
//		chartData.push(statObj);
//	}   
//	
//	xychart.data=chartData;
//}


function updateOneDimensionChart(items,tag,chart){
	
	var data = new google.visualization.DataTable();
	data.addColumn('string', tag);
    data.addColumn('number', 'Sales');
    data.addColumn({type:'string',role:'style'});
	var colors=['red', 'green', 'blue', 'yellow', 'orange'];
	
	var i=0;
    if (items!=null){
    for (item in items){
		data.addRow([item,items[item],'color:'+colors[i]]);
	    i++;
		}
    }
	
    var options = {
    		backgroundColor: { fill:'#ffffff',fillOpacity:0.1 },
    		 title: 'Items sold by '+tag.toLowerCase(),
    	      animation:{
    	        duration: 1000,
    	        easing: 'out',
    	        startup:true
    	      },
    	      is3D:true,
    	      vAxis: {minValue:0}
    	    };
	chart.draw(data,options);
}


function getMaxValue(arr){
	
	var maxValue=0;
    for (pos in arr){
    	if (parseInt(arr[pos])>maxValue){
    		maxValue=parseInt(arr[pos]);}
    }

    return maxValue;
}

function updateTwoDimensionChartWrap(items,chart){
	
	var chartdata=chart.getDataTable();
	var chartheaders={};
	for (let i=1;i<chartdata.getNumberOfColumns();i++){
		chartheaders[chartdata.getColumnLabel(i)]=i;
	}
	
    if (items!=null){
        var maxMinute=0;
        var minMinute=Number.MAX_SAFE_INTEGER;
        var position=getMaxValue(chartheaders)+1;
        
        for (var minute in items){
            if (parseInt(minute)>maxMinute){
            	maxMinute=parseInt(minute);
            }
            if (parseInt(minute)<minMinute){
            	minMinute=parseInt(minute);
            }
            
            for (value in items[minute]){
            	if (chartheaders[value]==null){
            		chartheaders[value]=position++;
            		chartdata.addColumn("number",value);
            	};
            }
    	}
        
        
        if (chartdata.getNumberOfRows()-1==minMinute){
        	chartdata.removeRow(minMinute);
        	}
                
        var dataArr=[];
        var offset=chartdata.getNumberOfRows();
        for (var i=0;i<=maxMinute-offset;i++){
        	dataArr[i]=new Array(position).fill(0);
        	dataArr[i][0]=i+offset;
        	}
        
        
        for (var minute in items){
        	for (value in items[minute]){
        		dataArr[minute-offset][chartheaders[value]]=items[minute][value];
        	}
        }

        chartdata.addRows(dataArr);
        
        if (chartdata.getNumberOfColumns()!=0){
        	chart.draw();
        }
    }
}




function refreshTwoDimensionChart(items,tag,chartdata,chartheaders,chart){
	
    if (items!=null){
        var maxMinute=0;
        var minMinute=0;
        var position=getMaxValue(chartheaders);
        
        for (var minute in items){
            if (parseInt(minute)>maxMinute){
            	maxMinute=parseInt(minute);
            }else
            	if (parseInt(minute)<minMinute){
            		minMinute=minute;
            	}
            
            for (value in items[minute]){
            	if (chartheaders[value]==null){
            		chartheaders[value]=position++;
            		chartdata.addColumn("number",value);
            	};
            }
    	}
        
        
        var dataArr=[];
        for (var i=chartdata.getNumberOfRows();i<=maxMinute;i++){
        	dataArr[i]=new Array(position+1).fill(0);
        	dataArr[i][0]=i;
        	}
        
        for (var minute in items){
        	for (value in items[minute]){
        		dataArr[minute][chartheaders[value]+1]=items[minute][value];
        	}
        }

        if (chartdata.getNumberOfRows()-1==minMinute){
        	chartData.removeRow(minMinute);
        	}
        
        chartdata.addRows(dataArr);
        
        
        var options = {
        		backgroundColor: { fill:'#ffffff',fillOpacity:0.1 },
        		 title: 'Sales by '+tag.toLowerCase()+' and minute',
        	      animation:{
          	        duration: 1000,
                      easing: "linear",
                      startup: true
          	      }
        	    };
        if (chartdata.getNumberOfColumns()!=0){
        	chart.draw(chartdata,options);
        }
    }
}

 //function buildTwoDimensionChart(items,tag,chart){
//	var data = new google.visualization.DataTable();
//
//    if (items!=null){
//    var maxMinute=0;
//    var headers={};
//    var position=0;
//    data.addColumn("number","Minute");
//    for (var minute in items){
//        if (parseInt(minute)>maxMinute){
//        	maxMinute=parseInt(minute);
//        }
//        for (value in items[minute]){
//        	if (headers[value]==null){
//        		headers[value]=position++;
//        		data.addColumn("number",value);
//        	};
//        }
//	}
//    
//    var dataArr=[];
//    for (var i=0;i<=maxMinute;i++){
//    	dataArr[i]=new Array(position+1).fill(0);
//    	dataArr[i][0]=i;
//    	}
//    
//    
//    for (var minute in items){
//    	for (value in items[minute]){
//    		dataArr[minute][headers[value]+1]=items[minute][value];
//    	}
//    }
//    data.addRows(dataArr);
//    }
//    
//    var options = {
//    		backgroundColor: { fill:'#ffffff',fillOpacity:0.1 },
//    		 title: 'Sales by '+tag.toLowerCase()+' and minute',
//    	      animation:{
//      	        duration: 1000,
//                  easing: "linear",
//                  startup: true
//      	      }
//    	    };
//    if (data.getNumberOfColumns()!=0){
//		
//    chart.draw(data,options);
//    }
//}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#onoff" ).click(function() { onoff(); });
});